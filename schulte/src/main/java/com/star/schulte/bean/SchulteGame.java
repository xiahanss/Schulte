package com.star.schulte.bean;

import com.star.schulte.listener.SchulteListener;
import com.star.schulte.util.SchulteUtil;

/**
 * 说明：游戏实体
 * 时间：2019/9/4 9:27
 */
public class SchulteGame {

    /**
     * 行数
     */
    private int row = 5;

    /**
     * 列数
     */
    private int column = 5;

    /**
     * 方块
     */
    private SchulteCell[][] cells;

    /**
     * 游戏状态
     */
    private SchulteStatus status;

    /**
     * 盲玩
     */
    private boolean blind;

    /**
     * 当前序号
     */
    private int index;

    /**
     * 总点击数
     */
    private int tapTotal;

    /**
     * 正确点击数
     */
    private int tapCorrect;

    /**
     * 错误点击数
     */
    private int tapError;

    /**
     * 配置
     */
    private SchulteConfig config = new SchulteConfig();

    /**
     * 监听器
     */
    private SchulteListener listener;

    /**
     * 开始倒计时
     */
    public void ready() {
        index = 0;
        tapTotal = 0;
        tapCorrect = 0;
        tapError = 0;
        setStatus(SchulteStatus.Ready);
        setCells(null);
    }

    /**
     * 开始游戏
     */
    public void start(SchulteCell[][] cells) {
        setStatus(SchulteStatus.Gaming);
        setCells(cells == null ? SchulteUtil.createCell(row, column) : cells);
        if (listener != null) {
            listener.onStart();
        }
    }

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

    public SchulteStatus getStatus() {
        return status;
    }

    public void setStatus(SchulteStatus status) {
        this.status = status;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public int getTapTotal() {
        return tapTotal;
    }

    public void setTapTotal(int tapTotal) {
        this.tapTotal = tapTotal;
    }

    public int getTapCorrect() {
        return tapCorrect;
    }

    public void setTapCorrect(int tapCorrect) {
        this.tapCorrect = tapCorrect;
    }

    public int getTapError() {
        return tapError;
    }

    public void setTapError(int tapError) {
        this.tapError = tapError;
    }

    public SchulteListener getListener() {
        return listener;
    }

    public void setListener(SchulteListener listener) {
        this.listener = listener;
    }

    public SchulteConfig getConfig() {
        return config;
    }

    public void setConfig(SchulteConfig config) {
        this.config = config;
    }

    public boolean isBlind() {
        return blind;
    }

    public void setBlind(boolean blind) {
        this.blind = blind;
    }
}
