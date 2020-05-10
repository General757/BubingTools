package com.bubing.camera.utils;

import android.text.TextUtils;
import android.util.Log;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.UnknownHostException;

/**
 * @ClassName: BubingLog
 * @Description: java类作用描述
 * @Author: bubing
 * @Date: 2020-05-08 22:14
 */
public class BubingLog {
    public static String customTagPrefix = "BubingLog_";

    private BubingLog() {
    }

    /**
     * 调试模式,默认打开所有的异常调试
     */
    public static void debug(String tag) {
        debug(tag, true);
    }

    /**
     * 调试模式,第二个参数表示所有catch住的log是否需要打印<br>
     * 一般来说,这些异常是由于不标准的数据格式,或者特殊需要主动产生的,
     * 并不是框架错误,如果不想每次打印,这里可以关闭异常显示
     */
    public static void debug(String tag, boolean isPrintException) {
        String tempTag = TextUtils.isEmpty(tag) ? "BubingLog_" : tag;
        BubingLog.customTagPrefix = tempTag;
        BubingLog.allowE = isPrintException;
        BubingLog.allowD = isPrintException;
        BubingLog.allowI = isPrintException;
        BubingLog.allowV = isPrintException;
    }

    public static boolean allowD = true;
    public static boolean allowE = true;
    public static boolean allowI = true;
    public static boolean allowV = true;
    public static boolean allowW = true;
    public static boolean allowWtf = true;

    private static String generateTag(StackTraceElement caller) {
        String tag = "%s.%s(L:%d)";
        String callerClazzName = caller.getClassName();
        callerClazzName = callerClazzName.substring(callerClazzName.lastIndexOf(".") + 1);
        tag = String.format(tag, callerClazzName, caller.getMethodName(), caller.getLineNumber());
        tag = TextUtils.isEmpty(customTagPrefix) ? tag : customTagPrefix + ":" + tag;
        return tag;
    }

    public static StackTraceElement getCallerStackTraceElement() {
        return Thread.currentThread().getStackTrace()[4];
    }

    /**
     * Handy function to get a loggable stack trace from a Throwable
     *
     * @param tr An exception to log
     */
    public static String getStackTraceString(Throwable tr) {
        if (tr == null)
            return "";

        // This is to reduce the amount of log spew that apps do in the non-error
        // condition of the network being unavailable.
        Throwable t = tr;
        while (t != null) {
            if (t instanceof UnknownHostException) {
                return "";
            }
            t = t.getCause();
        }

        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        tr.printStackTrace(pw);
        pw.flush();
        return sw.toString();
    }

    public static CustomLogger customLogger;

    public interface CustomLogger {
        void d(String tag, String content);

        void d(String tag, String content, Throwable tr);

        void e(String tag, String content);

        void e(String tag, String content, Throwable tr);

        void i(String tag, String content);

        void i(String tag, String content, Throwable tr);

        void v(String tag, String content);

        void v(String tag, String content, Throwable tr);

        void w(String tag, String content);

        void w(String tag, String content, Throwable tr);

        void w(String tag, Throwable tr);

        void wtf(String tag, String content);

        void wtf(String tag, String content, Throwable tr);

        void wtf(String tag, Throwable tr);
    }

    public static void d(String content) {
        d("", content);
    }

    public static void d(String tag, String content) {
        d(tag, content, null);
    }

    public static void d(String content, Throwable tr) {
        d("", content, tr);
    }

    public static void d(String tag, String content, Throwable tr) {
        if (!allowD)
            return;

        StackTraceElement caller = getCallerStackTraceElement();
        if (TextUtils.isEmpty(tag))
            tag = generateTag(caller);
        else
            tag = generateTag(caller) + ":" + tag;

        if (customLogger != null) {
            if (tr == null)
                customLogger.d(tag, content);
            else
                customLogger.d(tag, content, tr);
        } else {
            if (tr == null)
                Log.d(tag, content);
            else
                Log.d(tag, content, tr);
        }
    }

    public static void e(String content) {
        e("", content);
    }

    public static void e(String tag, String content) {
        if (!allowE)
            return;

        StackTraceElement caller = getCallerStackTraceElement();
        if (TextUtils.isEmpty(tag))
            tag = generateTag(caller);
        else
            tag = generateTag(caller) + ":" + tag;

        if (customLogger != null)
            customLogger.e(tag, content);
        else
            Log.e(tag, content);
    }

    public static void e(Exception e) {
        e("", e);
    }

    public static void e(String tag, Exception e) {
        if (!allowE)
            return;

        StackTraceElement caller = getCallerStackTraceElement();
        if (TextUtils.isEmpty(tag))
            tag = generateTag(caller);
        else
            tag = generateTag(caller) + ":" + tag;

        if (customLogger != null)
            customLogger.e(tag, e.getMessage(), e);
        else
            Log.e(tag, e.getMessage(), e);
    }

    public static void e(String content, Throwable tr) {
        e("", content, tr);
    }

    public static void e(String tag, String content, Throwable tr) {
        if (!allowE)
            return;

        StackTraceElement caller = getCallerStackTraceElement();
        if (TextUtils.isEmpty(tag))
            tag = generateTag(caller);
        else
            tag = generateTag(caller) + ":" + tag;

        if (customLogger != null)
            customLogger.e(tag, content, tr);
        else
            Log.e(tag, content, tr);
    }

    public static void i(String content) {
        i("", content);
    }

    public static void i(String tag, String content) {
        i(tag, content, null);
    }

    public static void i(String content, Throwable tr) {
        i("", content, tr);
    }

    public static void i(String tag, String content, Throwable tr) {
        if (!allowI)
            return;

        StackTraceElement caller = getCallerStackTraceElement();
        if (TextUtils.isEmpty(tag))
            tag = generateTag(caller);
        else
            tag = generateTag(caller) + ":" + tag;

        if (customLogger != null) {
            if (tr == null)
                customLogger.i(tag, content);
            else
                customLogger.i(tag, content, tr);
        } else {
            if (tr == null)
                Log.i(tag, content);
            else
                Log.i(tag, content, tr);
        }
    }

    public static void v(String content) {
        v("", content);
    }

    public static void v(String tag, String content) {
        v(tag, content, null);
    }

    public static void v(String content, Throwable tr) {
        v("", content, tr);
    }

    public static void v(String tag, String content, Throwable tr) {
        if (!allowV)
            return;

        StackTraceElement caller = getCallerStackTraceElement();
        if (TextUtils.isEmpty(tag))
            tag = generateTag(caller);
        else
            tag = generateTag(caller) + ":" + tag;

        if (customLogger != null) {
            if (tr == null)
                customLogger.v(tag, content);
            else
                customLogger.v(tag, content, tr);
        } else {
            if (tr == null)
                Log.v(tag, content);
            else
                Log.v(tag, content, tr);
        }
    }

    public static void w(String content) {
        w("", content);
    }

    public static void w(String tag, String content) {
        w(tag, content, null);
    }

    public static void w(Throwable tr) {
        w("", tr);
    }

    public static void w(String content, Throwable tr) {
        w("", content, tr);
    }

    public static void w(String tag, String content, Throwable tr) {
        if (!allowW)
            return;

        StackTraceElement caller = getCallerStackTraceElement();
        if (TextUtils.isEmpty(tag))
            tag = generateTag(caller);
        else
            tag = generateTag(caller) + ":" + tag;

        if (customLogger != null) {
            if (tr == null)
                customLogger.w(tag, content);
            else {
                if (TextUtils.isEmpty(content))
                    customLogger.w(tag, content, tr);
                else
                    customLogger.w(tag, tr);
            }
        } else {
            if (tr == null)
                Log.w(tag, content);
            else {
                if (TextUtils.isEmpty(content))
                    Log.w(tag, tr);
                else
                    Log.w(tag, content, tr);
            }
        }
    }

    public static void wtf(String content) {
        wtf("", content, null);
    }

    public static void wtf(String tag, String content) {
        wtf(tag, content, null);
    }

    public static void wtf(Throwable tr) {
        wtf("", "", tr);
    }

    public static void wtf(String content, Throwable tr) {
        wtf("", content, tr);
    }

    public static void wtf(String tag, String content, Throwable tr) {
        if (!allowWtf)
            return;

        StackTraceElement caller = getCallerStackTraceElement();
        if (TextUtils.isEmpty(tag))
            tag = generateTag(caller);
        else
            tag = generateTag(caller) + ":" + tag;

        if (customLogger != null) {
            if (tr == null)
                customLogger.wtf(tag, content);
            else {
                if (TextUtils.isEmpty(content))
                    customLogger.wtf(tag, content, tr);
                else
                    customLogger.wtf(tag, tr);
            }
        } else {
            if (tr == null)
                Log.wtf(tag, content);
            else {
                if (TextUtils.isEmpty(content))
                    Log.wtf(tag, tr);
                else
                    Log.wtf(tag, content, tr);
            }
        }
    }

}

