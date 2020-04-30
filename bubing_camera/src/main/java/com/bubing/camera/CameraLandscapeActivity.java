package com.bubing.camera;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.hardware.Camera;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.bubing.camera.utils.CameraUtils;
import com.bubing.camera.utils.ImageUtils;
import com.bubing.camera.utils.PermissionUtils;
import com.bubing.camera.utils.ScreenUtils;
import com.bubing.camera.widget.CameraPreview;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import androidx.core.app.ActivityCompat;

/**
 * @ClassName: CameraLandscapeActivity
 * @Description: 拍照界面-横屏
 * @Author: bubing
 * @Date: 2020-04-29 17:25
 */
public class CameraLandscapeActivity extends Activity implements View.OnClickListener {
    private static final String TAG = CameraLandscapeActivity.class.getSimpleName();

    private DirectionMode mDirectionMode;//拍摄类型
    private boolean isToast = true;//是否弹吐司，为了保证for循环只弹一次

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        int takeType = getIntent().getIntExtra(ConstantCamera.TAKE_TYPE, 0);
        mDirectionMode = DirectionMode.valueOf(takeType);
        Log.d(TAG, "initPermissions takeType: " + takeType + " mDirectionMode：" + mDirectionMode);
        if (mDirectionMode != null) {
            setContentView(R.layout.activity_camera_landscape);

            /*动态请求需要的权限*/
            boolean checkPermissionFirst = PermissionUtils.checkPermissionFirst(this, ConstantCamera.PERMISSION_CODE_FIRST,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.CAMERA});
            if (checkPermissionFirst)
                initView();
        } else {
            Log.e(TAG, "initPermissions : " + "拍照类型获取失败");
            finish();
        }
    }

    /**
     * 处理请求权限的响应
     *
     * @param requestCode  请求码
     * @param permissions  权限数组
     * @param grantResults 请求权限结果数组
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        boolean isPermissions = true;
        for (int i = 0; i < permissions.length; i++) {
            if (grantResults[i] == PackageManager.PERMISSION_DENIED) {
                isPermissions = false;
                if (!ActivityCompat.shouldShowRequestPermissionRationale(this, permissions[i])) { //用户选择了"不再询问"
                    if (isToast) {
                        Toast.makeText(this, "请手动打开该应用需要的权限", Toast.LENGTH_SHORT).show();
                        isToast = false;
                    }
                }
            }
        }
        isToast = true;
        if (isPermissions) {
            Log.d("onRequestPermission", "onRequestPermissionsResult: " + "允许所有权限");
            initView();
        } else {
            Log.d("onRequestPermission", "onRequestPermissionsResult: " + "有权限不允许");
            finish();
        }
    }

    private CameraPreview mCameraPreview;
    private LinearLayout mCameraCropContainerLayout;
    private ImageView mCameraCropImage;
    private ImageView mCameraFlashImage;
    private LinearLayout mCameraOptionLayout;
    private LinearLayout mCameraResultLayout;
    private View mCameraCropLeftView;

    private void initView() {
        mCameraPreview = (CameraPreview) findViewById(R.id.camera_preview);
        //获取屏幕最小边，设置为cameraPreview较窄的一边
        float screenMinSize = ScreenUtils.getScreenMin(this);
        //根据screenMinSize，计算出cameraPreview的较宽的一边，长宽比为标准的16:9
        float maxSize = screenMinSize / 9.0f * 16.0f;
        RelativeLayout.LayoutParams layoutParams;
        if (DirectionMode.MODE_COMPANY_PORTRAIT == mDirectionMode)
            layoutParams = new RelativeLayout.LayoutParams((int) screenMinSize, (int) maxSize);
        else
            layoutParams = new RelativeLayout.LayoutParams((int) maxSize, (int) screenMinSize);
        layoutParams.addRule(RelativeLayout.CENTER_IN_PARENT);
        mCameraPreview.setLayoutParams(layoutParams);

        mCameraCropLeftView = findViewById(R.id.camera_crop_left_view);
        mCameraCropContainerLayout = findViewById(R.id.camera_crop_container_layout);
        mCameraCropImage = (ImageView) findViewById(R.id.camera_crop_image);
        float width = 0;
        float height = 0;
        LinearLayout.LayoutParams params;
        if (DirectionMode.MODE_COMPANY_PORTRAIT == mDirectionMode) {
            width = (int) (screenMinSize * 0.8);
            height = (int) (width * 43.0f / 30.0f);
            params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, (int) height);
            mCameraCropContainerLayout.setLayoutParams(params);
            params = new LinearLayout.LayoutParams((int) width, (int) height);
            mCameraCropImage.setLayoutParams(params);
        } else if (DirectionMode.MODE_COMPANY_LANDSCAPE == mDirectionMode) {
            height = (int) (screenMinSize * 0.8);
            width = (int) (height * 43.0f / 30.0f);
            params = new LinearLayout.LayoutParams((int) width, ViewGroup.LayoutParams.MATCH_PARENT);
            mCameraCropContainerLayout.setLayoutParams(params);
            params = new LinearLayout.LayoutParams((int) width, (int) height);
            mCameraCropImage.setLayoutParams(params);
        } else {
            height = (int) (screenMinSize * 0.75);
            width = (int) (height * 75.0f / 47.0f);
            params = new LinearLayout.LayoutParams((int) width, ViewGroup.LayoutParams.MATCH_PARENT);
            mCameraCropContainerLayout.setLayoutParams(params);
            params = new LinearLayout.LayoutParams((int) width, (int) height);
            mCameraCropImage.setLayoutParams(params);
        }
        if (DirectionMode.MODE_IDCARD_FRONT == mDirectionMode) {
            mCameraCropImage.setImageResource(R.drawable.camera_idcard_front);
        } else if (DirectionMode.MODE_IDCARD_BACK == mDirectionMode) {
            mCameraCropImage.setImageResource(R.drawable.camera_idcard_back);
        } else if (DirectionMode.MODE_COMPANY_PORTRAIT == mDirectionMode) {
            mCameraCropImage.setImageResource(R.drawable.camera_company_portrait);
        } else if (DirectionMode.MODE_COMPANY_LANDSCAPE == mDirectionMode) {
            mCameraCropImage.setImageResource(R.drawable.camera_company_landscape);
        }

        mCameraOptionLayout = findViewById(R.id.camera_option_layout);
        mCameraFlashImage = (ImageView) findViewById(R.id.camera_flash_image);
        mCameraResultLayout = findViewById(R.id.camera_result_layout);

        mCameraPreview.setOnClickListener(this);
        findViewById(R.id.camera_close_image).setOnClickListener(this);
        findViewById(R.id.camera_take_image).setOnClickListener(this);
        mCameraFlashImage.setOnClickListener(this);
        findViewById(R.id.camera_confirm_image).setOnClickListener(this);
        findViewById(R.id.camera_cancel_image).setOnClickListener(this);

        /*增加0.5秒过渡界面，解决个别手机首次申请权限导致预览界面启动慢的问题*/
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mCameraPreview.setVisibility(View.VISIBLE);
                    }
                });
            }
        }, 500);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.camera_preview) {
            mCameraPreview.focus();
        } else if (id == R.id.camera_close_image) {
            finish();
        } else if (id == R.id.camera_take_image) {
            takePhoto();
        } else if (id == R.id.camera_flash_image) {
            boolean isFlashOn = mCameraPreview.switchFlashLight();
            mCameraFlashImage.setImageResource(isFlashOn ? R.drawable.camera_flash_on : R.drawable.camera_flash_off);
        } else if (id == R.id.camera_confirm_image) {
            confirm();
        } else if (id == R.id.camera_cancel_image) {
            mCameraOptionLayout.setVisibility(View.VISIBLE);
            mCameraResultLayout.setVisibility(View.GONE);
            mCameraPreview.setEnabled(true);
            mCameraPreview.startPreview();
        }
    }

    /**
     * 拍照
     */
    private void takePhoto() {
        mCameraOptionLayout.setVisibility(View.GONE);
        mCameraPreview.setEnabled(false);
        CameraUtils.getCamera().setOneShotPreviewCallback(new Camera.PreviewCallback() {
            @Override
            public void onPreviewFrame(final byte[] bytes, Camera camera) {
                final Camera.Size size = camera.getParameters().getPreviewSize(); //获取预览大小
                camera.stopPreview();
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Bitmap bitmap = ImageUtils.getBitmapFromByte(bytes, size.width, size.height);

                            //计算裁剪位置
                            float left, top, right, bottom;
                            if (DirectionMode.MODE_COMPANY_PORTRAIT == mDirectionMode) {
                                left = (float) mCameraCropImage.getLeft() / (float) mCameraPreview.getWidth();
                                top = ((float) mCameraCropContainerLayout.getTop() - (float) mCameraPreview.getTop()) / (float) mCameraPreview.getHeight();
                                right = (float) mCameraCropImage.getRight() / (float) mCameraPreview.getWidth();
                                bottom = (float) mCameraCropContainerLayout.getBottom() / (float) mCameraPreview.getHeight();
                            } else {
                                left = ((float) mCameraCropContainerLayout.getLeft() - (float) mCameraPreview.getLeft()) / (float) mCameraPreview.getWidth();
                                top = (float) mCameraCropImage.getTop() / (float) mCameraPreview.getHeight();
                                right = (float) mCameraCropContainerLayout.getRight() / (float) mCameraPreview.getWidth();
                                bottom = (float) mCameraCropImage.getBottom() / (float) mCameraPreview.getHeight();
                            }
                            //裁剪及保存到文件
                            Bitmap cropBitmap = Bitmap.createBitmap(bitmap,
                                    (int) (left * (float) bitmap.getWidth()),
                                    (int) (top * (float) bitmap.getHeight()),
                                    (int) ((right - left) * (float) bitmap.getWidth()),
                                    (int) ((bottom - top) * (float) bitmap.getHeight()));

                            final File cropFile = getCropFile();
                            BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(cropFile));
                            cropBitmap.compress(Bitmap.CompressFormat.JPEG, 100, bos);
                            bos.flush();
                            bos.close();
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    mCameraResultLayout.setVisibility(View.VISIBLE);
                                }
                            });
                            return;
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                mCameraOptionLayout.setVisibility(View.VISIBLE);
                                mCameraPreview.setEnabled(true);
                            }
                        });
                    }
                }).start();
            }
        });
    }

    private void takePhoto1() {
        mCameraOptionLayout.setVisibility(View.GONE);
        mCameraPreview.setEnabled(false);
        mCameraPreview.takePhoto(new Camera.PictureCallback() {
            @Override
            public void onPictureTaken(final byte[] data, Camera camera) {
                camera.stopPreview();
                //子线程处理图片，防止ANR
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            File originalFile = getOriginalFile();
                            FileOutputStream originalFileOutputStream = new FileOutputStream(originalFile);
                            originalFileOutputStream.write(data);
                            originalFileOutputStream.close();

                            Bitmap bitmap = BitmapFactory.decodeFile(originalFile.getPath());

                            //计算裁剪位置
                            float left, top, right, bottom;
                            if (DirectionMode.MODE_COMPANY_PORTRAIT == mDirectionMode) {
                                left = (float) mCameraCropImage.getLeft() / (float) mCameraPreview.getWidth();
                                top = ((float) mCameraCropContainerLayout.getTop() - (float) mCameraPreview.getTop()) / (float) mCameraPreview.getHeight();
                                right = (float) mCameraCropImage.getRight() / (float) mCameraPreview.getWidth();
                                bottom = (float) mCameraCropContainerLayout.getBottom() / (float) mCameraPreview.getHeight();
                            } else {
                                left = ((float) mCameraCropContainerLayout.getLeft() - (float) mCameraPreview.getLeft()) / (float) mCameraPreview.getWidth();
                                top = (float) mCameraCropImage.getTop() / (float) mCameraPreview.getHeight();
                                right = (float) mCameraCropContainerLayout.getRight() / (float) mCameraPreview.getWidth();
                                bottom = (float) mCameraCropImage.getBottom() / (float) mCameraPreview.getHeight();
                            }
                            //裁剪及保存到文件
                            Bitmap cropBitmap = Bitmap.createBitmap(bitmap,
                                    (int) (left * (float) bitmap.getWidth()),
                                    (int) (top * (float) bitmap.getHeight()),
                                    (int) ((right - left) * (float) bitmap.getWidth()),
                                    (int) ((bottom - top) * (float) bitmap.getHeight()));

                            final File cropFile = getCropFile();
                            BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(cropFile));
                            cropBitmap.compress(Bitmap.CompressFormat.JPEG, 100, bos);
                            bos.flush();
                            bos.close();
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    mCameraResultLayout.setVisibility(View.VISIBLE);
                                }
                            });
                            return;
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                mCameraOptionLayout.setVisibility(View.VISIBLE);
                                mCameraPreview.setEnabled(true);
                            }
                        });
                    }
                }).start();
            }
        });
    }

    /**
     * @return 拍摄图片原始文件
     */
    private File getOriginalFile() {
        if (DirectionMode.MODE_IDCARD_FRONT == mDirectionMode) {
            return new File(getExternalCacheDir(), "idCardFront.jpg");
        } else if (DirectionMode.MODE_IDCARD_BACK == mDirectionMode) {
            return new File(getExternalCacheDir(), "idCardBack.jpg");
        } else if (DirectionMode.MODE_COMPANY_PORTRAIT == mDirectionMode) {
            return new File(getExternalCacheDir(), "companyInfo.jpg");
        } else if (DirectionMode.MODE_COMPANY_LANDSCAPE == mDirectionMode) {
            return new File(getExternalCacheDir(), "companyInfo.jpg");
        }
        return new File(getExternalCacheDir(), "picture.jpg");
    }

    /**
     * @return 拍摄图片裁剪文件
     */
    private File getCropFile() {
        if (DirectionMode.MODE_IDCARD_FRONT == mDirectionMode) {
            return new File(getExternalCacheDir(), "idCardFrontCrop.jpg");
        } else if (DirectionMode.MODE_IDCARD_BACK == mDirectionMode) {
            return new File(getExternalCacheDir(), "idCardBackCrop.jpg");
        } else if (DirectionMode.MODE_COMPANY_PORTRAIT == mDirectionMode) {
            return new File(getExternalCacheDir(), "companyInfoCrop.jpg");
        } else if (DirectionMode.MODE_COMPANY_LANDSCAPE == mDirectionMode) {
            return new File(getExternalCacheDir(), "companyInfoCrop.jpg");
        }
        return new File(getExternalCacheDir(), "pictureCrop.jpg");
    }

    /**
     * 点击对勾，使用拍照结果，返回对应图片路径
     */
    private void confirm() {
        Intent intent = new Intent();
        intent.putExtra(ConstantCamera.IMAGE_PATH, getCropFile().getPath());
        setResult(ConstantCamera.RESULT_CODE, intent);
        finish();
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (mCameraPreview != null)
            mCameraPreview.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mCameraPreview != null)
            mCameraPreview.onStop();
    }
}
