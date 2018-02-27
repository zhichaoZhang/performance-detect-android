package com.joye.performance_detection.float_window;

import android.content.Context;
import android.view.WindowManager;

/**
 * 小米设备单独处理
 * <p>
 * Created by joye on 2018/2/26.
 */

public class FloatViewCompatMiui extends IFloatView {
    private Context mContext;

    public FloatViewCompatMiui(Context appContext) {
        super(appContext);
        this.mContext = appContext;
        this.mLp.type = WindowManager.LayoutParams.TYPE_PRIORITY_PHONE;
    }

    @Override
    void checkPermission(PermissionCheckListener permissionCheckListener) {
        if (permissionCheckListener == null) {
            throw new IllegalArgumentException("the param 'permissionCheckListener' must not be null.");
        }
        boolean hasPermission = AlertWindowPermissionUtil.hasPermission(mContext);
        if (!hasPermission) {
            Miui.req(mContext, permissionCheckListener);
        } else {
            permissionCheckListener.onSuccess();
        }
    }
}
