package com.joye.performancedetection;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import com.joye.performance_detection.BlockDetectUtil;
import com.joye.performance_detection.PerformanceFloatWidget;
import com.joye.performance_detection.float_window.FloatWindow;
import com.joye.performance_detection.float_window.FloatWindowManager;

public class MainActivity extends AppCompatActivity {
    FloatWindow floatWindow;
    PerformanceFloatWidget customView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        floatWindow = FloatWindowManager.get(BlockDetectUtil.PERFORMANCE_FLOAT_WINDOW);
        if (floatWindow == null) {
            customView = new PerformanceFloatWidget(getApplicationContext());
            floatWindow = new FloatWindowManager.Builder()
                    .setView(customView)
                    .setGravity(Gravity.TOP | Gravity.RIGHT, 50, 50)
                    .setTag(BlockDetectUtil.PERFORMANCE_FLOAT_WINDOW)
                    .build(getApplicationContext());
        }
    }

    /**
     * 开始检测帧绘制检测
     *
     * @param view
     */
    public void onStartFrameDetect(View view) {
        BlockDetectUtil.start();
    }

    /**
     * 显示悬浮窗
     *
     * @param view
     */
    public void showFloatWindow(View view) {
        BlockDetectUtil.setIsShowFPS(true);
        floatWindow.show();
    }

    /**
     * 隐藏悬浮窗
     *
     * @param view
     */
    public void hideFloatWindow(View view) {
        BlockDetectUtil.setIsShowFPS(false);
        floatWindow.hide();
    }
}
