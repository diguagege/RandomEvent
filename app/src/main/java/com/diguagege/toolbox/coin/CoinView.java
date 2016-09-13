package com.diguagege.toolbox.coin;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.util.AttributeSet;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;
import android.widget.ImageView;


import com.diguagege.random.R;

import java.util.HashSet;
import java.util.Set;

/**
 * 模拟硬币翻转的ImageView
 * <p/>
 * Created by diguagege on 2016/9/11.
 */
public class CoinView extends ImageView {

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
    /**
     * 动画时间
     */
    private int mDuration;
    /**
     * 延迟时间
     */
    private int mStartOffset;
    /**
     * Interpolator
     */
    private Interpolator mInterpolator = new DecelerateInterpolator();

    /**
     * 硬币的正面
     */
    private Drawable mFrontDrawable;

    /**
     * 硬币的反面
     */
    private Drawable mReversetDrawable;

    /**
     * 抛硬币的回调函数
     */
    private CoinAnimation.CoinAnimationListener mCoinAnimationListener;

    private Set<Animation> mOtherAnimation = new HashSet<Animation>();

    public CoinView(Context context) {
        super(context);
        setCoinDrawableIfNecessage();
    }

    public CoinView(Context context, AttributeSet attrs) {
        super(context, attrs);

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.CoinAnimation);

        mCircleCount = a.getInteger(R.styleable.CoinAnimation_circleCount, context.getResources().getInteger(R.integer.coin_default_circleCount));
        mXAxisDirection = a.getInteger(R.styleable.CoinAnimation_xAxisDirection, context.getResources().getInteger(R.integer.coin_default_xAxisDirection));
        mYAxisDirection = a.getInteger(R.styleable.CoinAnimation_yAxisDirection, context.getResources().getInteger(R.integer.coin_default_yAxisDirection));
        mZAxisDirection = a.getInteger(R.styleable.CoinAnimation_zAxisDirection, context.getResources().getInteger(R.integer.coin_default_zAxisDirection));
        mResult = a.getInteger(R.styleable.CoinAnimation_result, context.getResources().getInteger(R.integer.coin_default_result));

        mFrontDrawable = a.getDrawable(R.styleable.CoinAnimation_frontDrawable);
        mReversetDrawable = a.getDrawable(R.styleable.CoinAnimation_reverseDrawable);

        mDuration = a.getInteger(R.styleable.CoinAnimation_duration, context.getResources().getInteger(R.integer.coin_default_duration));
        mStartOffset = a.getInteger(R.styleable.CoinAnimation_startOffset, context.getResources().getInteger(R.integer.coin_default_startOffset));

        a.recycle();

        setCoinDrawableIfNecessage();
    }

    public CoinView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.CoinAnimation, defStyleAttr, 0);

        mCircleCount = a.getInteger(R.styleable.CoinAnimation_circleCount, context.getResources().getInteger(R.integer.coin_default_circleCount));
        mXAxisDirection = a.getInteger(R.styleable.CoinAnimation_xAxisDirection, context.getResources().getInteger(R.integer.coin_default_xAxisDirection));
        mYAxisDirection = a.getInteger(R.styleable.CoinAnimation_yAxisDirection, context.getResources().getInteger(R.integer.coin_default_yAxisDirection));
        mZAxisDirection = a.getInteger(R.styleable.CoinAnimation_zAxisDirection, context.getResources().getInteger(R.integer.coin_default_zAxisDirection));
        mResult = a.getInteger(R.styleable.CoinAnimation_result, context.getResources().getInteger(R.integer.coin_default_result));

        mFrontDrawable = a.getDrawable(R.styleable.CoinAnimation_frontDrawable);
        mReversetDrawable = a.getDrawable(R.styleable.CoinAnimation_reverseDrawable);

        mDuration = a.getInteger(R.styleable.CoinAnimation_duration, context.getResources().getInteger(R.integer.coin_default_duration));
        mStartOffset = a.getInteger(R.styleable.CoinAnimation_startOffset, context.getResources().getInteger(R.integer.coin_default_startOffset));

        a.recycle();

        setCoinDrawableIfNecessage();
    }

    /**
     * 设置mFrontDrawable和mReversetDrawable的值
     */
    private void setCoinDrawableIfNecessage() {
        if (mFrontDrawable == null) {
            mFrontDrawable = getDrawable();
        }
        if (mReversetDrawable == null) {
            mReversetDrawable = getDrawable();
        }
    }

    @Override
    public void setImageBitmap(Bitmap bm) {
        super.setImageBitmap(bm);
        setCoinDrawableIfNecessage();
    }

    @Override
    public void setImageDrawable(Drawable drawable) {
        super.setImageDrawable(drawable);
        setCoinDrawableIfNecessage();
    }

    @Override
    public void setImageResource(int resId) {
        super.setImageResource(resId);
        setCoinDrawableIfNecessage();
    }

    @Override
    public void setImageURI(Uri uri) {
        super.setImageURI(uri);
        setCoinDrawableIfNecessage();
    }

    /**
     * 设置硬币旋转的圈数
     *
     * @param circleCount
     * @return
     */
    public CoinView setCircleCount(int circleCount) {
        this.mCircleCount = circleCount;
        return this;
    }

    /**
     * 设置x轴旋转方向
     *
     * @param xAxisDirection CoinAnimation.DIRECTION_NONE  or  CoinAnimation.DIRECTION_CLOCKWISE  or  CoinAnimation.DIRECTION_ABTUCCLOCKWISE
     * @return
     */
    public CoinView setXAxisDirection(int xAxisDirection) {
        if(Math.abs(xAxisDirection) > 1){
            throw new RuntimeException("Math.abs(Direction) must be less than 1");
        }
        this.mXAxisDirection = xAxisDirection;
        return this;
    }

    /**
     * 设置y轴旋转方向
     *
     * @param yAxisDirection CoinAnimation.DIRECTION_NONE  or  CoinAnimation.DIRECTION_CLOCKWISE  or  CoinAnimation.DIRECTION_ABTUCCLOCKWISE
     * @return
     */
    public CoinView setYAxisDirection(int yAxisDirection) {
        if(Math.abs(yAxisDirection) > 1){
            throw new RuntimeException("Math.abs(Direction) must be less than 1");
        }
        this.mYAxisDirection = yAxisDirection;
        return this;
    }

    /**
     * 设置z轴选装方向
     *
     * @param zAxisDirection CoinAnimation.DIRECTION_NONE  or  CoinAnimation.DIRECTION_CLOCKWISE  or  CoinAnimation.DIRECTION_ABTUCCLOCKWISE
     * @return
     */
    public CoinView setZAxisDirection(int zAxisDirection) {
        if(Math.abs(zAxisDirection) > 1){
            throw new RuntimeException("Math.abs(Direction) must be less than 1");
        }
        this.mZAxisDirection = zAxisDirection;
        return this;
    }

    /**
     * 设置抛硬币的结果
     *
     * @param result CoinAnimation.RESULT_FRONT  正面
     *               CoinAnimation.RESULT_REVERSE  反面
     * @return
     */
    public CoinView setResult(int result) {
        if(Math.abs(result) != 1){
            throw new RuntimeException("Math.abs(Direction) must be 1");
        }
        this.mResult = result;
        return this;
    }

    /**
     * 设置硬币的正面图像
     *
     * @param frontDrawable
     * @return
     */
    public CoinView setFrontDrawable(Drawable frontDrawable) {
        this.mFrontDrawable = frontDrawable;
        return this;
    }

    /**
     * 设置硬币的反面图像
     *
     * @param reversetDrawable
     * @return
     */
    public CoinView setReversetDrawable(Drawable reversetDrawable) {
        this.mReversetDrawable = reversetDrawable;
        return this;
    }

    /**
     * 设置动画持续时间
     *
     * @param duration
     * @return
     */
    public CoinView setDuration(int duration) {
        this.mDuration = duration;
        return this;
    }

    /**
     * 设置动画延迟时间
     *
     * @param startOffset
     * @return
     */
    public CoinView setStartOffset(int startOffset) {
        this.mStartOffset = startOffset;
        return this;
    }

    /**
     * set Interpolator
     *
     * @param interpolator
     * @return
     */
    public CoinView setInterpolator(Interpolator interpolator) {
        this.mInterpolator = interpolator;
        return this;
    }

    /**
     * 添加一个Animation
     *
     * @param animation
     * @return
     */
    public CoinView addOtherAnimation(Animation animation) {
        mOtherAnimation.add(animation);
        return this;
    }

    /**
     * 移除一个Animation
     *
     * @param animation
     * @return
     */
    public CoinView removeOtherAnimation(Animation animation) {
        mOtherAnimation.remove(animation);
        return this;
    }

    /**
     * 清空Animation
     *
     * @return
     */
    public CoinView cleareOtherAnimation() {
        mOtherAnimation.clear();
        return this;
    }

    /**
     * 设置动画回调接口
     *
     * @param CoinAnimationListener
     * @return
     */
    public CoinView setCoinAnimationListener(CoinAnimation.CoinAnimationListener coinAnimationListener) {
        this.mCoinAnimationListener = coinAnimationListener;
        return this;
    }

    /**
     * 开始抛硬币动画
     */
    public void startCoin() {

        clearAnimation();

        CoinAnimation coinAnimation = new CoinAnimation(mCircleCount, mXAxisDirection, mYAxisDirection, mZAxisDirection, mResult, 400, 400);
        coinAnimation.setDuration(mDuration);
        coinAnimation.setStartOffset(mStartOffset);
        coinAnimation.setInterpolator(mInterpolator);
        coinAnimation.setCoinAnimationListener(new QTCoinAnimationListener(mCoinAnimationListener));

        AnimationSet as = new AnimationSet(false);
        as.addAnimation(coinAnimation);

        for (Animation animation : mOtherAnimation) {
            as.addAnimation(animation);
        }

        startAnimation(as);

    }

    /**
     * 可以改变ImageView的监听器
     */
    public class QTCoinAnimationListener implements CoinAnimation.CoinAnimationListener {

        private CoinAnimation.CoinAnimationListener mCoinAnimationListener;

        public QTCoinAnimationListener(CoinAnimation.CoinAnimationListener CoinAnimationListener) {
            mCoinAnimationListener = CoinAnimationListener;
        }

        @Override
        public void onDrawableChange(int result, CoinAnimation animation) {
            switch (result) {
                case CoinAnimation.RESULT_FRONT:
                    setImageDrawable(mFrontDrawable);
                    break;
                case CoinAnimation.RESULT_REVERSE:
                    setImageDrawable(mReversetDrawable);
                    break;
            }
            if (mCoinAnimationListener != null) {
                mCoinAnimationListener.onDrawableChange(result, animation);
            }
        }

        @Override
        public void onAnimationStart(Animation animation) {
            if (mCoinAnimationListener != null) {
                mCoinAnimationListener.onAnimationStart(animation);
            }
        }

        @Override
        public void onAnimationEnd(Animation animation) {
            if (mCoinAnimationListener != null) {
                mCoinAnimationListener.onAnimationEnd(animation);
            }
        }

        @Override
        public void onAnimationRepeat(Animation animation) {
            if (mCoinAnimationListener != null) {
                mCoinAnimationListener.onAnimationRepeat(animation);
            }
        }
    }
}
