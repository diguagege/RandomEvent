package com.diguagege.toolbox.turntable;

import android.animation.TypeEvaluator;

/**
 * Created by linhanwei on 16/9/11.
 */
public class RotateEvaluator implements TypeEvaluator {
    @Override
    public Object evaluate(float fraction, Object startValue, Object endValue) {
        float startRotate = (float) startValue;
        float endRotate = (float) endValue;
        float currentRotate = startRotate + endRotate * fraction;
        return currentRotate;
    }
}
