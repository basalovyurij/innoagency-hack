package org.innoagencyhack.ocrparser.models;

import java.util.List;

public class TableResponse {
    
    private int id;
    private List<TableResponseCell> cells;

    public void setCells(List<TableResponseCell> cells) {
        this.cells = cells;
    }

    public List<TableResponseCell> getCells() { 
        return cells;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }
}
