package com.diguagege.random;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;

import com.diguagege.toolbox.coin.CoinView;

import java.util.Random;

/**
 * Created by linhanwei on 16/9/12.
 */
public class CoinActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.coin_main);
        final int circleCount = getResources().getInteger(R.integer.coin_default_circleCount);

        final CoinView img = (CoinView) findViewById(R.id.tiv);

//        ScaleAnimation scaleSmallToBigAnimation = new ScaleAnimation(1f, 2f, 1f, 2f,
//                Animation.RELATIVE_TO_SELF, 0f, Animation.RELATIVE_TO_SELF, 0f);
//        scaleSmallToBigAnimation.setDuration(2500);
//        scaleSmallToBigAnimation.setRepeatMode(Animation.REVERSE);
//        scaleSmallToBigAnimation.setRepeatCount(1);
//        scaleSmallToBigAnimation.setInterpolator(new AccelerateDecelerateInterpolator());
//
//
//        TranslateAnimation translateAnimation = new TranslateAnimation(
//                Animation.RELATIVE_TO_PARENT, 0f,
//                Animation.RELATIVE_TO_PARENT, -0.16f,//以自身0.5宽度为轴
//                Animation.RELATIVE_TO_PARENT, 0f,
//                Animation.RELATIVE_TO_PARENT, -0.16f);//以y轴原点进行计算
//
//        translateAnimation.setDuration(2500);
//        translateAnimation.setRepeatMode(Animation.REVERSE);
//        translateAnimation.setRepeatCount(1);
//
//
//        img.addOtherAnimation(scaleSmallToBigAnimation);
//        img.addOtherAnimation(translateAnimation);
//        img.addOtherAnimation(scaleBigToSmallAnimation);



        Button btn = (Button) findViewById(R.id.start);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                img.setInterpolator(new DecelerateInterpolator())
                        .setDuration(5000)
                        .setCircleCount(circleCount)
                        .setXAxisDirection(CoinView.DIRECTION_ABTUCCLOCKWISE)
                        .setYAxisDirection(CoinView.DIRECTION_NONE)
                        .setZAxisDirection(CoinView.DIRECTION_NONE)
                        .setResult(new Random().nextInt(2) == 0 ? CoinView.RESULT_FRONT : CoinView.RESULT_REVERSE)
                        .startCoin();
            }
        });
    }
}
