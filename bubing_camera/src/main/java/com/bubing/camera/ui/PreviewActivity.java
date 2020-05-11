package com.bubing.camera.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bubing.camera.R;
import com.bubing.camera.constant.Constants;
import com.bubing.camera.models.album.AlbumModel;
import com.bubing.camera.models.album.entity.Photo;
import com.bubing.camera.result.ResultStorage;
import com.bubing.camera.setting.Setting;
import com.bubing.camera.ui.adapter.PreviewPhotosAdapter;
import com.bubing.camera.utils.ColorUtils;
import com.bubing.camera.utils.system.SystemUtils;
import com.bubing.camera.widget.PressedTextView;

import java.util.ArrayList;

import androidx.annotation.IdRes;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.PagerSnapHelper;
import androidx.recyclerview.widget.RecyclerView;

/**
 * @ClassName: PreviewActivity
 * @Description: 预览页
 * @Author: bubing
 * @Date: 2020-05-09 18:15
 */
public class PreviewActivity extends AppCompatActivity implements PreviewPhotosAdapter.OnClickListener, View.OnClickListener, PreviewFragment.OnPreviewFragmentClickListener {

    public static void start(Activity act, int albumItemIndex, int currIndex) {
        Intent intent = new Intent(act, PreviewActivity.class);
        intent.putExtra(Constants.Key.PREVIEW_ALBUM_ITEM_INDEX, albumItemIndex);
        intent.putExtra(Constants.Key.PREVIEW_PHOTO_INDEX, currIndex);
        act.startActivityForResult(intent, Constants.Code.REQUEST_PREVIEW_ACTIVITY);
    }

    /**
     * 一些旧设备在UI小部件更新之间需要一个小延迟
     * and a change of the status and navigation bar.
     */
    private static final int UI_ANIMATION_DELAY = 300;
    private final Handler mHideHandler = new Handler();
    private final Runnable mHidePart2Runnable = new Runnable() {
        @Override
        public void run() {
            SystemUtils.getInstance().systemUiHide(PreviewActivity.this, decorView);
        }
    };
    private RelativeLayout mBottomBar;
    private FrameLayout mToolBar;
    private final Runnable mShowPart2Runnable = new Runnable() {
        @Override
        public void run() {
            // 延迟显示UI元素
            mBottomBar.setVisibility(View.VISIBLE);
            mToolBar.setVisibility(View.VISIBLE);
        }
    };
    private boolean mVisible;
    View decorView;
    private TextView tvOriginal, tvNumber;
    private PressedTextView tvDone;
    private ImageView ivSelector;
    private RecyclerView rvPhotos;
    private PreviewPhotosAdapter adapter;
    private PagerSnapHelper snapHelper;
    private LinearLayoutManager lm;
    private int index;
    private ArrayList<Photo> photos = new ArrayList<>();
    private int resultCode = RESULT_CANCELED;
    private int lastPosition = 0;//记录recyclerView最后一次角标位置，用于判断是否转换了item
    private boolean isSingle = Setting.count == 1;
    private boolean unable = ResultStorage.count() == Setting.count;

    private FrameLayout flFragment;
    private PreviewFragment previewFragment;
    private int statusColor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        decorView = getWindow().getDecorView();
        SystemUtils.getInstance().systemUiInit(this, decorView);

        setContentView(R.layout.activity_preview_easy_photos);

        hideActionBar();
        adaptationStatusBar();
        if (null == AlbumModel.instance) {
            finish();
            return;
        }
        initData();
        initView();
    }

    private void adaptationStatusBar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            statusColor = ContextCompat.getColor(this, R.color.easy_photos_status_bar);
            if (ColorUtils.isWhiteColor(statusColor)) {
                getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            }
        }
    }

    private void hideActionBar() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }
    }

    private void initData() {
        Intent intent = getIntent();
        int albumItemIndex = intent.getIntExtra(Constants.Key.PREVIEW_ALBUM_ITEM_INDEX, 0);
        photos.clear();

        if (albumItemIndex == -1) {
            photos.addAll(ResultStorage.photos);
        } else {
            photos.addAll(AlbumModel.instance.getCurrAlbumItemPhotos(albumItemIndex));
        }
        index = intent.getIntExtra(Constants.Key.PREVIEW_PHOTO_INDEX, 0);
        lastPosition = index;
        mVisible = true;
    }

    private void toggle() {
        if (mVisible) {
            hide();
        } else {
            show();
        }
    }

    private void hide() {
        // Hide UI first
        AlphaAnimation hideAnimation = new AlphaAnimation(1.0f, 0.0f);
        hideAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                mBottomBar.setVisibility(View.GONE);
                mToolBar.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        hideAnimation.setDuration(UI_ANIMATION_DELAY);
        mBottomBar.startAnimation(hideAnimation);
        mToolBar.startAnimation(hideAnimation);
        mVisible = false;

        // Schedule a runnable to remove the status and navigation bar after a delay
        mHideHandler.removeCallbacks(mShowPart2Runnable);

        mHideHandler.postDelayed(mHidePart2Runnable, UI_ANIMATION_DELAY);

    }


    private void show() {
        // Show the system bar
        if (Build.VERSION.SDK_INT >= 16) {
            SystemUtils.getInstance().systemUiShow(this, decorView);
        }

        mVisible = true;

        // Schedule a runnable to display UI elements after a delay
        mHideHandler.removeCallbacks(mHidePart2Runnable);
        mHideHandler.post(mShowPart2Runnable);
    }

    @Override
    public void onPhotoClick() {
        toggle();
    }

    @Override
    public void onPhotoScaleChanged() {
        if (mVisible) hide();
    }

    @Override
    public void onBackPressed() {
        doBack();
    }

    private void doBack() {
        Intent intent = new Intent();
        intent.putExtra(Constants.Key.PREVIEW_CLICK_DONE, false);
        setResult(resultCode, intent);
        finish();
    }

    private void initView() {
        setClick(R.id.iv_back, R.id.tv_edit, R.id.tv_selector);

        mToolBar = (FrameLayout) findViewById(R.id.m_top_bar_layout);
        if (!SystemUtils.getInstance().hasNavigationBar(this)) {
            FrameLayout mRootView = (FrameLayout) findViewById(R.id.m_root_view);
            mRootView.setFitsSystemWindows(true);
            mToolBar.setPadding(0, SystemUtils.getInstance().getStatusBarHeight(this), 0, 0);
            if (ColorUtils.isWhiteColor(statusColor)) {
                SystemUtils.getInstance().setStatusDark(this, true);
            }
        }
        mBottomBar = (RelativeLayout) findViewById(R.id.m_bottom_bar);
        ivSelector = (ImageView) findViewById(R.id.iv_selector);
        tvNumber = (TextView) findViewById(R.id.tv_number);
        tvDone = (PressedTextView) findViewById(R.id.tv_done);
        tvOriginal = (TextView) findViewById(R.id.tv_original);
        flFragment = (FrameLayout) findViewById(R.id.fl_fragment);
        previewFragment = (PreviewFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_preview);
        if (Setting.showOriginalMenu) {
            processOriginalMenu();
        } else {
            tvOriginal.setVisibility(View.GONE);
        }

        setClick(tvOriginal, tvDone, ivSelector);

        initRecyclerView();
        shouldShowMenuDone();
    }

    private void initRecyclerView() {
        rvPhotos = (RecyclerView) findViewById(R.id.rv_photos);
        adapter = new PreviewPhotosAdapter(this, photos, this);
        lm = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        rvPhotos.setLayoutManager(lm);
        rvPhotos.setAdapter(adapter);
        rvPhotos.scrollToPosition(index);
        toggleSelector();
        snapHelper = new PagerSnapHelper();
        snapHelper.attachToRecyclerView(rvPhotos);
        rvPhotos.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                View view = snapHelper.findSnapView(lm);
                if (view == null) {
                    return;
                }
                int position = lm.getPosition(view);
                if (lastPosition == position) {
                    return;
                }
                lastPosition = position;
                previewFragment.setSelectedPosition(-1);
                tvNumber.setText(getString(R.string.preview_current_number_easy_photos,
                        lastPosition + 1, photos.size()));
                toggleSelector();
            }
        });
        tvNumber.setText(getString(R.string.preview_current_number_easy_photos, index + 1,
                photos.size()));
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (R.id.iv_back == id) {
            doBack();
        } else if (R.id.tv_selector == id) {
            updateSelector();
        } else if (R.id.iv_selector == id) {
            updateSelector();
        } else if (R.id.tv_original == id) {
            if (!Setting.originalMenuUsable) {
                Toast.makeText(this, Setting.originalMenuUnusableHint, Toast.LENGTH_SHORT).show();
                return;
            }
            Setting.selectedOriginal = !Setting.selectedOriginal;
            processOriginalMenu();
        } else if (R.id.tv_done == id) {
            Intent intent = new Intent();
            intent.putExtra(Constants.Key.PREVIEW_CLICK_DONE, true);
            setResult(RESULT_OK, intent);
            finish();
        }
//        else if (R.id.m_bottom_bar == id) {
//
//        } else if (R.id.tv_edit == id) {
//
//        }
    }

    private void processOriginalMenu() {
        if (Setting.selectedOriginal) {
            tvOriginal.setTextColor(ContextCompat.getColor(this, R.color.easy_photos_fg_accent));
        } else {
            if (Setting.originalMenuUsable) {
                tvOriginal.setTextColor(ContextCompat.getColor(this,
                        R.color.easy_photos_fg_primary));
            } else {
                tvOriginal.setTextColor(ContextCompat.getColor(this,
                        R.color.easy_photos_fg_primary_dark));
            }
        }
    }

    private void toggleSelector() {
        if (photos.get(lastPosition).selected) {
            ivSelector.setImageResource(R.drawable.ic_selector_true_easy_photos);
            if (!ResultStorage.isEmpty()) {
                for (int i = 0; i < ResultStorage.count(); i++) {
                    if (photos.get(lastPosition).path.equals(ResultStorage.getPhotoPath(i))) {
                        previewFragment.setSelectedPosition(i);
                        break;
                    }
                }
            }
        } else {
            ivSelector.setImageResource(R.drawable.ic_selector_easy_photos);
        }
        previewFragment.notifyDataSetChanged();
        shouldShowMenuDone();
    }

    private void updateSelector() {
        resultCode = RESULT_OK;
        Photo item = photos.get(lastPosition);
        if (isSingle) {
            singleSelector(item);
            return;
        }
        if (unable) {
            if (item.selected) {
                ResultStorage.removePhoto(item);
                if (unable) {
                    unable = false;
                }
                toggleSelector();
                return;
            }
            if (Setting.isOnlyVideo()) {
                Toast.makeText(this, getString(R.string.selector_reach_max_video_hint_easy_photos
                        , Setting.count), Toast.LENGTH_SHORT).show();

            } else if (Setting.showVideo) {
                Toast.makeText(this, getString(R.string.selector_reach_max_hint_easy_photos,
                        Setting.count), Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, getString(R.string.selector_reach_max_image_hint_easy_photos,
                        Setting.count), Toast.LENGTH_SHORT).show();
            }
            return;
        }
        item.selected = !item.selected;
        if (item.selected) {
            int res = ResultStorage.addPhoto(item);
            if (res != 0) {
                item.selected = false;
                switch (res) {
                    case -1:
                        Toast.makeText(this,
                                getString(R.string.selector_reach_max_image_hint_easy_photos,
                                        Setting.pictureCount), Toast.LENGTH_SHORT).show();
                        break;
                    case -2:
                        Toast.makeText(this,
                                getString(R.string.selector_reach_max_video_hint_easy_photos,
                                        Setting.videoCount), Toast.LENGTH_SHORT).show();
                        break;
                }
                return;
            }
            if (ResultStorage.count() == Setting.count) {
                unable = true;
            }
        } else {
            ResultStorage.removePhoto(item);
            previewFragment.setSelectedPosition(-1);
            if (unable) {
                unable = false;
            }
        }
        toggleSelector();
    }

    private void singleSelector(Photo photo) {
        if (!ResultStorage.isEmpty()) {
            if (ResultStorage.getPhotoPath(0).equals(photo.path)) {
                ResultStorage.removePhoto(photo);
                toggleSelector();
            } else {
                ResultStorage.removePhoto(0);
                ResultStorage.addPhoto(photo);
                toggleSelector();
            }
        } else {
            ResultStorage.addPhoto(photo);
            toggleSelector();
        }
    }

    private void shouldShowMenuDone() {
        if (ResultStorage.isEmpty()) {
            if (View.VISIBLE == tvDone.getVisibility()) {
                ScaleAnimation scaleHide = new ScaleAnimation(1f, 0f, 1f, 0f);
                scaleHide.setDuration(200);
                tvDone.startAnimation(scaleHide);
            }
            tvDone.setVisibility(View.GONE);
            flFragment.setVisibility(View.GONE);
        } else {
            if (View.GONE == tvDone.getVisibility()) {
                ScaleAnimation scaleShow = new ScaleAnimation(0f, 1f, 0f, 1f);
                scaleShow.setDuration(200);
                tvDone.startAnimation(scaleShow);
            }
            flFragment.setVisibility(View.VISIBLE);
            tvDone.setVisibility(View.VISIBLE);
            tvDone.setText(getString(R.string.selector_action_done_easy_photos, ResultStorage.count(),
                    Setting.count));
        }
    }

    @Override
    public void onPreviewPhotoClick(int position) {
        String path = ResultStorage.getPhotoPath(position);
        for (int i = 0; i < photos.size(); i++) {
            if (TextUtils.equals(path, photos.get(i).path)) {
                rvPhotos.scrollToPosition(i);
                lastPosition = i;
                tvNumber.setText(getString(R.string.preview_current_number_easy_photos, lastPosition + 1, photos.size()));
                previewFragment.setSelectedPosition(position);
                toggleSelector();
                return;
            }
        }
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
}

