package com.bubing.camera.utils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.text.TextUtils;

import com.bubing.camera.exception.BException;
import com.bubing.camera.exception.BExceptionType;

import java.io.File;
import java.io.FileNotFoundException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import androidx.core.content.FileProvider;

/**
 * @ClassName: UriUtils
 * @Description: Uri解析工具类
 * @Author: bubing
 * @Date: 2020-05-09 15:05
 */
public class UriUtils {

    public final static String getFileProviderName(Context context) {
        return context.getPackageName() + ".fileprovider";
    }

    public static Uri getUri(Context cxt, File file) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            return FileProvider.getUriForFile(cxt, UriUtils.getFileProviderName(cxt), file);
        } else {
            return Uri.fromFile(file);
        }
    }

    private static final String TAG = "UriUtils";

    @SuppressLint("NewApi")
    public static String getPathByUri(Context context, Uri uri) {
        final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;
        // DocumentProvider
        if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
            if (isExternalStorageDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                if ("primary".equalsIgnoreCase(type)) {
                    if (android.os.Build.VERSION.SDK_INT < Build.VERSION_CODES.Q)
                        return Environment.getExternalStorageDirectory() + "/" + split[1];
                    else
                        return context.getExternalFilesDir(Environment.DIRECTORY_PICTURES) + "/" + split[1];
                }
            }
            // DownloadsProvider
            else if (isDownloadsDocument(uri)) {
                final String id = DocumentsContract.getDocumentId(uri);
                final Uri contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));
                return getDataColumn(context, contentUri, null, null);
            }
            // MediaProvider
            else if (isMediaDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                Uri contentUri = null;
                if ("image".equals(type))
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                else if ("video".equals(type))
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                else if ("audio".equals(type))
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                final String selection = "_id=?";
                final String[] selectionArgs = new String[]{split[1]};
                return getDataColumn(context, contentUri, selection, selectionArgs);
            }
        }
        // MediaStore (and general)
        else if ("content".equalsIgnoreCase(uri.getScheme())) {
            // Return the remote address
            if (isGooglePhotosUri(uri))
                return uri.getLastPathSegment();
            return getDataColumn(context, uri, null, null);
        }
        // File
        else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }
        return null;
    }

    private static String getDataColumn(Context context, Uri uri, String selection, String[] selectionArgs) {
        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {column};
        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs, null);
            if (cursor != null && cursor.moveToFirst()) {
                final int column_index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(column_index);
            }
        } catch (IllegalArgumentException ex) {
            BubingLog.i(TAG, String.format(Locale.getDefault(), "getDataColumn: _data - [%s]", ex.getMessage()));
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is ExternalStorageProvider.
     */
    private static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is DownloadsProvider.
     */
    private static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is MediaProvider.
     */
    private static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

    /**
     * 判断是否是Google相册的图片，类似于content://com.google.android.apps.photos.content/...
     **/
    public static boolean isGooglePhotosUri(Uri uri) {
        return "com.google.android.apps.photos.content".equals(uri.getAuthority());
    }

    /**
     * 判断是否是Google相册的图片，类似于content://com.google.android.apps.photos.contentprovider/0/1/mediakey:/local%3A821abd2f-9f8c-4931-bbe9-a975d1f5fabc/ORIGINAL/NONE/1075342619
     **/
    public static boolean isGooglePlayPhotosUri(Uri uri) {
        return "com.google.android.apps.photos.contentprovider".equals(uri.getAuthority());
    }

    /**
     * 图片路径转uri
     */
    public static Uri getUriByPath(Context context, String path) {
        ContentResolver contentResolver = context.getContentResolver();
        Uri contentUri = MediaStore.Files.getContentUri("external");
        Cursor cursor = contentResolver.query(contentUri, new String[]{MediaStore.Files.FileColumns._ID}, MediaStore.Files.FileColumns.DATA + "=? ", new String[]{path}, null);
        if (cursor != null && cursor.moveToFirst()) {
            int id = cursor.getInt(cursor.getColumnIndex(MediaStore.MediaColumns._ID));
            cursor.close();
            return Uri.withAppendedPath(contentUri, "" + id);
        } else {
            if (new File(path).exists()) {
                ContentValues values = new ContentValues();
                values.put(MediaStore.Images.Media.DATA, path);
                return contentResolver.insert(contentUri, values);
            } else {
                return null;
            }
        }
    }

    /**
     * 将TakePhoto 提供的Uri 解析出文件绝对路径
     *
     * @param uri
     * @return
     */
    public static String parseOwnUri(Context context, Uri uri) {
        if (uri == null) {
            return null;
        }
        String path;
        if (TextUtils.equals(uri.getAuthority(), getFileProviderName(context))) {
            path = new File(uri.getPath().replace("camera_photos/", "")).getAbsolutePath();
        } else {
            path = uri.getPath();
        }
        return path;
    }

    /**
     * 获取一个临时的Uri, 通过传入字符串路径
     *
     * @param context
     * @param path
     * @return
     */
    public static Uri getTempUri(Context context, String path) {
        File file = new File(path);
        return getTempUri(context, file);
    }

    /**
     * 获取一个临时的Uri, 通过传入File对象
     *
     * @param context
     * @return
     */
    public static Uri getTempUri(Context context, File file) {
        if (!file.getParentFile().exists()) {
            file.getParentFile().mkdirs();
        }
        return getUriForFile(context, file);
    }

    /**
     * 获取一个临时的Uri, 文件名随机生成
     *
     * @param context
     * @return
     */
    public static Uri getTempUri(Context context) {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
        File file = new File(Environment.getExternalStorageDirectory(), "/images/" + timeStamp + ".jpg");
        if (!file.getParentFile().exists()) {
            file.getParentFile().mkdirs();
        }
        return getUriForFile(context, file);
    }

    /**
     * 创建一个用于拍照图片输出路径的Uri (FileProvider)
     *
     * @param context
     * @return
     */
    public static Uri getUriForFile(Context context, File file) {
        return FileProvider.getUriForFile(context, getFileProviderName(context), file);
    }

    /**
     * 将scheme为file的uri转成FileProvider 提供的content uri
     *
     * @param context
     * @param uri
     * @return
     */
    public static Uri convertFileUriToFileProviderUri(Context context, Uri uri) {
        if (uri == null) {
            return null;
        }
        if (ContentResolver.SCHEME_FILE.equals(uri.getScheme())) {
            return getUriForFile(context, new File(uri.getPath()));
        }
        return uri;

    }

    /**
     * 通过URI获取文件的路径
     *
     * @param uri
     * @param activity
     * @return Author JPH
     * Date 2016/6/6 0006 20:01
     */
    public static String getFilePathWithUri(Uri uri, Activity activity) throws BException {
        if (uri == null) {
            BubingLog.w(TAG, "uri is null,activity may have been recovered?");
            throw new BException(BExceptionType.TYPE_URI_NULL);
        }
        File picture = getFileWithUri(uri, activity);
        String picturePath = picture == null ? null : picture.getPath();
        if (TextUtils.isEmpty(picturePath)) {
            throw new BException(BExceptionType.TYPE_URI_PARSE_FAIL);
        }
        if (!ImageFiles.checkMimeType(activity, ImageFiles.getMimeType(activity, uri))) {
            throw new BException(BExceptionType.TYPE_NOT_IMAGE);
        }
        return picturePath;
    }

    /**
     * 通过URI获取文件
     *
     * @param uri
     * @param activity
     * @return Author JPH
     * Date 2016/10/25
     */
    public static File getFileWithUri(Uri uri, Activity activity) {
        String picturePath = null;
        String scheme = uri.getScheme();
        if (ContentResolver.SCHEME_CONTENT.equals(scheme)) {
            String[] filePathColumn = {MediaStore.Images.Media.DATA};
            Cursor cursor = activity.getContentResolver().query(uri, filePathColumn, null, null, null);//从系统表中查询指定Uri对应的照片
            cursor.moveToFirst();
            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            if (columnIndex >= 0) {
                picturePath = cursor.getString(columnIndex);  //获取照片路径
            } else if (TextUtils.equals(uri.getAuthority(), getFileProviderName(activity))) {
                picturePath = parseOwnUri(activity, uri);
            }
            cursor.close();
        } else if (ContentResolver.SCHEME_FILE.equals(scheme)) {
            picturePath = uri.getPath();
        }
        return TextUtils.isEmpty(picturePath) ? null : new File(picturePath);
    }

    /**
     * 通过从文件中得到的URI获取文件的路径
     *
     * @param uri
     * @param activity
     * @return Author JPH
     * Date 2016/6/6 0006 20:01
     */
    public static String getFilePathWithDocumentsUri(Uri uri, Activity activity) throws BException {
        if (uri == null) {
            BubingLog.e(TAG, "uri is null,activity may have been recovered?");
            return null;
        }
        if (ContentResolver.SCHEME_CONTENT.equals(uri.getScheme()) && uri.getPath().contains("document")) {
            File tempFile = ImageFiles.getTempFile(activity, uri);
            try {
                ImageFiles.inputStreamToFile(activity.getContentResolver().openInputStream(uri), tempFile);
                return tempFile.getPath();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                throw new BException(BExceptionType.TYPE_NO_FIND);
            }
        } else {
            return getFilePathWithUri(uri, activity);
        }
    }
}
