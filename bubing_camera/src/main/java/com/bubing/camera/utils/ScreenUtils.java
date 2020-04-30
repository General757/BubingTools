package com.bubing.camera.utils;

import android.content.Context;

/**
 * @ClassName: ScreenUtils
 * @Description: 屏幕相关工具类
 * @Author: bubing
 * @Date: 2020-04-29 15:01
 */
public class ScreenUtils {
    /**
     * 获取屏幕宽度（px）
     *
     * @param context
     * @return
     */
    public static int getScreenWidth(Context context) {
        return context.getResources().getDisplayMetrics().widthPixels;
    }

    /**
     * 获取屏幕高度（px）
     *
     * @param context
     * @return
     */
    public static int getScreenHeight(Context context) {
        return context.getResources().getDisplayMetrics().heightPixels;
    }

    /**
     * 获取屏幕最小（px）
     *
     * @param context
     * @return
     */
    public static float getScreenMin(Context context) {
        return Math.min(getScreenWidth(context), getScreenHeight(context));
    }

    /**
     * 获取屏幕最大（px）
     *
     * @param context
     * @return
     */
    public static float getScreenMax(Context context) {
        return Math.max(getScreenWidth(context), getScreenHeight(context));
    }
}
