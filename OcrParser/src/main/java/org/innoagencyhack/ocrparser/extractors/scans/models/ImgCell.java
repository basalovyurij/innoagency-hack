package org.innoagencyhack.ocrparser.extractors.scans.models;

import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.Rect;

public class ImgCell {

    public Mat Image;
    public MatOfPoint Contour;
    public Rect Rectangle;

    public ImgCell(Mat image, MatOfPoint contour, Rect rect) {
        Image = image;
        Contour = contour;
        Rectangle = rect;
    }
}
