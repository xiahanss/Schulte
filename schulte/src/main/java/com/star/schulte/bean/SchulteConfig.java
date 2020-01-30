package com.star.schulte.bean;

/**
 * 说明：舒尔特配置
 * 时间：2019/9/4 9:20
 */
public class SchulteConfig {

    //边框色
    private int borderColor = 0xFFCCCCCC;
    //边框大小
    private float borderSize = 0.2F;
    //方块色
    private int cellColor = 0xFFFFFFFF;
    //方块按下色
    private int cellPressColor = 0xFFF0F0F0;
    //字体色
    private int fontColor = 0xFF666666;
    //字体大小
    private float fontSize = 0.6F;
    //圆角
    private float corner = 0.1F;
    //开启动画
    private boolean animation = true;

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

    public int getCellPressColor() {
        return cellPressColor;
    }

    public void setCellPressColor(int cellPressColor) {
        this.cellPressColor = cellPressColor;
    }

    public float getCorner() {
        return corner;
    }

    public void setCorner(float corner) {
        this.corner = corner;
    }

    public boolean isAnimation() {
        return animation;
    }

    public void setAnimation(boolean animation) {
        this.animation = animation;
    }
}
