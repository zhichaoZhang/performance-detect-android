package com.joye.performance_detection.float_window;

import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.view.WindowManager;

/**
 * 悬浮视图实现，api版本为26-
 * <p>
 * Created by joye on 2018/2/26.
 */

public class FloatViewCompatV26 extends IFloatView {
    private Context mContext;

    @RequiresApi(api = Build.VERSION_CODES.O)
    public FloatViewCompatV26(Context appContext) {
        super(appContext);
        this.mContext = appContext;
        mLp.type = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
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
