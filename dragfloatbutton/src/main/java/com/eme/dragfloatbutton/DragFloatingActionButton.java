package com.eme.dragfloatbutton;

import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Context;
import android.support.design.widget.FloatingActionButton;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.animation.DecelerateInterpolator;

import com.eme.dragfloatbutton.utils.DisplayUtil;

/**
 * Created by eme on 2017/4/18.
 */

public class DragFloatingActionButton extends FloatingActionButton {
    private int screenWidth;
    private int screenHeight;
    private int screenWidthHalf;
    private int statusHeight;
    private int NavigationBarHeight;

    public DragFloatingActionButton(Context context) {
        super(context);
        init();
    }

    public DragFloatingActionButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public DragFloatingActionButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {

        screenWidth = DisplayUtil.getScreenWidth(getContext());
        Log.e("屏幕宽度",screenWidth+"");
        screenWidthHalf = screenWidth / 2;
        screenHeight = DisplayUtil.getScreenHeight(getContext());
        Log.e("屏幕高度度",screenHeight+"");
        statusHeight = DisplayUtil.getStatusHeight((Activity) getContext());
        Log.e("状态栏宽度",statusHeight+"");
        NavigationBarHeight = DisplayUtil.getNavigationBarHeight((Activity) getContext());
        Log.e("导航栏宽度",NavigationBarHeight+"");

    }

    private int lastX;
    private int lastY;
    private boolean isDrag;

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        int rawX = (int) ev.getRawX();
        int rawY = (int) ev.getRawY();


        switch (ev.getAction() & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_DOWN:
                isDrag = false;
                getParent().requestDisallowInterceptTouchEvent(true);
                lastX = rawX;
                lastY = rawY;
                break;
            case MotionEvent.ACTION_MOVE:
                isDrag = true;
                //计算手指移动了多少
                int dx = rawX -lastX;
                int dy = rawY -lastY;

                int l = getLeft() + dx;
                int b = getBottom() + dy;
                int r = getRight() + dx;
                int t = getTop() + dy;


                if (l < 0) {
                    l = 0;
                    r = l + getWidth();
                }
                if (t < 0) {
                    t = 0;
                    b = t +getHeight();
                }
                if (r > screenWidth) {
                    r = screenWidth;
                    l = r - getWidth();
                }
                if (b > screenHeight) {
                    b = screenHeight;
                    t = b - getHeight();
                }

                layout(l,t,r,b);
                lastX = (int) ev.getRawX();
                lastY = (int) ev.getRawY();
                postInvalidate();
              /*  //这里修复一些华为手机无法触发点击事件的问题
                int distance = (int) Math.sqrt(dx * dx + dy * dy);
                if (distance == 0){
                    isDrag = false;
                    break;
                }

                float x = getX()+dx;
                float y = getY() +dy;
                //检测是否到达边缘，左上右下
                x=x<0?0:x>screenWidth-getWidth()?screenWidth-getWidth():x;
                y=y<0?0:y>screenHeight-getHeight()?screenHeight-getHeight()-getBottom():y;
              // y = y<statusHeight? statusHeight:y>screenHeight- getHeight()?screenHeight - getHeight()-NavigationBarHeight:y-NavigationBarHeight;

                setX(x);
                setY(y);

                lastX = rawX;
                lastY = rawY;*/
                break;
            case MotionEvent.ACTION_UP:
                if (isDrag) {
                    //恢复按压效果
                    setPressed(false);
                    if (rawX >screenWidthHalf ){
                        animate().setInterpolator(new DecelerateInterpolator())
                                .setDuration(200)
                                .xBy(screenWidth -getWidth() -getX())
                                .start();
                    }else {
                        ObjectAnimator oa = ObjectAnimator.ofFloat(this,"x",getX(),0);
                        oa.setInterpolator(new DecelerateInterpolator());
                        oa.setDuration(200);
                        oa.start();
                    }
                }
                break;




        }
        return isDrag || super.onTouchEvent(ev);
    }
}
