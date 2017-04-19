package com.eme.dragfloatbutton.utils;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.res.Resources;
import android.os.Build;
import android.os.Handler;
import android.provider.Settings;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Surface;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

/**
 * Created by eme on 2017/4/18.
 */

public class DisplayUtil {
    private static float density;
    private static float scaledDensity;
    private static final Handler handler = new Handler();

    private static void setDensity(Context context) {
        if (density <= 0 || scaledDensity <= 0) {
            DisplayMetrics dm = context.getResources().getDisplayMetrics();
            density = dm.density;
            scaledDensity  = dm.scaledDensity;
        }
    }

    /**
     * 获得屏幕的密度
     *
     *
     */

    public static float getScreenDensity(Context context) {
        setDensity(context);
        return density;
    }

    public static float getScaledDensity(Context context) {
        setDensity(context);
        return scaledDensity;
    }

    /**
     * 获得屏幕宽度
     */

    public static int getScreenWidth(Context context) {
        DisplayMetrics dm = context.getResources().getDisplayMetrics();
        density = dm.density;
        scaledDensity = dm.scaledDensity;
        int screenWidth = dm.widthPixels;
        return screenWidth;
    }

    /**
     * 获得屏幕的高度
     */

    public static int getScreenHeight(Context context) {
        DisplayMetrics dm = context.getResources().getDisplayMetrics();
        density = dm.density;
        scaledDensity = dm.scaledDensity;
        int screenHeight = dm.heightPixels;
        return screenHeight;
    }

    /**
     * 将dip转换为px
     */

    public static float dip2px(Context context ,float dip) {
        if (density <= 0) {
            setDensity(context);
        }
        return dip * density + 0.5f * (dip >= 0 ? 1 : -1);
    }

    /**
     * 将px转换为dip
     *
     * @param px
     * @return px转换为dip
     */
    public static int px2dip(Context context, int px) {
        if (density <= 0) {

            setDensity(context);
        }
        return (int) (px / density + 0.5f * (px >= 0 ? 1 : -1));
    }

    /**
     * sp 转换为 px
     *
     * @param sp
     * @return sp 转换为 px
     */
    public static int sp2px(Context context, int sp) {
        if (scaledDensity <= 0) {

            setDensity(context);
        }
        return (int) (sp * scaledDensity + 0.5f * (sp >= 0 ? 1 : -1));
    }

    /**
     * sp 转换为 px
     *
     * @param sp
     * @return sp 转换为 px
     */
    public static float sp2px(Context context, float sp) {
        if (scaledDensity <= 0) {

            setDensity(context);
        }
        return sp * scaledDensity + 0.5f * (sp >= 0 ? 1 : -1);
    }

    /**
     * px 转换为 sp
     *
     * @param px
     * @return px 转换为 sp
     */
    public static int px2sp(Context context, int px) {
        if (scaledDensity <= 0) {

            setDensity(context);
        }
        return (int) (px / scaledDensity + 0.5f * (px >= 0 ? 1 : -1));
    }

    /**
     * 获取当前屏幕旋转角度
     *
     * @param activity
     * @return 0表示是竖屏; 90表示是左横屏; 180表示是反向竖屏; 270表示是右横屏
     */
    public static int getScreemRotation(Activity activity) {
        int rotation = activity.getWindowManager().getDefaultDisplay()
                .getRotation();
        switch (rotation) {
            case Surface.ROTATION_0:
                return 0;
            case Surface.ROTATION_90:
                return 90;
            case Surface.ROTATION_180:
                return 180;
            case Surface.ROTATION_270:
                return 270;
        }
        return 0;
    }

    /**
     * 锁住屏幕，让屏幕不能旋转
     *
     * @param activity
     *            需要锁定的Activity
     */
    public static void lockActivity(Activity activity) {
        int degree = getScreemRotation(activity);
        lockActivity(activity, degree);
    }

    /**
     * 根据传入的角度设置当前屏幕的状态，使之不能旋转
     *
     * @param activity
     *            需要锁定的Activity
     * @param degree
     *            旋转的角度。一般是通过{@link #getScreemRotation(Activity)}方法获得的数据
     */
    @TargetApi(Build.VERSION_CODES.GINGERBREAD)
    public static void lockActivity(Activity activity, int degree) {

        int display = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT;
        switch (degree) {
            case 90:
                display = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE;
                break;

            case 180:
                display = ActivityInfo.SCREEN_ORIENTATION_REVERSE_PORTRAIT;
                break;

            case 270:
                display = ActivityInfo.SCREEN_ORIENTATION_REVERSE_LANDSCAPE;
                break;
        }

        activity.setRequestedOrientation(display);
    }

    /**
     * 判断系统设置是否打开了屏幕旋转
     *
     * @param context
     * @return 系统设置是否打开了屏幕旋转
     */
    public static boolean isAccelerometerRotation(Context context) {
        int flag = Settings.System.getInt(context.getContentResolver(),
                Settings.System.ACCELEROMETER_ROTATION, 0);

        return flag != 0;
    }

    /**
     * 显示输入法
     *
     * @param view
     *            推荐将EditText传入
     */
    public static void showSoftInput(View view) {
        if (view instanceof EditText) {
            view.requestFocus();
            ((EditText) view).setCursorVisible(true);
        }
        InputMethodManager imm = (InputMethodManager) view.getContext()
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInputFromInputMethod(view.getWindowToken(),
                InputMethodManager.SHOW_IMPLICIT);
    }

    /**
     * 隐藏输入法
     *
     * @param view
     *            推荐将EditText传入
     */
    public static void hideSoftInput(View view) {
        InputMethodManager imm = (InputMethodManager) view.getContext()
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    /**
     * 延时显示输入法
     *
     * @param view
     *            推荐将EditText传入
     * @param delayMs
     *            延迟时间(毫秒)
     */
    public static void showSoftInputDelay(final View view, final long delayMs) {
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                showSoftInput(view);
            }
        }, delayMs);

    }

    /**
     * 延时隐藏输入法
     *
     * @param view
     *            推荐将EditText传入
     * @param delayMs
     *            延迟时间(毫秒)
     */
    public static void hideSoftInputDelay(final View view, final long delayMs) {
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                hideSoftInput(view);
            }
        }, delayMs);

    }

    /**
     * 获取状态栏高度
     */

    public static int getStatusHeight(Activity activity) {
       int statusHeight = -1;
        int resourceId = activity.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            statusHeight = activity.getResources().getDimensionPixelSize(resourceId);
        }
        return statusHeight;

    }

    public static int getNavigationBarHeight(Activity activity) {
        Resources resources = activity.getResources();
        int resourceId = resources.getIdentifier("navigation_bar_height","dimen", "android");
        int height = resources.getDimensionPixelSize(resourceId);
        Log.v("dbw", "Navi height:" + height);
        return height;
    }

}
