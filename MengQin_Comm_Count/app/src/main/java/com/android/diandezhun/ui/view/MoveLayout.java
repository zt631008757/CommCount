package com.android.diandezhun.ui.view;

import android.content.Context;
import android.graphics.Point;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import androidx.annotation.Nullable;

import com.android.baselibrary.util.Util;
import com.android.diandezhun.R;
import com.android.diandezhun.event.Event_CuttingBox_Move;

import org.greenrobot.eventbus.EventBus;

/**
 * Created by Robert on 2017/6/20.
 */

public class MoveLayout extends RelativeLayout {

    private int dragDirection = 0;
    private static final int TOP = 0x15;
    private static final int LEFT = 0x16;
    private static final int BOTTOM = 0x17;
    private static final int RIGHT = 0x18;
    private static final int LEFT_TOP = 0x11;
    private static final int RIGHT_TOP = 0x12;
    private static final int LEFT_BOTTOM = 0x13;
    private static final int RIGHT_BOTTOM = 0x14;
    private static final int CENTER = 0x19;

    private int screenWidth;
    private int screenHeight;

    private int oriLeft;
    private int oriRight;
    private int oriTop;
    private int oriBottom;


    /**
     * 标示此类的每个实例的id
     */
    private int identity = 0;


    /**
     * 触控区域设定
     */
    public static int Padding;   //边框内边距
    private int touchAreaLength;  //边框触摸区域 宽度

    private int minHeight = 120;
    private int minWidth = 180;
    private static final String TAG = "MoveLinearLayout";


    public MoveLayout(Context context) {
        super(context);
        touchAreaLength = Util.dip2px(context, 30);
        Padding = Util.dip2px(context, 10);
//        Padding = Util.dip2px(context, 0);
        init();
    }

    public MoveLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public MoveLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();

    }

    private void init() {
        screenHeight = 500;//getResources().getDisplayMetrics().heightPixels - 40;
        screenWidth = 500;// getResources().getDisplayMetrics().widthPixels;
    }

    public void setViewWidthHeight(int width, int height) {
        screenWidth = width;
        screenHeight = height;
    }

    public void setMinHeight(int height) {
        minHeight = height;
        if (minHeight < touchAreaLength * 2) {
            minHeight = touchAreaLength * 2;
        }
    }

    public void setMinWidth(int width) {
        minWidth = width;
        if (minWidth < touchAreaLength * 3) {
            minWidth = touchAreaLength * 3;
        }
    }

    private boolean mFixedSize = false;

    public void setFixedSize(boolean b) {
        mFixedSize = b;
    }

    private int mDeleteHeight = 0;
    private int mDeleteWidth = 0;
    private boolean isInDeleteArea = false;

    public void setDeleteWidthHeight(int width, int height) {
        mDeleteWidth = screenWidth - width;
        mDeleteHeight = height;
    }

    public Point getLeftTop() {
        int margin = Util.dip2px(getContext(), 13);
        int left = getLeft() + margin;
        int top = getTop() + margin;
        Point leftTop = new Point(left, top);
        return leftTop;
    }

    public Point getRightBottom() {
        int margin = Util.dip2px(getContext(), 13);
        int right = getRight() - margin;
        int bottom = getBottom() - margin;
        Point rightBottom = new Point(right, bottom);
        return rightBottom;
    }

    public View getSelfView() {
        LinearLayout linearLayout = findViewById(R.id.add_your_view_here);
        return linearLayout.getChildAt(0);
    }

    public void onDrag(int x, int y, int dx, int dy) {
        switch (dragDirection) {
            case LEFT:
                left(dx);
                break;
            case RIGHT:
                right(dx);
                break;
            case BOTTOM:
                bottom(dy);
                break;
            case TOP:
                top(dy);
                break;
            case CENTER:
                center(dx, dy);
                break;
            case LEFT_BOTTOM:
                left(dx);
                bottom(dy);
                break;
            case LEFT_TOP:
                left(dx);
                top(dy);
                break;
            case RIGHT_BOTTOM:
                right(dx);
                bottom(dy);
                break;
            case RIGHT_TOP:
                right(dx);
                top(dy);
                break;
        }

        LayoutParams lp = new LayoutParams(oriRight - oriLeft, oriBottom - oriTop);
        lp.setMargins(oriLeft, oriTop, screenWidth - oriRight, screenHeight - oriBottom);
        setLayoutParams(lp);


//        layout(oriLeft, oriTop, oriRight, oriBottom);

        EventBus.getDefault().post(new Event_CuttingBox_Move());

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int action = event.getAction();
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                //   Log.d(TAG, "onTouchEvent: down height="+ getHeight());
                oriLeft = getLeft();
                oriRight = getRight();
                oriTop = getTop();
                oriBottom = getBottom();

                dragDirection = getDirection((int) event.getX(), (int) event.getY());
                break;
//            case MotionEvent.ACTION_UP:
//             //      Log.d(TAG, "onTouchEvent: up");
//                spotL = false;
//                spotT = false;
//                spotR = false;
//                spotB = false;
//                requestLayout();
//               // invalidate();
//                EventBus.getDefault().post(new Event_CuttingBox_Move());
//                break;
////            case MotionEvent.ACTION_CANCEL:
////                Log.d(TAG, "onTouchEvent: cancel");
////                spotL = false;
////                spotT = false;
////                spotR = false;
////                spotB = false;
////                invalidate();
////                break;
//            case MotionEvent.ACTION_MOVE:
//                 // Log.d(TAG, "onTouchEvent: move");
//                int tempRawX = (int)event.getRawX();
//                int tempRawY = (int)event.getRawY();
//
//                int dx = tempRawX - lastX;
//                int dy = tempRawY - lastY;
//                lastX = tempRawX;
//                lastY = tempRawY;
//
//                switch (dragDirection) {
//                    case LEFT:
//                        left( dx);
//                        break;
//                    case RIGHT:
//                        right( dx);
//                        break;
//                    case BOTTOM:
//                        bottom(dy);
//                        break;
//                    case TOP:
//                        top( dy);
//                        break;
//                    case CENTER:
//                        center( dx, dy);
//                        break;
////                    case LEFT_BOTTOM:
////                        left( dx);
////                        bottom( dy);
////                        break;
////                    case LEFT_TOP:
////                        left( dx);
////                        top(dy);
////                        break;
////                    case RIGHT_BOTTOM:
////                        right( dx);
////                        bottom( dy);
////                        break;
////                    case RIGHT_TOP:
////                        right( dx);
////                        top( dy);
////                        break;
//                }
//
//                //new pos l t r b is set into oriLeft, oriTop, oriRight, oriBottom
//                LayoutParams lp = new LayoutParams(oriRight - oriLeft, oriBottom - oriTop);
//                lp.setMargins(oriLeft,oriTop,0,0);
//                setLayoutParams(lp);
//             //   Log.d(TAG, "onTouchEvent: set layout width="+(oriRight - oriLeft)+" height="+(oriBottom - oriTop));
//             //   Log.d(TAG, "onTouchEvent: marginLeft="+oriLeft+"  marginTop"+oriTop);
//
//                EventBus.getDefault().post(new Event_CuttingBox_Move());
//                break;
        }
        return super.onTouchEvent(event);
    }

    /**
     * 触摸点为中心->>移动
     */
    private void center(int dx, int dy) {
        int left = getLeft() + dx;
        int top = getTop() + dy;
        int right = getRight() + dx;
        int bottom = getBottom() + dy;

        if (left < -Padding) {
            left = -Padding;
            right = left + getWidth();
        }
        if (top < -Padding) {
            top = -Padding;
            bottom = top + getHeight();
        }
        if (right > screenWidth + Padding) {
            right = screenWidth + Padding;
            left = right - getWidth();

            Log.i("test",  (right - left) + "");
        }

        if (bottom > screenHeight + Padding) {
            bottom = screenHeight + Padding;
            top = bottom - getHeight() ;
        }

        oriLeft = left;
        oriTop = top;
        oriRight = right;
        oriBottom = bottom;

    }

    /**
     * 触摸点为上边缘
     */
    private void top(int dy) {
        oriTop += dy;
        if (oriTop < 0) {
            oriTop = 0;
        }
        if (oriBottom - oriTop < minHeight) {
            oriTop = oriBottom - minHeight;
        }
    }

    /**
     * 触摸点为下边缘
     */
    private void bottom(int dy) {

        oriBottom += dy;
        if (oriBottom > screenHeight) {
            oriBottom = screenHeight;
        }
        if (oriBottom - oriTop < minHeight) {
            oriBottom = minHeight + oriTop;
        }
    }

    /**
     * 触摸点为右边缘
     */
    private void right(int dx) {
        oriRight += dx;
        if (oriRight > screenWidth) {
            oriRight = screenWidth;
        }
        if (oriRight - oriLeft < minWidth) {
            oriRight = oriLeft + minWidth;
        }
    }

    /**
     * 触摸点为左边缘
     */
    private void left(int dx) {
        oriLeft += dx;
        if (oriLeft < 0) {
            oriLeft = 0;
        }
        if (oriRight - oriLeft < minWidth) {
            oriLeft = oriRight - minWidth;
        }
    }

    private int getDirection(int x, int y) {
        int left = getLeft();
        int right = getRight();
        int bottom = getBottom();
        int top = getTop();

        if (x < touchAreaLength && y < touchAreaLength) {
            return LEFT_TOP;
        }
        if (y < touchAreaLength && right - left - x < touchAreaLength) {
            return RIGHT_TOP;
        }
        if (x < touchAreaLength && bottom - top - y < touchAreaLength) {
            return LEFT_BOTTOM;
        }
        if (right - left - x < touchAreaLength && bottom - top - y < touchAreaLength) {
            return RIGHT_BOTTOM;
        }
        if (mFixedSize == true) {
            return CENTER;
        }

        if (x < touchAreaLength) {
            spotL = true;
            requestLayout();
            return LEFT;
        }
        if (y < touchAreaLength) {
            spotT = true;
            requestLayout();
            return TOP;
        }
        if (right - left - x < touchAreaLength) {
            spotR = true;
            requestLayout();
            return RIGHT;
        }
        if (bottom - top - y < touchAreaLength) {
            spotB = true;
            requestLayout();
            return BOTTOM;
        }
        return CENTER;
    }

    private boolean spotL = false;
    private boolean spotT = false;
    private boolean spotR = false;
    private boolean spotB = false;

    public int getIdentity() {
        return identity;
    }

    public void setIdentity(int identity) {
        this.identity = identity;
    }


    //delete listener
    private DeleteMoveLayout mListener = null;

    public interface DeleteMoveLayout {
        void onDeleteMoveLayout(int identity);
    }

}
