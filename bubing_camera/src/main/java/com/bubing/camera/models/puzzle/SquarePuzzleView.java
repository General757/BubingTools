package com.bubing.camera.models.puzzle;

import android.content.Context;
import android.util.AttributeSet;

/**
 * @ClassName: SquarePuzzleView
 * @Description: java类作用描述
 * @Author: bubing
 * @Date: 2020-05-09 16:24
 */
public class SquarePuzzleView extends PuzzleView {
    public SquarePuzzleView(Context context) {
        super(context);
    }

    public SquarePuzzleView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SquarePuzzleView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int width = getMeasuredWidth();
        int height = getMeasuredHeight();
        int length = width > height ? height : width;

        setMeasuredDimension(length, length);
    }
}
