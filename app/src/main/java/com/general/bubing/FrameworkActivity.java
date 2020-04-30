package com.general.bubing;

import android.os.Bundle;

import com.bubing.framework.AgentActivity;
import com.bubing.framework.PageIntent;
import com.general.bubing.framework.HomeGroupActivity;
import com.general.bubing.framework.LeadAlbum;

/**
 * @ClassName: FrameworkActivity
 * @Description: java类作用描述
 * @Author: bubing
 * @Date: 2020-04-30 14:00
 */
public class FrameworkActivity extends AgentActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void startMain() {
        App app = (App) getApplication();
        PageIntent intent = new PageIntent(this, app.getFirstStart() ? LeadAlbum.class : HomeGroupActivity.class);
        startPagement(intent);
    }

    @Override
    protected void startLaunchAnim() {

    }

    @Override
    protected void showImView(Bundle bundle) {

    }
}
