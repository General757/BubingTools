package com.bubing.camera.ui;

import android.Manifest;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bubing.camera.R;
import com.bubing.camera.constant.Constants;
import com.bubing.camera.exception.BException;
import com.bubing.camera.exception.BExceptionType;
import com.bubing.camera.models.ContextWrap;
import com.bubing.camera.models.ImageType;
import com.bubing.camera.models.ad.AdListener;
import com.bubing.camera.models.ad.AdUtils;
import com.bubing.camera.models.album.AlbumModel;
import com.bubing.camera.models.album.entity.Photo;
import com.bubing.camera.models.compress.CompressImage;
import com.bubing.camera.models.compress.CompressImageImpl;
import com.bubing.camera.models.crop.CropOptions;
import com.bubing.camera.models.crop.CropUtils;
import com.bubing.camera.models.crop.MultipleCrop;
import com.bubing.camera.models.ResultPhoto;
import com.bubing.camera.result.Result;
import com.bubing.camera.setting.Setting;
import com.bubing.camera.ui.adapter.AlbumItemsAdapter;
import com.bubing.camera.ui.adapter.PhotosAdapter;
import com.bubing.camera.utils.BubingLog;
import com.bubing.camera.utils.CameraUtils;
import com.bubing.camera.utils.ColorUtils;
import com.bubing.camera.utils.FileUtils;
import com.bubing.camera.utils.ImageFiles;
import com.bubing.camera.utils.ImageRotateUtil;
import com.bubing.camera.utils.PermissionUtil;
import com.bubing.camera.utils.PhotoUtils;
import com.bubing.camera.utils.SettingsUtils;
import com.bubing.camera.utils.StringUtils;
import com.bubing.camera.utils.UriUtils;
import com.bubing.camera.utils.bitmap.BitmapUtils;
import com.bubing.camera.utils.media.DurationUtils;
import com.bubing.camera.utils.media.MediaScannerConnectionUtils;
import com.bubing.camera.utils.system.SystemUtils;
import com.bubing.camera.widget.PressedTextView;
import com.soundcloud.android.crop.Crop;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.Map;

import androidx.annotation.IdRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SimpleItemAnimator;

public class EasyPhotosActivity extends AppCompatActivity implements AlbumItemsAdapter.OnClickListener, PhotosAdapter.OnClickListener, AdListener, View.OnClickListener {
    private static final String TAG = EasyPhotosActivity.class.getSimpleName();

    private AlbumModel albumModel;
    private ArrayList<Object> photoList = new ArrayList<>();
    private ArrayList<Object> albumItemList = new ArrayList<>();

    private RecyclerView rvPhotos;
    private PhotosAdapter photosAdapter;
    private GridLayoutManager gridLayoutManager;

    private RecyclerView rvAlbumItems;
    private AlbumItemsAdapter albumItemsAdapter;
    private RelativeLayout rootViewAlbumItems;

    private PressedTextView tvAlbumItems, tvDone, tvPreview;
    private TextView tvOriginal;
    private AnimatorSet setHide;
    private AnimatorSet setShow;

    private int currAlbumItemIndex = 0;

    private ImageView ivCamera;
    private TextView tvTitle;

    private LinearLayout mSecondMenus;

    private RelativeLayout permissionView;
    private TextView tvPermission;
    private View mBottomBar;

    public static void start(Activity activity, int requestCode) {
        Intent intent = new Intent(activity, EasyPhotosActivity.class);
        activity.startActivityForResult(intent, requestCode);
    }

    public static void start(Fragment fragment, int requestCode) {
        Intent intent = new Intent(fragment.getActivity(), EasyPhotosActivity.class);
        fragment.startActivityForResult(intent, requestCode);
    }

    public static void start(androidx.fragment.app.Fragment fragment, int requestCode) {
        Intent intent = new Intent(fragment.getContext(), EasyPhotosActivity.class);
        fragment.startActivityForResult(intent, requestCode);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_easy_photos);
        hideActionBar();
        adaptationStatusBar();
        if (!Setting.onlyStartCamera && null == Setting.imageEngine) {
            finish();
            return;
        }
        initSomeViews();
        if (PermissionUtil.checkAndRequestPermissionsInActivity(this, getNeedPermissions())) {
            hasPermissions();
        } else {
            permissionView.setVisibility(View.VISIBLE);
        }
    }

    private void hideActionBar() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null)
            actionBar.hide();
    }

    private void adaptationStatusBar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            int statusColor = getWindow().getStatusBarColor();
            if (statusColor == Color.TRANSPARENT) {
                statusColor = ContextCompat.getColor(this, R.color.colorPrimaryDark);
            }
            if (ColorUtils.isWhiteColor(statusColor)) {
                SystemUtils.getInstance().setStatusDark(this, true);
            }
        }
    }

    private void initSomeViews() {
        mBottomBar = findViewById(R.id.m_bottom_bar);
        permissionView = findViewById(R.id.rl_permissions_view);
        tvPermission = findViewById(R.id.tv_permission);
        rootViewAlbumItems = findViewById(R.id.root_view_album_items);
        tvTitle = findViewById(R.id.tv_title);
        if (Setting.isOnlyVideo())
            tvTitle.setText(R.string.video_selection_easy_photos);
        findViewById(R.id.iv_second_menu).setVisibility(Setting.showPuzzleMenu || Setting.showCleanMenu || Setting.showOriginalMenu ? View.VISIBLE : View.GONE);
        setClick(R.id.iv_back);
    }

    private void hasPermissions() {
        permissionView.setVisibility(View.GONE);
        if (Setting.onlyStartCamera) {
            launchCamera(Constants.Code.REQUEST_CAMERA);
            return;
        }
        AlbumModel.CallBack albumModelCallBack = new AlbumModel.CallBack() {
            @Override
            public void onAlbumWorkedCallBack() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        onAlbumWorkedDo();
                    }
                });
            }
        };
        albumModel = AlbumModel.getInstance();
        albumModel.query(this, albumModelCallBack);
    }

    protected String[] getNeedPermissions() {
        if (Setting.isShowCamera) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN)
                return new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE};
            return new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN)
                return new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE};
            return new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE};
        }
    }

    private void onAlbumWorkedDo() {
        initView();
    }

    private void initView() {
        if (albumModel.getAlbumItems().isEmpty()) {
            Toast.makeText(this, R.string.no_photos_easy_photos, Toast.LENGTH_LONG).show();
            if (Setting.isShowCamera) launchCamera(Constants.Code.REQUEST_CAMERA);
            else finish();
            return;
        }

        AdUtils.setAdListener(this);
        if (Setting.hasPhotosAd()) {
            findViewById(R.id.m_tool_bar_bottom_line).setVisibility(View.GONE);
        }
        ivCamera = findViewById(R.id.fab_camera);
        if (Setting.isShowCamera && Setting.isBottomRightCamera()) {
            ivCamera.setVisibility(View.VISIBLE);
        }
        if (!Setting.showPuzzleMenu) {
            findViewById(R.id.tv_puzzle).setVisibility(View.GONE);
        }
        mSecondMenus = findViewById(R.id.m_second_level_menu);
        int columns = getResources().getInteger(R.integer.photos_columns_easy_photos);
        tvAlbumItems = findViewById(R.id.tv_album_items);
        tvAlbumItems.setText(albumModel.getAlbumItems().get(0).name);
        tvDone = findViewById(R.id.tv_done);
        rvPhotos = findViewById(R.id.rv_photos);
        ((SimpleItemAnimator) rvPhotos.getItemAnimator()).setSupportsChangeAnimations(false);
        //去除item更新的闪光
        photoList.clear();
        photoList.addAll(albumModel.getCurrAlbumItemPhotos(0));
        int index = 0;
        if (Setting.hasPhotosAd()) {
            photoList.add(index, Setting.photosAdView);
        }
        if (Setting.isShowCamera && !Setting.isBottomRightCamera()) {
            if (Setting.hasPhotosAd()) index = 1;
            photoList.add(index, null);
        }
        photosAdapter = new PhotosAdapter(this, photoList, this);

        gridLayoutManager = new GridLayoutManager(this, columns);
        if (Setting.hasPhotosAd()) {
            gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                @Override
                public int getSpanSize(int position) {
                    if (position == 0) {
                        return gridLayoutManager.getSpanCount();//独占一行
                    } else {
                        return 1;//只占一行中的一列
                    }
                }
            });
        }
        rvPhotos.setLayoutManager(gridLayoutManager);
        rvPhotos.setAdapter(photosAdapter);
        tvOriginal = findViewById(R.id.tv_original);
        if (Setting.showOriginalMenu)
            processOriginalMenu();
        else
            tvOriginal.setVisibility(View.GONE);
        tvPreview = findViewById(R.id.tv_preview);

        initAlbumItems();
        shouldShowMenuDone();
        setClick(R.id.iv_album_items, R.id.tv_clear, R.id.iv_second_menu, R.id.tv_puzzle);
        setClick(tvAlbumItems, rootViewAlbumItems, tvDone, tvOriginal, tvPreview, ivCamera);
    }

    private void initAlbumItems() {
        rvAlbumItems = findViewById(R.id.rv_album_items);
        albumItemList.clear();
        albumItemList.addAll(albumModel.getAlbumItems());

        if (Setting.hasAlbumItemsAd()) {
            int albumItemsAdIndex = 2;
            if (albumItemList.size() < albumItemsAdIndex + 1) {
                albumItemsAdIndex = albumItemList.size() - 1;
            }
            albumItemList.add(albumItemsAdIndex, Setting.albumItemsAdView);
        }
        albumItemsAdapter = new AlbumItemsAdapter(this, albumItemList, 0, this);
        rvAlbumItems.setLayoutManager(new LinearLayoutManager(this));
        rvAlbumItems.setAdapter(albumItemsAdapter);
    }

    private void setClick(@IdRes int... ids) {
        for (int id : ids) {
            findViewById(id).setOnClickListener(this);
        }
    }

    private void setClick(View... views) {
        for (View v : views) {
            v.setOnClickListener(this);
        }
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (R.id.tv_album_items == id || R.id.iv_album_items == id) {//相册按钮
            showAlbumItems(View.GONE == rootViewAlbumItems.getVisibility());
        } else if (R.id.root_view_album_items == id) {//相册view
            showAlbumItems(false);
        } else if (R.id.iv_back == id) {//返回
            onBackPressed();
        } else if (R.id.tv_done == id) {//完成
            done();
        } else if (R.id.tv_clear == id) {//清空
            if (Result.isEmpty()) {
                processSecondMenu();
                return;
            }
            Result.removeAll();
            photosAdapter.change();
            shouldShowMenuDone();
            processSecondMenu();
        } else if (R.id.tv_original == id) {//原图
            if (!Setting.originalMenuUsable) {
                Toast.makeText(this, Setting.originalMenuUnusableHint, Toast.LENGTH_SHORT).show();
                return;
            }
            Setting.selectedOriginal = !Setting.selectedOriginal;
            processOriginalMenu();
            processSecondMenu();
        } else if (R.id.tv_preview == id) {//预览
            PreviewActivity.start(EasyPhotosActivity.this, -1, 0);
        } else if (R.id.fab_camera == id) {//拍照
            launchCamera(Constants.Code.REQUEST_CAMERA);
        } else if (R.id.iv_second_menu == id) {//分类menu
            processSecondMenu();
        } else if (R.id.tv_puzzle == id) {//拼一张
            processSecondMenu();
            PuzzleSelectorActivity.start(this);
        }
    }

    public void processSecondMenu() {
        if (mSecondMenus == null)
            return;

        if (View.VISIBLE == mSecondMenus.getVisibility()) {
            mSecondMenus.setVisibility(View.INVISIBLE);
            if (Setting.isShowCamera && Setting.isBottomRightCamera()) {
                ivCamera.setVisibility(View.VISIBLE);
            }
        } else {
            mSecondMenus.setVisibility(View.VISIBLE);
            if (Setting.isShowCamera && Setting.isBottomRightCamera()) {
                ivCamera.setVisibility(View.INVISIBLE);
            }
        }
    }

    private void done() {//点击完成按钮
        for (Photo photo : Result.photos) {
            try {
                if (photo.width == 0 || photo.height == 0)
                    BitmapUtils.calculateLocalImageSizeThroughBitmapOptions(this, photo);

                if (BitmapUtils.needChangeWidthAndHeight(this, photo)) {
                    int h = photo.width;
                    photo.width = photo.height;
                    photo.height = h;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        if (Setting.isCrop()) {
            try {
                onMultipleCrop(MultipleCrop.of(this, Result.photos, ImageType.OTHER), Setting.cropOptions);
            } catch (BException e) {
                onActivityResultAlbum();
                e.printStackTrace();
            }
        } else {
            onActivityResultAlbum();
        }
    }

    private void processOriginalMenu() {
        if (!Setting.showOriginalMenu) return;
        if (Setting.selectedOriginal) {
            tvOriginal.setTextColor(ContextCompat.getColor(this, R.color.easy_photos_fg_accent));
        } else {
            if (Setting.originalMenuUsable)
                tvOriginal.setTextColor(ContextCompat.getColor(this, R.color.easy_photos_fg_primary));
            else
                tvOriginal.setTextColor(ContextCompat.getColor(this, R.color.easy_photos_fg_primary_dark));
        }
    }

    private void showAlbumItems(boolean isShow) {//相册文件列表
        if (null == setShow)
            newAnimators();

        if (isShow) {
            rootViewAlbumItems.setVisibility(View.VISIBLE);
            setShow.start();
        } else {
            setHide.start();
        }
    }

    private void newAnimators() {//创建动画
        newHideAnim();
        newShowAnim();
    }

    private void newShowAnim() {//创建显示动画
        ObjectAnimator translationShow = ObjectAnimator.ofFloat(rvAlbumItems, "translationY", mBottomBar.getTop(), 0);
        ObjectAnimator alphaShow = ObjectAnimator.ofFloat(rootViewAlbumItems, "alpha", 0.0f, 1.0f);
        translationShow.setDuration(300);
        setShow = new AnimatorSet();
        setShow.setInterpolator(new AccelerateDecelerateInterpolator());
        setShow.play(translationShow).with(alphaShow);
    }

    private void newHideAnim() {//创建隐藏动画
        ObjectAnimator translationHide = ObjectAnimator.ofFloat(rvAlbumItems, "translationY", 0, mBottomBar.getTop());
        ObjectAnimator alphaHide = ObjectAnimator.ofFloat(rootViewAlbumItems, "alpha", 1.0f, 0.0f);
        translationHide.setDuration(200);
        setHide = new AnimatorSet();
        setHide.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                rootViewAlbumItems.setVisibility(View.GONE);
            }
        });
        setHide.setInterpolator(new AccelerateInterpolator());
        setHide.play(translationHide).with(alphaHide);
    }

    @Override
    public void onAlbumItemClick(int position, int realPosition) {//相册选择
        updatePhotos(realPosition);
        showAlbumItems(false);
        tvAlbumItems.setText(albumModel.getAlbumItems().get(realPosition).name);
    }

    private void updatePhotos(int currAlbumItemIndex) {//更新相册文件夹-图片展示
        this.currAlbumItemIndex = currAlbumItemIndex;
        photoList.clear();
        photoList.addAll(albumModel.getCurrAlbumItemPhotos(currAlbumItemIndex));
        int index = 0;
        if (Setting.hasPhotosAd())
            photoList.add(index, Setting.photosAdView);
        if (Setting.isShowCamera && !Setting.isBottomRightCamera()) {
            if (Setting.hasPhotosAd()) index = 1;
            photoList.add(index, null);
        }
        photosAdapter.change();
        rvPhotos.scrollToPosition(0);
    }

    private void shouldShowMenuDone() {//完成按钮展示更新
        if (Result.isEmpty()) {
            if (View.VISIBLE == tvDone.getVisibility()) {
                ScaleAnimation scaleHide = new ScaleAnimation(1f, 0f, 1f, 0f);
                scaleHide.setDuration(200);
                tvDone.startAnimation(scaleHide);
            }
            tvDone.setVisibility(View.INVISIBLE);
            tvPreview.setVisibility(View.INVISIBLE);
        } else {
            if (View.INVISIBLE == tvDone.getVisibility()) {
                ScaleAnimation scaleShow = new ScaleAnimation(0f, 1f, 0f, 1f);
                scaleShow.setDuration(200);
                tvDone.startAnimation(scaleShow);
            }
            tvDone.setVisibility(View.VISIBLE);
            tvPreview.setVisibility(View.VISIBLE);
        }
        tvDone.setText(getString(R.string.selector_action_done_easy_photos, Result.count(),
                Setting.count));
    }

    @Override
    public void onCameraClick() {//点击拍照
        launchCamera(Constants.Code.REQUEST_CAMERA);
    }

    @Override
    public void onPhotoClick(int position, int realPosition) {//点击缩略图进预览
        PreviewActivity.start(EasyPhotosActivity.this, currAlbumItemIndex, realPosition);
    }

    @Override
    public void onSelectorOutOfMax(@Nullable Integer result) {//选择超出可选择个数
        if (result == null) {
            if (Setting.isOnlyVideo())
                Toast.makeText(this, getString(R.string.selector_reach_max_video_hint_easy_photos, Setting.count), Toast.LENGTH_SHORT).show();
            else if (Setting.showVideo)
                Toast.makeText(this, getString(R.string.selector_reach_max_hint_easy_photos, Setting.count), Toast.LENGTH_SHORT).show();
            else
                Toast.makeText(this, getString(R.string.selector_reach_max_image_hint_easy_photos, Setting.count), Toast.LENGTH_SHORT).show();
            return;
        }
        switch (result) {
            case -1:
                Toast.makeText(this, getString(R.string.selector_reach_max_image_hint_easy_photos, Setting.pictureCount), Toast.LENGTH_SHORT).show();
                break;
            case -2:
                Toast.makeText(this, getString(R.string.selector_reach_max_video_hint_easy_photos, Setting.videoCount), Toast.LENGTH_SHORT).show();
                break;
        }
    }

    @Override
    public void onSelectorChanged() {//选择图片
        shouldShowMenuDone();
    }

    @Override
    public void onBackPressed() {//返回处理
        if (null != rootViewAlbumItems && rootViewAlbumItems.getVisibility() == View.VISIBLE) {
            showAlbumItems(false);
            return;
        }

        if (null != mSecondMenus && View.VISIBLE == mSecondMenus.getVisibility()) {
            processSecondMenu();
            return;
        }
        if (albumModel != null) albumModel.stopQuery();
        if (Setting.hasPhotosAd()) {
            photosAdapter.clearAd();
        }
        if (Setting.hasAlbumItemsAd()) {
            albumItemsAdapter.clearAd();
        }
        setResult(RESULT_CANCELED);
        finish();
    }

    @Override
    protected void onDestroy() {//页面销毁处理
        if (albumModel != null) albumModel.stopQuery();
        super.onDestroy();
    }

    @Override
    public void onPhotosAdLoaded() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                photosAdapter.change();
            }
        });
    }

    @Override
    public void onAlbumItemsAdLoaded() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                albumItemsAdapter.notifyDataSetChanged();
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull final String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        PermissionUtil.onPermissionResult(this, permissions, grantResults,
                new PermissionUtil.PermissionCallBack() {
                    @Override
                    public void onSuccess() {
                        hasPermissions();
                    }

                    @Override
                    public void onShouldShow() {
                        tvPermission.setText(R.string.permissions_again_easy_photos);
                        permissionView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                if (PermissionUtil.checkAndRequestPermissionsInActivity(EasyPhotosActivity.this, getNeedPermissions())) {
                                    hasPermissions();
                                }
                            }
                        });
                    }

                    @Override
                    public void onFailed() {
                        tvPermission.setText(R.string.permissions_die_easy_photos);
                        permissionView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                SettingsUtils.startMyApplicationDetailsForResult(EasyPhotosActivity.this, getPackageName());
                            }
                        });
                    }
                });
    }

    private File tempImageFile = null;
    private Uri tempImageUri = null;
    private Uri tempImageQUri = null;//Android10

    /**
     * 创建图片地址uri,用于保存拍照后的照片 Android 10以后使用这种方法
     */
    private Uri createImageUri() {
        String status = Environment.getExternalStorageState();
        // 判断是否有SD卡,优先使用SD卡存储,当没有SD卡时使用手机存储
        if (status.equals(Environment.MEDIA_MOUNTED)) {
            return getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, new ContentValues());
        } else {
            return getContentResolver().insert(MediaStore.Images.Media.INTERNAL_CONTENT_URI, new ContentValues());
        }
    }

    /**
     * 创建相机拍照文件存储临时地址
     */
    private void createCameraTempImageFile() {
        File dir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        if (null == dir) {
            dir = new File(Environment.getExternalStorageDirectory(), File.separator + "DCIM" + File.separator + "Camera" + File.separator);
        }
        if (!dir.exists() || !dir.isDirectory()) {
            if (!dir.mkdirs()) {
                dir = getExternalFilesDir(null);
                if (null == dir || !dir.exists()) {
                    dir = getFilesDir();
                    if (null == dir || !dir.exists()) {
                        String cacheDirPath = File.separator + "data" + File.separator + "data" + File.separator + getPackageName() + File.separator + "cache" + File.separator;
                        dir = new File(cacheDirPath);
                        if (!dir.exists())
                            dir.mkdirs();
                    }
                }
            }
        }

        try {
            tempImageFile = File.createTempFile("IMG", ".jpg", dir);
        } catch (IOException e) {
            e.printStackTrace();
            tempImageFile = null;
        }
    }

    /**
     * 启动相机
     *
     * @param requestCode startActivityForResult的请求码
     */
    private void launchCamera(int requestCode) {
        if (!CameraUtils.cameraIsCanUse()) {
            permissionView.setVisibility(View.VISIBLE);
            tvPermission.setText(R.string.permissions_die_easy_photos);
            permissionView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    SettingsUtils.startMyApplicationDetailsForResult(EasyPhotosActivity.this, getPackageName());
                }
            });
            return;
        }
        toAndroidCamera(requestCode);
    }

    /**
     * 启动系统相机
     *
     * @param requestCode 请求相机的请求码
     */
    private void toAndroidCamera(int requestCode) {
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (cameraIntent.resolveActivity(getPackageManager()) != null) {
            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.P) {
                tempImageQUri = createImageUri();
                cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, tempImageQUri);
                cameraIntent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
                startActivityForResult(cameraIntent, requestCode);
                return;
            }

            createCameraTempImageFile();
            if (tempImageFile != null && tempImageFile.exists()) {
                tempImageUri = UriUtils.getUri(this, tempImageFile);
                cameraIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION); //对目标应用临时授权该Uri所代表的文件
                cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, tempImageUri);//将拍取的照片保存到指定URI
                startActivityForResult(cameraIntent, requestCode);
            } else {
                Toast.makeText(this, R.string.camera_temp_file_error_easy_photos, Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, R.string.msg_no_camera_easy_photos, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Constants.Code.REQUEST_SETTING_APP_DETAILS) {//应用详情-回调
            if (PermissionUtil.checkAndRequestPermissionsInActivity(this, getNeedPermissions())) {
                hasPermissions();
            } else {
                permissionView.setVisibility(View.VISIBLE);
            }
            return;
        } else if (requestCode == Constants.Code.REQUEST_CAMERA) {//拍照-回调
            if (resultCode == RESULT_OK) {//确定
                Uri imageUri = null;
                Photo photo = null;
                if (Build.VERSION.SDK_INT > Build.VERSION_CODES.P) {
                    imageUri = tempImageQUri;
                    photo = PhotoUtils.getPhoto(this, tempImageQUri);
                    if (photo == null) {
                        BubingLog.e(TAG, "onCameraResultForQ() -》photo = null");
                        return;
                    }
                } else {
                    if (tempImageFile == null || !tempImageFile.exists())
                        throw new RuntimeException("EasyPhotos拍照保存的图片不存在");
                    else {
                        imageUri = tempImageUri;
                        BitmapFactory.Options options = new BitmapFactory.Options();
                        options.inJustDecodeBounds = true;
                        BitmapFactory.decodeFile(tempImageFile.getAbsolutePath(), options);
                        photo = new Photo(tempImageFile.getName(), imageUri,
                                tempImageFile.getAbsolutePath(), tempImageFile.lastModified() / 1000,
                                options.outWidth, options.outHeight, tempImageFile.length(),
                                DurationUtils.getDuration(tempImageFile.getAbsolutePath()),
                                options.outMimeType);
                        photo.selectedOriginal = Setting.selectedOriginal;
                    }
                }
                if (imageUri != null) {
                    if (Setting.isCorrectImage())
                        ImageRotateUtil.of().correctImage(this, imageUri);
                    if (Setting.isCrop()) {
                        try {
//                            Photo photo = PhotoUtils.getPhoto(this, imageUri);
//                            Uri outPutUri = Uri.fromFile(ImageFiles.getCropTempFile(this, imageUri));//生成临时裁切输出路径
                            onSingleCrop(photo, PhotoUtils.getCropPhoto(this, photo, ImageType.CAMERA), Setting.cropOptions);
                        } catch (BException e) {
                            onActivityResultCamera();
                            e.printStackTrace();
                        }
                    } else {
                        onActivityResultCamera();
                    }
                } else
                    throw new RuntimeException("EasyPhotos拍照保存的图片不存在");
            } else if (resultCode == RESULT_CANCELED) {//取消
                // 删除临时文件
                if (tempImageFile != null && tempImageFile.exists()) {
                    tempImageFile.delete();
                    tempImageFile = null;
                }
                if (Setting.onlyStartCamera) {
                    finish();
                }
            }
        } else if (requestCode == Constants.Code.REQUEST_PREVIEW_ACTIVITY) {//预览-回调
            if (resultCode == RESULT_OK) {//确定
                if (data.getBooleanExtra(Constants.Key.PREVIEW_CLICK_DONE, false)) {
                    done();
                    return;
                }
                photosAdapter.change();
                processOriginalMenu();
                shouldShowMenuDone();
            } else if (resultCode == RESULT_CANCELED) {//取消
                processOriginalMenu();
            }
        } else if (requestCode == Constants.Code.REQUEST_PUZZLE_SELECTOR) {//拼图选择图片/拼图成图-回调
            if (resultCode == RESULT_OK) {//确定
                Photo puzzlePhoto = data.getParcelableExtra(Constants.Key.RESULT_PHOTOS);
                if (puzzlePhoto != null)
                    addNewPhoto(puzzlePhoto);
            } else if (resultCode == RESULT_CANCELED) {//取消
            }
        } else if (requestCode == Constants.Code.RC_CROP || requestCode == Crop.REQUEST_CROP) {//裁剪照片-回调
            if (resultCode == Activity.RESULT_OK) {//确定
                if (multipleCrop != null) {
                    cropContinue(true);
                } else {
                    ArrayList<ResultPhoto> resultPhotos = new ArrayList<>();
                    mResultPhoto.setCropped(true);
                    resultPhotos.add(mResultPhoto);
                    takeCompressResult(resultPhotos);
                }
            } else if (resultCode == Activity.RESULT_CANCELED) {//取消-裁剪的照片没有保存
                if (multipleCrop != null) {
                    if (data != null) {
                        Bitmap bitmap = data.getParcelableExtra("data");//获取裁剪的结果数据
                        ImageFiles.writeToFile(bitmap, mResultPhoto.getOriginalUri());//将裁剪的结果写入到文件
                        cropContinue(true);
                    } else {
                        cropContinue(false);
                    }
                } else {
                    if (data != null) {
                        Bitmap bitmap = data.getParcelableExtra("data");//获取裁剪的结果数据
                        ImageFiles.writeToFile(bitmap, mResultPhoto.getOriginalUri());//将裁剪的结果写入到文件

                        ArrayList<ResultPhoto> resultPhotos = new ArrayList<>();
                        mResultPhoto.setCropped(true);
                        resultPhotos.add(mResultPhoto);
                        takeCompressResult(resultPhotos);
                    } else {
//                            listener.takeCancel();
                    }
                }
            } else {
                if (multipleCrop != null) {
                    cropContinue(false);
                } else {
//                        listener.takeCancel();
                }
            }
        }
    }

    private void addNewPhoto(Photo photo) {
        MediaScannerConnectionUtils.refresh(this, photo.path);
        photo.selectedOriginal = Setting.selectedOriginal;

        String albumItem_all_name = albumModel.getAllAlbumName(this);
        albumModel.album.getAlbumItem(albumItem_all_name).addImageItem(0, photo);
        String folderPath = new File(photo.path).getParentFile().getAbsolutePath();
        String albumName = StringUtils.getLastPathSegment(folderPath);
        albumModel.album.addAlbumItem(albumName, folderPath, photo.path, photo.uri);
        albumModel.album.getAlbumItem(albumName).addImageItem(0, photo);

        albumItemList.clear();
        albumItemList.addAll(albumModel.getAlbumItems());
        if (Setting.hasAlbumItemsAd()) {
            int albumItemsAdIndex = 2;
            if (albumItemList.size() < albumItemsAdIndex + 1)
                albumItemsAdIndex = albumItemList.size() - 1;
            albumItemList.add(albumItemsAdIndex, Setting.albumItemsAdView);
        }
        albumItemsAdapter.notifyDataSetChanged();

        if (Setting.count == 1) {
            Result.clear();
            onSelectorOutOfMax(Result.addPhoto(photo));
        } else {
            if (Result.count() >= Setting.count) {
                onSelectorOutOfMax(null);
            } else {
                onSelectorOutOfMax(Result.addPhoto(photo));
            }
        }
        rvAlbumItems.scrollToPosition(0);
        albumItemsAdapter.setSelectedPosition(0);
        shouldShowMenuDone();
    }

    private void onActivityResultCamera() {//拍照回调
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.P) {
            onCameraResultForQ();
            return;
        }

        if (tempImageFile == null || !tempImageFile.exists()) {
            throw new RuntimeException("EasyPhotos拍照保存的图片不存在");
        }
        onCameraResult();
    }

    private void onCameraResultForQ() {
        Photo photo = PhotoUtils.getPhoto(this, tempImageQUri);
        if (photo == null) {
            Log.e("easyPhotos", "onCameraResultForQ() -》photo = null");
            return;
        }

        MediaScannerConnectionUtils.refresh(this, new File(photo.path));// 更新媒体库
        if (Setting.onlyStartCamera || albumModel.getAlbumItems().isEmpty()) {
            Intent data = new Intent();
            photo.selectedOriginal = Setting.selectedOriginal;
            ArrayList<ResultPhoto> resultPhotos = new ArrayList<>();
            resultPhotos.add(PhotoUtils.getResultPhoto(this, photo, ImageType.CAMERA));
            data.putParcelableArrayListExtra(Constants.Key.RESULT_PHOTOS, resultPhotos);
            data.putExtra(Constants.Key.RESULT_SELECTED_ORIGINAL, Setting.selectedOriginal);
            setResult(RESULT_OK, data);
            finish();
            return;
        }

        addNewPhoto(photo);
    }

    private void onCameraResult() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd_HH_mm_ss", Locale.getDefault());
        String imageName = "IMG_%s.jpg";
        String filename = String.format(imageName, dateFormat.format(new Date()));
        File reNameFile = new File(tempImageFile.getParentFile(), filename);
        if (!reNameFile.exists()) {
            if (tempImageFile.renameTo(reNameFile))
                tempImageFile = reNameFile;
        }
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(tempImageFile.getAbsolutePath(), options);
        MediaScannerConnectionUtils.refresh(this, tempImageFile);// 更新媒体库
        if (Setting.onlyStartCamera || albumModel.getAlbumItems().isEmpty()) {
            Intent data = new Intent();
            Uri uri = UriUtils.getUri(this, tempImageFile);
            Photo photo = new Photo(tempImageFile.getName(), uri, tempImageFile.getAbsolutePath(), tempImageFile.lastModified() / 1000, options.outWidth,
                    options.outHeight, tempImageFile.length(), DurationUtils.getDuration(tempImageFile.getAbsolutePath()), options.outMimeType);
            photo.selectedOriginal = Setting.selectedOriginal;
            ArrayList<ResultPhoto> resultPhotos = new ArrayList<>();
            resultPhotos.add(PhotoUtils.getResultPhoto(this, photo, ImageType.CAMERA));
            data.putParcelableArrayListExtra(Constants.Key.RESULT_PHOTOS, resultPhotos);
            data.putExtra(Constants.Key.RESULT_SELECTED_ORIGINAL, Setting.selectedOriginal);
            setResult(RESULT_OK, data);
            finish();
            return;
        }

        Uri uri = UriUtils.getUri(this, tempImageFile);
        Photo photo = new Photo(tempImageFile.getName(), uri, tempImageFile.getAbsolutePath(), tempImageFile.lastModified() / 1000, options.outWidth,
                options.outHeight, tempImageFile.length(), DurationUtils.getDuration(tempImageFile.getAbsolutePath()), options.outMimeType);
        addNewPhoto(photo);
    }

    private void onActivityResultAlbum() {//相册回调
        Intent intent = new Intent();
        Result.processOriginal();
        ArrayList<ResultPhoto> resultPhotos = new ArrayList<>();
        for (Photo photo : Result.photos) {
            resultPhotos.add(PhotoUtils.getResultPhoto(this, photo, ImageType.CAMERA));
        }
        intent.putParcelableArrayListExtra(Constants.Key.RESULT_PHOTOS, resultPhotos);

        intent.putExtra(Constants.Key.RESULT_SELECTED_ORIGINAL, Setting.selectedOriginal);
        setResult(RESULT_OK, intent);
        finish();
    }

    private ResultPhoto mResultPhoto;
    private MultipleCrop multipleCrop;

    //-----crop------
    public void onSingleCrop(Photo photo, ResultPhoto resultPhoto, CropOptions options) throws BException {
        this.mResultPhoto = resultPhoto;
        BubingLog.d(TAG, " onSingleCrop Photo：" + photo.toString() + " resultPhoto：" + resultPhoto.toString());
        if (!ImageFiles.checkMimeType(this, ImageFiles.getMimeType(this, photo.uri))) {
            Toast.makeText(this, getResources().getText(R.string.tip_type_not_image), Toast.LENGTH_SHORT).show();
            throw new BException(BExceptionType.TYPE_NOT_IMAGE);
        }
        cropWithNonException(photo, mResultPhoto, options);
    }

    public void onMultipleCrop(MultipleCrop multipleCrop, CropOptions options) throws BException {
        this.multipleCrop = multipleCrop;
        onSingleCrop(multipleCrop.getPhotos().get(0), multipleCrop.getResultPhotos().get(0), options);
    }

    private void cropWithNonException(Photo photo, ResultPhoto resultPhoto, CropOptions options) {
        this.mResultPhoto = resultPhoto;
        if (options.isWithOwnCrop()) {
            CropUtils.cropWithOwnApp(ContextWrap.of(this), photo.uri, resultPhoto.getOriginalUri(), options);
        } else {
            CropUtils.cropWithOtherAppBySafely(ContextWrap.of(this), photo.uri, resultPhoto.getOriginalUri(), options);
        }
    }

    private void cropContinue(boolean preSuccess) {
        Map result = multipleCrop.setCropWithUri(mResultPhoto, preSuccess);
        int index = (int) result.get("index");
        boolean isLast = (boolean) result.get("isLast");

        if (isLast) {
            if (preSuccess) {
                takeCompressResult(multipleCrop.getResultPhotos());
            } else {
                takeCompressResult(multipleCrop.getResultPhotos(), mResultPhoto.getOriginalUri().getPath() + getResources().getString(R.string.msg_crop_canceled));
            }
        } else {
            cropWithNonException(multipleCrop.getPhotos().get(index + 1), multipleCrop.getResultPhotos().get(index + 1), Setting.cropOptions);
        }
    }

    private void deleteRawFile(ArrayList<ResultPhoto> images) {
        for (ResultPhoto image : images) {
            if (ImageType.CAMERA == image.getImageType()) {
                FileUtils.delete(image.getOriginalPath());
                image.setOriginalPath("");
            }
        }
    }

    private ProgressDialog wailLoadDialog;//显示压缩对话框

    private void takeCompressResult(final ArrayList<ResultPhoto> resultPhotos, final String... message) {
        if (!Setting.isCompress()) {
            onResultCallBack(resultPhotos, message);
        } else {
            if (Setting.isCompressDialog())
                wailLoadDialog = CropUtils.showProgressDialog(this, getResources().getString(R.string.tip_compress));
            CompressImageImpl.of(this, Setting.compressConfig, resultPhotos, new CompressImage.CompressListener() {
                @Override
                public void onCompressSuccess(ArrayList<ResultPhoto> images) {
                    if (!Setting.compressConfig.isEnableReserveRaw()) {
                        deleteRawFile(images);
                    }
                    onResultCallBack(resultPhotos);
                    if (wailLoadDialog != null && !isFinishing())
                        wailLoadDialog.dismiss();
                }

                @Override
                public void onCompressFailed(ArrayList<ResultPhoto> images, String msg) {
                    if (!Setting.compressConfig.isEnableReserveRaw()) {
                        deleteRawFile(images);
                    }
                    onResultCallBack(images, String.format(getResources().getString(R.string.tip_compress_failed), message.length > 0 ? message[0] : "", msg, resultPhotos.get(0).getCompressPath()));
                    if (wailLoadDialog != null && !isFinishing())
                        wailLoadDialog.dismiss();
                }
            }).compress();
        }
    }

    private void onResultCallBack(ArrayList<ResultPhoto> resultPhotos, String... message) {
        if (message.length > 0) {
            Toast.makeText(this, "onResultCallBack resultPhotos：" + resultPhotos + " message：" + message[0], Toast.LENGTH_SHORT).show();
            BubingLog.i("onResultCallBack result：" + resultPhotos.toString() + " message：" + message[0]);
//            listener.takeFail(result, message[0]);
        } else if (multipleCrop != null && multipleCrop.isHasFailed()) {
            Toast.makeText(this, "onResultCallBack resultPhotos：" + resultPhotos + " message：" + getResources().getString(R.string.msg_crop_failed), Toast.LENGTH_SHORT).show();
            BubingLog.i("onResultCallBack resultPhotos：" + resultPhotos.toString() + " message：" + getResources().getString(R.string.msg_crop_failed));
//            listener.takeFail(result, getResources().getString(R.string.msg_crop_failed));
        } else if (Setting.compressConfig != null) {
            boolean hasFailed = false;
            for (ResultPhoto image : resultPhotos) {
                if (image == null || !image.isCompressed()) {
                    hasFailed = true;
                    break;
                }
            }
            if (hasFailed) {
                Toast.makeText(this, "onResultCallBack hasFailed：" + hasFailed + " message：" + getString(R.string.msg_compress_failed), Toast.LENGTH_SHORT).show();
                BubingLog.i("onResultCallBack hasFailed：" + hasFailed + " message：" + getString(R.string.msg_compress_failed));
//                listener.takeFail(result, getString(R.string.msg_compress_failed));
            } else {
                Toast.makeText(this, "onResultCallBack hasFailed：" + hasFailed + " resultPhotos：" + resultPhotos, Toast.LENGTH_SHORT).show();
                BubingLog.i("onResultCallBack hasFailed：" + hasFailed + " result：" + resultPhotos.toString());
//                listener.takeSuccess(result);
            }
        } else {
            Toast.makeText(this, "onResultCallBack result：" + resultPhotos, Toast.LENGTH_SHORT).show();
            BubingLog.i("onResultCallBack result：" + resultPhotos.toString());
//            listener.takeSuccess(result);
            onActivityResult(resultPhotos);
        }
        clearParams();
    }

    private void clearParams() {
//        compressConfig = null;
//        takePhotoOptions = null;
//        cropOptions = null;
        multipleCrop = null;
    }

    private void onActivityResult(ArrayList<ResultPhoto> resultPhotos) {//相册回调
        Intent intent = new Intent();
        intent.putParcelableArrayListExtra(Constants.Key.RESULT_PHOTOS, resultPhotos);
        intent.putExtra(Constants.Key.RESULT_SELECTED_ORIGINAL, Setting.selectedOriginal);
        setResult(RESULT_OK, intent);
        finish();
    }
}
