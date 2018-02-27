package com.joye.performance_detection;

import android.content.Context;
import android.os.Build;
import android.os.Looper;
import android.support.annotation.RequiresApi;
import android.util.Printer;
import android.view.Choreographer;
import android.view.View;
import android.widget.TextView;

import com.joye.performance_detection.float_window.FloatWindow;
import com.joye.performance_detection.float_window.FloatWindowManager;

import java.util.concurrent.TimeUnit;

/**
 * 根据Choreographer绘制每一帧的回调，监控是否产生卡顿
 * <p>
 * Created by joye on 2018/1/25.
 */

public class BlockDetectUtil {
    public static final String PERFORMANCE_FLOAT_WINDOW = "performance_float_window";
    //是否显示帧率
    private static boolean isShowFPS = false;
    //是否继续检测
    private static boolean isDetectContinue = false;
    //刷新间隔 单位：毫秒
    private static int refreshInterval = 300;
    //上次帧率刷新时间
    private static long mLastFPSRefreshTs = 0L;


    public static void setIsShowFPS(boolean showFPS) {
        isShowFPS = showFPS;
    }

    public static void setRefreshInterval(int interval) {
        refreshInterval = interval;
    }

    public static void start() {
        if (!isDetectContinue) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                detectByChoreographer();
            } else {
                detectByLooper();
            }
        }
        isDetectContinue = true;
    }

    public static void stop() {
        isDetectContinue = false;
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    private static void detectByChoreographer() {
        Choreographer.getInstance().postFrameCallback(new Choreographer.FrameCallback() {
            long lastFrameTimeNanos = 0;
            long currentFrameTimeNanos = 0;

            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void doFrame(long frameTimeNanos) {
                if (lastFrameTimeNanos == 0) {
                    lastFrameTimeNanos = frameTimeNanos;
                }
                currentFrameTimeNanos = frameTimeNanos;
                long diffMs = TimeUnit.MILLISECONDS.convert(currentFrameTimeNanos - lastFrameTimeNanos, TimeUnit.NANOSECONDS);
                lastFrameTimeNanos = currentFrameTimeNanos;
                if (diffMs == 0) {
                    diffMs = (long) 16.6;
                }

                if (isShowFPS) {
                    long current = System.currentTimeMillis();
                    if (current - mLastFPSRefreshTs > refreshInterval) {
                        int fps = (int) (1000 / diffMs);
                        refreshFPS(fps);
                        mLastFPSRefreshTs = current;
                    }
                }

                if (diffMs > 16.7f) {
                    long droppedCount = (long) (diffMs / 16.6);
                    if (droppedCount > 1) {
                        System.out.println("掉帧数 : " + droppedCount);
                    }
                }

                if (UiBlockLogMonitor.getInstance().isMonitor()) {
                    UiBlockLogMonitor.getInstance().stopMonitor();
                }

                if (isDetectContinue) {
                    UiBlockLogMonitor.getInstance().startMonitor();
                    Choreographer.getInstance().postFrameCallback(this);
                }
            }
        });
    }

    //刷新帧率显示
    private static void refreshFPS(int fps) {
        FloatWindow floatWindow = FloatWindowManager.get(PERFORMANCE_FLOAT_WINDOW);

        if (floatWindow != null && floatWindow.getView() != null) {
            View floatView = floatWindow.getView();
            if (floatView != null && floatView instanceof PerformanceFloatWidget) {
                PerformanceFloatWidget performanceFloatWidget = (PerformanceFloatWidget) floatWindow.getView();
                performanceFloatWidget.setFPS(String.valueOf(fps));
            }
        }
    }

    private static void detectByLooper() {
        Looper.getMainLooper().setMessageLogging(new Printer() {
            private static final String START = ">>>>> Dispatching";
            private static final String END = "<<<<< Finished";

            @Override
            public void println(String x) {
                if (x.startsWith(START)) {
                    UiBlockLogMonitor.getInstance().startMonitor();
                }
                if (x.startsWith(END)) {
                    UiBlockLogMonitor.getInstance().stopMonitor();
                }
            }
        });
    }
}
