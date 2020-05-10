package com.bubing.camera.models.puzzle.template.slant;

import com.bubing.camera.models.puzzle.Line;

/**
 * @ClassName: TwoSlantLayout
 * @Description: java类作用描述
 * @Author: bubing
 * @Date: 2020-05-09 16:43
 */
public class TwoSlantLayout extends NumberSlantLayout {
    public TwoSlantLayout(int theme) {
        super(theme);
    }

    @Override
    public int getThemeCount() {
        return 2;
    }

    @Override
    public void layout() {
        switch (theme) {
            case 0:
                addLine(0, Line.Direction.HORIZONTAL, 0.56f, 0.44f);
                break;
            case 1:
                addLine(0, Line.Direction.VERTICAL, 0.56f, 0.44f);
                break;
        }
    }
}
