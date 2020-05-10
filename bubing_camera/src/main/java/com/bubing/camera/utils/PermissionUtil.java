package com.bubing.camera.utils;

import android.app.Activity;
import android.content.pm.PackageManager;

import com.bubing.camera.constant.Constants;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.PermissionChecker;

/**
 * @ClassName: PermissionUtil
 * @Description: 运行时权限工具类
 * @Author: bubing
 * @Date: 2020-05-09 17:24
 */
public class PermissionUtil {


    public interface PermissionCallBack {
        void onSuccess();

        void onShouldShow();

        void onFailed();
    }

    public static boolean checkAndRequestPermissionsInActivity(Activity cxt, String... checkPermissions) {
        boolean isHas = true;
        List<String> permissions = new ArrayList<>();
        for (String checkPermission : checkPermissions) {
            if (PermissionChecker.checkSelfPermission(cxt, checkPermission) != PackageManager.PERMISSION_GRANTED) {
                isHas = false;
                permissions.add(checkPermission);
            }
        }
        if (!isHas) {
            String[] p = permissions.toArray(new String[permissions.size()]);
            requestPermissionsInActivity(cxt, Constants.Code.REQUEST_PERMISSION, p);
        }
        return isHas;
    }

    private static void requestPermissionsInActivity(Activity cxt, int requestCode, String... permissions) {
        ActivityCompat.requestPermissions(cxt, permissions, requestCode);
    }

    public static void onPermissionResult(Activity cxt, @NonNull String[] permissions, @NonNull int[] grantResults, PermissionCallBack listener) {
        int length = grantResults.length;
        List<Integer> positions = new ArrayList<>();
        if (length > 0) {
            for (int i = 0; i < length; i++) {
                if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                    positions.add(i);
                }
            }
        }
        if (positions.size() == 0) {
            listener.onSuccess();
            return;
        }
        progressNoPermission(cxt, listener, permissions, positions, 0);

    }

    private static void progressNoPermission(Activity cxt, PermissionCallBack listener, String[] permissions, List<Integer> positions, int i) {
        int index = positions.get(i);
        if (ActivityCompat.shouldShowRequestPermissionRationale(cxt, permissions[index])) {
            listener.onShouldShow();
            return;
        }
        if (i < positions.size() - 1) {
            i++;
            progressNoPermission(cxt, listener, permissions, positions, i);
            return;
        }
        listener.onFailed();
    }

}
