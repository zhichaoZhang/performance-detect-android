package com.joye.performance_detection.float_window;

import android.annotation.TargetApi;
import android.app.AppOpsManager;
import android.content.Context;
import android.os.Binder;
import android.os.Build;
import android.provider.Settings;

import java.lang.reflect.Method;

/**
 * SYSTEM_ALERT_WINDOW权限检查工具类
 * <p>
 * Created by joye on 2018/2/26.
 */

public class AlertWindowPermissionUtil {

    /**
     * 检查是否有SYSTEM_ALERT_WINDOW权限
     *
     * @param context 上下文
     * @return Boolean
     */
    public static boolean hasPermission(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            return checkAboveM(context);
        } else
            return Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT || checkBelowM(context);
    }

    /**
     * 检测通过Activity申请权限后，是否正确获取到权限
     *
     * @param context 上下文
     * @return Boolean
     */
    public static boolean hasPermissionOnActivityResult(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            return checkAboveM(context);
        } else
            return Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT || checkBelowM(context);
    }

    /**
     * 6.0系统以下权限检查，利用AppOpsManager类，反射checkOp方法判断
     * <p>
     * 理论上6.0以上系统才添加了动态权限申请，但是国内厂商系统在6.0以下就添加了权限管理。
     *
     * @param context 上下文
     * @return Boolean
     */
    static boolean checkBelowM(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            AppOpsManager appOpsManager = (AppOpsManager) context.getSystemService(Context.APP_OPS_SERVICE);
            try {
                Method checkOpMethod = AppOpsManager.class.getMethod("checkOp", int.class, int.class, String.class);
                //AppOpsManager.OP_SYSTEM_ALERT_WINDOW = 24
                int result = (int) checkOpMethod.invoke(appOpsManager, 24, Binder.getCallingUid(), context.getPackageName());
                return AppOpsManager.MODE_ALLOWED == result;
            } catch (Exception e) {
                e.printStackTrace();
            }
            return false;
        }
        return true;
    }

    /**
     * 6.0系统及以上权限检查
     *
     * @param context 上下文
     * @return Boolean
     */
    @TargetApi(Build.VERSION_CODES.M)
    static boolean checkAboveM(Context context) {
        return Settings.canDrawOverlays(context);
    }

    /**
     * Android O系统 权限检测
     *
     * @param context 上下文
     * @return Boolean
     */
    static boolean checkAboveO(Context context) {
        return false;
    }

}
