package com.general.bubing;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void onCameraClick(View view) {
        Intent intent = new Intent(this, CameraActivity.class);
        startActivity(intent);
    }

    public void onFrameworkClick(View view) {
        Intent intent = new Intent(this, FrameworkActivity.class);
        startActivity(intent);
    }

    public void onCameraPhoto(View view) {
        Intent intent = new Intent(this, CameraPhoto1Activity.class);
        startActivity(intent);
    }

    public void onLoadingClicked(View view) {
        Intent intent = new Intent(this, LoadingActivity.class);
        startActivity(intent);
    }
}
