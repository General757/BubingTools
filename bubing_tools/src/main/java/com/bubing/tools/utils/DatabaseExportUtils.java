//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//
package com.bubing.tools.utils;

import android.content.Context;
import android.os.Environment;
import android.text.TextUtils;

/**
 * @ClassName: DatabaseExportUtils
 * @Author: Bubing
 * @Date: 2020/9/9 3:06 PM
 * @Description: java类作用描述
 */
public class DatabaseExportUtils {
    private static final boolean DEBUG = true;
    private static final String TAG = "DatabaseExportUtils";

    private DatabaseExportUtils() {
        throw new Error("Do not need instantiate!");
    }

    public boolean startExportDatabase(Context context, String targetFile, String databaseName) {
        BubingLog.d("DatabaseExportUtils", "start export ...");
        if (!"mounted".equals(Environment.getExternalStorageState())) {
            BubingLog.w("DatabaseExportUtils", "cannot find SDCard");
            return false;
        } else {
            String sourceFilePath = Environment.getDataDirectory() + "/data/" + context.getPackageName() + "/databases/" + databaseName;
            String destFilePath = Environment.getExternalStorageDirectory() + (TextUtils.isEmpty(targetFile) ? context.getPackageName() + ".db" : targetFile);
            boolean isCopySuccess = FileUtils.copyFile(sourceFilePath, destFilePath);
            if (isCopySuccess) {
                BubingLog.d("DatabaseExportUtils", "copy database file success. target file : " + destFilePath);
            } else {
                BubingLog.w("DatabaseExportUtils", "copy database file failure");
            }

            return isCopySuccess;
        }
    }
}

