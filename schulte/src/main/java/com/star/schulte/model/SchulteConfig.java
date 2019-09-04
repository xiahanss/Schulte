package com.star.schulte.model;

/**
 * 说明：配置
 * 时间：2019/9/4 9:20
 */
public class SchulteConfig {

    private int borderColor = 0xFFCCCCCC;
    private float borderSize = 0.2F;
    private int cellColor = 0xFFFFFFFF;
    private int fontColor = 0xFF666666;
    private float fontSize = 0.6F;
    private int countDownTime = 3000;

    public int getBorderColor() {
        return borderColor;
    }

    public void setBorderColor(int borderColor) {
        this.borderColor = borderColor;
    }

    public float getBorderSize() {
        return borderSize;
    }

    public void setBorderSize(float borderSize) {
        this.borderSize = borderSize;
    }

    public int getCellColor() {
        return cellColor;
    }

    public void setCellColor(int cellColor) {
        this.cellColor = cellColor;
    }

    public int getFontColor() {
        return fontColor;
    }

    public void setFontColor(int fontColor) {
        this.fontColor = fontColor;
    }

    public float getFontSize() {
        return fontSize;
    }

    public void setFontSize(float fontSize) {
        this.fontSize = fontSize;
    }

    public int getCountDownTime() {
        return countDownTime;
    }

    public void setCountDownTime(int countDownTime) {
        this.countDownTime = countDownTime;
    }
}
