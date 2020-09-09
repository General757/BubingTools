//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//
package com.bubing.tools.utils;

import android.annotation.TargetApi;
import android.app.Activity;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName: FragmentHelper
 * @Author: Bubing
 * @Date: 2020/9/9 3:21 PM
 * @Description: Fragment-工具
 */
@TargetApi(15)
public class FragmentHelper {
    Activity activity;
    List<Fragment> list;

    public FragmentHelper(Activity activity) {
        this.activity = activity;
        this.list = new ArrayList();
    }

    public void add(int id, Fragment fragment, FragmentManager manager) {
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.add(id, fragment);
        transaction.addToBackStack((String) null);
        this.list.add(fragment);
        this.commit(transaction);
    }

    public void add_fade(int id, Fragment fragment, FragmentManager manager) {
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.setTransition(0);
        transaction.add(id, fragment);
        transaction.addToBackStack((String) null);
        this.list.add(fragment);
        this.commit(transaction);
    }

    public void replace(int id, Fragment fragment, FragmentManager manager) {
        FragmentTransaction transaction = manager.beginTransaction();
        this.list.add(fragment);
        transaction.replace(id, fragment);
        this.commit(transaction);
    }

    public void pop(FragmentManager manager) {
        FragmentTransaction transaction = manager.beginTransaction();
        if (manager.getBackStackEntryCount() > 0) {
            manager.popBackStack();
            if (this.list.size() >= 2) {
                for (int i = 0; i < this.list.size(); ++i) {
                    if (i == this.list.size() - 2) {
                        ((Fragment) this.list.get(i)).setUserVisibleHint(true);
                    } else if (i == this.list.size() - 1) {
                        this.list.remove(i);
                    } else {
                        ((Fragment) this.list.get(i)).setUserVisibleHint(false);
                    }
                }
            }

            this.commit(transaction);
        } else {
            this.list = null;
            this.activity.finish();
        }

    }

    private void hideFragment(Fragment fragment, FragmentTransaction transaction) {
        for (int i = 0; i < this.list.size(); ++i) {
            if (!((Fragment) this.list.get(i)).equals(fragment)) {
                transaction.hide((Fragment) this.list.get(i));
            }
        }

    }

    private void commit(FragmentTransaction transaction) {
        transaction.commitAllowingStateLoss();
    }
}

