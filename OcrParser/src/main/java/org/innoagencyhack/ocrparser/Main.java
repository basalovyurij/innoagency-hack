package org.innoagencyhack.ocrparser;

import nu.pattern.OpenCV;

/**
 *
 * @author yurij
 */
public class Main {
    
    
    public static void main(String[] args) throws Exception {
        System.setProperty("file.encoding", "utf-8"); 
        System.setProperty("sun.java2d.cmm", "sun.java2d.cmm.kcms.KcmsServiceProvider");
        OpenCV.loadShared();
        
        try(FileParser parser = new FileParser()) {
            parser.run();
        }
    }
}
