package com.bubing.camera.models.puzzle.template.slant;

import com.bubing.camera.utils.BubingLog;
import com.bubing.camera.models.puzzle.slant.SlantPuzzleLayout;

/**
 * @ClassName: NumberSlantLayout
 * @Description: java类作用描述
 * @Author: bubing
 * @Date: 2020-05-09 16:25
 */
public abstract class NumberSlantLayout extends SlantPuzzleLayout {

    static final String TAG = "NumberSlantLayout";
    protected int theme;

    public NumberSlantLayout(int theme) {
        if (theme >= getThemeCount())
            BubingLog.e(TAG, "NumberSlantLayout: the most theme count is " + getThemeCount() + " ,you should let theme from 0 to " + (getThemeCount() - 1) + " .");
        this.theme = theme;
    }

    public abstract int getThemeCount();

    public int getTheme() {
        return theme;
    }
}
