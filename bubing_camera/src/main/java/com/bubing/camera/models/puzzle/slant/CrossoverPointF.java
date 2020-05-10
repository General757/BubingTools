package com.bubing.camera.models.puzzle.slant;

import android.graphics.PointF;

/**
 * @ClassName: CrossoverPointF
 * @Description: 两条线的交点
 * @Author: bubing
 * @Date: 2020-05-09 16:29
 */
class CrossoverPointF extends PointF {
    SlantLine horizontal;
    SlantLine vertical;

    CrossoverPointF() {

    }

    CrossoverPointF(float x, float y) {
        this.x = x;
        this.y = y;
    }

    CrossoverPointF(SlantLine horizontal, SlantLine vertical) {
        this.horizontal = horizontal;
        this.vertical = vertical;
    }

    void update() {
        if (horizontal == null || vertical == null) {
            return;
        }
        SlantUtils.intersectionOfLines(this, horizontal, vertical);
    }
}
