package com.joye.performance_detection.float_window;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;

/**
 * 悬浮视图抽象
 * 窗口默认类型为TYPE_TOAST
 * 窗口默认尺寸为WRAP_CONTENT
 * 窗口默认位置为顶部居中
 * <p>
 * Created by joye on 2018/2/26.
 */

public abstract class IFloatView {
    private WindowManager mWm;//窗口管理器
    protected WindowManager.LayoutParams mLp;//窗口布局参数
    private View mCustomView;//自定义窗口视图
    private boolean mIsFirstShow = true;//是否第一次显示

    public IFloatView(Context appContext) {
        this.mWm = (WindowManager) appContext.getSystemService(Context.WINDOW_SERVICE);
        this.mLp = new WindowManager.LayoutParams();
        this.mLp.flags = WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
        this.mLp.type = WindowManager.LayoutParams.TYPE_TOAST;
        this.mLp.width = WindowManager.LayoutParams.WRAP_CONTENT;
        this.mLp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        this.mLp.gravity = Gravity.TOP | Gravity.CENTER_HORIZONTAL;
    }

    /**
     * 设置自定义视图
     *
     * @param customView 自定义视图
     */
    void setView(View customView) {
        if (customView == null) {
            throw new IllegalArgumentException("the custom view must not be null.");
        }
        this.mCustomView = customView;
    }

    /**
     * 设置视图显示尺寸
     * 默认值为WRAP_CONTENT
     *
     * @param height 高度
     * @param width  宽度
     */
    void setSize(int height, int width) {
        this.mLp.height = height;
        this.mLp.width = width;
    }

    /**
     * 设置视图位置及偏移量
     *
     * @param gravity 视图位置 LEFT、RIGHT、BOTTOM、TOP
     * @param xOffset 横向偏移量
     * @param yOffset 纵向偏移量
     */
    void setGravity(int gravity, int xOffset, int yOffset) {
        this.mLp.gravity = gravity;
        this.mLp.x = xOffset;
        this.mLp.y = yOffset;
    }

    /**
     * 更新悬浮窗位置
     *
     * @param x 横坐标
     * @param y 纵坐标
     */
    void updateLocation(int x, int y) {
        this.mLp.gravity = Gravity.NO_GRAVITY;
        this.mLp.x = x;
        this.mLp.y = y;
        mWm.updateViewLayout(mCustomView, mLp);
    }

    /**
     * 获取当前视图X坐标
     *
     * @return X坐标
     */
    public int getX() {
        return mLp.x;
    }

    /**
     * 获取当前视图Y坐标
     *
     * @return Y坐标
     */
    public int getY() {
        return mLp.y;
    }

    /**
     * 显示悬浮窗
     * 如果是第一次显示，需要将视图添加到WindowManager中。否则设置视图VISIBLE
     */
    void show() {
        if (mCustomView == null) {
            return;
        }
        if (mIsFirstShow) {
            mWm.addView(mCustomView, mLp);
            mIsFirstShow = false;
        }
        mCustomView.setVisibility(View.VISIBLE);
    }

    /**
     * 隐藏悬浮窗
     * 设置视图INVISIBLE
     */
    void hide() {
        if (mCustomView == null) {
            return;
        }
        mCustomView.setVisibility(View.GONE);
    }

    /**
     * 销毁悬浮窗，即从WindowManager中移除
     */
    void dismiss() {
        if (mCustomView == null) {
            return;
        }
        mWm.removeView(mCustomView);
        mIsFirstShow = true;
    }

    /**
     * 获取当前悬浮视图
     *
     * @return View
     */
    public View getView() {
        return mCustomView;
    }

    /**
     * 检查悬浮窗权限
     *
     * @param permissionCheckListener 检查结果回调
     */
    abstract void checkPermission(PermissionCheckListener permissionCheckListener);

    /**
     * 悬浮窗权限检查回调
     */
    interface PermissionCheckListener {
        /**
         * 获取权限成功
         */
        void onSuccess();

        /**
         * 获取权限失败
         */
        void onFail();
    }
}
