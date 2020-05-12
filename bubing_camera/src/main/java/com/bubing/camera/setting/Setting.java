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
    public static int minWidth = 1;//设置显示照片的最小宽度
    public static int minHeight = 1;//设置显示照片的最小高度
    public static long minSize = 1;//设置显示照片的最小文件大小
    public static int count = 1;//设置选择数
    public static int pictureCount = -1;//设置选择图片数(设置此参数后setCount失效)
    public static int videoCount = -1;//设置选择视频数(设置此参数后setCount失效)

    public static WeakReference<View> photosAdView = null;//使用图片列表的广告View
    public static WeakReference<View> albumItemsAdView = null;//使用专辑项目列表的广告View
    public static boolean photoAdIsOk = false;//图片列表广告是否加载完毕
    public static boolean albumItemsAdIsOk = false;//专辑项目列表广告是否加载完毕

    public static ArrayList<Photo> selectedPhotos = new ArrayList<>();//设置默认选择图片地址集合
    public static boolean showOriginalMenu = false;//原图选项是否显示
    public static boolean selectedOriginal = false;//原图选项默认状态是否为选中状态
    public static boolean originalMenuUsable = false;//原图按钮是否可使用
    public static String originalMenuUnusableHint = "";//原图按钮不可使用时给用户的文字提示
    public static boolean isShowCamera = false;//是否显示相机按钮
    public static int cameraLocation = 1;//设置相机按钮位置
    public static boolean onlyStartCamera = false;//仅启动相册
    public static boolean showPuzzleMenu = false;//是否显示拼图按钮
    public static List<String> filterTypes = new ArrayList<>();//过滤
    public static boolean showGif = false;//是否显示gif图
    public static boolean showVideo = false;//是否显示video
    public static boolean showCleanMenu = true;//相册选择页是否显示清空按钮
    public static long videoMinSecond = 0L;//显示最少多少秒的视频
    public static long videoMaxSecond = Long.MAX_VALUE;//显示最多多少秒的视频
    public static ImageEngine imageEngine = null;//图片加载引擎的具体实现

    public static CropOptions cropOptions = null;//是否剪切-配置
    public static boolean correctImage = false;//是对拍的照片进行旋转角度纠正

    public static CompressConfig compressConfig = null;//是否对照片进行压缩-压缩配置
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
        showPuzzleMenu = false;
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
