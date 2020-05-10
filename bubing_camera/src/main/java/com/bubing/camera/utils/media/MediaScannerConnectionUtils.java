package com.bubing.camera.utils.media;

import android.content.Context;
import android.media.MediaScannerConnection;

import java.io.File;
import java.util.List;

/**
 * @ClassName: MediaScannerConnectionUtils
 * @Description: 更新媒体库
 * @Author: bubing
 * @Date: 2020-05-09 15:35
 */
public class MediaScannerConnectionUtils {
    public static void refresh(Context cxt, String... filePaths) {
        MediaScannerConnection.scanFile(cxt.getApplicationContext(), filePaths, null, null);
    }

    public static void refresh(Context cxt, File... files) {
        for (File file : files) {
            String filePath = file.getAbsolutePath();
            refresh(cxt, filePath);
        }
    }

    public static void refresh(Context cxt, List<String> filePathList) {
        for (String filePath : filePathList) {
            refresh(cxt, filePath);
        }
    }
}
