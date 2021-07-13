package com.android.diandezhun.ui.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewParent;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.android.baselibrary.tool.Log;
import com.android.baselibrary.util.Util;
import com.android.diandezhun.R;
import com.android.diandezhun.manager.ImageRecConfig;

import java.util.ArrayList;
import java.util.List;

/**
 * 手画圈 控件
 */
public class DrawView extends View {

    Context context;

    public DrawView(Context context, List<Point> defaultPoints, int left, int top, int defaultWidth, int defaultHeight) {
        super(context, null);
        this.context = context;
        this.defaultPoints = defaultPoints;
        this.defaultWidth = defaultWidth;
        this.defaultHeight = defaultHeight;

        Log.i("left:" + left + " top:" + top);

        for (Point defaultPoint : defaultPoints) {
            Point point = new Point();
            point.x = defaultPoint.x - left;
            point.y = defaultPoint.y - top;

            points.add(point);
        }
        init();
    }

    //坐标点
    List<Point> defaultPoints;
    List<Point> points = new ArrayList<>();
    Paint mPaint;
    Path mPath;
    int defaultWidth = 0;
    int defaultHeight = 0;


    private int strokeWidth = 1;       //写字笔宽度

    private void init() {
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.DITHER_FLAG);
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setFilterBitmap(true);
        mPaint.setStrokeJoin(Paint.Join.ROUND);
        mPaint.setStrokeCap(Paint.Cap.ROUND);
        mPaint.setStrokeWidth(strokeWidth);
        mPaint.setColor(Color.WHITE);
        if (mPath == null) {
            mPath = new Path();
        }
    }

    public Path getCurrentPointList() {
        int margin = Util.dip2px(context, 13);
        MoveLayout moveLayout = (MoveLayout) getParent().getParent().getParent().getParent();
        int left = moveLayout.getLeft() + margin;
        int top = moveLayout.getTop() + margin;

        double scaleX = (double) getWidth() / (double) defaultWidth;
        double scaleY = (double) getHeight() / (double) defaultHeight;
        Path path = new Path();
        for (int i = 0; i < points.size(); i++) {
            Point currentPoint = points.get(i);
//            Log.i("x:"+ currentPoint.x + "  y:"+ currentPoint.y);
            double x = (currentPoint.x * scaleX) + left;
            double y = (currentPoint.y * scaleY) + top;

            if (i == 0) {
                path.moveTo((int) x, (int) y);
            } else {
                path.lineTo((int) x, (int) y);
            }
        }
        path.close();
        return path;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        mPath.reset();

        mPath.moveTo(0, 0);
        mPath.lineTo(getWidth(), 0);
        mPath.lineTo(getWidth(), getHeight());
        mPath.lineTo(0, getHeight());
        mPath.close();

        mPath.setFillType(Path.FillType.EVEN_ODD);
        double scaleX = (double) getWidth() / (double) defaultWidth;
        double scaleY = (double) getHeight() / (double) defaultHeight;

        mPath.moveTo((int) (points.get(0).x * scaleX), (int) (points.get(0).y * scaleY));
        for (int i = 0; i < points.size(); i++) {
            if (i > 0) {
                Point point = points.get(i);
                mPath.lineTo((int) (point.x * scaleX), (int) (point.y * scaleY));
            }
        }
        mPath.close();
        mPaint.setColor(ImageRecConfig.getFrame_BgColor(context));
        canvas.drawPath(mPath, mPaint);
    }
}
