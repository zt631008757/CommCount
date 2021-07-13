package com.android.diandezhun.ui.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.SeekBar;

public class VerticalSeekBar extends SeekBar {

    public final static int DICTION_LEFT = -1;
    public final static int DICTION_RIGHT = 1;

    private int orientation;

    public VerticalSeekBar(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(h, w, oldh, oldw);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (orientation == DICTION_LEFT) {
            canvas.rotate(-90);
            canvas.translate(-getHeight(), 0);
        } else {
            canvas.rotate(90);
            canvas.translate(0, -getWidth());
        }
        super.onDraw(canvas);
    }

//    @Override
//    protected float getEventX(MotionEvent event) {
//        if (orientation == DICTION_LEFT) {
//            return getHeight() - event.getY();
//        } else {
//            return event.getY();
//        }
//    }
//
//    @Override
//    protected float getEventY(MotionEvent event) {
//        if (orientation == DICTION_LEFT) {
//            return event.getX();
//        } else {
//            return -event.getX() + getWidth();
//        }
//    }


    @Override
    public float getX() {
        if (orientation == DICTION_LEFT) {
            return getHeight() - getY();
        } else {
            return getY();
        }
    }

    @Override
    public float getY() {
        return super.getY();

    }
}
