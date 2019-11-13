package com.star.schulte.util;

import android.view.View;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;

/**
 * 说明：动画控制器
 * 时间：2019/9/27 9:49
 */
public class CellAnimation {

    private boolean enable;
    private float progress;
    private float step;
    private boolean reverse;

    private Interpolator interpolator;

    public CellAnimation(long duration) {
        this(duration, new LinearInterpolator());
    }

    public CellAnimation(long duration, Interpolator interpolator) {
        this.step = 16F / duration;
        this.interpolator = interpolator;
    }

    public void start() {
        enable = true;
        progress = 0;
    }

    public float progress() {
        if (reverse) {
            if (!enable) {
                return 0;
            }
            progress = progress - step;
            if (progress < 0) {
                enable = false;
                progress = 0;
            }
        } else {
            if (!enable) {
                return 1;
            }
            progress = progress + step;
            if (progress > 1) {
                enable = false;
                progress = 1;
            }
        }
        return interpolator.getInterpolation(progress);
    }

    public void invalidate(View view) {
        if (enable) {
            view.invalidate();
        }
    }

    public boolean isEnable() {
        return enable;
    }

    public void setEnable(boolean enable) {
        this.enable = enable;
    }

    public float getProgress() {
        return progress;
    }

    public void setProgress(float progress) {
        this.progress = progress;
    }

    public float getStep() {
        return step;
    }

    public void setStep(float step) {
        this.step = step;
    }

    public boolean isReverse() {
        return reverse;
    }

    public void setReverse(boolean reverse) {
        this.reverse = reverse;
    }
}
