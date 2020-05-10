package com.bubing.camera.setting;

import android.view.View;

import com.bubing.camera.constant.Constants;
import com.bubing.camera.engine.ImageEngine;
import com.bubing.camera.models.album.entity.Photo;
import com.bubing.camera.models.compress.CompressConfig;
import com.bubing.camera.models.crop.CropOptions;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import androidx.annotation.IntDef;

/**
 * @ClassName: Setting
 * @Description: CameraPhotoTools的设置值
 * @Author: bubing
 * @Date: 2020-05-09 14:31
 */
public class Setting {
    public static int minWidth = 1;
    public static int minHeight = 1;
    public static long minSize = 1;
    public static int count = 1;
    public static int pictureCount = -1;
    public static int videoCount = -1;
    public static WeakReference<View> photosAdView = null;
    public static WeakReference<View> albumItemsAdView = null;
    public static boolean photoAdIsOk = false;
    public static boolean albumItemsAdIsOk = false;
    public static ArrayList<Photo> selectedPhotos = new ArrayList<>();
    public static boolean showOriginalMenu = false;
    public static boolean originalMenuUsable = false;
    public static String originalMenuUnusableHint = "";
    public static boolean selectedOriginal = false;
    public static boolean isShowCamera = false;
    public static int cameraLocation = 1;
    public static boolean onlyStartCamera = false;
    public static boolean showPuzzleMenu = true;
    public static List<String> filterTypes = new ArrayList<>();
    public static boolean showGif = false;
    public static boolean showVideo = false;
    public static boolean showCleanMenu = true;
    public static long videoMinSecond = 0L;
    public static long videoMaxSecond = Long.MAX_VALUE;
    public static ImageEngine imageEngine = null;
    public static CropOptions cropOptions = null;
    public static boolean correctImage = false;//是对拍的照片进行旋转角度纠正
    public static CompressConfig compressConfig = null;
    public static boolean compressDialog = false;//是否显示压缩对话框

    public static final int LIST_FIRST = 0;
    public static final int BOTTOM_RIGHT = 1;

    @Retention(RetentionPolicy.SOURCE)
    @IntDef(value = {LIST_FIRST, BOTTOM_RIGHT})
    public @interface Location {

    }

    public static void clear() {
        minWidth = 1;
        minHeight = 1;
        minSize = 1;
        count = 1;
        pictureCount = -1;
        videoCount = -1;
        photosAdView = null;
        albumItemsAdView = null;
        photoAdIsOk = false;
        albumItemsAdIsOk = false;
        selectedPhotos.clear();
        showOriginalMenu = false;
        originalMenuUsable = false;
        originalMenuUnusableHint = "";
        selectedOriginal = false;
        isShowCamera = false;
        cameraLocation = BOTTOM_RIGHT;
        onlyStartCamera = false;
        showPuzzleMenu = true;
        filterTypes.clear();
        showGif = false;
        showVideo = false;
        showCleanMenu = true;
        videoMinSecond = 0L;
        videoMaxSecond = Long.MAX_VALUE;
//        imageEngine = null;
        cropOptions = null;
        correctImage = false;
        compressConfig = null;
        compressDialog = false;
    }

    public static boolean isFilter(String type) {
        type = type.toLowerCase();
        for (String filterType : Setting.filterTypes) {
            if (type.contains(filterType)) {
                return true;
            }
        }
        return false;
    }

    public static boolean isOnlyVideo() {
        return filterTypes.size() == 1 && filterTypes.get(0).equals(Constants.Type.VIDEO);
    }

    public static boolean hasPhotosAd() {
        return photosAdView != null && photosAdView.get() != null;
    }

    public static boolean hasAlbumItemsAd() {
        return albumItemsAdView != null && albumItemsAdView.get() != null;
    }

    public static boolean isBottomRightCamera() {
        return cameraLocation == BOTTOM_RIGHT;
    }

    public static boolean isCrop() {
        return cropOptions != null;
    }

    public static boolean isCorrectImage() {
        return correctImage;
    }

    public static boolean isCompress() {
        return compressConfig != null;
    }

    public static boolean isCompressDialog() {
        return compressDialog;
    }
}
