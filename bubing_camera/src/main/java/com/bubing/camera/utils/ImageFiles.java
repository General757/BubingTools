package com.bubing.camera.utils;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.text.TextUtils;
import android.webkit.MimeTypeMap;
import android.widget.Toast;

import com.bubing.camera.exception.BException;
import com.bubing.camera.exception.BExceptionType;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

/**
 * @ClassName: ImageFiles
 * @Description: ImageFiles工具类
 * @Author: bubing
 * @Date: 2020-05-09 19:51
 */
public class ImageFiles {
    private static final String TAG = ImageFiles.class.getName();

    /**
     * 将bitmap写入到文件
     *
     * @param bitmap
     */
    public static void writeToFile(Bitmap bitmap, Uri imageUri) {
        if (bitmap == null) {
            return;
        }
        File file = new File(imageUri.getPath());
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bos);
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(file);
            fos.write(bos.toByteArray());
            bos.flush();
            fos.flush();
        } catch (java.io.IOException e) {
            e.printStackTrace();
        } finally {
            if (fos != null) {
                try {
                    fos.close();
                    if (bos != null) {
                        bos.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * InputStream 转File
     */
    public static void inputStreamToFile(InputStream is, File file) throws BException {
        if (file == null) {
            BubingLog.i(TAG, "inputStreamToFile:file not be null");
            throw new BException(BExceptionType.TYPE_WRITE_FAIL);
        }
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(file);
            byte[] buffer = new byte[1024 * 10];
            int i;
            while ((i = is.read(buffer)) != -1) {
                fos.write(buffer, 0, i);
            }
        } catch (IOException e) {
            BubingLog.e(TAG, "InputStream 写入文件出错:" + e.toString());
            throw new BException(BExceptionType.TYPE_WRITE_FAIL);
        } finally {
            try {
                fos.flush();
                fos.close();
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 获取临时文件
     *
     * @param context
     * @param photoUri
     * @return
     */
    public static File getCropTempFile(Activity context, Uri photoUri) throws BException {
        String minType = getMimeType(context, photoUri);
        if (!checkMimeType(context, minType)) {
            throw new BException(BExceptionType.TYPE_NOT_IMAGE);
        }
        String filesPath = FilePathUtils.getInstance().getFileCacheDir(context, FilePathUtils.FileType.TEMP);//out path
        return new File(filesPath, UUID.randomUUID().toString() + "." + minType);
    }
    /**
     * 获取临时文件
     *
     * @param context
     * @param photoUri
     * @return
     */
    public static File getTempFile(Activity context, Uri photoUri) throws BException {
        String minType = getMimeType(context, photoUri);
        if (!checkMimeType(context, minType)) {
            throw new BException(BExceptionType.TYPE_NOT_IMAGE);
        }
        File filesDir = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        if (!filesDir.exists()) {
            filesDir.mkdirs();
        }
        File photoFile = new File(filesDir, UUID.randomUUID().toString() + "." + minType);
        return photoFile;
    }

    /**
     * 检查文件类型是否是图片
     *
     * @param minType
     * @return
     */
    public static boolean checkMimeType(Context context, String minType) {
        boolean isPicture = TextUtils.isEmpty(minType) ? false : ".jpg|.gif|.png|.bmp|.jpeg|.webp|".contains(minType.toLowerCase()) ? true : false;
        if (!isPicture)
            Toast.makeText(context, "选择的不是图片", Toast.LENGTH_SHORT).show();
        return isPicture;
    }

    /**
     * To find out the extension of required object in given uri
     * Solution by http://stackoverflow.com/a/36514823/1171484
     */
    public static String getMimeType(Activity context, Uri uri) {
        String extension;
        //Check uri format to avoid null
        if (ContentResolver.SCHEME_CONTENT.equals(uri.getScheme())) {
            //If scheme is a content
            extension = MimeTypeMap.getSingleton().getExtensionFromMimeType(context.getContentResolver().getType(uri));
            if (TextUtils.isEmpty(extension)) {
                extension = MimeTypeMap.getFileExtensionFromUrl(Uri.fromFile(new File(uri.getPath())).toString());
            }
        } else {
            //If scheme is a File
            //This will replace white spaces with %20 and also other special characters. This will avoid returning null values on file
            // name with spaces and special characters.
            extension = MimeTypeMap.getFileExtensionFromUrl(Uri.fromFile(new File(uri.getPath())).toString());
            if (TextUtils.isEmpty(extension)) {
                extension = MimeTypeMap.getSingleton().getExtensionFromMimeType(context.getContentResolver().getType(uri));
            }
        }
        if (TextUtils.isEmpty(extension)) {
            extension = getMimeTypeByFileName(UriUtils.getFileWithUri(uri, context).getName());
        }
        return extension;
    }

    public static String getMimeTypeByFileName(String fileName) {
        return fileName.substring(fileName.lastIndexOf("."), fileName.length());
    }
}
