package com.bubing.camera.models.crop;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.text.TextUtils;
import android.widget.Toast;

import com.bubing.camera.constant.Constants;
import com.bubing.camera.exception.BException;
import com.bubing.camera.exception.BExceptionType;
import com.bubing.camera.models.ContextWrap;
import com.bubing.camera.models.IntentWap;
import com.bubing.camera.utils.BubingLog;
import com.bubing.camera.utils.IntentUtils;
import com.soundcloud.android.crop.Crop;

import java.util.List;

/**
 * @ClassName: CropUtils
 * @Description: 裁剪工具类
 * @Author: bubing
 * @Date: 2020-05-09 20:03
 */
public class CropUtils {
    private static final String TAG = CropUtils.class.getName();

    /**
     * @param contextWrap
     * @param intentWap
     */
    public static void startActivityForResult(ContextWrap contextWrap, IntentWap intentWap) {
        if (contextWrap.getFragment() != null) {
            contextWrap.getFragment().startActivityForResult(intentWap.getIntent(), intentWap.getRequestCode());
        } else {
            contextWrap.getActivity().startActivityForResult(intentWap.getIntent(), intentWap.getRequestCode());
        }
    }

    /**
     * 安全地发送Intent
     *
     * @param contextWrap
     * @param intentWapList 要发送的Intent以及候选Intent
     * @param defaultIndex  默认发送的Intent
     * @param isCrop        是否为裁切照片的Intent
     * @throws BException
     */
    public static void sendIntentBySafely(ContextWrap contextWrap, List<IntentWap> intentWapList, int defaultIndex, boolean isCrop) throws BException {
        if (defaultIndex + 1 > intentWapList.size()) {
            throw new BException(isCrop ? BExceptionType.TYPE_NO_MATCH_PICK_INTENT : BExceptionType.TYPE_NO_MATCH_CROP_INTENT);
        }
        IntentWap intentWap = intentWapList.get(defaultIndex);
        List result = contextWrap.getActivity().getPackageManager().queryIntentActivities(intentWap.getIntent(), PackageManager.MATCH_ALL);
        if (result.isEmpty()) {
            sendIntentBySafely(contextWrap, intentWapList, ++defaultIndex, isCrop);
        } else {
            startActivityForResult(contextWrap, intentWap);
        }
    }

    /**
     * 拍照前检查是否有相机
     **/
    public static void captureBySafely(ContextWrap contextWrap, IntentWap intentWap) throws BException {
        List result = contextWrap.getActivity().getPackageManager().queryIntentActivities(intentWap.getIntent(), PackageManager.MATCH_ALL);
        if (result.isEmpty()) {
            Toast.makeText(contextWrap.getActivity(), "没有相机", Toast.LENGTH_SHORT).show();
            throw new BException(BExceptionType.TYPE_NO_CAMERA);
        } else {
            startActivityForResult(contextWrap, intentWap);
        }
    }

    /**
     * 通过第三方工具裁切照片，当没有第三方裁切工具时，会自动使用自带裁切工具进行裁切
     *
     * @param contextWrap
     * @param imageUri
     * @param outPutUri
     * @param options
     */
    public static void cropWithOtherAppBySafely(ContextWrap contextWrap, Uri imageUri, Uri outPutUri, CropOptions options) {
        Intent nativeCropIntent = IntentUtils.getCropIntentWithOtherApp(imageUri, outPutUri, options);
        List result = contextWrap.getActivity().getPackageManager().queryIntentActivities(nativeCropIntent, PackageManager.MATCH_ALL);
        if (result.isEmpty()) {
            cropWithOwnApp(contextWrap, imageUri, outPutUri, options);
        } else {
            startActivityForResult(contextWrap, new IntentWap(IntentUtils.getCropIntentWithOtherApp(imageUri, outPutUri, options), Constants.Code.RC_CROP));
        }
    }

    /**
     * 通过TakePhoto自带的裁切工具裁切图片
     *
     * @param contextWrap
     * @param imageUri
     * @param outPutUri
     * @param options
     */
    public static void cropWithOwnApp(ContextWrap contextWrap, Uri imageUri, Uri outPutUri, CropOptions options) {
        if (options.getAspectX() * options.getAspectY() > 0) {
            if (contextWrap.getFragment() != null) {
                Crop.of(imageUri, outPutUri)
                        .withAspect(options.getAspectX(), options.getAspectY())
                        .start(contextWrap.getActivity(), contextWrap.getFragment());
            } else {
                Crop.of(imageUri, outPutUri).withAspect(options.getAspectX(), options.getAspectY()).start(contextWrap.getActivity());
            }
        } else if (options.getOutputX() * options.getOutputY() > 0) {
            if (contextWrap.getFragment() != null) {
                Crop.of(imageUri, outPutUri)
                        .withMaxSize(options.getOutputX(), options.getOutputY())
                        .start(contextWrap.getActivity(), contextWrap.getFragment());
            } else {
                Crop.of(imageUri, outPutUri).withMaxSize(options.getOutputX(), options.getOutputY()).start(contextWrap.getActivity());
            }
        } else {
            if (contextWrap.getFragment() != null) {
                Crop.of(imageUri, outPutUri).asSquare().start(contextWrap.getActivity(), contextWrap.getFragment());
            } else {
                Crop.of(imageUri, outPutUri).asSquare().start(contextWrap.getActivity());
            }
        }
    }

    /**
     * 是否裁剪之后返回数据
     **/
    public static boolean isReturnData() {
        String release = Build.VERSION.RELEASE;
        int sdk = Build.VERSION.SDK_INT;
        BubingLog.i(TAG, "release:" + release + "sdk:" + sdk);
        String manufacturer = android.os.Build.MANUFACTURER;
        if (!TextUtils.isEmpty(manufacturer)) {
            if (manufacturer.toLowerCase().contains("lenovo")) {//对于联想的手机返回数据
                return true;
            }
        }
        //        if (sdk>=21){//5.0或以上版本要求返回数据
        //            return  true;
        //        }
        return false;
    }

    /**
     * 显示圆形进度对话框
     *
     * @param activity
     * @param progressTitle 显示的标题
     * @return
     * @author JPH
     * Date 2014-12-12 下午7:04:09
     */
    public static ProgressDialog showProgressDialog(final Activity activity, String... progressTitle) {
        if (activity == null || activity.isFinishing()) {
            return null;
        }
        String title = "提示";
        if (progressTitle != null && progressTitle.length > 0) {
            title = progressTitle[0];
        }
        ProgressDialog progressDialog = new ProgressDialog(activity);
        progressDialog.setTitle(title);
        progressDialog.setCancelable(false);
        progressDialog.show();
        return progressDialog;
    }
}

