package com.bubing.camera.models.ad;

import com.bubing.camera.TakePhotoImpl;

/**
 * @ClassName: AdUtils
 * @Description: AdUtils工具类
 * @Author: bubing
 * @Date: 2020-05-09 15:49
 */
public class AdUtils {
    /**
     * 设置广告监听
     * 内部使用，无需关心
     *
     * @param adListener 广告监听
     */
    public static void setAdListener(AdListener adListener) {
        TakePhotoImpl.setAdListener(adListener);
    }

    /**
     * 刷新图片列表广告数据
     */
    public static void notifyPhotosAdLoaded() {
        TakePhotoImpl.notifyPhotosAdLoaded();
    }

    /**
     * 刷新专辑项目列表广告
     */
    public static void notifyAlbumItemsAdLoaded() {
        TakePhotoImpl.notifyAlbumItemsAdLoaded();
    }

}
