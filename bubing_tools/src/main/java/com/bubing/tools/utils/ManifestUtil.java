//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.bubing.tools.utils;

import android.app.Application;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager.NameNotFoundException;

/**
 * @ClassName: ManifestUtil
 * @Author: Bubing
 * @Date: 2020/9/9 3:27 PM
 * @Description: 清单-工具
 */
public class ManifestUtil {
    public ManifestUtil() {
    }

    public static int getMetaDataInt(Application application, String metaName) {
        int ret = 0;

        try {
            ApplicationInfo appInfo = application.getPackageManager().getApplicationInfo(application.getPackageName(), 128);
            ret = appInfo.metaData.getInt(metaName);
        } catch (NameNotFoundException var4) {
            var4.printStackTrace();
        }

        return ret;
    }

    public static String getMetaDataString(Application application, String metaName) {
        String ret = null;

        try {
            ApplicationInfo appInfo = application.getPackageManager().getApplicationInfo(application.getPackageName(), 128);
            ret = appInfo.metaData.getString(metaName);
        } catch (NameNotFoundException var4) {
            var4.printStackTrace();
        }

        return ret;
    }
}
