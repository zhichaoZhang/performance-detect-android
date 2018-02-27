package com.joye.performance_detection;

import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

/**
 * Ui线程卡顿日志监视器
 * 通过设置固定时长（1000ms）的延迟RUNNABLE任务。
 * 如果主线程没有卡顿，会在1000ms内RUNNABLE任务取消，否则会执行RUNNABLE任务。
 * RUNNABLE任务负责打印出主线程的堆栈信息。
 * <p>
 * Created by joye on 2018/1/25.
 */

public class UiBlockLogMonitor {
    private static final String TAG = "UiBlockLogMonitor";
    //单例
    private static UiBlockLogMonitor mInstance = new UiBlockLogMonitor();
    //执行日志打印线程
    private HandlerThread mLogThread = new HandlerThread("ui-block-log");
    //日志线程Handler
    private Handler mIoHandler;
    //日志监控消息类型
    private final int MSG_LOG_MINITOR = 1;
    //Ui线程阻塞阈值
    private static long TIME_BLOCK_THRESHOLD_VALUE = 1000L;

    private UiBlockLogMonitor() {
        mLogThread.start();
        mIoHandler = new Handler(mLogThread.getLooper()) {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch (msg.what) {
                    case MSG_LOG_MINITOR:
                        recordUiThreadStackTrace();
                        break;
                }
            }
        };
    }

    public static UiBlockLogMonitor getInstance() {
        return mInstance;
    }

    /**
     * 设置Ui线程阻塞时长监测阈值
     *
     * @param thresholdValue 单位ms
     */
    public static void setBlockTimeThresholdValue(long thresholdValue) {
        if (thresholdValue <= 0) {
            Log.e(TAG, "setBlockTimeThresholdValue: the 'thresholdValue' must be greater than zero.");
            return;
        }
        TIME_BLOCK_THRESHOLD_VALUE = thresholdValue;
    }

    //Ui线程堆栈日志打印任务
    private void recordUiThreadStackTrace() {
        StackTraceElement[] stackTrace = Looper.getMainLooper().getThread().getStackTrace();
        if (stackTrace != null) {
            StringBuilder sbLog = new StringBuilder();
            for (StackTraceElement stackTraceElement : stackTrace) {
                sbLog.append(stackTraceElement.toString()).append("\n");
            }
            Log.e(TAG, "UI block happened: ----------->");
            Log.e(TAG, sbLog.toString());
            Log.e(TAG, "UI block info end: <-----------");
        } else {
            Log.e(TAG, "run: con't get ui thread stack trace.");
        }
    }

    /**
     * 检查是否已经开启监控
     *
     * @return true已开启，false未开启
     */
    public boolean isMonitor() {
        return mIoHandler.hasMessages(MSG_LOG_MINITOR);
    }

    /**
     * 开启监控
     */
    public void startMonitor() {
        mIoHandler.sendEmptyMessageDelayed(MSG_LOG_MINITOR, TIME_BLOCK_THRESHOLD_VALUE);
    }

    /**
     * 结束监控
     */
    public void stopMonitor() {
        mIoHandler.removeMessages(MSG_LOG_MINITOR);
    }
}
