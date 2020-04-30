package com.general.bubing.framework;

import android.view.KeyEvent;
import android.view.View;
import android.widget.FrameLayout;

import com.bubing.framework.GroupActivity;
import com.bubing.framework.SingleActivity;
import com.general.bubing.R;

import java.util.HashMap;
import java.util.Set;

/**
 * @ClassName: FrameworkActivity
 * @Description: java类作用描述
 * @Author: bubing
 * @Date: 2020-04-30 14:00
 */
/**
 * @author YanYan 首页banner主控
 * @fileName HomeGroupActivity.java @date 2017年1月6日下午12:24:582017
 */
public class HomeGroupActivity extends GroupActivity {
    HashMap<View, Class<? extends SingleActivity>> children;


    @Override
    public void onBuild() {
        setContentView(R.layout.activity_main_group);

        FrameLayout container = (FrameLayout) findViewById(R.id.container);

        setContainer(container);

        setChildren();
    }

    @Override
    public void onClose() {

    }

    public void setChildren() {
        View menu_first = findViewById(R.id.menu_first);
        View menu_second = findViewById(R.id.menu_second);
        View menu_third = findViewById(R.id.menu_third);
        View menu_more = findViewById(R.id.menu_more);

        children = new HashMap<View, Class<? extends SingleActivity>>();
        children.put(menu_first, HomeFirstActivity.class);
        children.put(menu_second, HomeSecondActivity.class);
        children.put(menu_third, HomeThirdActivity.class);
        children.put(menu_more, HomeMoreActivity.class);

        Set<View> keyset = children.keySet();

        for (View menu : keyset) {
            menu.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    setSelected(v);
                }
            });
        }

        setSelected(menu_first);

    }

    public void setSelected(View selected) {
        Class<? extends SingleActivity> mClass = children.get(selected);
        displayChild(mClass, null);

        Set<View> keyset = children.keySet();

        for (View menu : keyset) {
            menu.setSelected(menu == selected);
        }

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return false;
    }

}
