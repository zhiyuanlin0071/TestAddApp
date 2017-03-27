package com.mktech.testaddapp;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Instrumentation;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.view.ViewCompat;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.ScaleAnimation;

/**
 * Created by ken on 2017/2/9.
 */
public class CommonMethods {

    private static final String TAG = CommonMethods.class.getSimpleName();

    public static SparseArray<Integer> createOptionItem(int resID, int strID) {
        SparseArray<Integer> sparseArray = new SparseArray<>();
        sparseArray.put(Constants.KEY_ITEM_ICON, resID);
        sparseArray.put(Constants.KEY_ITEM_NAME, strID);
        return sparseArray;
    }

    public static void startApp(Context context, final String packageName, final String className) {
        ComponentName component = new ComponentName(packageName, className);
        Intent intent = new Intent();
        intent.setComponent(component);
        context.startActivity(intent);
    }

    public static void simulateKeystroke(final int KeyCode) {
        new Thread(new Runnable() {
            public void run() {
                new Thread(new Runnable() {
                    public void run() {
                        try {
                            Instrumentation inst = new Instrumentation();
                            inst.sendKeyDownUpSync(KeyCode);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }).start();

                try {
                    Instrumentation inst = new Instrumentation();
                    inst.sendKeyDownUpSync(KeyCode);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    public static void zoomOut(View view, float size) {
        if (view == null) {
            return;
        }
        if (Build.VERSION.SDK_INT >= 21) {
            ViewCompat.animate(view).scaleX(size).scaleY(size).translationZ(1).start();
        } else {
            ViewCompat.animate(view).scaleX(size).scaleY(size).start();
            view.bringToFront();
            ViewGroup parent = (ViewGroup) view.getParent();
            if (parent != null) {
                parent.requestLayout();
                parent.invalidate();
            }
        }
    }

    public static void zoomIn(View view, float size) {
        if (view == null) {
            return;
        }
        if (Build.VERSION.SDK_INT >= 21) {
            ViewCompat.animate(view).scaleX(size).scaleY(size).translationZ(0).start();
        } else {
            ViewCompat.animate(view).scaleX(size).scaleY(size).start();
            ViewGroup parent = (ViewGroup) view.getParent();
            if (parent != null) {
                parent.requestLayout();
                parent.invalidate();
            }
        }
    }

    public static void scaleView(View view, float fromX, float toX, float fromY, float toY, int duration) {
        AnimatorSet animatorSet = new AnimatorSet();
        ObjectAnimator scaleX = ObjectAnimator.ofFloat(view, "scaleX", fromX, toX);
        ObjectAnimator scaleY = ObjectAnimator.ofFloat(view, "scaleY", fromY, toY);
        animatorSet.setDuration(duration);
        animatorSet.setInterpolator(new LinearInterpolator());
        animatorSet.play(scaleX).with(scaleY);
        animatorSet.start();
    }

    public static void scaleAnim(View view, float fromX, float toX, float fromY, float toY, int duration) {
        ScaleAnimation animation = new ScaleAnimation(fromX, toX, fromY, toY, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        animation.setDuration(duration);
        animation.setFillAfter(true);
        view.startAnimation(animation);
    }

    public static boolean isAppInstalled(Context context, String packageName) {
        PackageInfo packageInfo;
        try {
            packageInfo = context.getPackageManager().getPackageInfo(packageName, 0);
        } catch (PackageManager.NameNotFoundException e) {
            packageInfo = null;
            e.printStackTrace();
        }

        if (packageInfo == null) {
            return false;
        } else {
            return true;
        }
    }

    /**
     * @param context
     * @param packageName
     * @return  if null, means the app not installed, else we can start app by "startActivity(intent);"
     */
    public static Intent getAppLaunchIntent(Context context, String packageName) {
        Intent intent = context.getPackageManager().getLaunchIntentForPackage(packageName);
        return intent;
    }

    public static void setMargins (View view, int left, int top, int right, int bottom) {
        if (view.getLayoutParams() instanceof ViewGroup.MarginLayoutParams) {
            ViewGroup.MarginLayoutParams p = (ViewGroup.MarginLayoutParams) view.getLayoutParams();
            p.setMargins(left, top, right, bottom);
            view.requestLayout();
        }
    }
}
