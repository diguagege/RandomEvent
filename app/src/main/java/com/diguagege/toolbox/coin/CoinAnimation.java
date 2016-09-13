package com.diguagege.toolbox.coin;

import android.graphics.Camera;
import android.graphics.Matrix;
import android.util.Log;
import android.view.animation.Animation;
import android.view.animation.Transformation;

/**
 * 抛硬币的3D动画，该动画在每半个旋转周期调用接口里的方法，请求View切换View里的内容
 *
 * Created by diguagege on 2016/9/2.
 */
public class CoinAnimation extends Animation {

    public static final int DIRECTION_NONE = 0; // 在该方向上不变化
    public static final int DIRECTION_CLOCKWISE = 1; // 顺时针
    public static final int DIRECTION_ABTUCCLOCKWISE = -1; // 逆时针

    public static final int RESULT_FRONT = 1; // 正面
    public static final int RESULT_REVERSE = -1; // 反面

    /**
     * 圈数
     */
    private int mCircleCount;
    /**
     * x轴旋转方向
     */
    private int mXAxisDirection;
    /**
     * y轴旋转方向
     */
    private int mYAxisDirection;
    /**
     * z轴旋转方向
     */
    private int mZAxisDirection;
    /**
     * 抛硬币的结果
     */
    private int mResult;

    // 总共需要转动的度数
    private int mTotalAngle;
    // 当前ImageView显示的drawable的序号
    private int mCurrentResult = -1;

    private Camera mCamera;

    private int mWidth;
    private int mHeight;

    public CoinAnimation(int circleCount, int xAxisDirection, int yAxisDirection, int zAxisDirection, int result, int width, int height) {
        this.mCircleCount = circleCount;
        this.mXAxisDirection = xAxisDirection;
        this.mYAxisDirection = yAxisDirection;
        this.mZAxisDirection = zAxisDirection;
        this.mResult = result;

        mTotalAngle = 360 * mCircleCount;
        mCamera = new Camera();

        mWidth = width;
        mHeight = height;
    }

    public CoinAnimation(int circleCount, int xAxisDirection, int yAxisDirection, int zAxisDirection, int result) {
        this.mCircleCount = circleCount;
        this.mXAxisDirection = xAxisDirection;
        this.mYAxisDirection = yAxisDirection;
        this.mZAxisDirection = zAxisDirection;
        this.mResult = result;

        mTotalAngle = 360 * mCircleCount;
        mCamera = new Camera();
    }

    private CoinAnimationListener mCoinAnimationListener;

    public void setCoinAnimationListener(CoinAnimationListener mCoinAnimationListener) {
        this.mCoinAnimationListener = mCoinAnimationListener;
        setAnimationListener(mCoinAnimationListener);
    }

    @Override
    public void initialize(int width, int height, int parentWidth, int parentHeight) {
        super.initialize(width, height, parentWidth, parentHeight);
        if (mWidth == 0) {
            mWidth = width;
        }

        if (mHeight == 0) {
            mHeight = height;
        }
    }

    @Override
    protected void applyTransformation(float interpolatedTime, Transformation t) {

        // 在当前周期里的角度数
        int degreeInCircle = ((int) (interpolatedTime * mTotalAngle)) % 360;

        // 改变ImageView里的Drawable
        if (degreeInCircle > 90 && degreeInCircle < 270) {
            if (mCurrentResult != -mResult) {
                mCurrentResult = -mResult;
                // 调用接口，改变ImageView的Drawable
                if (mCoinAnimationListener != null) {
                    mCoinAnimationListener.onDrawableChange(mCurrentResult, this);
                }
            }
        } else {
            if (mCurrentResult != mResult) {
                mCurrentResult = mResult;
                // 调用接口，改变ImageView的Drawable
                if (mCoinAnimationListener != null) {
                    mCoinAnimationListener.onDrawableChange(mCurrentResult, this);
                }
            }
        }

        Matrix matrix = t.getMatrix();

        // 设置偏转的角度
        mCamera.save();
        mCamera.rotate(mXAxisDirection * degreeInCircle, mYAxisDirection * degreeInCircle, mZAxisDirection * degreeInCircle);

        mCamera.getMatrix(matrix);
        mCamera.restore();

        // 在View的中心点旋转
        matrix.preTranslate(-(mWidth >> 1), -(mHeight >> 1));
        matrix.postTranslate(mWidth >> 1, mHeight >> 1);

    }

    public interface CoinAnimationListener extends AnimationListener {

        /**
         * 需要显示硬币的正面/反面
         *
         * @param result    需要显示正面还是反面   CoinImageView.RESULT_FRONT或者CoinImageView.RESULT_REVERSE
         * @param animation The started animation.
         */
        void onDrawableChange(int result, CoinAnimation animation);
    }
}
