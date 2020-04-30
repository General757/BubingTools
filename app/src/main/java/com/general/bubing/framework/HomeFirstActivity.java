package com.general.bubing.framework;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.bubing.framework.PageIntent;
import com.bubing.framework.SingleActivity;
import com.general.bubing.R;

/**
 * @ClassName: HomeFirstActivity
 * @Description: 第一页
 * @Author: bubing
 * @Date: 2017年1月6日下午1:32:21
 */
public class HomeFirstActivity extends SingleActivity {
    @Override
    public void onBuild() {
        super.onBuild();
        setContentView(R.layout.activity_home_first);

        TextView firstBttn1 = (TextView) findViewById(R.id.masterText);
        firstBttn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PageIntent intent = new PageIntent(HomeFirstActivity.this, StyleActivity.class);
                Bundle extras = new Bundle();
                extras.putInt("styleId", 1);
                intent.setExtras(extras);
                startPagement(intent);
            }
        });
        TextView firstBttn2 = (TextView) findViewById(R.id.ordinaryText);
        firstBttn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PageIntent intent = new PageIntent(HomeFirstActivity.this, StyleActivity.class);
                Bundle extras = new Bundle();
                extras.putInt("styleId", 2);
                intent.setExtras(extras);
                startPagement(intent, SingleActivity.ANIM_TO_RIGHT);
            }
        });
        TextView firstBttn3 = (TextView) findViewById(R.id.recordingText);
        firstBttn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PageIntent intent = new PageIntent(HomeFirstActivity.this, StyleActivity.class);
                Bundle extras = new Bundle();
                extras.putInt("styleId", 3);
                intent.setExtras(extras);
                startPagement(intent, SingleActivity.ANIM_TO_TOP);
            }
        });
        TextView firstBttn4 = (TextView) findViewById(R.id.otherText);
        firstBttn4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PageIntent intent = new PageIntent(HomeFirstActivity.this, StyleActivity.class);
                Bundle extras = new Bundle();
                extras.putInt("styleId", 4);
                intent.setExtras(extras);
                startPagement(intent, SingleActivity.ANIM_TO_BOTTOM);
            }
        });
    }

}
