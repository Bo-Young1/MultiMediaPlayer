package com.coship.multimediaplayer.utils;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;

/**
 * Created by 980559 on 2017/4/10.
 */
public class ScreenUtils {

    private static int screenW;
    private static int screenH;
    private static float screenDensity;

    public static void initScreen(AppCompatActivity mActivity){
        DisplayMetrics metric = new DisplayMetrics();
        mActivity.getWindowManager().getDefaultDisplay().getMetrics(metric);
        screenW = (int)(metric.widthPixels * 0.6);
        screenH = (int)(metric.heightPixels * 0.8);
        screenDensity = metric.density;
    }

    public static int getScreenW(){
        return screenW;
    }

    public static int getScreenH(){
        return screenH;
    }

    public static float getScreenDensity(){
        return screenDensity;
    }

    //根据手机的分辨率从 dp 的单位 转成为 px(像素)
    public static int dp2px(float dpValue) {
        return (int) (dpValue * getScreenDensity() + 0.5f);
    }

    //根据手机的分辨率从 px(像素) 的单位 转成为 dp
    public static int px2dp(float pxValue) {
        return (int) (pxValue / getScreenDensity() + 0.5f);
    }
}
