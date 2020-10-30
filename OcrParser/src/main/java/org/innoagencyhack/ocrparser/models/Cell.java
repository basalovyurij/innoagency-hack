package org.innoagencyhack.ocrparser.models;

public class Cell {

    private final int Width;
    private final int Height;
    private final int Colspan;
    private final int Rowspan;
    private final int Rownum;
    private final int Colnum;
    private final String Text;

    public Cell(int width, int height, int colspan, int rowspan, int rownum, int colnum, String text) {
        Width = width;
        Height = height;
        Colspan = colspan;
        Rowspan = rowspan;
        Rownum = rownum;
        Colnum = colnum;
        Text = text;
    }

    public int getWidth() {
        return Width;
    }

    public int getHeight() {
        return Height;
    }

    public int getColspan() {
        return Colspan;
    }

    public int getRowspan() {
        return Rowspan;
    }

    public int getRownum() {
        return Rownum;
    }

    public int getColnum() {
        return Colnum;
    }

    public String getText() {
        return Text;
    }
}
