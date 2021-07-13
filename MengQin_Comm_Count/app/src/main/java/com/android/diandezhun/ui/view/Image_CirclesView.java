package com.android.diandezhun.ui.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Picture;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

import com.android.baselibrary.tool.Log;
import com.android.diandezhun.R;
import com.android.diandezhun.bean.Count_DetailInfo;
import com.android.diandezhun.bean.ImageRec_CircleInfo;
import com.android.diandezhun.manager.ImageRecConfig;

public class Image_CirclesView extends View {

    public Image_CirclesView(Context context, Count_DetailInfo countDetailInfo, ImageRec_CircleInfo circleInfo) {
        super(context);
        this.circleInfo = circleInfo;
        this.countDetailInfo = countDetailInfo;
        paint = new Paint();
        init();
    }

    Paint paint;
    ImageRec_CircleInfo circleInfo;
    Count_DetailInfo countDetailInfo;

    private void init() {
        if (ImageRecConfig.getCircleStyle(getContext()) == 0) {
            //画圆
            if (circleInfo.isAuto) {
                setBackgroundResource(R.drawable.shape_commcount_circlrbg_green);
            } else {
                setBackgroundResource(R.drawable.shape_commcount_circlrbg_red);
            }
        } else {
            //加号
            if (circleInfo.isAuto) {
                setBackgroundResource(R.drawable.ico_commcount_jiahao_green);
            } else {
                setBackgroundResource(R.drawable.ico_commcount_jiahao_red);
            }
        }
    }

//    @Override
//    public void draw(Canvas canvas) {
//        super.draw(canvas);
//
//        //画圆
//        int width = getWidth();
//        int height = getHeight();
//        if (circleInfo.isAuto) {
//            paint.setColor(ImageRecConfig.getCircleColor(getContext()));
//        } else {
//            paint.setColor(ImageRecConfig.getCircleColor_Custom(getContext()));
//        }
//
//        int radius = width / 2;
//
//        //画圆/加号
//        if (ImageRecConfig.getCircleStyle(getContext()) == 0) {
//            //画圆
//            canvas.drawCircle(width / 2, height / 2, radius, paint);
//        } else {
//            //画加号, 一横一竖
//            Bitmap mBitmap;
//            if (circleInfo.isAuto) {
//                mBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.ico_commcount_jiahao_green);
//            } else {
//                mBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.ico_commcount_jiahao_red);
//            }
//            // 指定图片绘制区域
//            Rect src = new Rect(0, 0, mBitmap.getWidth(), mBitmap.getHeight());
//            // 指定图片在屏幕上显示的区域(原图大小)
//            Rect dst = new Rect(0, 0, width, height);
//            canvas.drawBitmap(mBitmap, src, dst, null);
//        }
//    }

    @Override
    public void layout(int l, int t, int r, int b) {
        //大小为
        int radius = (int) ((circleInfo.r / 2) * countDetailInfo.radius_scale);
        int left = (int) ((circleInfo.x - radius) / countDetailInfo.scale);
        int top = (int) ((circleInfo.y - radius) / countDetailInfo.scale);
        int right = (int) ((circleInfo.x + radius) / countDetailInfo.scale);
        int bottom = (int) ((circleInfo.y + radius) / countDetailInfo.scale);
//        Log.i("radius:" + radius + " left:" + left + " top:" + top);
        super.layout(left, top, right, bottom);
    }
}
