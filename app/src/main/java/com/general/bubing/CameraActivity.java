package com.general.bubing;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bubing.camera.TakePhotoImpl;
import com.bubing.camera.constant.Constants;
import com.bubing.camera.constant.StartupType;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

/**
 * @ClassName: CameraActivity
 * @Description: java类作用描述
 * @Author: bubing
 * @Date: 2020-04-30 13:01
 */
public class CameraActivity extends AppCompatActivity {

    public static final int REQUEST_CAMERA = 0x01;

    private ImageView imageView;
    private TextView imageText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);

        imageView = (ImageView) findViewById(R.id.picture_image);
        imageText = (TextView) findViewById(R.id.picture_text);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CAMERA && resultCode == RESULT_OK && data != null) {
            int type = data.getIntExtra(Constants.Key.RESULT_CERTIFICATE_TYPE, 0);
            String path = data.getStringExtra(Constants.Key.RESULT_CERTIFICATE_PATH);
            Log.e("!!! ### ", "type：" + type + "image_path：" + path);
            if (type > 0 && !TextUtils.isEmpty(path)) {
                imageView.setImageBitmap(BitmapFactory.decodeFile(path));
                if (type == StartupType.CAMERA_IDCARD_FRONT.getValue()) { //身份证正面
                    imageText.setText("身份证正面");
                } else if (type == StartupType.CAMERA_IDCARD_BACK.getValue()) {  //身份证反面
                    imageText.setText("身份证反面");
                } else if (type == StartupType.CAMERA_COMPANY_PORTRAIT.getValue()) {  //营业执照竖版
                    imageText.setText("营业执照竖版");
                } else if (type == StartupType.CAMERA_COMPANY_LANDSCAPE.getValue()) {  //营业执照横版
                    imageText.setText("营业执照横版");
                }
            }
        }
    }

    /**
     * 身份证正面
     */
    public void frontIdCard(View view) {
        TakePhotoImpl.createCamera(this, StartupType.CAMERA_IDCARD_FRONT).start(REQUEST_CAMERA);
    }

    /**
     * 身份证反面
     */
    public void backIdCard(View view) {
        TakePhotoImpl.createCamera(this, StartupType.CAMERA_IDCARD_BACK).start(REQUEST_CAMERA);
    }

    /**
     * 营业执照竖版
     */
    public void businessLicensePortrait(View view) {
        TakePhotoImpl.createCamera(this, StartupType.CAMERA_COMPANY_PORTRAIT).start(REQUEST_CAMERA);
    }

    /**
     * 营业执照横版
     */
    public void businessLicenseLandscape(View view) {
        TakePhotoImpl.createCamera(this, StartupType.CAMERA_COMPANY_LANDSCAPE).start(REQUEST_CAMERA);
    }
}
