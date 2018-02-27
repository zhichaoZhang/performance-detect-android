package com.joye.performance_detection.float_window;

import android.content.Context;
import android.view.WindowManager;

/**
 * 悬浮视图实现，api版本为14-23
 * <p>
 * Created by joye on 2018/2/26.
 */

public class FloatViewCompatV14 extends IFloatView {

    public FloatViewCompatV14(Context appContext) {
        super(appContext);
        this.mLp.type = WindowManager.LayoutParams.TYPE_PRIORITY_PHONE;
    }

    @Override
    public void checkPermission(PermissionCheckListener permissionCheckListener) {
        if (permissionCheckListener == null) {
            throw new IllegalArgumentException("the param 'permissionCheckListener' must not be null.");
        }
        permissionCheckListener.onSuccess();
    }
}
