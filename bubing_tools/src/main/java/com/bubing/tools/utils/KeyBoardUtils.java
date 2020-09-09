//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.bubing.tools.utils;

import android.content.Context;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

/**
 * @ClassName: KeyBoardUtils
 * @Author: Bubing
 * @Date: 2020/9/9 3:26 PM
 * @Description: 软键盘-弹出/关闭
 */
public class KeyBoardUtils {
    public KeyBoardUtils() {
    }

    public static void openKeybord(EditText mEditText, Context mContext) {
        InputMethodManager imm = (InputMethodManager) mContext.getSystemService("input_method");
        imm.showSoftInput(mEditText, 2);
        imm.toggleSoftInput(2, 1);
    }

    public static void closeKeybord(EditText mEditText, Context mContext) {
        InputMethodManager imm = (InputMethodManager) mContext.getSystemService("input_method");
        imm.hideSoftInputFromWindow(mEditText.getWindowToken(), 0);
    }
}

