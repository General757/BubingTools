package com.bubing.camera.builder;

import android.app.Activity;
import android.net.Uri;
import android.view.View;

import com.bubing.camera.callback.SelectCallback;
import com.bubing.camera.constant.Constants;
import com.bubing.camera.constant.StartupType;
import com.bubing.camera.engine.ImageEngine;
import com.bubing.camera.models.ad.AdListener;
import com.bubing.camera.models.album.entity.Photo;
import com.bubing.camera.models.compress.CompressConfig;
import com.bubing.camera.models.crop.CropOptions;
import com.bubing.camera.result.EasyResult;
import com.bubing.camera.result.Result;
import com.bubing.camera.setting.Setting;
import com.bubing.camera.ui.EasyPhotosActivity;
import com.bubing.camera.utils.UriUtils;

import java.io.File;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Arrays;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

/**
 * @ClassName: AlbumBuilder
 * @Description: CameraPhotoTools的启动管理器
 * @Author: bubing
 * @Date: 2020-05-09 14:25
 */
public class AlbumBuilder {
    private static final String TAG = "AlbumBuilder ";
    private static AlbumBuilder instance;
    private WeakReference<Activity> mActivity;
    private WeakReference<Fragment> mFragmentV;
    private WeakReference<android.app.Fragment> mFragment;
    private StartupType startupType;
    private WeakReference<AdListener> adListener;

    //私有构造函数，不允许外部调用，真正实例化通过静态方法实现
    private AlbumBuilder(Activity activity, StartupType startupType) {
        mActivity = new WeakReference<Activity>(activity);
        this.startupType = startupType;
    }

    private AlbumBuilder(android.app.Fragment fragment, StartupType startupType) {
        mFragment = new WeakReference<android.app.Fragment>(fragment);
        this.startupType = startupType;
    }

    private AlbumBuilder(FragmentActivity activity, StartupType startupType) {
        mActivity = new WeakReference<Activity>(activity);
        this.startupType = startupType;
    }

    private AlbumBuilder(Fragment fragment, StartupType startupType) {
        mFragmentV = new WeakReference<Fragment>(fragment);
        this.startupType = startupType;
    }


    /**
     * 内部处理相机和相册的实例
     *
     * @param activity Activity的实例
     * @return AlbumBuilder EasyPhotos的实例
     */

    private static AlbumBuilder with(Activity activity, StartupType startupType) {
        clear();
        instance = new AlbumBuilder(activity, startupType);
        return instance;
    }

    private static AlbumBuilder with(android.app.Fragment fragment, StartupType startupType) {
        clear();
        instance = new AlbumBuilder(fragment, startupType);
        return instance;
    }

    private static AlbumBuilder with(FragmentActivity activity, StartupType startupType) {
        clear();
        instance = new AlbumBuilder(activity, startupType);
        return instance;
    }

    private static AlbumBuilder with(Fragment fragmentV, StartupType startupType) {
        clear();
        instance = new AlbumBuilder(fragmentV, startupType);
        return instance;
    }


    /**
     * 创建相机
     *
     * @param activity 上下文
     * @return AlbumBuilder
     */

    public static AlbumBuilder createCamera(Activity activity) {
        return AlbumBuilder.with(activity, StartupType.CAMERA);
    }

    public static AlbumBuilder createCamera(android.app.Fragment fragment) {
        return AlbumBuilder.with(fragment, StartupType.CAMERA);
    }

    public static AlbumBuilder createCamera(FragmentActivity activity) {
        return AlbumBuilder.with(activity, StartupType.CAMERA);
    }

    public static AlbumBuilder createCamera(Fragment fragmentV) {
        return AlbumBuilder.with(fragmentV, StartupType.CAMERA);
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
        if (Setting.imageEngine != imageEngine) {
            Setting.imageEngine = imageEngine;
        }
        if (isShowCamera) {
            return AlbumBuilder.with(activity, StartupType.ALBUM_CAMERA);
        } else {
            return AlbumBuilder.with(activity, StartupType.ALBUM);
        }
    }

    public static AlbumBuilder createAlbum(android.app.Fragment fragment, boolean isShowCamera, @NonNull ImageEngine imageEngine) {
        if (Setting.imageEngine != imageEngine) {
            Setting.imageEngine = imageEngine;
        }
        if (isShowCamera) {
            return AlbumBuilder.with(fragment, StartupType.ALBUM_CAMERA);
        } else {
            return AlbumBuilder.with(fragment, StartupType.ALBUM);
        }
    }

    public static AlbumBuilder createAlbum(FragmentActivity activity, boolean isShowCamera, @NonNull ImageEngine imageEngine) {
        if (Setting.imageEngine != imageEngine) {
            Setting.imageEngine = imageEngine;
        }
        if (isShowCamera) {
            return AlbumBuilder.with(activity, StartupType.ALBUM_CAMERA);
        } else {
            return AlbumBuilder.with(activity, StartupType.ALBUM);
        }
    }

    public static AlbumBuilder createAlbum(Fragment fragmentV, boolean isShowCamera, @NonNull ImageEngine imageEngine) {
        if (Setting.imageEngine != imageEngine) {
            Setting.imageEngine = imageEngine;
        }
        if (isShowCamera) {
            return AlbumBuilder.with(fragmentV, StartupType.ALBUM_CAMERA);
        } else {
            return AlbumBuilder.with(fragmentV, StartupType.ALBUM);
        }
    }

    /**
     * 设置选择数
     *
     * @param selectorMaxCount 最大选择数
     * @return AlbumBuilder
     */
    public AlbumBuilder setCount(int selectorMaxCount) {
        Setting.count = selectorMaxCount;
        return AlbumBuilder.this;
    }

    /**
     * 设置选择图片数(设置此参数后setCount失效)
     *
     * @param selectorMaxCount 最大选择数
     * @return AlbumBuilder
     */
    public AlbumBuilder setPictureCount(int selectorMaxCount) {
        Setting.pictureCount = selectorMaxCount;
        return AlbumBuilder.this;
    }

    /**
     * 设置选择视频数(设置此参数后setCount失效)
     *
     * @param selectorMaxCount 最大选择数
     * @return AlbumBuilder
     */
    public AlbumBuilder setVideoCount(int selectorMaxCount) {
        Setting.videoCount = selectorMaxCount;
        return AlbumBuilder.this;
    }

    /**
     * 设置相机按钮位置
     *
     * @param cLocation 使用Material Design风格相机按钮 默认 BOTTOM_RIGHT
     * @return AlbumBuilder
     */
    public AlbumBuilder setCameraLocation(@Setting.Location int cLocation) {
        Setting.cameraLocation = cLocation;
        return AlbumBuilder.this;
    }

    /**
     * 设置显示照片的最小文件大小
     *
     * @param minFileSize 最小文件大小，单位Bytes
     * @return AlbumBuilder
     */
    public AlbumBuilder setMinFileSize(long minFileSize) {
        Setting.minSize = minFileSize;
        return AlbumBuilder.this;
    }

    /**
     * 设置显示照片的最小宽度
     *
     * @param minWidth 照片的最小宽度，单位Px
     * @return AlbumBuilder
     */
    public AlbumBuilder setMinWidth(int minWidth) {
        Setting.minWidth = minWidth;
        return AlbumBuilder.this;
    }

    /**
     * 设置显示照片的最小高度
     *
     * @param minHeight 显示照片的最小高度，单位Px
     * @return AlbumBuilder
     */
    public AlbumBuilder setMinHeight(int minHeight) {
        Setting.minHeight = minHeight;
        return AlbumBuilder.this;
    }

    /**
     * 设置默认选择图片集合
     *
     * @param selectedPhotos 默认选择图片集合
     * @return AlbumBuilder
     */
    public AlbumBuilder setSelectedPhotos(ArrayList<Photo> selectedPhotos) {
        Setting.selectedPhotos.clear();
        if (selectedPhotos == null || selectedPhotos.isEmpty())
            return AlbumBuilder.this;

        Setting.selectedPhotos.addAll(selectedPhotos);
        Setting.selectedOriginal = selectedPhotos.get(0).selectedOriginal;
        return AlbumBuilder.this;
    }

    /**
     * 设置默认选择图片地址集合
     *
     * @param selectedPhotoPaths 默认选择图片地址集合
     * @return AlbumBuilder
     * @Deprecated android 10 不推荐使用直接使用Path方式，推荐使用Photo类
     */
    @Deprecated
    public AlbumBuilder setSelectedPhotoPaths(ArrayList<String> selectedPhotoPaths) {
        Setting.selectedPhotos.clear();
        ArrayList<Photo> selectedPhotos = new ArrayList<>();
        for (String path : selectedPhotoPaths) {
            File file = new File(path);
            Uri uri = null;
            if (null != mActivity && null != mActivity.get()) {
                uri = UriUtils.getUri(mActivity.get(), file);
            }
            if (null != mFragment && null != mFragment.get()) {
                uri = UriUtils.getUri(mFragment.get().getActivity(), file);
            }
            if (null != mFragmentV && null != mFragmentV.get()) {
                uri = UriUtils.getUri(mFragmentV.get().getActivity(), file);
            }
            if (uri == null) {
                uri = Uri.fromFile(file);
            }

            selectedPhotos.add(new Photo(null, uri, path, 0, 0, 0, 0, 0, null));
        }
        Setting.selectedPhotos.addAll(selectedPhotos);
        return AlbumBuilder.this;
    }

    /**
     * 原图按钮设置,不调用该方法不显示原图按钮
     *
     * @param isChecked    原图选项默认状态是否为选中状态
     * @param usable       原图按钮是否可使用
     * @param unusableHint 原图按钮不可使用时给用户的文字提示
     * @return AlbumBuilder
     */
    public AlbumBuilder setOriginalMenu(boolean isChecked, boolean usable, String unusableHint) {
        Setting.showOriginalMenu = true;
        Setting.selectedOriginal = isChecked;
        Setting.originalMenuUsable = usable;
        Setting.originalMenuUnusableHint = unusableHint;
        return AlbumBuilder.this;
    }

    /**
     * 是否显示拼图按钮
     *
     * @param shouldShow 是否显示
     * @return AlbumBuilder
     */
    public AlbumBuilder setPuzzleMenu(boolean shouldShow) {
        Setting.showPuzzleMenu = shouldShow;
        return AlbumBuilder.this;
    }

    /**
     * 只显示Video
     *
     * @return @return AlbumBuilder
     */
    public AlbumBuilder onlyVideo() {
        return filter(Constants.Type.VIDEO);
    }

    /**
     * 过滤
     *
     * @param types {@link Constants}
     * @return @return AlbumBuilder
     */
    public AlbumBuilder filter(String... types) {
        Setting.filterTypes = Arrays.asList(types);
        return AlbumBuilder.this;
    }

    /**
     * 是否显示gif图
     *
     * @param shouldShow 是否显示
     * @return @return AlbumBuilder
     */
    public AlbumBuilder setGif(boolean shouldShow) {
        Setting.showGif = shouldShow;
        return AlbumBuilder.this;
    }

    /**
     * 是否显示video
     *
     * @param shouldShow 是否显示
     * @return @return AlbumBuilder
     */
    public AlbumBuilder setVideo(boolean shouldShow) {
        Setting.showVideo = shouldShow;
        return AlbumBuilder.this;
    }

    /**
     * 显示最少多少秒的视频
     *
     * @param second 秒
     * @return @return AlbumBuilder
     */
    public AlbumBuilder setVideoMinSecond(int second) {
        Setting.videoMinSecond = second * 1000;
        return AlbumBuilder.this;
    }

    /**
     * 显示最多多少秒的视频
     *
     * @param second 秒
     * @return @return AlbumBuilder
     */
    public AlbumBuilder setVideoMaxSecond(int second) {
        Setting.videoMaxSecond = second * 1000;
        return AlbumBuilder.this;
    }

    /**
     * 相册选择页是否显示清空按钮
     *
     * @param shouldShow
     * @return
     */
    public AlbumBuilder setCleanMenu(boolean shouldShow) {
        Setting.showCleanMenu = shouldShow;
        return AlbumBuilder.this;
    }

    /**
     * 是否剪切-配置
     *
     * @param cropOptions
     * @return
     */
    public AlbumBuilder setCropOptions(CropOptions cropOptions) {
        Setting.cropOptions = cropOptions;
        return AlbumBuilder.this;
    }

    /**
     * 是对拍的照片进行旋转角度纠正
     *
     * @param correctImage
     * @return
     */
    public AlbumBuilder setCropOptions(boolean correctImage) {
        Setting.correctImage = correctImage;
        return AlbumBuilder.this;
    }

    /**
     * 是否对照片进行压缩-压缩配置
     *
     * @param compressConfig
     * @return
     */
    public AlbumBuilder setCompressConfig(CompressConfig compressConfig) {
        Setting.compressConfig = compressConfig;
        return AlbumBuilder.this;
    }

    /**
     * 是否显示压缩对话框
     *
     * @param compressDialog
     * @return
     */
    public AlbumBuilder setCompressDialog(boolean compressDialog) {
        Setting.compressDialog = compressDialog;
        return AlbumBuilder.this;
    }

    private void setSettingParams() {
        switch (startupType) {
            case CAMERA:
                Setting.onlyStartCamera = true;
                Setting.isShowCamera = true;
                break;
            case ALBUM:
                Setting.isShowCamera = false;
                break;
            case ALBUM_CAMERA:
                Setting.isShowCamera = true;
                break;
        }
        if (!Setting.filterTypes.isEmpty()) {
            if (Setting.isFilter(Constants.Type.GIF)) {
                Setting.showGif = true;
            }
            if (Setting.isFilter(Constants.Type.VIDEO)) {
                Setting.showVideo = true;
            }
        }
        if (Setting.isOnlyVideo()) {
            //只选择视频 暂不支持拍照/拼图等
            Setting.isShowCamera = false;
            Setting.showPuzzleMenu = false;
            Setting.showGif = false;
            Setting.showVideo = true;
        }
        if (Setting.pictureCount != -1 || Setting.videoCount != -1) {
            Setting.count = Setting.pictureCount + Setting.videoCount;
            if (Setting.pictureCount == -1 || Setting.videoCount == -1) {
                Setting.count++;
            }
        }
    }

    /**
     * 设置启动属性
     *
     * @param requestCode startActivityForResult的请求码
     */

    public void start(int requestCode) {
        setSettingParams();
        launchCameraPhotosActivity(requestCode);
    }

    /**
     * 正式启动
     *
     * @param requestCode startActivityForResult的请求码
     */
    private void launchCameraPhotosActivity(int requestCode) {
        if (null != mActivity && null != mActivity.get()) {
            EasyPhotosActivity.start(mActivity.get(), requestCode);
            return;
        }
        if (null != mFragment && null != mFragment.get()) {
            EasyPhotosActivity.start(mFragment.get(), requestCode);
            return;
        }
        if (null != mFragmentV && null != mFragmentV.get()) {
            EasyPhotosActivity.start(mFragmentV.get(), requestCode);
        }
    }

    public void start(SelectCallback callback) {
        setSettingParams();
        if (null != mActivity && null != mActivity.get() && mActivity.get() instanceof FragmentActivity) {
            EasyResult.get((FragmentActivity) mActivity.get()).startEasyPhoto(callback);
            return;
        }
        if (null != mFragmentV && null != mFragmentV.get()) {
            EasyResult.get(mFragmentV.get()).startEasyPhoto(callback);
            return;
        }
        throw new RuntimeException("mActivity or mFragmentV maybe null, you can not use this " + "method... ");
    }

    /**
     * 清除所有数据
     */
    private static void clear() {
        Result.clear();
        Setting.clear();
        instance = null;
    }

//*********************AD************************************

    /**
     * 设置广告(不设置该选项则表示不使用广告)
     *
     * @param photosAdView         使用图片列表的广告View
     * @param photosAdIsLoaded     图片列表广告是否加载完毕
     * @param albumItemsAdView     使用专辑项目列表的广告View
     * @param albumItemsAdIsLoaded 专辑项目列表广告是否加载完毕
     * @return AlbumBuilder
     */
    public AlbumBuilder setAdView(View photosAdView, boolean photosAdIsLoaded, View albumItemsAdView, boolean albumItemsAdIsLoaded) {
        Setting.photosAdView = new WeakReference<View>(photosAdView);
        Setting.albumItemsAdView = new WeakReference<View>(albumItemsAdView);
        Setting.photoAdIsOk = photosAdIsLoaded;
        Setting.albumItemsAdIsOk = albumItemsAdIsLoaded;
        return AlbumBuilder.this;
    }

    /**
     * 设置广告监听
     * 内部使用，无需关心
     *
     * @param adListener 广告监听
     */
    public static void setAdListener(AdListener adListener) {
        if (null == instance) return;
        if (instance.startupType == StartupType.CAMERA) return;
        instance.adListener = new WeakReference<AdListener>(adListener);
    }

    /**
     * 刷新图片列表广告数据
     */
    public static void notifyPhotosAdLoaded() {
        if (Setting.photoAdIsOk) {
            return;
        }
        if (null == instance) {
            return;
        }
        if (instance.startupType == StartupType.CAMERA) {
            return;
        }
        if (null == instance.adListener) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    if (null != instance && null != instance.adListener) {
                        Setting.photoAdIsOk = true;
                        instance.adListener.get().onPhotosAdLoaded();
                    }
                }
            }).start();
            return;
        }
        Setting.photoAdIsOk = true;
        instance.adListener.get().onPhotosAdLoaded();
    }

    /**
     * 刷新专辑项目列表广告
     */
    public static void notifyAlbumItemsAdLoaded() {
        if (Setting.albumItemsAdIsOk) {
            return;
        }
        if (null == instance) {
            return;
        }
        if (instance.startupType == StartupType.CAMERA) {
            return;
        }
        if (null == instance.adListener) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    if (null != instance && null != instance.adListener) {
                        Setting.albumItemsAdIsOk = true;
                        instance.adListener.get().onAlbumItemsAdLoaded();
                    }
                }
            }).start();
            return;
        }
        Setting.albumItemsAdIsOk = true;
        instance.adListener.get().onAlbumItemsAdLoaded();
    }
}
