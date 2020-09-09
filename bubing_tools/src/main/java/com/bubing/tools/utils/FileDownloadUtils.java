//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//
package com.bubing.tools.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

/**
 * @ClassName: FileDownloadUtils
 * @Author: Bubing
 * @Date: 2020/9/9 3:16 PM
 * @Description: 文件下载-工具
 */
public class FileDownloadUtils {
    public FileDownloadUtils() {
    }

    public static File downloadBitmap(String dir, String url) {
        File file = null;

        try {
            String fileName = url.substring(url.lastIndexOf("/") + 1, url.length());
            URL myURL = new URL(url);
            URLConnection conn = myURL.openConnection();
            conn.connect();
            InputStream is = conn.getInputStream();
            if (is == null) {
                throw new RuntimeException("stream is null");
            }

            File file1 = new File(dir);
            file = new File(dir + fileName);
            if (!file1.exists()) {
                file1.mkdirs();
            }

            if (!file.exists()) {
                file.createNewFile();
            }

            FileOutputStream fos = new FileOutputStream(dir + fileName);
            byte[] buf = new byte[1024];

            while (true) {
                int numread = is.read(buf);
                if (numread == -1) {
                    try {
                        is.close();
                    } catch (Exception var11) {
                        BubingLog.e("tag", "error: " + var11.getMessage(), var11);
                    }
                    break;
                }

                fos.write(buf, 0, numread);
            }
        } catch (Exception var12) {
        }

        return file;
    }
}

