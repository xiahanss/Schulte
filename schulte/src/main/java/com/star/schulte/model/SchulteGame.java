package com.star.schulte.model;

/**
 * 说明：
 * 时间：2019/9/4 9:27
 */
public class SchulteGame {

    private int row = 3;
    private int column = 3;
    private SchulteCell[][] cells;

    public int getRow() {
        return row;
    }

    public void setRow(int row) {
        this.row = row;
    }

    public int getColumn() {
        return column;
    }

    public void setColumn(int column) {
        this.column = column;
    }

    public SchulteCell[][] getCells() {
        return cells;
    }

    public void setCells(SchulteCell[][] cells) {
        this.cells = cells;
    }
}
