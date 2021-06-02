package com.example.greatexitproject.scrolling;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.HorizontalScrollView;

public class HorizontalSrollView extends HorizontalScrollView {

    public HorizontalSrollView(Context context) {
        super(context);
    }

    public HorizontalSrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public HorizontalSrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public boolean canScrollHorizontally(int direction) {
        return false;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return false;
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        return false;
    }
}
