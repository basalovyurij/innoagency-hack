package org.innoagencyhack.ocrparser;

import java.io.File;
import javax.imageio.ImageIO;
import nu.pattern.OpenCV;
import org.innoagencyhack.ocrparser.extractors.scans.ImageHelper;

/**
 *
 * @author yurij
 */
public class Main {
    
    
    public static void main(String[] args) throws Exception {
        System.setProperty("file.encoding", "utf-8"); 
        System.setProperty("sun.java2d.cmm", "sun.java2d.cmm.kcms.KcmsServiceProvider");
        OpenCV.loadShared();

        ImageIO.write(ImageHelper.mat2BufferedImage(ImageHelper.removeNoice(ImageHelper.bufferedImageToMat(ImageIO.read(new File("../1.png"))))), "png", new File("../2.png"));
        
//        try(FileParser parser = new FileParser()) {
//            parser.reParseUndefined();
//        }
    }
}
