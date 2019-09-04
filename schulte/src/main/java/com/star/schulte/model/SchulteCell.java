package com.star.schulte.model;

/**
 * 说明：方格
 * 时间：2019/9/4 9:13
 */
public class SchulteCell {

    //值
    private int value;

    //状态
    private SchulteCellStatus status;

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public SchulteCellStatus getStatus() {
        return status;
    }

    public void setStatus(SchulteCellStatus status) {
        this.status = status;
    }
}
