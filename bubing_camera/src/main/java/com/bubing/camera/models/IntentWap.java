package com.bubing.camera.models;

import android.content.Intent;

/**
 * @ClassName: IntentWap
 * @Description: java类作用描述
 * @Author: bubing
 * @Date: 2020-05-09 20:18
 */
public class IntentWap {
    private Intent intent;
    private int requestCode;

    public IntentWap() {
    }

    public IntentWap(Intent intent, int requestCode) {
        this.intent = intent;
        this.requestCode = requestCode;
    }

    public Intent getIntent() {
        return intent;
    }

    public void setIntent(Intent intent) {
        this.intent = intent;
    }

    public int getRequestCode() {
        return requestCode;
    }

    public void setRequestCode(int requestCode) {
        this.requestCode = requestCode;
    }
}
