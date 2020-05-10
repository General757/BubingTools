package com.bubing.camera.models;

import android.app.Activity;

import androidx.fragment.app.Fragment;

/**
 * @ClassName: ContextWrap
 * @Description: java类作用描述
 * @Author: bubing
 * @Date: 2020-05-09 14:11
 */
public class ContextWrap {

    private Activity activity;
    private Fragment fragmentV;
    private android.app.Fragment fragment;

    public static ContextWrap of(Activity activity) {
        return new ContextWrap(activity);
    }

    public static ContextWrap of(Fragment fragmentV) {
        return new ContextWrap(fragmentV);
    }

    public static ContextWrap of(android.app.Fragment fragment) {
        return new ContextWrap(fragment);
    }

    private ContextWrap(Activity activity) {
        this.activity = activity;
    }

    private ContextWrap(Fragment fragmentV) {
        this.fragmentV = fragmentV;
        this.activity = fragmentV.getActivity();
    }

    private ContextWrap(android.app.Fragment fragment) {
        this.fragment = fragment;
        this.activity = fragment.getActivity();
    }

    public Activity getActivity() {
        return activity;
    }

    public void setActivity(Activity activity) {
        this.activity = activity;
    }

    public Fragment getFragmentV() {
        return fragmentV;
    }

    public void setFragmentV(Fragment fragmentV) {
        this.fragmentV = fragmentV;
    }

    public android.app.Fragment getFragment() {
        return fragment;
    }

    public void setFragment(android.app.Fragment fragment) {
        this.fragment = fragment;
    }
}
