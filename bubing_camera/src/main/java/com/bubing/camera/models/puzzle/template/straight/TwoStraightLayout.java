package com.bubing.camera.models.puzzle.template.straight;

import com.bubing.camera.utils.BubingLog;
import com.bubing.camera.models.puzzle.Line;

/**
 * @ClassName: TwoStraightLayout
 * @Description: java类作用描述
 * @Author: bubing
 * @Date: 2020-05-09 16:47
 */
public class TwoStraightLayout extends NumberStraightLayout {
    private float mRadio = 1f / 2;

    public TwoStraightLayout(int theme) {
        super(theme);
    }

    public TwoStraightLayout(float radio, int theme) {
        super(theme);
        if (mRadio > 1) {
            BubingLog.e(TAG, "CrossLayout: the radio can not greater than 1f");
            mRadio = 1f;
        }
        mRadio = radio;
    }

    @Override
    public int getThemeCount() {
        return 6;
    }

    @Override
    public void layout() {
        switch (theme) {
            case 0:
                addLine(0, Line.Direction.HORIZONTAL, mRadio);
                break;
            case 1:
                addLine(0, Line.Direction.VERTICAL, mRadio);
                break;
            case 2:
                addLine(0, Line.Direction.HORIZONTAL, 1f / 3);
                break;
            case 3:
                addLine(0, Line.Direction.HORIZONTAL, 2f / 3);
                break;
            case 4:
                addLine(0, Line.Direction.VERTICAL, 1f / 3);
                break;
            case 5:
                addLine(0, Line.Direction.VERTICAL, 2f / 3);
                break;
            default:
                addLine(0, Line.Direction.HORIZONTAL, mRadio);
                break;
        }
    }
}

