package com.joye.performance_detection.float_window;

import android.content.Context;
import android.os.Build;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

/**
 * 悬浮窗抽象类
 * <p>
 * Created by joye on 2018/2/26.
 */

public class FloatWindow {
    private static final String TAG = "FloatWindow";
    private IFloatView mFloatView;

    public FloatWindow(Context context, FloatWindowManager.Builder builder) {
        this.mFloatView = create(context);
        mFloatView.setView(builder.getCustomView());
        mFloatView.setSize(builder.getHeight(), builder.getWidth());
        mFloatView.setGravity(builder.getGravity(), builder.getxOffset(), builder.getyOffset());
        initTouchEvent();
    }

    public void show() {
        mFloatView.checkPermission(new IFloatView.PermissionCheckListener() {
            @Override
            public void onSuccess() {
                mFloatView.show();
            }

            @Override
            public void onFail() {
                Log.e(TAG, "onFail: request float window permission fail.");
            }
        });
    }

    public void hide() {
        mFloatView.hide();
    }

    public void dismiss() {
        mFloatView.dismiss();
    }

    public View getView() {
        return mFloatView.getView();
    }

    //初始化触摸事件，支持悬浮窗拖拽
    private void initTouchEvent() {
        View view = mFloatView.getView();
        view.setOnTouchListener(new View.OnTouchListener() {
            float lastX, lastY, changeX, changeY;
            int newX, newY;

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        lastX = event.getRawX();
                        lastY = event.getRawY();
                        break;
                    case MotionEvent.ACTION_MOVE:
                        changeX = event.getRawX() - lastX;
                        changeY = event.getRawY() - lastY;
                        newX = (int) (mFloatView.getX() + changeX);
                        newY = (int) (mFloatView.getY() + changeY);
                        mFloatView.updateLocation(newX, newY);
                        lastX = event.getRawX();
                        lastY = event.getRawY();
                        break;
                }
                return false;
            }
        });
    }

    private IFloatView create(Context context) {
        IFloatView floatView;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            floatView = new FloatViewCompatV26(context);
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            floatView = new FloatViewCompatV23(context);
        } else if (Miui.rom()) {
            floatView = new FloatViewCompatMiui(context);
        } else {
            floatView = new FloatViewCompatV14(context);
        }
        return floatView;
    }
}
