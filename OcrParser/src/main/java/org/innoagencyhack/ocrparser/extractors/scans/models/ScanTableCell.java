package org.innoagencyhack.ocrparser.extractors.scans.models;

import java.util.Comparator;
import org.opencv.core.Mat;
import org.opencv.core.Rect;

public class ScanTableCell {

    public Mat image;
    public int x;
    public int y;
    public int width;
    public int height;

    public ScanTableCell(Rect rectangle, Mat image) {
        this.image = image;
        x = rectangle.x;
        y = rectangle.y;
        width = rectangle.width;
        height = rectangle.height;
    }
    
    public static class RowComparator implements Comparator<ScanTableCell> {

        @Override
        public int compare(ScanTableCell prev, ScanTableCell next) {
            return prev.y - next.y;
        }
    }
    
    public static class ColumnComparator implements Comparator<ScanTableCell> {

        @Override
        public int compare(ScanTableCell prev, ScanTableCell next) {
            return prev.x - next.x;
        }
    }
}
