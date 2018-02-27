package com.joye.performance_detection.float_window;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;

/**
 * 申请ACTION_MANAGE_OVERLAY_PERMISSION权限的Activity
 */
public class RequestPermissionActivity extends AppCompatActivity {
    private static final int REQ_OVERLAY_PERMISSION = 0x0001;
    private static IFloatView.PermissionCheckListener mPerCheckListener;

    public static void requestPermission(Context context, IFloatView.PermissionCheckListener permissionCheckListener) {
        if (context == null || permissionCheckListener == null) {
            throw new IllegalArgumentException("the param 'context' or 'permissionCheckListener' must not be null.");
        }
        mPerCheckListener = permissionCheckListener;
        Intent intent = new Intent(context, RequestPermissionActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION);
            intent.setData(Uri.parse("package:" + getPackageName()));
            startActivityForResult(intent, REQ_OVERLAY_PERMISSION);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQ_OVERLAY_PERMISSION) {
            if (AlertWindowPermissionUtil.hasPermissionOnActivityResult(this)) {
                mPerCheckListener.onSuccess();
            } else {
                mPerCheckListener.onFail();
            }
        }
        finish();
    }
}
