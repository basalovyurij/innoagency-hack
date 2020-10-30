package org.innoagencyhack.ocrparser.models;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.List;

public class ParseResponse {
    
    private List<TableResponse> tables;
    private String text;
    private String error;

    public ParseResponse(Throwable ex) {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        ex.printStackTrace(pw);
        error = sw.toString();
    }
    
    public ParseResponse(List<TableResponse> tables, String text) {
        this.tables = tables;
        this.text = text;
    }
    
    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public List<TableResponse> getTables() {
        return tables;
    }
    
    public void setTables(List<TableResponse> tables) {
        this.tables = tables;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }
}
