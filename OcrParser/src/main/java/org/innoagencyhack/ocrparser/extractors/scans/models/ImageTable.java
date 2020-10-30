package org.innoagencyhack.ocrparser.extractors.scans.models;

import java.util.ArrayList;

import org.opencv.core.Mat;

public class ImageTable {

    public Mat Image;
    public ArrayList<Mat> Cells;

    public ImageTable(Mat img, ArrayList<Mat> tableCells) {
        Image = img;
        Cells = tableCells;
    }
}
