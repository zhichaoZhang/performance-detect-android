package com.joye.performance_detection.float_window;

import android.content.Context;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;

import java.util.HashMap;
import java.util.Map;

/**
 * 悬浮窗管理类
 * <p>
 * Created by joye on 2018/2/26.
 */

public class FloatWindowManager {
    private static final String TAG = "FloatWindowManager";
    private static final String DEFAULT_FLOAT_WINDOW_TAG = "default_float_window_tag";
    private static Map<String, FloatWindow> mFloatWindowMap = new HashMap<>();

    public static FloatWindow get() {
        return get(DEFAULT_FLOAT_WINDOW_TAG);
    }

    public static FloatWindow get(String tag) {
        return mFloatWindowMap.get(tag);
    }

    /**
     * 悬浮窗口构建器
     */
    public static class Builder {
        private View customView;
        private int width = WindowManager.LayoutParams.WRAP_CONTENT;
        private int height = WindowManager.LayoutParams.WRAP_CONTENT;
        private int gravity = Gravity.TOP | Gravity.CENTER_HORIZONTAL;
        private int xOffset;
        private int yOffset;
        private String tag = DEFAULT_FLOAT_WINDOW_TAG;

        public View getCustomView() {
            return customView;
        }

        public int getWidth() {
            return width;
        }

        public int getHeight() {
            return height;
        }

        public int getGravity() {
            return gravity;
        }

        public int getxOffset() {
            return xOffset;
        }

        public int getyOffset() {
            return yOffset;
        }

        public Builder setView(View customView) {
            this.customView = customView;
            return this;
        }

        public Builder setSize(int width, int height) {
            this.width = width;
            this.height = height;
            return this;
        }

        public Builder setGravity(int gravity, int xOffset, int yOffset) {
            this.gravity = gravity;
            this.xOffset = xOffset;
            this.yOffset = yOffset;
            return this;
        }

        public Builder setTag(String tag) {
            this.tag = tag;
            return this;
        }

        public FloatWindow build(Context applicationContext) {
            if (mFloatWindowMap.containsKey(tag)) {
                throw new IllegalArgumentException("setTag: the tag '" + tag + "' has duplicate, you must use other tag.");
            }
            if(customView == null) {
                throw new IllegalArgumentException("the custom float view must not be null.");
            }

            FloatWindow floatWindow = new FloatWindow(applicationContext, this);
            mFloatWindowMap.put(tag, floatWindow);
            return floatWindow;
        }
    }

}
