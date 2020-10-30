package org.innoagencyhack.ocrparser.extractors.scans;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;
import javax.imageio.ImageIO;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.core.MatOfPoint;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.innoagencyhack.ocrparser.extractors.scans.models.ImageKernelContainer;
import org.innoagencyhack.ocrparser.extractors.scans.models.ImgCell;

public class ImageHelper {

    private static final double THRESH = 192;
    private static final double MAX_VAL = 255;
    private static final int TRESH_TYPE = Imgproc.THRESH_BINARY | Imgproc.THRESH_OTSU;
    private static final double ALPHA = 0.5;
    private static final double BETA = 1.0 - ALPHA;
    
    public static Mat bufferedImageToMat(BufferedImage bi) throws Exception {
        try(ByteArrayOutputStream bis = new ByteArrayOutputStream()) {
            ImageIO.write(bi, "png", bis);
            bis.flush();
            Mat mat = Imgcodecs.imdecode(new MatOfByte(bis.toByteArray()), Imgcodecs.CV_LOAD_IMAGE_UNCHANGED);
            
            if(bi.getType() == BufferedImage.TYPE_BYTE_GRAY)
                return mat;
            
            Mat res = new Mat();
            Imgproc.cvtColor(mat, res, Imgproc.COLOR_BGR2GRAY);
            return res;
        }
    }

    public static BufferedImage mat2BufferedImage(Mat image) throws Exception {
        MatOfByte mob = new MatOfByte();
        Imgcodecs.imencode(".png", image, mob);

        try(ByteArrayInputStream bis = new ByteArrayInputStream(mob.toArray())) {
            return ImageIO.read(bis);
        }
    }
    
    public static List<Mat> getTextRegions(Mat image) throws Exception {
        int fontSize = fontHeight(image);
        Mat kernel = Imgproc.getStructuringElement(Imgproc.MORPH_RECT, new Size(fontSize * 0.4, fontSize * 0.7));
        Mat erode = new Mat();
        Imgproc.erode(image, erode, kernel, new Point(-1,-1), 2);
        
        Mat tresh_img = getTresholdImage(erode);
        
        ArrayList<MatOfPoint> contours = getContours(tresh_img);
        List<Mat> res = new ArrayList<>();
        List<Rect> rectList = new ArrayList<>();
        for(MatOfPoint contour : contours) {
            Rect rect = Imgproc.boundingRect(contour);
            if(Math.min(rect.size().height, rect.size().width) > fontSize*1.2 & checkSize(image, rect)) {
                rectList.add(rect);
                Imgproc.rectangle(image, rect.br(), rect.tl(), new Scalar(0, 255, 0));
            }
        }
        rectList = sortRect(rectList);
        for(Rect rect : rectList){
            res.add(new Mat(image, rect)); 
        }
        
        return res;
    }
    
    public static List<Rect> sortRect(List<Rect> rectList){
        int delta = 30;
        List<Rect> sortList = new ArrayList<>();
        int firstmin, secondmin;
        while (rectList.size()>1){
        firstmin = minY(rectList);
        secondmin = minYHeigth(rectList, firstmin);
        FirstRectIs flag = firstRectInsideOutsideNo(rectList.get(firstmin), rectList.get(secondmin));
        if (flag!=FirstRectIs.No){
            if (flag == FirstRectIs.Outside){
                rectList.remove(secondmin);
            }
            if (flag == FirstRectIs.Inside){
                rectList.remove(firstmin);
            }    
            continue;
        }
        if (rectList.get(firstmin).y + rectList.get(firstmin).height + delta < rectList.get(secondmin).y + rectList.get(secondmin).height){
            sortList.add(rectList.get(firstmin));
            rectList.remove(firstmin);
        } else if (rectList.get(firstmin).x > rectList.get(secondmin).x) {
            sortList.add(rectList.get(secondmin));
            rectList.remove(secondmin);
        } else {
            sortList.add(rectList.get(firstmin));
            rectList.remove(firstmin);
        }
        }
        sortList.add(rectList.get(0));
        
        return sortList;
    }
    
    public enum FirstRectIs {No, Inside, Outside}
    
    public static FirstRectIs firstRectInsideOutsideNo(Rect first, Rect second){
        FirstRectIs flag = FirstRectIs.No;
        if (first.y<second.y & first.x<second.x & first.y+first.height>second.y+second.height & first.x+first.width>second.x+second.width){
            flag = FirstRectIs.Outside;
        }
        if (first.y>second.y & first.x>second.x & first.y+first.height<second.y+second.height & first.x+first.width<second.x+second.width){
            flag = FirstRectIs.Inside;
        }           
        return flag;
    }
        
    public static int minY(List<Rect> rectList){
        int indexMin = 0;
        for( int i=1; i<rectList.size(); i++){
            if (rectList.get(indexMin).y > rectList.get(i).y){
                indexMin = i;
            }
        }    
    return indexMin;
    }
    
    public static int minYHeigth(List<Rect> rectList, int exception){
    int indexMin = exception!=0? 0 : 1;
    for( int i=0; i<rectList.size(); i++){
        if (((rectList.get(indexMin).y + rectList.get(indexMin).height) > (rectList.get(i).y + rectList.get(i).height))& i!=exception){
            indexMin = i;
        }
    }    
    return indexMin;
    }
    
    public static boolean checkSize(Mat image, Rect rect){
        return !(image.size().height == rect.height | image.size().width == rect.width);
    }
    public static Mat getTresholdImage(Mat image) {
        Mat threshImg = new Mat();
        Imgproc.threshold(image, threshImg, THRESH, MAX_VAL, TRESH_TYPE);
        return threshImg;
    }

    public static Mat getReversedTresholdImage(Mat image) {
        Mat threshImg = getTresholdImage(image);
        Mat reversedImg = new Mat();
        Core.bitwise_not(threshImg, reversedImg);
        return reversedImg;
    }

    public static Mat getContoursImage(Mat image, ImageKernelContainer kernels) {
        Mat vErodeImg = new Mat();
        Imgproc.erode(image, vErodeImg, kernels.verticalKernel);
        Mat verticalLinesImg = new Mat();
        Imgproc.dilate(vErodeImg, verticalLinesImg, kernels.verticalKernel);

        Mat hErodeImg = new Mat();
        Imgproc.erode(image, hErodeImg, kernels.horizontalKernel);
        Mat horizontalLinesImg = new Mat();
        Imgproc.dilate(hErodeImg, horizontalLinesImg, kernels.horizontalKernel);

        Mat finalImg = new Mat();
        Core.addWeighted(verticalLinesImg, ALPHA, horizontalLinesImg, BETA, 0.0, finalImg);

        Mat erodeFinalImg = new Mat();
        Imgproc.erode(finalImg, erodeFinalImg, kernels.mainKernel);

        Mat contoursImg = getTresholdImage(erodeFinalImg);
        //Core.bitwise_not(erodeFinalImg, contoursImg);

        return contoursImg;
    }

    public static ArrayList<MatOfPoint> getContours(Mat image) {
        ArrayList<MatOfPoint> contours = new ArrayList<>();
        Mat hierarchy = new Mat();
        Imgproc.findContours(image, contours, hierarchy, Imgproc.RETR_TREE, Imgproc.CHAIN_APPROX_SIMPLE);
        return contours;
    }

    public static ImgCell getOriginalFragment(MatOfPoint contour, Mat originalImage) {
        Rect boundingRect = Imgproc.boundingRect(contour);
        Mat fragment = new Mat(originalImage, boundingRect);
        return new ImgCell(fragment, contour, boundingRect);
    }

    public static BufferedImage getPreparedImage(Mat image) throws Exception {
        Mat normalized = new Mat();
        Core.normalize(image, normalized, 0, 255, Core.NORM_MINMAX);
        Mat blured = new Mat();
        Imgproc.GaussianBlur(normalized, blured, new Size(new Point(5, 5)), 0);
        Mat threshImage = getTresholdImage(blured);

        Point startPoint = new Point(0, 0);
        Point endPoint = new Point(image.width(), image.height());
        Scalar color = new Scalar(255, 255, 255);
        Imgproc.rectangle(threshImage, startPoint, endPoint, color, 2);
        
        return mat2BufferedImage(threshImage);
    }
    
    public static ImageKernelContainer getKernels(Mat image) {
        int kernelLength = fontHeight(image);
        Mat vertical = Imgproc.getStructuringElement(Imgproc.MORPH_RECT, new Size(1, kernelLength));
        Mat horizontal = Imgproc.getStructuringElement(Imgproc.MORPH_RECT, new Size(kernelLength, 1));
        Mat main = Imgproc.getStructuringElement(Imgproc.MORPH_RECT, new Size(1, 1));
        return new ImageKernelContainer(vertical, horizontal, main);
    }
    
    private static int fontHeight(Mat image) {
        return (int)(image.size().height / 80.0);
    }

    public static boolean isBlank(Mat image) {
        Mat reversed = new Mat();
        Core.bitwise_not(image, reversed);
        Integer countOfBlackPoints = Core.countNonZero(reversed);
        return countOfBlackPoints < image.elemSize() * 0.2;
    }
    
    public static BufferedImage rotate90(BufferedImage src, int angle) {
        double theta = (Math.PI * 2) / 360 * (360 - angle);
        int width = src.getWidth();
        int height = src.getHeight();

        BufferedImage dest;
        if(angle == 180) {
            dest = new BufferedImage(width, height, src.getType());
        } else {
            dest = new BufferedImage(height, width, src.getType());
        }
        
        Graphics2D graphics2D = dest.createGraphics();
        
        switch (angle) {
            case 90:
                graphics2D.translate((width - height) / 2, (width - height) / 2);
                graphics2D.rotate(theta, height / 2, width / 2);
                break;
            case 180:
                graphics2D.translate(0, 0);
                graphics2D.rotate(theta, width / 2, height / 2);
                break;
            case 270:
                graphics2D.translate((height - width) / 2, (height - width) / 2);
                graphics2D.rotate(theta, height / 2, width / 2);
                break;
        }
        
        graphics2D.drawRenderedImage(src, null);

        return dest;
    }
}
