package com.bubing.tools.loading.render.circle.rotate;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.view.animation.Interpolator;

import androidx.interpolator.view.animation.FastOutSlowInInterpolator;

import com.bubing.tools.loading.render.LoadingRenderer;
import com.bubing.tools.utils.DensityUtils;

/**
 * @ClassName: LinesLoadingRenderer
 * @Author: Bubing
 * @Date: 2020/8/24 4:34 PM
 * @Description: java类作用描述
 */
public class LinesLoadingRenderer extends LoadingRenderer {
    private static final Interpolator MATERIAL_INTERPOLATOR = new FastOutSlowInInterpolator();

    private static final int DEGREE_360 = 360;
    private static final int NUM_POINTS = 5;

    private static final float MAX_SWIPE_DEGREES = 0.8f * DEGREE_360;
    private static final float FULL_GROUP_ROTATION = 3.0f * DEGREE_360;

    private static final float COLOR_START_DELAY_OFFSET = 0.4f;
    private static final float END_TRIM_DURATION_OFFSET = 1.0f;
    private static final float START_TRIM_DURATION_OFFSET = 0.5f;

    private static final float DEFAULT_CENTER_RADIUS = 12.5f;
    private static final float DEFAULT_STROKE_WIDTH = 2.5f;

    private static final int[] DEFAULT_COLORS = new int[]{
            Color.RED, Color.GREEN, Color.BLUE
    };

    private static final int LINE_COUNT = 12;
    private static final int DEGREE_PER_LINE = 360 / LINE_COUNT;

    private final Paint mPaint = new Paint();

    private int mSize;
    private int[] mColors;
    private int mColorIndex;
    private int mCurrentColor;

    private float mStrokeInset;

    private float mStrokeWidth;
    private float mCenterRadius;

    private float mRenderDegrees;

    private LinesLoadingRenderer(Context context) {
        super(context);
        init(context);
        setupPaint();
    }

    private void init(Context context) {
        mStrokeWidth = DensityUtils.dip2px(context, DEFAULT_STROKE_WIDTH);
        mCenterRadius = DensityUtils.dip2px(context, DEFAULT_CENTER_RADIUS);

        mSize = (int) Math.min(mWidth, mHeight);
        mColors = DEFAULT_COLORS;

        setColorIndex(0);
        initStrokeInset(mWidth, mHeight);
    }

    private void setupPaint() {
        mPaint.setAntiAlias(true);
//        mPaint.setStrokeWidth(mStrokeWidth);
//        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeCap(Paint.Cap.ROUND);
    }

    @Override
    protected void draw(Canvas canvas) {
        int saveCount = canvas.save();
//        int saveCount = canvas.saveLayer(0, 0, getWidth(), getHeight(), null, Canvas.ALL_SAVE_FLAG);

//        mTempBounds.set(mBounds);
//        mTempBounds.inset(mStrokeInset, mStrokeInset);
//
//        canvas.rotate(mGroupRotation, mTempBounds.centerX(), mTempBounds.centerY());
//
//        if (mSwipeDegrees != 0) {
//            mPaint.setColor(mCurrentColor);
//            canvas.drawArc(mTempBounds, mStartDegrees, mSwipeDegrees, false, mPaint);
//        }

        mPaint.setColor(mCurrentColor);
        drawLoading(canvas, mRenderDegrees * DEGREE_PER_LINE);
        canvas.restoreToCount(saveCount);
    }

    private void drawLoading(Canvas canvas, float rotateDegrees) {
        int width = mSize / 12, height = mSize / 6;
        mPaint.setStrokeWidth(width);

        canvas.rotate(rotateDegrees, mSize / 2, mSize / 2);
        canvas.translate(mSize / 2, mSize / 2);

        for (int i = 0; i < LINE_COUNT; i++) {
            canvas.rotate(DEGREE_PER_LINE);
            mPaint.setAlpha((int) (255f * (i + 1) / LINE_COUNT));
            canvas.translate(0, -mSize / 2 + width / 2);
            canvas.drawLine(0, 0, 0, height, mPaint);
            canvas.translate(0, mSize / 2 - width / 2);
        }
    }

    @Override
    protected void computeRender(float renderProgress) {
        mRenderDegrees = renderProgress * (LINE_COUNT - 1);
        updateRingColor(renderProgress);
    }

    @Override
    protected void setAlpha(int alpha) {
        mPaint.setAlpha(alpha);
    }

    @Override
    protected void setColorFilter(ColorFilter cf) {
        mPaint.setColorFilter(cf);
    }

    @Override
    protected void reset() {
    }

    private void setColorIndex(int index) {
        mColorIndex = index;
        mCurrentColor = mColors[mColorIndex];
    }

    private int getNextColor() {
        return mColors[getNextColorIndex()];
    }

    private int getNextColorIndex() {
        return (mColorIndex + 1) % (mColors.length);
    }

    private void goToNextColor() {
        setColorIndex(getNextColorIndex());
    }

    private void initStrokeInset(float width, float height) {
        float minSize = Math.min(width, height);
        float strokeInset = minSize / 2.0f - mCenterRadius;
        float minStrokeInset = (float) Math.ceil(mStrokeWidth / 2.0f);
        mStrokeInset = strokeInset < minStrokeInset ? minStrokeInset : strokeInset;
    }

    private int getStartingColor() {
        return mColors[mColorIndex];
    }

    private void updateRingColor(float interpolatedTime) {
        if (interpolatedTime > COLOR_START_DELAY_OFFSET) {
            mCurrentColor = evaluateColorChange((interpolatedTime - COLOR_START_DELAY_OFFSET)
                    / (1.0f - COLOR_START_DELAY_OFFSET), getStartingColor(), getNextColor());
        }
    }

    private int evaluateColorChange(float fraction, int startValue, int endValue) {
        int startA = (startValue >> 24) & 0xff;
        int startR = (startValue >> 16) & 0xff;
        int startG = (startValue >> 8) & 0xff;
        int startB = startValue & 0xff;

        int endA = (endValue >> 24) & 0xff;
        int endR = (endValue >> 16) & 0xff;
        int endG = (endValue >> 8) & 0xff;
        int endB = endValue & 0xff;

        return ((startA + (int) (fraction * (endA - startA))) << 24)
                | ((startR + (int) (fraction * (endR - startR))) << 16)
                | ((startG + (int) (fraction * (endG - startG))) << 8)
                | ((startB + (int) (fraction * (endB - startB))));
    }

    private void apply(LinesLoadingRenderer.Builder builder) {
        this.mWidth = builder.mWidth > 0 ? builder.mWidth : this.mWidth;
        this.mHeight = builder.mHeight > 0 ? builder.mHeight : this.mHeight;
        this.mStrokeWidth = builder.mStrokeWidth > 0 ? builder.mStrokeWidth : this.mStrokeWidth;
        this.mCenterRadius = builder.mCenterRadius > 0 ? builder.mCenterRadius : this.mCenterRadius;

        this.mDuration = builder.mDuration > 0 ? builder.mDuration : this.mDuration;

        this.mColors = builder.mColors != null && builder.mColors.length > 0 ? builder.mColors : this.mColors;

        setColorIndex(0);
        setupPaint();
        initStrokeInset(this.mWidth, this.mHeight);
    }

    public static class Builder {
        private Context mContext;

        private int mWidth;
        private int mHeight;
        private int mStrokeWidth;
        private int mCenterRadius;

        private int mDuration;

        private int[] mColors;

        public Builder(Context mContext) {
            this.mContext = mContext;
        }

        public LinesLoadingRenderer.Builder setWidth(int width) {
            this.mWidth = width;
            return this;
        }

        public LinesLoadingRenderer.Builder setHeight(int height) {
            this.mHeight = height;
            return this;
        }

        public LinesLoadingRenderer.Builder setStrokeWidth(int strokeWidth) {
            this.mStrokeWidth = strokeWidth;
            return this;
        }

        public LinesLoadingRenderer.Builder setCenterRadius(int centerRadius) {
            this.mCenterRadius = centerRadius;
            return this;
        }

        public LinesLoadingRenderer.Builder setDuration(int duration) {
            this.mDuration = duration;
            return this;
        }

        public LinesLoadingRenderer.Builder setColors(int[] colors) {
            this.mColors = colors;
            return this;
        }

        public LinesLoadingRenderer build() {
            LinesLoadingRenderer loadingRenderer = new LinesLoadingRenderer(mContext);
            loadingRenderer.apply(this);
            return loadingRenderer;
        }
    }
}
