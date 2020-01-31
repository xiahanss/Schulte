package com.star.schulte.listener;

/**
 * 说明：游戏监听器
 * 时间：2019/9/4 11:01
 */
public interface SchulteListener {

    /**
     * 准备开始
     */
    void onReady();

    /**
     * 开始游戏
     */
    void onStart();

    /**
     * 点击错误
     */
    void onTapError(int index, int currentIndex);

    /**
     * 点击正确
     */
    void onProgress(int index, int maxIndex);

    /**
     * 游戏完成
     */
    void onFinish(int totalTap, int correctTap);

}
