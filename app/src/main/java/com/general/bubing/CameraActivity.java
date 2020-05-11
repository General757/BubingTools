package com.general.bubing;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bubing.camera.CameraTools;
import com.bubing.camera.constant.DirectionMode;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

/**
 * @ClassName: CameraActivity
 * @Description: java类作用描述
 * @Author: bubing
 * @Date: 2020-04-30 13:01
 */
public class CameraActivity extends AppCompatActivity {
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
        if (resultCode == RESULT_OK) {
            //获取文件路径，显示图片
            final String path = CameraTools.getImagePath(data);
            Log.e("!!! ### ", "image_path：" + path);
            if (!TextUtils.isEmpty(path)) {
                imageView.setImageBitmap(BitmapFactory.decodeFile(path));
                if (requestCode == DirectionMode.MODE_IDCARD_FRONT.getValue()) { //身份证正面
                    imageText.setText("身份证正面");
                } else if (requestCode == DirectionMode.MODE_IDCARD_BACK.getValue()) {  //身份证反面
                    imageText.setText("身份证反面");
                } else if (requestCode == DirectionMode.MODE_COMPANY_PORTRAIT.getValue()) {  //营业执照竖版
                    imageText.setText("营业执照竖版");
                } else if (requestCode == DirectionMode.MODE_COMPANY_LANDSCAPE.getValue()) {  //营业执照横版
                    imageText.setText("营业执照横版");
                }
            }
        }
    }

    /**
     * 身份证正面
     */
    public void frontIdCard(View view) {
        CameraTools.create(this).openCertificateCamera(DirectionMode.MODE_IDCARD_FRONT);
    }

    /**
     * 身份证反面
     */
    public void backIdCard(View view) {
        CameraTools.create(this).openCertificateCamera(DirectionMode.MODE_IDCARD_BACK);
    }

    /**
     * 营业执照竖版
     */
    public void businessLicensePortrait(View view) {
        CameraTools.create(this).openCertificateCamera(DirectionMode.MODE_COMPANY_PORTRAIT);
    }

    /**
     * 营业执照横版
     */
    public void businessLicenseLandscape(View view) {
        CameraTools.create(this).openCertificateCamera(DirectionMode.MODE_COMPANY_LANDSCAPE);
    }
}
