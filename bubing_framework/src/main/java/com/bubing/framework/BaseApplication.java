package com.bubing.framework;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import java.util.Vector;

/**
 * Created by generalYan on 2019/10/28.
 */
public class BaseApplication extends Application {

    static final String APP_SHARENAME = BaseApplication.class.getName() + ".APP_SHARENAME";

    FrameLayout container;

    Vector<SingleActivity> pagestack;

    public AgentActivity currAgent;

    @Override
    public void onCreate() {
        super.onCreate();
    }

    boolean canResume() {
        return container != null && pagestack != null && pagestack.size() > 0;
    }

    void onRestore(AgentActivity pageAgent) {
        this.container = pageAgent.container;
        this.pagestack = pageAgent.pagestack;
    }

    void onResumed(AgentActivity pageAgent) {
        int count = pagestack.size();
        SingleActivity page = pagestack.get(count - 1);

        if (page instanceof GroupActivity) {
            GroupActivity group = (GroupActivity) page;
            pageAgent.pageIndex = group.findMaxIndex();
        } else {
            pageAgent.pageIndex = page.pageIndex;
        }

        for (SingleActivity instance : pagestack) {
            instance.doResume(pageAgent);
        }

        pageAgent.container = container;
        pageAgent.pagestack = pagestack;

        ViewGroup group = (ViewGroup) container.getParent();
        group.removeView(container);
        pageAgent.setContentView(container);

        container = null;
        pagestack = null;
    }

    public String getSharedString(String key, String defValue) {
        SharedPreferences sf = getSharedPreferences(APP_SHARENAME, Context.MODE_PRIVATE);
        return sf.getString(key, defValue);
    }

    public void setSharedString(String key, String value) {
        SharedPreferences sf = getSharedPreferences(APP_SHARENAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sf.edit();
        editor.putString(key, value);
        editor.commit();
    }

    public int getSharedInt(String key, int defValue) {
        SharedPreferences sf = getSharedPreferences(APP_SHARENAME, Context.MODE_PRIVATE);
        return sf.getInt(key, defValue);
    }

    public void setSharedInt(String key, int value) {
        SharedPreferences sf = getSharedPreferences(APP_SHARENAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sf.edit();
        editor.putInt(key, value);
        editor.commit();
    }

    public long getSharedLong(String key, long defValue) {
        SharedPreferences sf = getSharedPreferences(APP_SHARENAME, Context.MODE_PRIVATE);
        return sf.getLong(key, defValue);
    }

    public void setSharedLong(String key, long value) {
        SharedPreferences sf = getSharedPreferences(APP_SHARENAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sf.edit();
        editor.putLong(key, value);
        editor.commit();
    }

    public float getSharedFloat(String key, float defValue) {
        SharedPreferences sf = getSharedPreferences(APP_SHARENAME, Context.MODE_PRIVATE);
        return sf.getFloat(key, defValue);
    }

    public void setSharedFloat(String key, float value) {
        SharedPreferences sf = getSharedPreferences(APP_SHARENAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sf.edit();
        editor.putFloat(key, value);
        editor.commit();
    }

    public boolean getSharedBoolean(String key, boolean defValue) {
        SharedPreferences sf = getSharedPreferences(APP_SHARENAME, Context.MODE_PRIVATE);
        return sf.getBoolean(key, defValue);
    }

    public void setSharedBoolean(String key, boolean value) {
        SharedPreferences sf = getSharedPreferences(APP_SHARENAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sf.edit();
        editor.putBoolean(key, value);
        editor.commit();
    }

}

