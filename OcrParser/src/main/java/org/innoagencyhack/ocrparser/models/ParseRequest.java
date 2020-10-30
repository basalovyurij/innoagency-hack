package org.innoagencyhack.ocrparser.models;

public class ParseRequest {

    private byte[] file;
    private boolean image;
    private boolean parseTable;

    public ParseRequest(byte[] file, boolean image, boolean parseTable) {
        this.file = file;
        this.image = image;
        this.parseTable = parseTable;
    }
    
    public byte[] getFile() {
        return file;
    }

    public void setFile(byte[] file) {
        this.file = file;
    }

    public boolean isImage() {
        return image;
    }

    public void setImage(boolean image) {
        this.image = image;
    }

    public boolean isParseTable() {
        return parseTable;
    }

    public void setParseTable(boolean parseTable) {
        this.parseTable = parseTable;
    }    
}
