package com.diguagege.toolbox.turntable;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Shader;
import android.graphics.SweepGradient;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.animation.DecelerateInterpolator;

import com.diguagege.random.R;

/**
 * Created by linhanwei on 16/9/11.
 */
public class Turntable extends View {
    private Context mContext;
    // 圆心X,Y坐标
    private int mCenterX = 500;
    private int mCenterY = 500;
    private int mRadius = 150;

    private int mDownX;
    private int mDownY;
    private int mMoveX;
    private int mMoveY;
    private long mDownTimeMillis;
    private long mUpTimeMillis;

    private Drawable mRedCircle;

    // 外圈圆形的画板
    private Paint mCirclePaint;
    private Paint mTrianglePaint;

    private Matrix mMatrix;
    private Matrix mTriangleMatrix;

    private float mRotate = 270;
    private Shader mShader;
    private Shader mTriangleShader;

    private int mDistance = 450;
    private int mTriangleX = mCenterX;
    private int mTriangleY = mCenterY - mDistance;

    private ValueAnimator valueAnimator;

    private boolean mIsFirstStart = false;

    private boolean mIsClockwise = true; //转盘是否顺时针

    public Turntable(Context context) {
        super(context);
        init(context);
    }

    public Turntable(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public Turntable(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }


    @Override
    protected void onDraw(Canvas canvas) {
        drawInnerCircle(canvas);
        drawOutCircle(canvas);
        drawTriangle(canvas);
    }

    private void updateTriangleByTouch() {
        float updateRotate = mRotate + angle();
        mTriangleX = (int) (Math.cos(updateRotate * Math.PI / 180) * mDistance + mCenterX) == 0 ? mTriangleX : (int) (Math.cos(updateRotate * Math.PI / 180) * mDistance + mCenterX);
        mTriangleY = (int) (Math.sin(updateRotate * Math.PI / 180) * mDistance + mCenterY) == 0 ? mTriangleY : (int) (Math.sin(updateRotate * Math.PI / 180) * mDistance + mCenterY);
        invalidate();
    }

    private void updateTriangle(float totalAngle, long timeMillis) {
        valueAnimator = ValueAnimator.ofObject(new RotateEvaluator(), mRotate, totalAngle);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float value = (float) animation.getAnimatedValue();
                if (Float.isNaN(value)) {
                    return;
                }
                mRotate = value;
                mTriangleX = (int) (Math.cos(mRotate * Math.PI / 180) * mDistance + mCenterX);
                mTriangleY = (int) (Math.sin(mRotate * Math.PI / 180) * mDistance + mCenterY);
                invalidate();
            }
        });
        valueAnimator.setDuration(timeMillis);
        valueAnimator.setInterpolator(new DecelerateInterpolator());
        valueAnimator.start();
    }

    private void stopUpdateTriangle() {
        if (valueAnimator != null) {
            valueAnimator.cancel();
        }
    }

    /**
     * 绘制内圈圆
     *
     * @param canvas
     */
    private void drawInnerCircle(Canvas canvas) {
        canvas.save();
        mRedCircle.draw(canvas);
        canvas.restore();
    }

    /**
     * 绘制外圈圆
     *
     * @param canvas
     */
    private void drawOutCircle(Canvas canvas) {
        int radius = mCenterX > mCenterY ? mCenterY : mCenterX;
        radius -= mRadius;

        canvas.drawArc(mCenterX - radius, mCenterY - radius,
                mCenterX + radius, mCenterY + radius, 0, 360, false, mCirclePaint);
    }

    private void drawTriangle(Canvas canvas) {
        canvas.drawCircle(mTriangleX, mTriangleY, 30, mTrianglePaint);
    }

    /**
     * 初始化
     */
    private void init(Context context) {
        mIsFirstStart = true;
        mContext = context;
        initInnerCircle();
        initOutCircle();
        initTriangle();
        updateTriangle(45, 2000);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mIsFirstStart = false;
            }
        }, 2000);
    }

    /**
     * 初始化内圈圆
     */
    private void initInnerCircle() {
        // 初始化内圈圆
        mRedCircle = getResources().getDrawable(R.drawable.redcircle);
        int mCircleWidth = mRedCircle.getIntrinsicWidth();
        int mCircleHeight = mRedCircle.getIntrinsicHeight();

        mRedCircle.setBounds(mCenterX - (mCircleWidth / 2), mCenterY - (mCircleHeight / 2),
                mCenterX + (mCircleWidth / 2), mCenterY + (mCircleHeight / 2));
    }

    /**
     * 初始化外圈圆
     */
    private void initOutCircle() {
        // 初始化外圈圆的相关信息
        mCirclePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mCirclePaint.setStyle(Paint.Style.STROKE);
        mCirclePaint.setStrokeCap(Paint.Cap.ROUND);
        mCirclePaint.setStrokeWidth(20);

        mShader = new SweepGradient(560, 500
                , new int[]{0xFFe02008, 0XFFf74d18}
                , new float[]{0F, 1F});
        mMatrix = new Matrix();
        mMatrix.setRotate(mRotate, mCenterX, mCenterY);
        mShader.setLocalMatrix(mMatrix);
        mCirclePaint.setShader(mShader);
    }

    /**
     * 初始化三角形（还不确定用什么方式，是自己绘制还是贴图）
     */
    private void initTriangle() {
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
    public boolean onTouchEvent(MotionEvent event) {
        if (mIsFirstStart) {
            return true;
        }
        int action = event.getAction();
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                stopUpdateTriangle();
                mDownX = (int) event.getX();
                mDownY = (int) event.getY();
                mMoveX = mDownX;
                mMoveY = mDownY;
                if ((mDownX < mTriangleX - 30 || mDownX > mTriangleX + 30) && (mDownY < mTriangleY - 30 || mDownY > mTriangleY + 30)) {
                    return false;
                }
                mDownTimeMillis = System.currentTimeMillis();
                break;
            case MotionEvent.ACTION_MOVE:
                mMoveX = (int) event.getX();
                mMoveY = (int) event.getY();
                mUpTimeMillis = System.currentTimeMillis();
                if ((mDownX - mCenterX) * (mMoveY - mCenterY) - (mDownY - mCenterY) * (mMoveX - mCenterX) > 0) {
                    mIsClockwise = true;
                } else {
                    mIsClockwise = false;
                }
                updateTriangleByTouch();
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                int x = (int) (5000 / (mUpTimeMillis - mDownTimeMillis));
                mRotate += angle();
                long timeMillis = 5000;
                float distance = angle() * x;
                if (Math.abs(distance) < 1000f) {
                    timeMillis = 1500;
                } else if (Math.abs(distance) >= 1000f && Math.abs(distance) < 2000f) {
                    timeMillis = 4000;
                } else {
                    timeMillis = 5000;
                }
                updateTriangle(distance, timeMillis);
                mIsClockwise = true;
                break;
        }
        return true;
    }


    /**
     * 计算滑动角度
     * @return
     */
    public float angle() {
        float dx1, dx2, dy1, dy2;
        float angle;
        dx1 = mDownX - mCenterX;
        dy1 = mDownY - mCenterY;
        dx2 = mMoveX - mCenterX;
        dy2 = mMoveY - mCenterY;
        float c = (float) Math.sqrt(dx1 * dx1 + dy1 * dy1) * (float) Math.sqrt(dx2 * dx2 + dy2 * dy2);
        if (c == 0) return -1;
        angle = (float) (Math.acos((dx1 * dx2 + dy1 * dy2) / c) * 180 / Math.PI);
        if (Float.isNaN(angle)) {
            angle = 0;
        }
        return mIsClockwise ? angle : -angle;
    }
}
