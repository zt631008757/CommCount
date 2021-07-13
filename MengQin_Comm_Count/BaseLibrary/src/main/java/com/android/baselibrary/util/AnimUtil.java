package com.android.baselibrary.util;

import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;

import com.android.baselibrary.interface_.CommCallBack;


public class AnimUtil {

    static int animTime = 150;

    public static void enterFromRight(View view) {
        if (view == null) return;
        TranslateAnimation ta = new TranslateAnimation(Animation.RELATIVE_TO_PARENT, 1, Animation.RELATIVE_TO_PARENT, 0, Animation.RELATIVE_TO_SELF, 0, Animation.RELATIVE_TO_SELF, 0);
        ta.setDuration(animTime);
        view.startAnimation(ta);
    }

    public static void outToRight(View view, final CommCallBack callBack) {
        if (view == null) return;
        TranslateAnimation ta = new TranslateAnimation(Animation.RELATIVE_TO_PARENT, 0, Animation.RELATIVE_TO_PARENT, 1, Animation.RELATIVE_TO_SELF, 0, Animation.RELATIVE_TO_SELF, 0);
        ta.setDuration(animTime);
        ta.setAnimationListener(new Animation.AnimationListener() {

            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                if (callBack != null)
                    callBack.onResult(null);
            }
        });
        view.startAnimation(ta);
    }

    public static void enterFromTop(View view) {
        if (view == null) return;
        TranslateAnimation ta = new TranslateAnimation(Animation.RELATIVE_TO_PARENT, 0, Animation.RELATIVE_TO_PARENT, 0, Animation.RELATIVE_TO_SELF, -1, Animation.RELATIVE_TO_SELF, 0);
        ta.setDuration(animTime);
        view.startAnimation(ta);
    }

    public static void outToTop(View view, final CommCallBack callBack) {
        if (view == null) return;
        TranslateAnimation ta = new TranslateAnimation(Animation.RELATIVE_TO_PARENT, 0, Animation.RELATIVE_TO_PARENT, 0, Animation.RELATIVE_TO_SELF, 0, Animation.RELATIVE_TO_SELF, -1);
        ta.setDuration(animTime);
        ta.setAnimationListener(new Animation.AnimationListener() {

            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                if (callBack != null)
                    callBack.onResult(null);
            }
        });
        view.startAnimation(ta);
    }

    public static void enterFromBottom(View view) {
        if (view == null) return;
        TranslateAnimation ta = new TranslateAnimation(Animation.RELATIVE_TO_PARENT, 0, Animation.RELATIVE_TO_PARENT, 0, Animation.RELATIVE_TO_SELF, 1, Animation.RELATIVE_TO_SELF, 0);
        ta.setDuration(animTime);
        view.startAnimation(ta);
    }

    public static void outToBottom(View view, final CommCallBack callBack) {
        if (view == null) return;
        TranslateAnimation ta = new TranslateAnimation(Animation.RELATIVE_TO_PARENT, 0, Animation.RELATIVE_TO_PARENT, 0, Animation.RELATIVE_TO_SELF, 0, Animation.RELATIVE_TO_SELF, 1);
        ta.setDuration(animTime);
        ta.setAnimationListener(new Animation.AnimationListener() {

            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                if (callBack != null)
                    callBack.onResult(null);
            }
        });
        view.startAnimation(ta);
    }

    public static void fadeIn(View view) {
        if (view == null) return;
        AlphaAnimation aa = new AlphaAnimation(0, 1);
        aa.setDuration(animTime);
        view.startAnimation(aa);
    }

    public static void fadeOut(View view, final CommCallBack callBack) {
        if (view == null) return;
        AlphaAnimation aa = new AlphaAnimation(1, 0);
        aa.setDuration(animTime);
        aa.setFillAfter(true);
        aa.setAnimationListener(new Animation.AnimationListener() {

            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                if (callBack != null)
                    callBack.onResult(null);
            }
        });
        view.startAnimation(aa);
    }
}
