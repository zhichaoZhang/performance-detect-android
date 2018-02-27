package com.joye.performance_detection;

import android.content.Context;
import android.support.constraint.ConstraintLayout;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

/**
 * 性能参数展示悬浮视图
 * <p>
 * Created by joye on 2018/2/27.
 */

public class PerformanceFloatWidget extends ConstraintLayout {
    private TextView tvFps;

    public PerformanceFloatWidget(Context context) {
        this(context, null);
    }

    public PerformanceFloatWidget(Context context, AttributeSet attrs) {
        this(context, attrs, -1);
    }

    public PerformanceFloatWidget(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        View view = LayoutInflater.from(context).inflate(R.layout.performance_params_view, this, true);
        tvFps = view.findViewById(R.id.tv_fps);
    }

    /**
     * 设置帧率
     *
     * @param fps 帧率
     */
    public void setFPS(String fps) {
        if (tvFps != null) {
            tvFps.setText(fps);
        }
    }
}
