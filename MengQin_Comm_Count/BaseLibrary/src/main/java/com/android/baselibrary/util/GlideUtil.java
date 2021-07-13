package com.android.baselibrary.util;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.transition.DrawableCrossFadeFactory;

import static com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade;

public class GlideUtil {

    public static RequestOptions getMode(Context context, Object url, int loadingImg) {
        RequestOptions options = new RequestOptions();
        options.skipMemoryCache(false);
        options.diskCacheStrategy(DiskCacheStrategy.RESOURCE);
        options.priority(Priority.NORMAL);
        options.error(loadingImg);
        options.encodeQuality(100);

        //设置占位符,默认
        options.placeholder(loadingImg);
        return options;
    }

    //传入尺寸
    public static void displayImgWithSize(Context context, Object url, ImageView imageView, int loadingImg, int width, int height) {
        try {
            RequestOptions options = getMode(context, url, loadingImg);
            options.override(width, height).diskCacheStrategy(DiskCacheStrategy.RESOURCE);

            //通过apply方法将设置的属性添加到glide
            Glide.with(context).load(url).apply(options).transition(withCrossFade()).into(imageView);
        } catch (Exception e) {

        }
    }

    public static void displayImage(Context context, Object url, ImageView imageView, int loadingImg) {
        try {
            DrawableCrossFadeFactory drawableCrossFadeFactory = new DrawableCrossFadeFactory.Builder(300).setCrossFadeEnabled(true).build();
            RequestOptions options = getMode(context, url, loadingImg);
            //通过apply方法将设置的属性添加到glide
            Glide.with(context).load(url).apply(options).transition(DrawableTransitionOptions.with(drawableCrossFadeFactory)).into(imageView);
        } catch (Exception e) {

        }
    }

    public static void displayImage(Context context, Object url, ImageView imageView, int loadingImg, int coner) {
        try {
            DrawableCrossFadeFactory drawableCrossFadeFactory = new DrawableCrossFadeFactory.Builder(300).setCrossFadeEnabled(true).build();
            RequestOptions options = getMode(context, url, loadingImg);
            options.transforms(new RoundedCorners(Util.dip2px(context, coner)));
            //通过apply方法将设置的属性添加到glide
            Glide.with(context).load(url).apply(options).transition(DrawableTransitionOptions.with(drawableCrossFadeFactory)).into(imageView);
        } catch (Exception e) {

        }
    }
}
