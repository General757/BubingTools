package com.bubing.camera;

import android.app.Activity;
import android.content.Intent;

import com.bubing.camera.constant.Constants;
import com.bubing.camera.constant.DirectionMode;
import com.bubing.camera.ui.CameraLandscapeActivity;
import com.bubing.camera.ui.CameraPortraitActivity;

import java.lang.ref.WeakReference;

import androidx.fragment.app.Fragment;

/**
 * @ClassName: CameraTools
 * @Description: 相机工具
 * @Author: bubing
 * @Date: 2020-04-29 17:28
 */
public class CameraTools {
    private final WeakReference<Activity> mActivity;
    private final WeakReference<Fragment> mFragment;

    public static CameraTools create(Activity activity) {
        return new CameraTools(activity);
    }

    public static CameraTools create(Fragment fragment) {
        return new CameraTools(fragment);
    }

    private CameraTools(Activity activity) {
        this(activity, (Fragment) null);
    }

    private CameraTools(Fragment fragment) {
        this(fragment.getActivity(), fragment);
    }

    private CameraTools(Activity activity, Fragment fragment) {
        this.mActivity = new WeakReference(activity);
        this.mFragment = new WeakReference(fragment);
    }

    /**
     * 打开证件相机
     *
     * @param directionMode 拍摄证件方向
     */
    public void openCertificateCamera(DirectionMode directionMode) {
        Activity activity = this.mActivity.get();
        Fragment fragment = this.mFragment.get();
        Intent intent = null;
        if (directionMode == DirectionMode.MODE_COMPANY_PORTRAIT)
            intent = new Intent(activity, CameraPortraitActivity.class);
        else
            intent = new Intent(activity, CameraLandscapeActivity.class);
        if (intent != null) {
            intent.putExtra(Constants.Key.RESULT_CERTIFICATE_TYPE, directionMode.getValue());
            if (fragment != null)
                fragment.startActivityForResult(intent, directionMode.getValue());
            else
                activity.startActivityForResult(intent, directionMode.getValue());
        }
    }

    /**
     * 获取图片路径
     *
     * @param data Intent
     * @return 图片路径
     */
    public static String getImagePath(Intent data) {
        if (data != null)
            return data.getStringExtra(Constants.Key.RESULT_CERTIFICATE_PATH);
        return "";
    }
}
