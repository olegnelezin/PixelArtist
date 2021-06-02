package com.example.greatexitproject.scrolling;

import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ScrollView;

import androidx.annotation.RequiresApi;

public final class MultiScrollView extends ScrollView {
    private int scX, scY;
    private final float MAX_MOVEMENT = 25;
    public MultiScrollView(Context context) {
        super(context);
    }

    public MultiScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MultiScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public boolean canScrollHorizontally(int direction) {
        return true;
    }

    @Override
    public boolean canScrollVertically(int direction) {
        return true;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            scX = (int) ev.getX();
            scY = (int) ev.getY();
        } else if (ev.getAction() == MotionEvent.ACTION_MOVE) {
            float x = Math.abs(ev.getX() - scX);
            float y = Math.abs(ev.getY() - scY);
            return x >= MAX_MOVEMENT || y >= MAX_MOVEMENT;
        }
        return false;
    }
}
