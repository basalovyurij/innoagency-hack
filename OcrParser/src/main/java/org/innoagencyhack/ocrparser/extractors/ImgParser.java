package org.innoagencyhack.ocrparser.extractors;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.List;
import javax.imageio.ImageIO;
import org.opencv.core.Mat;
import org.innoagencyhack.ocrparser.extractors.scans.ImageRotator;
import org.innoagencyhack.ocrparser.extractors.scans.ImageHelper;
import org.innoagencyhack.ocrparser.extractors.scans.TableObtainer;
import org.innoagencyhack.ocrparser.extractors.scans.tesseract.TesseractOcr;
import org.innoagencyhack.ocrparser.models.ParseResponse;
import org.innoagencyhack.ocrparser.models.TableResponse;

public class ImgParser implements IParser, AutoCloseable {

    private final TesseractOcr tesseract;
    private final TableObtainer tableObtainer;
    private final boolean parseTable;

    public ImgParser(boolean parseTable) throws Exception {
        this.tesseract = new TesseractOcr();
        this.parseTable = parseTable;
        
        if(parseTable)
            this.tableObtainer = new TableObtainer(tesseract);
        else
            this.tableObtainer = null;
    }

    @Override
    public ParseResponse parse(byte[] file) throws Exception {
        try(ByteArrayInputStream bis = new ByteArrayInputStream(file)) {
            BufferedImage img = ImageIO.read(bis);
            int dpi = (int) (Math.max(img.getWidth(), img.getHeight()) / 11.7);
            return parse(img, dpi);
        }
    }
    
    public ParseResponse parse(BufferedImage img, Integer dpi) throws Exception {
        try {
            tesseract.setDpi(dpi);
                               
            int angle = tesseract.getOritentation(img);            
            if(angle != 0) 
                img = ImageHelper.rotate90(img, angle);
            
            String txt = tesseract.ocr(img);
//            Mat image = ImageHelper.bufferedImageToMat(img); 
//            List<Mat> textRegions = ImageHelper.getTextRegions(image);
//            StringBuilder sb = new StringBuilder();
//            for(Mat textRegion : textRegions) {
//                String textBlock = tesseract.ocr(ImageHelper.mat2BufferedImage(textRegion));
//                sb.append(textBlock.trim());
//                sb.append("\n");
//                sb.append("\n");
//            }
            
            List<TableResponse> tables = new ArrayList<>();
//            if(parseTable) {        
//                Mat rotatedImg = ImageRotator.getAlignedImage(image);
//                tables = tableObtainer.getTablesList(rotatedImg);
//            }
            
            return new ParseResponse(tables, txt.toString());
        } catch (Exception ex) {
            ex.printStackTrace();
            return new ParseResponse(ex);
        }
    }
 
            
    @Override
    public void close() throws Exception {
        tesseract.close();
    }
}
