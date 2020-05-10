package com.bubing.camera.utils;

import android.text.TextUtils;

/**
 * @ClassName: StringUtils
 * @Description: 字符串工具类
 * @Author: bubing
 * @Date: 2020-05-09 17:54
 */
public class StringUtils {

    public static boolean isEmpty(CharSequence str) {
        return TextUtils.isEmpty(str);
    }

    public static boolean isEmpty(String str) {
        return str == null || str.trim().length() == 0;
    }

    public static String getLastPathSegment(String content) {
        if (content == null || content.length() == 0) {
            return "";
        }
        String[] segments = content.split("/");
        if (segments.length > 0) {
            return segments[segments.length - 1];
        }
        return "";
    }

}
