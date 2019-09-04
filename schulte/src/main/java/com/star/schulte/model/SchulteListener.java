package com.star.schulte.model;

/**
 * 说明：
 * 时间：2019/9/4 11:01
 */
public interface SchulteListener {

    void onCountDown(long time);

    void onStart();

    void onProgress(int index);

    void onFinish(int totalTap, int correctTap);

}
