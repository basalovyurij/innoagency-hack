package org.innoagencyhack.ocrparser.extractors.scans.tesseract;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import javax.imageio.ImageIO;
import org.apache.commons.pool2.BasePooledObjectFactory;
import org.apache.commons.pool2.PooledObject;
import org.apache.commons.pool2.impl.DefaultPooledObject;
import org.apache.commons.pool2.impl.GenericObjectPool;
import org.bytedeco.javacpp.BytePointer;
import org.bytedeco.leptonica.PIX;
import static org.bytedeco.leptonica.global.lept.pixDestroy;
import static org.bytedeco.leptonica.global.lept.pixReadMem;
import org.bytedeco.tesseract.PageIterator;
import org.bytedeco.tesseract.TessBaseAPI;
import org.bytedeco.tesseract.global.tesseract;

/**
 *
 * @author Yuriy Basalov
 */
public final class TesseractOcr implements AutoCloseable {

    public static final int MIN_DPI = 70;
    private static final String DATA_PATH = "tessdata/med";
    private static final GenericObjectPool<TessBaseAPI> POOL;

    static {
        POOL = new GenericObjectPool<>(new TesseractFactory());
        POOL.setMaxIdle(50);
        POOL.setMaxTotal(50);
    }

    private final TessBaseAPI api;

    public TesseractOcr() throws Exception {
        api = POOL.borrowObject();
        setDpi(MIN_DPI);
    }

    public void setDpi(Integer dpi) {
        if (dpi >= MIN_DPI) {
            api.SetVariable("user_defined_dpi", dpi.toString());
        }
    }

    public String ocr(BufferedImage img) throws IOException {
        PIX image = toPIX(img);
        try {
            api.SetImage(image);
            return extractTextFromApi();
        } finally {
            api.ClearAdaptiveClassifier();
            api.Clear();
            pixDestroy(image);
        }
    }

    public int getOritentation(BufferedImage img) throws IOException {
        PIX image = toPIX(img);
        try {
            api.SetImage(image);
            
            int orientation = extractOrientationFromApi();

            return orientation;
        } finally {
            api.ClearAdaptiveClassifier();
            api.Clear();
            pixDestroy(image);
        }
    }

    public OritentationAndTextInfo getOritentationAndText(BufferedImage img) throws IOException {
        PIX image = toPIX(img);
        try {
            api.SetImage(image);

            int orientation = extractOrientationFromApi();
            String text = extractTextFromApi();

            return new OritentationAndTextInfo(orientation, text);
        } finally {
            api.ClearAdaptiveClassifier();
            api.Clear();
            pixDestroy(image);
        }
    }

    private int extractOrientationFromApi() {
        api.SetPageSegMode(tesseract.PSM_AUTO_OSD);
        PageIterator iterator = api.AnalyseLayout();
        if (iterator == null)
            return 0;
        
        int[] orientation = new int[1];
        int[] writing_direction = new int[1];
        int[] textline_order = new int[1];
        float[] deskew_angle = new float[1];

        iterator.Orientation(orientation, writing_direction, textline_order, deskew_angle);

        return orientation[0] * 90;
    }

    private String extractTextFromApi() throws UnsupportedEncodingException {
        api.SetPageSegMode(tesseract.PSM_AUTO);
        BytePointer textPointer = api.GetUTF8Text();
        try {
            String text = textPointer.getString("UTF-8").trim();
            if (text.isEmpty()) {
                textPointer.deallocate();
                api.SetPageSegMode(tesseract.PSM_SINGLE_BLOCK);
                textPointer = api.GetUTF8Text();
                text = textPointer.getString("UTF-8");
            }
            return text.replace("-\n", "").trim();
        } finally {
            textPointer.deallocate();
        }
    }

    private static PIX toPIX(BufferedImage img) throws IOException {
        byte[] imageBytes;
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            ImageIO.write(img, "png", baos);
            imageBytes = baos.toByteArray();
        }
        return pixReadMem(imageBytes, imageBytes.length);
    }

    @Override
    public void close() throws Exception {
        POOL.returnObject(api);
    }

    private static class TesseractFactory extends BasePooledObjectFactory<TessBaseAPI> {

        @Override
        public TessBaseAPI create() {
            TessBaseAPI inst = new TessBaseAPI();
            inst.Init(DATA_PATH, "osd+rus+eng+Cyrillic");
            inst.SetPageSegMode(tesseract.PSM_AUTO);
            return inst;
        }

        @Override
        public PooledObject<TessBaseAPI> wrap(TessBaseAPI inst) {
            return new DefaultPooledObject<>(inst);
        }

        @Override
        public void passivateObject(PooledObject<TessBaseAPI> p) {
            p.getObject().ClearAdaptiveClassifier();
            p.getObject().Clear();
        }

        @Override
        public void destroyObject(PooledObject<TessBaseAPI> p) throws Exception {
            p.getObject().End();
            p.getObject().close();
            super.destroyObject(p);
        }
    }
}
