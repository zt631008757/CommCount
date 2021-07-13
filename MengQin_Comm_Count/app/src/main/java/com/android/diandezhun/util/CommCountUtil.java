package com.android.diandezhun.util;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.Point;

import androidx.appcompat.app.AppCompatActivity;

import com.android.baselibrary.tool.CommLoading;
import com.android.baselibrary.tool.Log;
import com.android.diandezhun.ui.activity.ImageRecognitionActivity;
import com.luck.picture.lib.tools.BitmapUtils;

import java.io.File;
import java.util.List;

/**
 * 坐标点计算
 */
public class CommCountUtil {


    //计算目标点 是否在矩形框内
    public static boolean isInRect(Point leftTop, Point rightBottom, Point target) {
        if (target.x > leftTop.x && target.x < rightBottom.x
                & target.y > leftTop.y && target.y < rightBottom.y
        ) {
            return true;
        } else {
            return false;
        }
    }

    public static Point[] getRect(List<Point> pointList) {
        int minX = -1;
        int minY = -1;
        int maxX = -1;
        int maxY = -1;
        for (Point point : pointList) {
            if (minX == -1) {
                minX = point.x;
                minY = point.y;
                maxX = point.x;
                maxY = point.y;
            }
            if (point.x < minX) {
                minX = point.x;
            }
            if (point.y < minY) {
                minY = point.y;
            }
            if (point.x > maxX) {
                maxX = point.x;
            }
            if (point.y > maxY) {
                maxY = point.y;
            }
        }
        Point leftTop = new Point(minX, minY);
        Point rightBottom = new Point(maxX, maxY);
        return new Point[]{leftTop, rightBottom};
    }

    //图片裁剪
    public static void crop(Context mContext, File uploadFile, int previewviewWidth, int previewviewHeight) {
        long beginTime = System.currentTimeMillis();
        //旋转图片
        Log.i("开始裁剪");
        Bitmap bitmap_old = BitmapFactory.decodeFile(uploadFile.getPath());

        //裁剪图片
        int old_width = Math.min(bitmap_old.getWidth(), bitmap_old.getHeight());
        int old_height = Math.max(bitmap_old.getWidth(), bitmap_old.getHeight());

        //计算照片与屏幕的缩放比例
        double scale_width = (double) old_width / previewviewWidth;
        double scale_height = (double) old_height / previewviewHeight;

        double scale = Math.min(scale_width, scale_height);
        int new_Width = (int) (previewviewWidth * scale);
        int new_Height = (int) (previewviewHeight * scale);

        int left = (old_width - new_Width) / 2;
        int top = 0;

        Log.i("裁剪前  getWidth:" + old_width + " getHeight:" + old_height + " scale:" + scale);
        Bitmap bmp = Bitmap.createBitmap(bitmap_old, left, top, new_Width, new_Height, null, false);
        Log.i("裁剪后  getWidth:" + bmp.getWidth() + " getHeight:" + bmp.getHeight());
        BitmapUtils.saveBitmapFile(bmp, uploadFile);
        Log.i("结束裁剪，耗时：" + ((System.currentTimeMillis() - beginTime) / 1000d));
    }
}
