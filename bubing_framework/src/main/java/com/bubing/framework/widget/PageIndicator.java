package com.bubing.framework.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;

import com.bubing.framework.R;

public class PageIndicator extends View {

    Drawable drawable;

    int mDipSize;

    int mCount;

    int mIndex;

    int divider;

    Rect bounds = new Rect(0, 0, 0, 0);

    public PageIndicator(Context context) {
        this(context, null);
    }

    public PageIndicator(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PageIndicator(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.PageIndicator);
        drawable = a.getDrawable(R.styleable.PageIndicator_indicatorDrawable);

        if (drawable == null) {
            drawable = context.getResources().getDrawable(R.drawable.indicator_base_drawable);
        }

        mDipSize = a.getDimensionPixelSize(R.styleable.PageIndicator_indicatorDipSize, 12);
        divider = a.getDimensionPixelSize(R.styleable.PageIndicator_indicatorDipWidth, divider);

        a.recycle();
    }

    public void setPageCount(int count) {
        mCount = count;

        mIndex = 0;

        requestLayout();
    }

    public void setPageIndex(int index) {
        mIndex = index;

        invalidate();
    }

    @Override
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width = 0;

        if (mCount > 0) {
            width = mCount * mDipSize + (mCount - 1) * divider;
        } else {
            width = 0;
        }

        setMeasuredDimension(width, mDipSize);

    }

    @Override
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        for (int i = 0; i < mCount; i++) {
            // int saved = canvas.save();

            if (mIndex == i) {
                drawable.setState(new int[]{android.R.attr.state_selected});
            } else {
                drawable.setState(new int[]{});
            }

            bounds.left = i * (mDipSize + divider);

            bounds.top = 0;

            bounds.bottom = mDipSize;

            bounds.right = bounds.left + mDipSize;

            drawable.setBounds(bounds);

            drawable.draw(canvas);

            // canvas.restoreToCount(saved);
        }
    }
}
