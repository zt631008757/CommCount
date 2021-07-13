package com.android.diandezhun.ui.view;

import android.content.Context;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.Region;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

import androidx.annotation.Nullable;

import com.android.baselibrary.interface_.CommCallBack;
import com.android.baselibrary.tool.Log;
import com.android.diandezhun.bean.Count_DetailInfo;
import com.android.diandezhun.bean.ImageRec_CircleInfo;
import com.android.diandezhun.util.CommCountUtil;

/**
 * 圆圈容器
 */
public class CommCount_CircleContainer extends RelativeLayout {

    Context mContext;

    public CommCount_CircleContainer(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;
    }

    public CommCount_CircleContainer(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mContext = context;
    }

    Count_DetailInfo countDetailInfo;   //数数量信息
    CommCallBack callBack;
    //    Point leftTop;
//    Point rightBottom;
    public int border_type = 0;
    Region region = new Region();

    public void init(Count_DetailInfo countDetailInfo) {
        this.countDetailInfo = countDetailInfo;
    }

    public void setCallBack(CommCallBack callBack) {
        this.callBack = callBack;
    }

    //重绘小球
    public void drawCircles(Point leftTop, Point rightBottom) {
        Rect rect = new Rect(leftTop.x, leftTop.y, rightBottom.x, rightBottom.y);
        region.set(rect);
        drawCircle();
    }

    public void drawCircles(Path path) {
        //构建椭圆path
        Rect rect = new Rect(0, 0, getWidth(), getHeight());
        //构建Region
        region.setPath(path, new Region(rect));
        drawCircle();
    }

    private void drawCircle() {
        removeAllViews();
        for (ImageRec_CircleInfo circleInfo : countDetailInfo.circles) {
            Point target = new Point((int) (circleInfo.x / countDetailInfo.scale), (int) (circleInfo.y / countDetailInfo.scale));
            //判断是否在矩形框内， 并且半径大于等于 精度值
//            int radius = (int) (circleInfo.r / 2);
//            if (PointCalculateUtil.isInRect(leftTop, rightBottom, target) && radius >= countDetailInfo.mostRadius / 2) {
//                if (!circleInfo.isDelete) {
//                    Image_CirclesView circlesView = new Image_CirclesView(mContext, countDetailInfo, circleInfo);
//                    addView(circlesView);
//                }
//            }
            if (region.contains(target.x, target.y) && circleInfo.r >= countDetailInfo.mostRadius) {
                if (!circleInfo.isDelete) {
                    Image_CirclesView circlesView = new Image_CirclesView(mContext, countDetailInfo, circleInfo);
                    addView(circlesView);
                }
            }
        }
        if (callBack != null) {
            callBack.onResult(getChildCount());
        }
    }

    public void clearCircle() {
        removeAllViews();
        if (callBack != null) {
            callBack.onResult(getChildCount());
        }
    }

    //点击控件
    public void click(float x, float y) {
        boolean isIncircle = false;
        Point target = new Point((int) x, (int) y);
        for (ImageRec_CircleInfo circleInfo : countDetailInfo.circles) {
            Point leftTop = new Point((int) ((circleInfo.x - circleInfo.r / 2) / countDetailInfo.scale), (int) ((circleInfo.y - circleInfo.r / 2) / countDetailInfo.scale));
            Point rightBottom = new Point((int) ((circleInfo.x + circleInfo.r / 2) / countDetailInfo.scale), (int) ((circleInfo.y + circleInfo.r / 2) / countDetailInfo.scale));
//            int radius = (int) (circleInfo.r / 2);
            if (CommCountUtil.isInRect(leftTop, rightBottom, target) && circleInfo.r >= countDetailInfo.mostRadius) {
                //当前有点
                if (circleInfo.isDelete) {
                    //有点隐藏了， 就显示出来
//                    circleInfo.isDelete = false;
                } else {
                    //有点显示中，就隐藏
                    circleInfo.isDelete = true;
                    isIncircle = true;
                }
            }
        }
        Log.i("isIncircle：" + isIncircle);
        if (!isIncircle) {
            //在圈外， 就新增
            addCircle(x, y);
        }
        drawCircle();
    }

    //添加圆圈
    private void addCircle(float x, float y) {
        ImageRec_CircleInfo circleInfo = new ImageRec_CircleInfo();
        circleInfo.isAuto = false;
        circleInfo.isDelete = false;
        circleInfo.x = (int) (x * countDetailInfo.scale);
        circleInfo.y = (int) (y * countDetailInfo.scale);
        circleInfo.r = countDetailInfo.mostRadius;
        Log.i("添加圆点 半径：" + circleInfo.r);

        countDetailInfo.circles.add(circleInfo);
    }

    //删除当前小球
    private void deleteCircle(ImageRec_CircleInfo circleInfo) {
        circleInfo.isDelete = true;
        drawCircle();
    }

}
