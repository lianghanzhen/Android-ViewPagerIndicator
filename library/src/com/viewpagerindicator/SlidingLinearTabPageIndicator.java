package com.viewpagerindicator;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;


public class SlidingLinearTabPageIndicator extends LinearTabPageIndicator {

    private float mCurrentPositionOffset;

    private Paint mIndicatorPaint;
    private int mIndicatorColor;
    private int mIndicatorHeight;
    private Paint mUnderlinePaint;
    private int mUnderlineColor;
    private int mUnderlineHeight;

    public SlidingLinearTabPageIndicator(Context context) {
        this(context, null);
    }

    public SlidingLinearTabPageIndicator(Context context, AttributeSet attrs) {
        super(context, attrs);

        // load default resources
        final int defaultIndicatorColor = getResources().getColor(R.color.default_indicator_color);
        final int defaultIndicatorHeight = getResources().getDimensionPixelSize(R.dimen.default_indicator_height);
        final int defaultUnderlineColor = getResources().getColor(R.color.default_underline_color);
        final int defaultUnderlineHeight = getResources().getDimensionPixelSize(R.dimen.default_underline_height);

        if (attrs == null) {
            mIndicatorColor = defaultIndicatorColor;
            mIndicatorHeight = defaultIndicatorHeight;
            mUnderlineColor = defaultUnderlineColor;
            mUnderlineHeight = defaultUnderlineHeight;
        } else {
            final TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.SlidingLinearTabPageIndicator);
            mIndicatorColor = typedArray.getColor(R.styleable.SlidingLinearTabPageIndicator_linearIndicatorColor, defaultIndicatorColor);
            mIndicatorHeight = typedArray.getDimensionPixelSize(R.styleable.SlidingLinearTabPageIndicator_linearIndicatorHeight, defaultIndicatorHeight);
            if (mIndicatorHeight <= 0)
                mIndicatorHeight = defaultIndicatorHeight;
            mUnderlineColor = typedArray.getColor(R.styleable.SlidingLinearTabPageIndicator_linearUnderlineColor, defaultUnderlineColor);
            mUnderlineHeight = typedArray.getDimensionPixelSize(R.styleable.SlidingLinearTabPageIndicator_linearUnderlineHeight, defaultUnderlineHeight);
            typedArray.recycle();
        }
        mIndicatorPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mUnderlinePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        final int tabCount = getChildCount();
        if (tabCount == 0) {
            return;
        }

        final int parentHeight = getHeight();
        final View currentTabView = getChildAt(mSelectedTabIndex);

        // draw underline
        if (mUnderlineHeight > 0) {
            mUnderlinePaint.setColor(mUnderlineColor);
            canvas.drawRect(0, parentHeight - mUnderlineHeight, getWidth(), parentHeight, mUnderlinePaint);
        }

        // draw indicator
        if (mIndicatorHeight > 0) {
            mIndicatorPaint.setColor(mIndicatorColor);
            float indicatorLeft = currentTabView.getLeft();
            float indicatorRight = currentTabView.getRight();
            if (mCurrentPositionOffset > 0f && mSelectedTabIndex < tabCount - 1) {
                final View nextTabView = getChildAt(mSelectedTabIndex + 1);
                final float nextLeft = nextTabView.getLeft();
                final float nextRight = nextTabView.getRight();
                indicatorLeft = mCurrentPositionOffset * nextLeft + (1f - mCurrentPositionOffset) * indicatorLeft;
                indicatorRight = mCurrentPositionOffset * nextRight + (1f - mCurrentPositionOffset) * indicatorRight;
            }
            canvas.drawRect(indicatorLeft, parentHeight - mIndicatorHeight, indicatorRight, parentHeight, mIndicatorPaint);
        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        mSelectedTabIndex = position;
        mCurrentPositionOffset = positionOffset;
        invalidate();

        super.onPageScrolled(position, positionOffset, positionOffsetPixels);
    }

}
