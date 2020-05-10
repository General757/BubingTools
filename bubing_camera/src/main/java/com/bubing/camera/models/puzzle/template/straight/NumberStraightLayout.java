package com.bubing.camera.models.puzzle.template.straight;

import android.util.Log;

import com.bubing.camera.models.puzzle.straight.StraightPuzzleLayout;

/**
 * @ClassName: NumberStraightLayout
 * @Description: java类作用描述
 * @Author: bubing
 * @Date: 2020-05-09 16:45
 */
public abstract class NumberStraightLayout extends StraightPuzzleLayout {
    static final String TAG = "NumberStraightLayout";
    protected int theme;

    public NumberStraightLayout(int theme) {
        if (theme >= getThemeCount()) {
            Log.e(TAG, "NumberStraightLayout: the most theme count is "
                    + getThemeCount()
                    + " ,you should let theme from 0 to "
                    + (getThemeCount() - 1)
                    + " .");
        }
        this.theme = theme;
    }

    public abstract int getThemeCount();

    public int getTheme() {
        return theme;
    }
}

