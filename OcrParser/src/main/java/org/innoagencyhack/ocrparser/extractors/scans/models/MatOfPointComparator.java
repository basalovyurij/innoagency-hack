package org.innoagencyhack.ocrparser.extractors.scans.models;

import java.util.Comparator;

import org.opencv.core.MatOfPoint;
import org.opencv.imgproc.Imgproc;

public class MatOfPointComparator implements Comparator<MatOfPoint> {

    @Override
    public int compare(MatOfPoint prev, MatOfPoint next) {
        double prevArea = Imgproc.contourArea(prev);
        double nextArea = Imgproc.contourArea(next);
        return (int) (nextArea - prevArea);
    }
}
