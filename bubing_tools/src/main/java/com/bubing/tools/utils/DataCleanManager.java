//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//
package com.bubing.tools.utils;

import android.content.Context;
import android.os.Environment;

import java.io.File;
import java.math.BigDecimal;

/**
 * @ClassName: DataCleanManager
 * @Author: Bubing
 * @Date: 2020/9/9 3:09 PM
 * @Description: java类作用描述
 */
public class DataCleanManager {
    public DataCleanManager() {
    }

    public static void cleanInternalCache(Context context) {
        deleteFilesByDirectory(context.getCacheDir());
    }

    public static void cleanDatabases(Context context) {
        deleteFilesByDirectory(new File(context.getFilesDir().getPath() + context.getPackageName() + "/databases"));
    }

    public static void cleanSharedPreference(Context context) {
        deleteFilesByDirectory(new File(context.getFilesDir().getPath() + context.getPackageName() + "/shared_prefs"));
    }

    public static void cleanDatabaseByName(Context context, String dbName) {
        context.deleteDatabase(dbName);
    }

    public static void cleanFiles(Context context) {
        deleteFilesByDirectory(context.getFilesDir());
    }

    public static void cleanExternalCache(Context context) {
        if (Environment.getExternalStorageState().equals("mounted")) {
            deleteFilesByDirectory(context.getExternalCacheDir());
        }

    }

    public static void cleanCustomCache(String filePath) {
        deleteFilesByDirectory(new File(filePath));
    }

    public static void cleanApplicationData(Context context, String... filePath) {
        cleanInternalCache(context);
        cleanExternalCache(context);
        cleanDatabases(context);
        cleanSharedPreference(context);
        cleanFiles(context);
        String[] var2 = filePath;
        int var3 = filePath.length;

        for (int var4 = 0; var4 < var3; ++var4) {
            String fp = var2[var4];
            cleanCustomCache(fp);
        }

    }

    private static void deleteFilesByDirectory(File directory) {
        if (directory != null && directory.exists() && directory.isDirectory()) {
            File[] var1 = directory.listFiles();
            int var2 = var1.length;

            for (int var3 = 0; var3 < var2; ++var3) {
                File item = var1[var3];
                item.delete();
            }
        }

    }

    public static long getFolderSize(File file) throws Exception {
        long size = 0L;

        try {
            File[] fileList = file.listFiles();

            for (int i = 0; i < fileList.length; ++i) {
                if (fileList[i].isDirectory()) {
                    size += getFolderSize(fileList[i]);
                } else {
                    size += fileList[i].length();
                }
            }
        } catch (Exception var5) {
            var5.printStackTrace();
        }

        return size;
    }

    public static String getCacheSize(File file) throws Exception {
        return getFormatSize((double) getFolderSize(file));
    }

    public static String getFormatSize(double size) {
        double kiloByte = size / 1024.0D;
        if (kiloByte < 1.0D) {
            return size + "Byte";
        } else {
            double megaByte = kiloByte / 1024.0D;
            if (megaByte < 1.0D) {
                BigDecimal result1 = new BigDecimal(Double.toString(kiloByte));
                return result1.setScale(2, 4).toPlainString() + "KB";
            } else {
                double gigaByte = megaByte / 1024.0D;
                if (gigaByte < 1.0D) {
                    BigDecimal result2 = new BigDecimal(Double.toString(megaByte));
                    return result2.setScale(2, 4).toPlainString() + "MB";
                } else {
                    double teraBytes = gigaByte / 1024.0D;
                    BigDecimal result4;
                    if (teraBytes < 1.0D) {
                        result4 = new BigDecimal(Double.toString(gigaByte));
                        return result4.setScale(2, 4).toPlainString() + "GB";
                    } else {
                        result4 = new BigDecimal(teraBytes);
                        return result4.setScale(2, 4).toPlainString() + "TB";
                    }
                }
            }
        }
    }
}

