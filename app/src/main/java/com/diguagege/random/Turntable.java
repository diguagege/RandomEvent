package com.diguagege.random;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Shader;
import android.graphics.SweepGradient;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.View;

/**
 * Created by hanwei on 16-9-8.
 */
public class Turntable extends View {
    private Paint mCirclePaint;
    private Paint mTrianglePaint;
    int centerX = 560;
    int centerY = 500;
    private Drawable mRedCircle;
    private int mCircleWidth;
    private int mCircleHeight;

    private Matrix mMatrix;
    private Matrix mTriangleMatrix;
    private float mRotate = 0;
    private Shader mShader;
    private Shader mTriangleShader;

    private int mTriangleX = 960; // (100 - 950)
    private int mTriangleY = 560; // (80 - 1040)

    public Turntable(Context context) {
        super(context);
        init();
    }

    private void init() {
        // 初始化外圈圆的相关信息
        mCirclePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mCirclePaint.setStyle(Paint.Style.STROKE);
        mCirclePaint.setStrokeCap(Paint.Cap.ROUND);
        mCirclePaint.setStrokeWidth(20);

        mShader = new SweepGradient(560, 500
                , new int[]{0xFFe02008, 0XFFf74d18}
                , new float[]{0F, 1F});
        mMatrix = new Matrix();
        mMatrix.setRotate(mRotate, 560, 500);
        mShader.setLocalMatrix(mMatrix);
        mCirclePaint.setShader(mShader);

        // 初始化内圈圆
        mRedCircle = getResources().getDrawable(R.drawable.redcircle);
        mCircleWidth = mRedCircle.getIntrinsicWidth();
        mCircleHeight = mRedCircle.getIntrinsicHeight();

        mRedCircle.setBounds(centerX - (mCircleWidth / 2), centerY - (mCircleHeight / 2),
                centerX + (mCircleWidth / 2), centerY + (mCircleHeight / 2));


        // 初始化三角形的信息
        mTriangleMatrix = new Matrix();
        mTriangleMatrix.setRotate(mRotate, 560, 500);
        mTrianglePaint = new Paint();
        mTrianglePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mTrianglePaint.setStrokeWidth(2);
        mTriangleShader = new SweepGradient(560, 500
                , new int[]{0xFFe02008, 0XFFf74d18}
                , new float[]{0F, 1F});
        mTriangleShader.setLocalMatrix(mTriangleMatrix);
        mTrianglePaint.setShader(mShader);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        Log.d("RandomDebug", "OnDraw");
        drawCircle(canvas);
        drawRedBound(canvas);
        drawTriangle(canvas);
        if (mRotate >= 360) {
            mRotate = 0;
        }
        mRotate -= 6;
        mTriangleX = (int) (Math.cos(mRotate) * 480 + centerX);
        mTriangleY = (int) (Math.sin(mRotate) * 480 + centerY);
        invalidate();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    private void drawCircle(Canvas canvas) {
        int radius = centerX > centerY ? centerY : centerX;
        radius -= 150;

        canvas.drawArc(centerX - radius, centerY - radius,
                centerX + radius, centerY + radius, 0, 360, false, mCirclePaint);
    }


    private void drawRedBound(Canvas canvas) {
        mRedCircle.draw(canvas);
    }

    private void drawTriangle(Canvas canvas) {
        Path path4 = new Path();
        path4.moveTo(mTriangleX, mTriangleY);
        path4.quadTo(mTriangleX + 30, mTriangleY - 30, mTriangleX + 60, mTriangleY);
        path4.lineTo(mTriangleX + 30, mTriangleY - 60);
        path4.close();
        canvas.drawPath(path4, mTrianglePaint);
    }
}
