//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//
package com.bubing.tools.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Environment;
import android.util.Base64;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 * @ClassName: DataHelper
 * @Author: Bubing
 * @Date: 2020/9/9 3:10 PM
 * @Description: java类作用描述
 */
public class DataHelper {
    private static SharedPreferences mSharedPreferences;
    public static final String SP_NAME = "config";

    public DataHelper() {
    }

    public static void SetStringSF(Context context, String key, String value) {
        if (mSharedPreferences == null) {
            mSharedPreferences = context.getSharedPreferences("config", 0);
        }

        mSharedPreferences.edit().putString(key, value).apply();
    }

    public static String getStringSF(Context context, String key) {
        if (mSharedPreferences == null) {
            mSharedPreferences = context.getSharedPreferences("config", 0);
        }

        return mSharedPreferences.getString(key, (String) null);
    }

    public static void SetIntergerSF(Context context, String key, int value) {
        if (mSharedPreferences == null) {
            mSharedPreferences = context.getSharedPreferences("config", 0);
        }

        mSharedPreferences.edit().putInt(key, value).apply();
    }

    public static int getIntergerSF(Context context, String key) {
        if (mSharedPreferences == null) {
            mSharedPreferences = context.getSharedPreferences("config", 0);
        }

        return mSharedPreferences.getInt(key, -1);
    }

    public static void removeSF(Context context, String key) {
        if (mSharedPreferences == null) {
            mSharedPreferences = context.getSharedPreferences("config", 0);
        }

        mSharedPreferences.edit().remove(key).apply();
    }

    public static void clearShareprefrence(Context context) {
        if (mSharedPreferences == null) {
            mSharedPreferences = context.getSharedPreferences("config", 0);
        }

        mSharedPreferences.edit().clear().apply();
    }

    public static <T> boolean saveDeviceData(Context context, String key, T device) {
        if (mSharedPreferences == null) {
            mSharedPreferences = context.getSharedPreferences("config", 0);
        }

        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        try {
            ObjectOutputStream oos = new ObjectOutputStream(baos);
            oos.writeObject(device);
            String oAuth_Base64 = new String(Base64.encode(baos.toByteArray(), 0));
            mSharedPreferences.edit().putString(key, oAuth_Base64).apply();
            return true;
        } catch (Exception var6) {
            var6.printStackTrace();
            return false;
        }
    }

    public static <T> T getDeviceData(Context context, String key) {
        if (mSharedPreferences == null) {
            mSharedPreferences = context.getSharedPreferences("config", 0);
        }

        T device = null;
        String productBase64 = mSharedPreferences.getString(key, (String) null);
        if (productBase64 == null) {
            return null;
        } else {
            byte[] base64 = Base64.decode(productBase64.getBytes(), 0);
            ByteArrayInputStream bais = new ByteArrayInputStream(base64);

            try {
                ObjectInputStream bis = new ObjectInputStream(bais);
                device = (T) bis.readObject();
            } catch (Exception var7) {
                var7.printStackTrace();
            }

            return device;
        }
    }

    public static File getCacheFile(Context context) {
        if (Environment.getExternalStorageState().equals("mounted")) {
            File file = null;
            file = context.getExternalCacheDir();
            if (file == null) {
                file = new File(getCacheFilePath(context));
                if (!file.exists()) {
                    file.mkdirs();
                }
            }

            System.out.println("!!! HTTP 缓存文件夹 " + file.getPath());
            return file;
        } else {
            System.out.println("!!! HTTP 缓存文件夹 " + context.getCacheDir().getPath());
            return context.getCacheDir();
        }
    }

    public static String getCacheFilePath(Context context) {
        String packageName = context.getPackageName();
        return Environment.getExternalStorageDirectory() + packageName;
    }

    public static long getDirSize(File dir) {
        if (dir == null) {
            return 0L;
        } else if (!dir.isDirectory()) {
            return 0L;
        } else {
            long dirSize = 0L;
            File[] files = dir.listFiles();
            File[] var4 = files;
            int var5 = files.length;

            for (int var6 = 0; var6 < var5; ++var6) {
                File file = var4[var6];
                if (file.isFile()) {
                    dirSize += file.length();
                } else if (file.isDirectory()) {
                    dirSize += file.length();
                    dirSize += getDirSize(file);
                }
            }

            return dirSize;
        }
    }

    public static boolean DeleteDir(File dir) {
        if (dir == null) {
            return false;
        } else if (!dir.isDirectory()) {
            return false;
        } else {
            File[] files = dir.listFiles();
            File[] var2 = files;
            int var3 = files.length;

            for (int var4 = 0; var4 < var3; ++var4) {
                File file = var2[var4];
                if (file.isFile()) {
                    file.delete();
                } else if (file.isDirectory()) {
                    DeleteDir(file);
                }
            }

            return true;
        }
    }

    public static String BytyToString(InputStream in) throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        byte[] buf = new byte[1024];
        boolean var3 = false;

        while (in.read(buf) != -1) {
            out.write(buf, 0, buf.length);
        }

        String result = out.toString();
        out.close();
        return result;
    }
}

