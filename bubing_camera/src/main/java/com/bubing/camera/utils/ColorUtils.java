package com.bubing.camera.utils;

/**
 * @ClassName: ColorUtils
 * @Description: java类作用描述
 * @Author: bubing
 * @Date: 2020-05-09 18:09
 */
public class ColorUtils {
    /**
     * 判断颜色是否偏黑色
     *
     * @param color 颜色
     * @return
     */
    public static boolean isBlackColor(int color) {
        int grey = toGrey(color);
        return grey < 50;
    }

    /**
     * 颜色转换成灰度值
     *
     * @param rgb 颜色
     * @return　灰度值
     */
    public static int toGrey(int rgb) {
        int blue = rgb & 0x000000FF;
        int green = (rgb & 0x0000FF00) >> 8;
        int red = (rgb & 0x00FF0000) >> 16;
        return (red * 38 + green * 75 + blue * 15) >> 7;
    }

    public static boolean isWhiteColor(int color) {
        int grey = toGrey(color);
        return grey > 200;
    }
}
