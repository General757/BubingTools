//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.bubing.tools.utils;

import android.content.Context;

/**
 * @ClassName: RUtils
 * @Author: Bubing
 * @Date: 2020/9/9 4:09 PM
 * @Description: java类作用描述
 */
public class RUtils {
    public static final String POINT = ".";
    public static final String R = "R";
    public static final String JOIN = "$";
    public static final String ANIM = "anim";
    public static final String ATTR = "attr";
    public static final String COLOR = "color";
    public static final String DIMEN = "dimen";
    public static final String DRAWABLE = "drawable";
    public static final String ID = "id";
    public static final String LAYOUT = "layout";
    public static final String MENU = "menu";
    public static final String RAW = "raw";
    public static final String STRING = "string";
    public static final String STYLE = "style";
    public static final String STYLEABLE = "styleable";

    private RUtils() {
        throw new Error("Do not need instantiate!");
    }

    public static int getAnim(Context context, String name) {
        try {
            return (Integer) Class.forName(context.getPackageName() + "." + "R" + "$" + "anim").getDeclaredField(name).get((Object) null);
        } catch (Exception var3) {
            var3.printStackTrace();
            return -1;
        }
    }

    public static int getAttr(Context context, String name) {
        try {
            return (Integer) Class.forName(context.getPackageName() + "." + "R" + "$" + "attr").getDeclaredField(name).get((Object) null);
        } catch (Exception var3) {
            var3.printStackTrace();
            return -1;
        }
    }

    public static int getColor(Context context, String name) {
        try {
            return (Integer) Class.forName(context.getPackageName() + "." + "R" + "$" + "color").getDeclaredField(name).get((Object) null);
        } catch (Exception var3) {
            var3.printStackTrace();
            return -1;
        }
    }

    public static int getDimen(Context context, String name) {
        try {
            return (Integer) Class.forName(context.getPackageName() + "." + "R" + "$" + "dimen").getDeclaredField(name).get((Object) null);
        } catch (Exception var3) {
            var3.printStackTrace();
            return -1;
        }
    }

    public static int getDrawable(Context context, String name) {
        try {
            return (Integer) Class.forName(context.getPackageName() + "." + "R" + "$" + "drawable").getDeclaredField(name).get((Object) null);
        } catch (Exception var3) {
            var3.printStackTrace();
            return -1;
        }
    }

    public static int getId(Context context, String name) {
        try {
            return (Integer) Class.forName(context.getPackageName() + "." + "R" + "$" + "id").getDeclaredField(name).get((Object) null);
        } catch (Exception var3) {
            var3.printStackTrace();
            return -1;
        }
    }

    public static int getLayout(Context context, String name) {
        try {
            return (Integer) Class.forName(context.getPackageName() + "." + "R" + "$" + "layout").getDeclaredField(name).get((Object) null);
        } catch (Exception var3) {
            var3.printStackTrace();
            return -1;
        }
    }

    public static int getMenu(Context context, String name) {
        try {
            return (Integer) Class.forName(context.getPackageName() + "." + "R" + "$" + "menu").getDeclaredField(name).get((Object) null);
        } catch (Exception var3) {
            var3.printStackTrace();
            return -1;
        }
    }

    public static int getRaw(Context context, String name) {
        try {
            return (Integer) Class.forName(context.getPackageName() + "." + "R" + "$" + "raw").getDeclaredField(name).get((Object) null);
        } catch (Exception var3) {
            var3.printStackTrace();
            return -1;
        }
    }

    public static int getString(Context context, String name) {
        try {
            return (Integer) Class.forName(context.getPackageName() + "." + "R" + "$" + "string").getDeclaredField(name).get((Object) null);
        } catch (Exception var3) {
            var3.printStackTrace();
            return -1;
        }
    }

    public static int getStyle(Context context, String name) {
        try {
            return (Integer) Class.forName(context.getPackageName() + "." + "R" + "$" + "style").getDeclaredField(name).get((Object) null);
        } catch (Exception var3) {
            var3.printStackTrace();
            return -1;
        }
    }

    public static int[] getStyleable(Context context, String name) {
        try {
            return (int[]) ((int[]) Class.forName(context.getPackageName() + "." + "R" + "$" + "styleable").getDeclaredField(name).get((Object) null));
        } catch (Exception var3) {
            var3.printStackTrace();
            return null;
        }
    }

    public static int getStyleableAttribute(Context context, String styleableName, String attributeName) {
        try {
            return (Integer) Class.forName(context.getPackageName() + "." + "R" + "$" + "styleable").getDeclaredField(styleableName + "_" + attributeName).get((Object) null);
        } catch (Exception var4) {
            var4.printStackTrace();
            return -1;
        }
    }
}

