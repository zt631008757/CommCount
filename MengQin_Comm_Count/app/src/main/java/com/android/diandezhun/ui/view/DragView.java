package com.android.diandezhun.ui.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.diandezhun.R;
import com.android.diandezhun.manager.ImageRecConfig;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Robert on 2017/6/21.
 */

public class DragView extends RelativeLayout implements MoveLayout.DeleteMoveLayout {

    private int mSelfViewWidth = 0;
    private int mSelfViewHeight = 0;

    private Context mContext;

    /**
     * the identity of the moveLayout
     */
    private int mLocalIdentity = 0;

    public List<MoveLayout> mMoveLayoutList;

    public List<MoveLayout> getmMoveLayoutList() {
        return mMoveLayoutList;
    }


    public MoveLayout getMoveLayout()
    {
        if(mMoveLayoutList!=null && mMoveLayoutList.size()>0)
        {
            return mMoveLayoutList.get(0);
        }
        else
        {
            return null;
        }
    }

    /*
     * 拖拽框最小尺寸
     */
    private int mMinHeight = 120;
    private int mMinWidth = 180;

    private boolean mIsAddDeleteView = false;
    private TextView deleteArea;

    private int DELETE_AREA_WIDTH = 180;
    private int DELETE_AREA_HEIGHT = 90;


    public DragView(Context context) {
        super(context);
        init(context, this);
    }

    public DragView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, this);
    }

    public DragView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, this);
    }

    private void init(Context c, DragView thisp) {
        mContext = c;
        mMoveLayoutList = new ArrayList<>();
    }

    Paint paint = new Paint();

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //  Log.e(TAG, "onDraw: height=" + getHeight());
        mSelfViewWidth = getWidth();
        mSelfViewHeight = getHeight();
        if (mMoveLayoutList != null) {
            int count = mMoveLayoutList.size();
            for (int a = 0; a < count; a++) {
                mMoveLayoutList.get(a).setViewWidthHeight(mSelfViewWidth, mSelfViewHeight);
                mMoveLayoutList.get(a).setDeleteWidthHeight(DELETE_AREA_WIDTH, DELETE_AREA_HEIGHT);
            }
        }

        //画镂空背景
        for (MoveLayout moveLayout : mMoveLayoutList) {
            int canvasWidth = canvas.getWidth();
            int canvasHeight = canvas.getHeight();
            int layerId = canvas.saveLayer(0, 0, canvasWidth, canvasHeight, null, Canvas.ALL_SAVE_FLAG);
            //正常绘制背景
            paint.setColor(ImageRecConfig.getFrame_BgColor(mContext));
            canvas.drawRect(0, 0, mSelfViewWidth, mSelfViewHeight, paint);
            //使用CLEAR作为PorterDuffXfermode绘制蓝色的矩形
            paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
            paint.setColor(0xFF66AAFF);
            canvas.drawRect(moveLayout.getLeftTop().x, moveLayout.getLeftTop().y, moveLayout.getRightBottom().x, moveLayout.getRightBottom().y, paint);
            //最后将画笔去除Xfermode
            paint.setXfermode(null);
            canvas.restoreToCount(layerId);

        }


    }

    /**
     * call set Min height before addDragView
     *
     * @param height
     */
    private void setMinHeight(int height) {
        mMinHeight = height;
    }

    /**
     * call set Min width before addDragView
     *
     * @param width
     */
    private void setMinWidth(int width) {
        mMinWidth = width;
    }

    public void addDragView(View selfView, int left, int top, int right, int bottom, boolean isFixedSize) {
        addDragView(selfView, left, top, right, bottom, isFixedSize, mMinWidth, mMinHeight);
    }

    /**
     * 每个moveLayout都可以拥有自己的最小尺寸
     */
    public void addDragView(int resId, int left, int top, int right, int bottom, boolean isFixedSize, boolean whitebg, int minwidth, int minheight) {
        LayoutInflater inflater2 = LayoutInflater.from(mContext);
        View selfView = inflater2.inflate(resId, null);
        addDragView(selfView, left, top, right, bottom, isFixedSize, minwidth, minheight);
    }

    /**
     * 每个moveLayout都可以拥有自己的最小尺寸
     */
    public void addDragView(View selfView, int left, int top, int right, int bottom, boolean isFixedSize, int minwidth, int minheight) {

        MoveLayout moveLayout = new MoveLayout(mContext);

//        moveLayout.setClickable(true);
        moveLayout.setMinHeight(minheight);
        moveLayout.setMinWidth(minwidth);
        int tempWidth = right - left;
        int tempHeight = bottom - top;
        if (tempWidth < minwidth) tempWidth = minwidth;
        if (tempHeight < minheight) tempHeight = minheight;

        //set postion
        LayoutParams lp = new LayoutParams(tempWidth, tempHeight);
        lp.setMargins(left, top, 0, 0);
        moveLayout.setLayoutParams(lp);

        //add sub view (has click indicator)
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View dragSubView = inflater.inflate(R.layout.view_drag_sub, null);
        LinearLayout addYourViewHere = (LinearLayout) dragSubView.findViewById(R.id.add_your_view_here);
        LinearLayout.LayoutParams lv = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        if (selfView != null) {
            addYourViewHere.addView(selfView, lv);
        }

        moveLayout.addView(dragSubView);

        //set fixed size
        moveLayout.setFixedSize(isFixedSize);

        moveLayout.setIdentity(mLocalIdentity++);

        addView(moveLayout);

        mMoveLayoutList.add(moveLayout);
    }

    public void clearView() {
        removeAllViews();
        mMoveLayoutList.clear();
    }

    //拖拽事件
    public void onDrag(int x, int y, int dx, int dy) {
        for (MoveLayout moveLayout : mMoveLayoutList) {
            moveLayout.onDrag(x, y, dx, dy);
        }
    }


    @Override
    public void onDeleteMoveLayout(int identity) {
        int count = mMoveLayoutList.size();
        for (int a = 0; a < count; a++) {
            if (mMoveLayoutList.get(a).getIdentity() == identity) {
                //delete
                removeView(mMoveLayoutList.get(a));
            }
        }
    }


}
