package org.innoagencyhack.ocrparser.models;

public class TableResponseCell {

    private String text;
    private final double width;
    private final int rowNumber;
    private final int colNumber;
    private final int gridSpan;
    private final int rowSpan;

    public TableResponseCell(String text, double width, int rowNumber, int colNumber, int gridSpan, int rowSpan) {
        this.text = text;
        this.width = width;
        this.rowNumber = rowNumber;
        this.colNumber = colNumber;
        this.gridSpan = gridSpan;
        this.rowSpan = rowSpan;
    }

    public String getText() {
        return text;
    }
    
    public void setText(String text) {
        this.text = text;
    }
     
    public double getWidth() {
        return width;
    }

    public int getRowNumber() {
        return rowNumber;
    }

    public int getColNumber() {
        return colNumber;
    }

    public int getGridSpan() {
        return gridSpan;
    }

    public int getRowSpan() {
        return rowSpan;
    }
}
