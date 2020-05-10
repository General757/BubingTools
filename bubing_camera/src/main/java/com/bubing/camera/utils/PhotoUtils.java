package com.bubing.camera.utils;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;

import com.bubing.camera.exception.BException;
import com.bubing.camera.models.ImageType;
import com.bubing.camera.models.album.entity.Photo;
import com.bubing.camera.models.ResultPhoto;

import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName: PhotoUtils
 * @Description: Photo工具
 * @Author: bubing
 * @Date: 2020-05-09 18:55
 */
public class PhotoUtils {

    public static Photo getPhoto(Context mContext, Uri uri) {
        if (mContext == null || uri == null)
            return null;
        Cursor cursor = mContext.getContentResolver().query(uri, null, null, null, null);
        if (cursor == null)
            return null;
        else {
            Photo mPhoto = null;
            if (cursor.moveToFirst()) {
                String path = cursor.getString(cursor.getColumnIndex(MediaStore.MediaColumns.DATA));
                String name = cursor.getString(cursor.getColumnIndex(MediaStore.MediaColumns.DISPLAY_NAME));
                long dateTime = cursor.getLong(cursor.getColumnIndex(MediaStore.MediaColumns.DATE_MODIFIED));
                String type = cursor.getString(cursor.getColumnIndex(MediaStore.MediaColumns.MIME_TYPE));
                long size = cursor.getLong(cursor.getColumnIndex(MediaStore.MediaColumns.SIZE));
                int width = cursor.getInt(cursor.getColumnIndex(MediaStore.MediaColumns.WIDTH));
                int height = cursor.getInt(cursor.getColumnIndex(MediaStore.MediaColumns.HEIGHT));
                mPhoto = new Photo(name, uri, path, dateTime, width, height, size, 0, type);
            }
            cursor.close();

            return mPhoto;
        }
    }

    public static ResultPhoto getCropPhoto(Activity activity, Photo photo, ImageType imageType) throws BException {
        ResultPhoto resultPhoto = new ResultPhoto();
        resultPhoto.setUri(photo.uri);
        resultPhoto.setName(photo.name);
        resultPhoto.setPath(photo.path);
        resultPhoto.setType(photo.type);
        resultPhoto.setWidth(photo.width);
        resultPhoto.setHeight(photo.height);
        resultPhoto.setSize(photo.size);
        resultPhoto.setDuration(photo.duration);
        resultPhoto.setTime(photo.time);
        resultPhoto.setSelectedOriginal(photo.selectedOriginal);

        resultPhoto.setImageType(imageType);
        resultPhoto.setOriginalUri(Uri.fromFile(ImageFiles.getCropTempFile(activity, photo.uri)));//生成临时裁切输出路径
        resultPhoto.setOriginalPath(resultPhoto.getOriginalUri().getPath());

        return resultPhoto;
    }

    public static ResultPhoto getResultPhoto(Activity activity, Photo photo, ImageType imageType) {
        ResultPhoto resultPhoto = new ResultPhoto();
        resultPhoto.setUri(photo.uri);
        resultPhoto.setName(photo.name);
        resultPhoto.setPath(photo.path);
        resultPhoto.setType(photo.type);
        resultPhoto.setWidth(photo.width);
        resultPhoto.setHeight(photo.height);
        resultPhoto.setSize(photo.size);
        resultPhoto.setDuration(photo.duration);
        resultPhoto.setTime(photo.time);
        resultPhoto.setSelectedOriginal(photo.selectedOriginal);

        resultPhoto.setImageType(imageType);
        resultPhoto.setOriginalUri(photo.uri);//生成临时裁切输出路径
        resultPhoto.setOriginalPath(photo.path);

        return resultPhoto;
    }

    public static List<ResultPhoto> getResultPhotos(Activity activity, List<Photo> photos, ImageType imageType) {
        List<ResultPhoto> resultPhotos = new ArrayList<>();
        try {
            for (Photo photo : photos) {
                resultPhotos.add(getCropPhoto(activity, photo, imageType));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return resultPhotos;
    }


    public static ArrayList<Photo> getPhotos(Activity activity, List<ResultPhoto> resultPhotos, ImageType imageType) {
        ArrayList<Photo> photos = new ArrayList<>();
        Photo photo = null;
        for (ResultPhoto resultPhoto : resultPhotos) {
            photo = new Photo();
            photo.uri = resultPhoto.getUri();
            photo.name = resultPhoto.getName();
            photo.path = resultPhoto.getPath();
            photo.type = resultPhoto.getType();
            photo.width = resultPhoto.getWidth();
            photo.height = resultPhoto.getHeight();
            photo.size = resultPhoto.getSize();
            photo.duration = resultPhoto.getDuration();
            photo.time = resultPhoto.getTime();
            photo.selectedOriginal = resultPhoto.isSelectedOriginal();

            photos.add(photo);
        }
        return photos;
    }
}
