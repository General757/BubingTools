package com.bubing.camera;

import android.app.Activity;
import android.app.Fragment;

import com.bubing.camera.builder.AlbumBuilder;
import com.bubing.camera.engine.ImageEngine;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;

/**
 * @ClassName: CameraPhotoTools
 * @Description: java类作用描述
 * @Author: bubing
 * @Date: 2020-05-09 14:04
 * <p>
 * GitHub:https://github.com/crazycodeboy/TakePhoto
 * GitHub:https://github.com/HuanTanSheng/EasyPhotos
 */
public class CameraPhotoTools {

    /**
     * 创建相机
     *
     * @param activity 上下文
     * @return AlbumBuilder
     */
    public static AlbumBuilder createCamera(Activity activity) {
        return AlbumBuilder.createCamera(activity);
    }

    public static AlbumBuilder createCamera(Fragment fragment) {
        return AlbumBuilder.createCamera(fragment);
    }

    public static AlbumBuilder createCamera(FragmentActivity activity) {
        return AlbumBuilder.createCamera(activity);
    }

    public static AlbumBuilder createCamera(androidx.fragment.app.Fragment fragmentV) {
        return AlbumBuilder.createCamera(fragmentV);
    }

    /**
     * 创建相册
     *
     * @param activity     上下文
     * @param isShowCamera 是否显示相机按钮
     * @param imageEngine  图片加载引擎的具体实现
     * @return
     */
    public static AlbumBuilder createAlbum(Activity activity, boolean isShowCamera, @NonNull ImageEngine imageEngine) {
        return AlbumBuilder.createAlbum(activity, isShowCamera, imageEngine);
    }

    public static AlbumBuilder createAlbum(Fragment fragment, boolean isShowCamera, @NonNull ImageEngine imageEngine) {
        return AlbumBuilder.createAlbum(fragment, isShowCamera, imageEngine);
    }

    public static AlbumBuilder createAlbum(FragmentActivity activity, boolean isShowCamera, @NonNull ImageEngine imageEngine) {
        return AlbumBuilder.createAlbum(activity, isShowCamera, imageEngine);
    }

    public static AlbumBuilder createAlbum(androidx.fragment.app.Fragment fragmentV, boolean isShowCamera, @NonNull ImageEngine imageEngine) {
        return AlbumBuilder.createAlbum(fragmentV, isShowCamera, imageEngine);
    }
}
