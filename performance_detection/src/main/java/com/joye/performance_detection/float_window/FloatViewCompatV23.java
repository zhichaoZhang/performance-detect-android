package com.joye.performance_detection.float_window;

import android.content.Context;
import android.view.WindowManager;

/**
 * 悬浮视图实现，api版本为23-26
 * <p>
 * Created by joye on 2018/2/26.
 */

public class FloatViewCompatV23 extends IFloatView {
    private Context mContext;

    public FloatViewCompatV23(Context appContext) {
        super(appContext);
        this.mContext = appContext;
        this.mLp.type = WindowManager.LayoutParams.TYPE_PRIORITY_PHONE;
    }

    @Override
    public void checkPermission(PermissionCheckListener permissionCheckListener) {
        if (permissionCheckListener == null) {
            throw new IllegalArgumentException("the param 'permissionCheckListener' must not be null.");
        }
        boolean hasPermission = AlertWindowPermissionUtil.hasPermission(mContext);
        if (!hasPermission) {
            RequestPermissionActivity.requestPermission(mContext, permissionCheckListener);
        } else {
            permissionCheckListener.onSuccess();
        }
    }
}
