package com.bubing.camera;

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
import com.bubing.camera.result.ResultStorage;
import com.bubing.camera.result.TakeResult;
import com.bubing.camera.setting.Setting;
import com.bubing.camera.ui.CameraLandscapeActivity;
import com.bubing.camera.ui.CameraPortraitActivity;
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
 * @ClassName: TakePhotoImpl
 * @Description: java类作用描述
 * @Author: bubing
 * @Date: 2020-05-11 13:31
 * GitHub:https://github.com/crazycodeboy/TakePhoto
 * GitHub:https://github.com/HuanTanSheng/EasyPhotos
 */
public class TakePhotoImpl {
    private static final String TAG = TakePhotoImpl.class.getName();

    public WeakReference<Activity> mActivity;
    public WeakReference<Fragment> mFragmentV;
    public WeakReference<android.app.Fragment> mFragment;
    public StartupType startupType;

    private static TakePhotoImpl instance;

    //私有构造函数，不允许外部调用，真正实例化通过静态方法实现
    private TakePhotoImpl(Activity activity, StartupType startupType) {
        mActivity = new WeakReference<Activity>(activity);
        this.startupType = startupType;
    }

    private TakePhotoImpl(FragmentActivity activity, StartupType startupType) {
        mActivity = new WeakReference<Activity>(activity);
        this.startupType = startupType;
    }

    private TakePhotoImpl(android.app.Fragment fragment, StartupType startupType) {
        mFragment = new WeakReference<android.app.Fragment>(fragment);
        this.startupType = startupType;
    }

    private TakePhotoImpl(Fragment fragment, StartupType startupType) {
        mFragmentV = new WeakReference<Fragment>(fragment);
        this.startupType = startupType;
    }

    //内部处理相机和相册的实例
    private static TakePhotoImpl with(Activity activity, StartupType startupType) {
        clear();
        instance = new TakePhotoImpl(activity, startupType);
        return instance;
    }

    private static TakePhotoImpl with(android.app.Fragment fragment, StartupType startupType) {
        clear();
        instance = new TakePhotoImpl(fragment, startupType);
        return instance;
    }

    private static TakePhotoImpl with(FragmentActivity activity, StartupType startupType) {
        clear();
        instance = new TakePhotoImpl(activity, startupType);
        return instance;
    }

    private static TakePhotoImpl with(Fragment fragmentV, StartupType startupType) {
        clear();
        instance = new TakePhotoImpl(fragmentV, startupType);
        return instance;
    }

    /**
     * 创建相机
     *
     * @param activity 上下文
     * @return TakePhotoImpl
     */

    public static TakePhotoImpl createCamera(Activity activity) {
        return TakePhotoImpl.with(activity, StartupType.CAMERA);
    }

    public static TakePhotoImpl createCamera(android.app.Fragment fragment) {
        return TakePhotoImpl.with(fragment, StartupType.CAMERA);
    }

    public static TakePhotoImpl createCamera(FragmentActivity activity) {
        return TakePhotoImpl.with(activity, StartupType.CAMERA);
    }

    public static TakePhotoImpl createCamera(Fragment fragmentV) {
        return TakePhotoImpl.with(fragmentV, StartupType.CAMERA);
    }

    /**
     * 打开证件相机
     *
     * @param startupType 启动模式-拍摄证件方向
     */
    public static TakePhotoImpl createCamera(Activity activity, StartupType startupType) {
        return TakePhotoImpl.with(activity, StartupType.CAMERA);
    }

    public static TakePhotoImpl createCamera(android.app.Fragment fragment, StartupType startupType) {
        return TakePhotoImpl.with(fragment, StartupType.CAMERA);
    }

    public static TakePhotoImpl createCamera(FragmentActivity activity, StartupType startupType) {
        return TakePhotoImpl.with(activity, StartupType.CAMERA);
    }

    public static TakePhotoImpl createCamera(Fragment fragmentV, StartupType startupType) {
        return TakePhotoImpl.with(fragmentV, StartupType.CAMERA);
    }

    /**
     * 创建相册
     *
     * @param activity     上下文
     * @param isShowCamera 是否显示相机按钮
     * @param imageEngine  图片加载引擎的具体实现
     * @return
     */
    public static TakePhotoImpl createAlbum(Activity activity, boolean isShowCamera, @NonNull ImageEngine imageEngine) {
        if (Setting.imageEngine != imageEngine)
            Setting.imageEngine = imageEngine;

        if (isShowCamera)
            return TakePhotoImpl.with(activity, StartupType.ALBUM_CAMERA);
        else
            return TakePhotoImpl.with(activity, StartupType.ALBUM);
    }

    public static TakePhotoImpl createAlbum(android.app.Fragment fragment, boolean isShowCamera, @NonNull ImageEngine imageEngine) {
        if (Setting.imageEngine != imageEngine)
            Setting.imageEngine = imageEngine;

        if (isShowCamera)
            return TakePhotoImpl.with(fragment, StartupType.ALBUM_CAMERA);
        else
            return TakePhotoImpl.with(fragment, StartupType.ALBUM);
    }

    public static TakePhotoImpl createAlbum(FragmentActivity activity, boolean isShowCamera, @NonNull ImageEngine imageEngine) {
        if (Setting.imageEngine != imageEngine)
            Setting.imageEngine = imageEngine;

        if (isShowCamera)
            return TakePhotoImpl.with(activity, StartupType.ALBUM_CAMERA);
        else
            return TakePhotoImpl.with(activity, StartupType.ALBUM);
    }

    public static TakePhotoImpl createAlbum(Fragment fragmentV, boolean isShowCamera, @NonNull ImageEngine imageEngine) {
        if (Setting.imageEngine != imageEngine)
            Setting.imageEngine = imageEngine;

        if (isShowCamera)
            return TakePhotoImpl.with(fragmentV, StartupType.ALBUM_CAMERA);
        else
            return TakePhotoImpl.with(fragmentV, StartupType.ALBUM);
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
     * 正式启动
     *
     * @param requestCode startActivityForResult的请求码
     */
    private void launchActivity(int requestCode) {
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

    public void openActivity(int requestCode) {
        if (startupType == StartupType.CAMERA_COMPANY_PORTRAIT) {
            if (null != mActivity && null != mActivity.get()) {
                CameraPortraitActivity.start(mActivity.get(), startupType, requestCode);
                return;
            }
            if (null != mFragment && null != mFragment.get()) {
                CameraPortraitActivity.start(mFragment.get(), startupType, requestCode);
                return;
            }
            if (null != mFragmentV && null != mFragmentV.get()) {
                CameraPortraitActivity.start(mFragmentV.get(), startupType, requestCode);
            }
        } else {
            if (null != mActivity && null != mActivity.get()) {
                CameraLandscapeActivity.start(mActivity.get(), startupType, requestCode);
                return;
            }
            if (null != mFragment && null != mFragment.get()) {
                CameraLandscapeActivity.start(mFragment.get(), startupType, requestCode);
                return;
            }
            if (null != mFragmentV && null != mFragmentV.get()) {
                CameraLandscapeActivity.start(mFragmentV.get(), startupType, requestCode);
            }
        }
    }

    /**
     * 设置启动属性
     *
     * @param requestCode startActivityForResult的请求码
     */
    public void start(int requestCode) {
        if (startupType != null && (StartupType.CAMERA_IDCARD_FRONT == startupType ||
                StartupType.CAMERA_IDCARD_BACK == startupType ||
                StartupType.CAMERA_COMPANY_PORTRAIT == startupType ||
                StartupType.CAMERA_COMPANY_LANDSCAPE == startupType)) {
            openActivity(requestCode);
        } else {
            setSettingParams();
            launchActivity(requestCode);
        }
    }

    public void start(SelectCallback callback) {
        setSettingParams();
        if (null != mActivity && null != mActivity.get() && mActivity.get() instanceof FragmentActivity) {
            TakeResult.get((FragmentActivity) mActivity.get()).startEasyPhoto(callback);
            return;
        }
        if (null != mFragmentV && null != mFragmentV.get()) {
            TakeResult.get(mFragmentV.get()).startEasyPhoto(callback);
            return;
        }
        throw new RuntimeException("mActivity or mFragmentV maybe null, you can not use this " + "method... ");
    }

    /**
     * 清除所有数据
     */
    private static void clear() {
        ResultStorage.clear();
        Setting.clear();
        instance = null;
    }

    //*********************配置参数************************************

    /**
     * 设置选择数
     *
     * @param selectorMaxCount 最大选择数
     * @return TakePhotoImpl
     */
    public TakePhotoImpl setCount(int selectorMaxCount) {
        Setting.count = selectorMaxCount;
        return TakePhotoImpl.this;
    }

    /**
     * 设置选择图片数(设置此参数后setCount失效)
     *
     * @param selectorMaxCount 最大选择数
     * @return TakePhotoImpl
     */
    public TakePhotoImpl setPictureCount(int selectorMaxCount) {
        Setting.pictureCount = selectorMaxCount;
        return TakePhotoImpl.this;
    }

    /**
     * 设置选择视频数(设置此参数后setCount失效)
     *
     * @param selectorMaxCount 最大选择数
     * @return TakePhotoImpl
     */
    public TakePhotoImpl setVideoCount(int selectorMaxCount) {
        Setting.videoCount = selectorMaxCount;
        return TakePhotoImpl.this;
    }

    /**
     * 设置相机按钮位置
     *
     * @param cLocation 使用Material Design风格相机按钮 默认 BOTTOM_RIGHT
     * @return TakePhotoImpl
     */
    public TakePhotoImpl setCameraLocation(@Setting.Location int cLocation) {
        Setting.cameraLocation = cLocation;
        return TakePhotoImpl.this;
    }

    /**
     * 设置显示照片的最小文件大小
     *
     * @param minFileSize 最小文件大小，单位Bytes
     * @return TakePhotoImpl
     */
    public TakePhotoImpl setMinFileSize(long minFileSize) {
        Setting.minSize = minFileSize;
        return TakePhotoImpl.this;
    }

    /**
     * 设置显示照片的最小宽度
     *
     * @param minWidth 照片的最小宽度，单位Px
     * @return TakePhotoImpl
     */
    public TakePhotoImpl setMinWidth(int minWidth) {
        Setting.minWidth = minWidth;
        return TakePhotoImpl.this;
    }

    /**
     * 设置显示照片的最小高度
     *
     * @param minHeight 显示照片的最小高度，单位Px
     * @return TakePhotoImpl
     */
    public TakePhotoImpl setMinHeight(int minHeight) {
        Setting.minHeight = minHeight;
        return TakePhotoImpl.this;
    }

    /**
     * 设置默认选择图片集合
     *
     * @param selectedPhotos 默认选择图片集合
     * @return TakePhotoImpl
     */
    public TakePhotoImpl setSelectedPhotos(ArrayList<Photo> selectedPhotos) {
        Setting.selectedPhotos.clear();
        if (selectedPhotos == null || selectedPhotos.isEmpty())
            return TakePhotoImpl.this;

        Setting.selectedPhotos.addAll(selectedPhotos);
        Setting.selectedOriginal = selectedPhotos.get(0).selectedOriginal;
        return TakePhotoImpl.this;
    }

    /**
     * 设置默认选择图片地址集合
     *
     * @param selectedPhotoPaths 默认选择图片地址集合
     * @return TakePhotoImpl
     * @Deprecated android 10 不推荐使用直接使用Path方式，推荐使用Photo类
     */
    @Deprecated
    public TakePhotoImpl setSelectedPhotoPaths(ArrayList<String> selectedPhotoPaths) {
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
        return TakePhotoImpl.this;
    }

    /**
     * 原图按钮设置,不调用该方法不显示原图按钮
     *
     * @param isChecked    原图选项默认状态是否为选中状态
     * @param usable       原图按钮是否可使用
     * @param unusableHint 原图按钮不可使用时给用户的文字提示
     * @return TakePhotoImpl
     */
    public TakePhotoImpl setOriginalMenu(boolean isChecked, boolean usable, String unusableHint) {
        Setting.showOriginalMenu = true;
        Setting.selectedOriginal = isChecked;
        Setting.originalMenuUsable = usable;
        Setting.originalMenuUnusableHint = unusableHint;
        return TakePhotoImpl.this;
    }

    /**
     * 是否显示拼图按钮
     *
     * @param shouldShow 是否显示
     * @return TakePhotoImpl
     */
    public TakePhotoImpl setPuzzleMenu(boolean shouldShow) {
        Setting.showPuzzleMenu = shouldShow;
        return TakePhotoImpl.this;
    }

    /**
     * 只显示Video
     *
     * @return @return TakePhotoImpl
     */
    public TakePhotoImpl onlyVideo() {
        return filter(Constants.Type.VIDEO);
    }

    /**
     * 过滤
     *
     * @param types {@link Constants}
     * @return @return TakePhotoImpl
     */
    public TakePhotoImpl filter(String... types) {
        Setting.filterTypes = Arrays.asList(types);
        return TakePhotoImpl.this;
    }

    /**
     * 是否显示gif图
     *
     * @param shouldShow 是否显示
     * @return @return TakePhotoImpl
     */
    public TakePhotoImpl setGif(boolean shouldShow) {
        Setting.showGif = shouldShow;
        return TakePhotoImpl.this;
    }

    /**
     * 是否显示video
     *
     * @param shouldShow 是否显示
     * @return @return TakePhotoImpl
     */
    public TakePhotoImpl setVideo(boolean shouldShow) {
        Setting.showVideo = shouldShow;
        return TakePhotoImpl.this;
    }

    /**
     * 显示最少多少秒的视频
     *
     * @param second 秒
     * @return @return TakePhotoImpl
     */
    public TakePhotoImpl setVideoMinSecond(int second) {
        Setting.videoMinSecond = second * 1000;
        return TakePhotoImpl.this;
    }

    /**
     * 显示最多多少秒的视频
     *
     * @param second 秒
     * @return @return TakePhotoImpl
     */
    public TakePhotoImpl setVideoMaxSecond(int second) {
        Setting.videoMaxSecond = second * 1000;
        return TakePhotoImpl.this;
    }

    /**
     * 相册选择页是否显示清空按钮
     *
     * @param shouldShow
     * @return
     */
    public TakePhotoImpl setCleanMenu(boolean shouldShow) {
        Setting.showCleanMenu = shouldShow;
        return TakePhotoImpl.this;
    }

    /**
     * 是否剪切-配置
     *
     * @param cropOptions
     * @return
     */
    public TakePhotoImpl setCropOptions(CropOptions cropOptions) {
        Setting.cropOptions = cropOptions;
        return TakePhotoImpl.this;
    }

    /**
     * 是对拍的照片进行旋转角度纠正
     *
     * @param correctImage
     * @return
     */
    public TakePhotoImpl setCropOptions(boolean correctImage) {
        Setting.correctImage = correctImage;
        return TakePhotoImpl.this;
    }

    /**
     * 是否对照片进行压缩-压缩配置
     *
     * @param compressConfig
     * @return
     */
    public TakePhotoImpl setCompressConfig(CompressConfig compressConfig) {
        Setting.compressConfig = compressConfig;
        return TakePhotoImpl.this;
    }

    /**
     * 是否显示压缩对话框
     *
     * @param compressDialog
     * @return
     */
    public TakePhotoImpl setCompressDialog(boolean compressDialog) {
        Setting.compressDialog = compressDialog;
        return TakePhotoImpl.this;
    }

    //*********************AD************************************
    private WeakReference<AdListener> adListener;

    /**
     * 设置广告(不设置该选项则表示不使用广告)
     *
     * @param photosAdView         使用图片列表的广告View
     * @param photosAdIsLoaded     图片列表广告是否加载完毕
     * @param albumItemsAdView     使用专辑项目列表的广告View
     * @param albumItemsAdIsLoaded 专辑项目列表广告是否加载完毕
     * @return TakePhotoImpl
     */
    public TakePhotoImpl setAdView(View photosAdView, boolean photosAdIsLoaded, View albumItemsAdView, boolean albumItemsAdIsLoaded) {
        Setting.photosAdView = new WeakReference<View>(photosAdView);
        Setting.albumItemsAdView = new WeakReference<View>(albumItemsAdView);
        Setting.photoAdIsOk = photosAdIsLoaded;
        Setting.albumItemsAdIsOk = albumItemsAdIsLoaded;
        return TakePhotoImpl.this;
    }

    /**
     * 设置广告监听
     * 内部使用，无需关心
     *
     * @param adListener 广告监听
     */
    public static void setAdListener(AdListener adListener) {
        if (null == instance || instance.startupType == StartupType.CAMERA)
            return;

        instance.adListener = new WeakReference<AdListener>(adListener);
    }

    /**
     * 刷新图片列表广告数据
     */
    public static void notifyPhotosAdLoaded() {
        if (Setting.photoAdIsOk)
            return;

        if (null == instance || instance.startupType == StartupType.CAMERA)
            return;

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
        if (Setting.albumItemsAdIsOk)
            return;

        if (null == instance || instance.startupType == StartupType.CAMERA)
            return;

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
