package com.hy.materialweather.uitls;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;

/**
 * Created by Administrator on 2017/3/25.
 */

public class AnimUtils {
    public static final String TAG = AnimUtils.class.getName() + " ";

    public static final long PERFECT_MILLS = 300;
    public static final int MINI_RADIUS = 0;

    /**
     * 向四周伸张，直到完成显示。
     */
    @SuppressLint("NewApi")
    public static Animator show(View myView, float startRadius, long durationMills) {
        if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.LOLLIPOP) {
            myView.setVisibility(View.VISIBLE);
            Utils.d(TAG + "SDK动画要求不达到");
            return null;
        }

        int cx = (myView.getLeft() + myView.getRight()) / 2;
        int cy = (myView.getTop() + myView.getBottom()) / 2;

        int w = myView.getWidth();
        int h = myView.getHeight();

        // 勾股定理 & 进一法
        int finalRadius = (int) Math.sqrt(w * w + h * h) + 1;

        Animator anim = ViewAnimationUtils.createCircularReveal(myView, cx, cy, startRadius, finalRadius);
        myView.setVisibility(View.VISIBLE);
        anim.setDuration(durationMills);
        anim.start();
        return anim;
    }

    /**
     * 由满向中间收缩，直到隐藏。
     */
    @SuppressLint("NewApi")
    public static Animator hide(final View myView, float endRadius, long durationMills) {
        if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.LOLLIPOP) {
            myView.setVisibility(View.INVISIBLE);
            Utils.d(TAG + "SDK动画要求不达到");
            return null;
        }

        int cx = (myView.getLeft() + myView.getRight()) / 2;
        int cy = (myView.getTop() + myView.getBottom()) / 2;
        int w = myView.getWidth();
        int h = myView.getHeight();

        // 勾股定理 & 进一法
        int initialRadius = (int) Math.sqrt(w * w + h * h) + 1;

        Animator anim = ViewAnimationUtils.createCircularReveal(myView, cx, cy, initialRadius, endRadius);
        anim.setDuration(durationMills);
        anim.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                myView.setVisibility(View.INVISIBLE);
            }
        });

        anim.start();
        return anim;
    }

    /**
     * 从指定View开始向四周伸张(伸张颜色或图片为colorOrImageRes), 然后进入另一个Activity,
     * 返回至 @thisActivity 后显示收缩动画。
     */
    public static void startActivityForResult(
            final Activity thisActivity, final Intent intent, final Integer requestCode, final Bundle bundle,
            final View triggerView, int colorOrImageRes, final long durationMills) {

        if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.LOLLIPOP) {
            thisActivity.startActivity(intent);
            return;
        }

        int[] location = new int[2];
        triggerView.getLocationInWindow(location);
        final int cx = location[0] + triggerView.getWidth() / 2;
        final int cy = location[1] + triggerView.getHeight() / 2;
        final ImageView view = new ImageView(thisActivity);
        view.setScaleType(ImageView.ScaleType.CENTER_CROP);
        view.setImageResource(colorOrImageRes);
        final ViewGroup decorView = (ViewGroup) thisActivity.getWindow().getDecorView();
        int w = decorView.getWidth();
        int h = decorView.getHeight();
        decorView.addView(view, w, h);
        final int finalRadius = (int) Math.sqrt(w * w + h * h) + 1;
        Animator anim = ViewAnimationUtils.createCircularReveal(view, cx, cy, 0, finalRadius);
        anim.setDuration(durationMills);
        //加速插值器
        anim.setInterpolator(new AccelerateInterpolator());
        anim.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);

                if (requestCode == null) {
                    thisActivity.startActivity(intent);
                } else if (bundle == null) {
                    thisActivity.startActivityForResult(intent, requestCode);
                } else {
                    thisActivity.startActivityForResult(intent, requestCode, bundle);
                }

                // 默认渐隐过渡动画.
                thisActivity.overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);

                // 默认显示返回至当前Activity的动画.
                triggerView.postDelayed(new Runnable() {
                    @Override
                    public void run() {

                        Animator anim =
                                ViewAnimationUtils.createCircularReveal(view, cx, cy, finalRadius, 0);
                        anim.setDuration(durationMills);
                        anim.addListener(new AnimatorListenerAdapter() {
                            @Override
                            public void onAnimationEnd(Animator animation) {
                                super.onAnimationEnd(animation);
                                try {
                                    decorView.removeView(view);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                        anim.start();

                    }
                }, 1000);


            }
        });
        anim.start();

    }


    /*下面的方法全是重载，用简化上面方法的构建*/

    public static void startActivityForResult(
            Activity thisActivity, Intent intent, Integer requestCode, View triggerView, int colorOrImageRes) {
        startActivityForResult(thisActivity, intent, requestCode, null, triggerView, colorOrImageRes, PERFECT_MILLS);
    }

    public static void startActivity(
            Activity thisActivity, Intent intent, View triggerView, int colorOrImageRes, long durationMills) {
        startActivityForResult(thisActivity, intent, null, null, triggerView, colorOrImageRes, durationMills);
    }

    public static void startActivity(
            Activity thisActivity, Intent intent, View triggerView, int colorOrImageRes) {
        startActivity(thisActivity, intent, triggerView, colorOrImageRes, PERFECT_MILLS);
    }

    public static void startActivity(Activity thisActivity, Class<?> targetClass, View triggerView, int colorOrImageRes) {
        startActivity(thisActivity, new Intent(thisActivity, targetClass), triggerView, colorOrImageRes, PERFECT_MILLS);
    }

    public static void show(View myView) {
        show(myView, MINI_RADIUS, PERFECT_MILLS);
    }

    public static void hide(View myView) {
        hide(myView, MINI_RADIUS, PERFECT_MILLS);
    }

    /**
     * 卡片展开动画进入新的Activity，展开收缩动画
     */
    public static void CardStartActivity(
            final Activity thisActivity, final Intent intent, final View triggerView, int colorOrImageRes, final int durationMills) {
        if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.LOLLIPOP) {
            thisActivity.startActivity(intent);
            return;
        }

        //顶层加入与设置View相同位置的ImageView
//        int[] location = new int[2];
//        triggerView.getLocationInWindow(location);
//        final int cx = location[0] + triggerView.getWidth() / 2;
//        final int cy = location[1] + triggerView.getHeight() / 2;
//        final ImageView view = new ImageView(thisActivity);
//        view.setScaleType(ImageView.ScaleType.CENTER_CROP);
//        view.setImageResource(colorOrImageRes);
//        final ViewGroup decorView = (ViewGroup) thisActivity.getWindow().getDecorView();
//        int w = decorView.getWidth();
//        int h = decorView.getHeight();
//        decorView.addView(view, w, h);

        //设置展开动画，View动画，缩放加位移，加速插值器
        AnimationSet set = new AnimationSet(true);
        ScaleAnimation scaleAnimation =
                new ScaleAnimation(1.0f,
                        Utils.getScreenDisplay(thisActivity).widthPixels / triggerView.getWidth(),
                        1.0f,
                        Utils.getScreenDisplay(thisActivity).heightPixels / triggerView.getHeight(),
                        Animation.RELATIVE_TO_SELF,
                        triggerView.getWidth() / 2,
                        Animation.RELATIVE_TO_SELF,
                        triggerView.getHeight() / 2);
//        TranslateAnimation translateAnimation =
//                new TranslateAnimation();


        set.addAnimation(scaleAnimation);
        set.setDuration(durationMills);
        set.setInterpolator(new AccelerateInterpolator());

        //动画结束时打开Activity
        set.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                thisActivity.startActivity(intent);

                // 默认渐隐过渡动画.
                thisActivity.overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);

                // 默认显示返回至当前Activity的动画.
                triggerView.postDelayed(new Runnable() {
                    @Override
                    public void run() {

//                        Animator anim =
//                                ViewAnimationUtils.createCircularReveal(view, cx, cy, finalRadius, 0);
//                        anim.setDuration(durationMills);
//                        anim.addListener(new AnimatorListenerAdapter() {
//                            @Override
//                            public void onAnimationEnd(Animator animation) {
//                                super.onAnimationEnd(animation);
//                                try {
//                                    decorView.removeView(view);
//                                } catch (Exception e) {
//                                    e.printStackTrace();
//                                }
//                            }
//                        });
//                        anim.start();

                    }
                }, 1000);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        //播放动画
        triggerView.setAnimation(set);
        set.start();
    }

}
