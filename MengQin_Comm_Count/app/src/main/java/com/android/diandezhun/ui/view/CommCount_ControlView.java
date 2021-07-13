package com.android.diandezhun.ui.view;

import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;

import androidx.annotation.Nullable;

import com.android.baselibrary.tool.Log;
import com.android.diandezhun.R;
import com.android.diandezhun.bean.Count_DetailInfo;
import com.android.diandezhun.event.Event_Count_BgChange;
import com.android.diandezhun.event.Event_Count_ChangeCircleRadius;
import com.android.diandezhun.manager.ImageRecConfig;
import com.jaygoo.widget.OnRangeChangedListener;
import com.jaygoo.widget.RangeSeekBar;
import com.jaygoo.widget.VerticalRangeSeekBar;

import org.greenrobot.eventbus.EventBus;

/**
 * 操控控件，
 */
public class CommCount_ControlView extends LinearLayout implements View.OnClickListener {

    Context context;

    public CommCount_ControlView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        initView(attrs);
    }

    public CommCount_ControlView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        initView(attrs);
    }

    SeekBar seekbar_circle_size;
    SeekBar seekbar_circle_fillter;
    ImageView iv_editbg_jia, iv_editbg_jian, iv_editfillter_jian, iv_editfillter_jia, iv_shape_style, iv_edit_bgcolor_jia, iv_edit_bgcolor_jian;
    VerticalRangeSeekBar vertical_seekbar;
    RelativeLayout rl_control_top, rl_control_bottom, rl_control_right;

    Count_DetailInfo countDetailInfo;

    public void setCountDetailInfo(Count_DetailInfo countDetailInfo) {
        this.countDetailInfo = countDetailInfo;

        if (countDetailInfo != null) {
            //初始化圆点直径进度
            seekbar_circle_size.setProgress((int) (countDetailInfo.radius_scale * 10) - 5);

            //初始化精度进度
            seekbar_circle_fillter.setMax(countDetailInfo.radius_max - countDetailInfo.radius_min);
            seekbar_circle_fillter.setProgress(countDetailInfo.mostRadius - countDetailInfo.radius_min);

            //初始化背景
            vertical_seekbar.setValue(ImageRecConfig.getFrame_BgColor_Alpha(context));
        }
    }

    private void initView(AttributeSet attrs) {
        View.inflate(context, R.layout.view_commcount_controlview, this);

        seekbar_circle_size = findViewById(R.id.seekbar_circle_size);
        seekbar_circle_fillter = findViewById(R.id.seekbar_circle_fillter);
        iv_editbg_jia = findViewById(R.id.iv_editbg_jia);
        iv_editbg_jian = findViewById(R.id.iv_editbg_jian);
        iv_editfillter_jian = findViewById(R.id.iv_editfillter_jian);
        iv_editfillter_jia = findViewById(R.id.iv_editfillter_jia);
        iv_shape_style = findViewById(R.id.iv_shape_style);
        vertical_seekbar = findViewById(R.id.vertical_seekbar);
        rl_control_top = findViewById(R.id.rl_control_top);
        rl_control_bottom = findViewById(R.id.rl_control_bottom);
        rl_control_right = findViewById(R.id.rl_control_right);
        iv_edit_bgcolor_jia = findViewById(R.id.iv_edit_bgcolor_jia);
        iv_edit_bgcolor_jian = findViewById(R.id.iv_edit_bgcolor_jian);

        iv_editbg_jia.setOnClickListener(this);
        iv_editbg_jian.setOnClickListener(this);
        iv_editfillter_jian.setOnClickListener(this);
        iv_editfillter_jia.setOnClickListener(this);
        iv_shape_style.setOnClickListener(this);
        iv_edit_bgcolor_jia.setOnClickListener(this);
        iv_edit_bgcolor_jian.setOnClickListener(this);

        seekbar_circle_size.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                //改变圆的大小
                double radius_scale = 0;
                if (progress > 5) {
                    radius_scale = (progress - 5) * 2 + 10;
                } else {
                    radius_scale = progress + 5;
                }
                countDetailInfo.radius_scale = radius_scale / 10d;
//                Log.i("countDetailInfo.radius_scale:" + countDetailInfo.radius_scale);
                EventBus.getDefault().post(new Event_Count_ChangeCircleRadius());
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        seekbar_circle_fillter.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                //改变精度的大小
                countDetailInfo.mostRadius = progress + countDetailInfo.radius_min;
//                Log.i("countDetailInfo.mostRadius:" + countDetailInfo.mostRadius);
                EventBus.getDefault().post(new Event_Count_ChangeCircleRadius());
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        vertical_seekbar.setOnRangeChangedListener(new OnRangeChangedListener() {
            @Override
            public void onRangeChanged(RangeSeekBar view, float leftValue, float rightValue, boolean isFromUser) {
                //leftValue is left seekbar value, rightValue is right seekbar value
                Log.i("leftValue:" + leftValue);
                ImageRecConfig.setFrame_BgColor_Alpha(context, (int) leftValue);
                EventBus.getDefault().post(new Event_Count_BgChange());
            }

            @Override
            public void onStartTrackingTouch(RangeSeekBar view, boolean isLeft) {
                //start tracking touch
            }

            @Override
            public void onStopTrackingTouch(RangeSeekBar view, boolean isLeft) {
                //stop tracking touch
            }
        });

        checkStyle();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_editbg_jia:
                seekbar_circle_size.setProgress(seekbar_circle_size.getProgress() + 1);
                break;
            case R.id.iv_editbg_jian:
                seekbar_circle_size.setProgress(seekbar_circle_size.getProgress() - 1);
                break;
            case R.id.iv_editfillter_jian:
                seekbar_circle_fillter.setProgress(seekbar_circle_fillter.getProgress() - 1);
                break;
            case R.id.iv_editfillter_jia:
                seekbar_circle_fillter.setProgress(seekbar_circle_fillter.getProgress() + 1);
                break;
            case R.id.iv_shape_style:
                int style = ImageRecConfig.getCircleStyle(context);
                if (style == 0) {
                    ImageRecConfig.setCircleStyle(context, 1);
                } else {
                    ImageRecConfig.setCircleStyle(context, 0);
                }
                checkStyle();
                EventBus.getDefault().post(new Event_Count_ChangeCircleRadius());
                break;
            case R.id.iv_edit_bgcolor_jia:
                vertical_seekbar.setValue(ImageRecConfig.getFrame_BgColor_Alpha(context) + 1);
                break;
            case R.id.iv_edit_bgcolor_jian:
                vertical_seekbar.setValue(ImageRecConfig.getFrame_BgColor_Alpha(context) - 1);
                break;
        }
    }

    //切换形状
    private void checkStyle() {
        if (ImageRecConfig.getCircleStyle(context) == 0) {
            //圆形
            iv_shape_style.setImageResource(R.drawable.ico_count_editsize_shape_circle);
        } else {
            //加号
            iv_shape_style.setImageResource(R.drawable.ico_count_editsize_shape_jiahao);
        }
    }

    //切换隐藏/显示
    public void checkHiden() {
        int show_index = ImageRecConfig.getShowIndex(context);

        rl_control_top.setVisibility(GONE);
        rl_control_bottom.setVisibility(GONE);
        rl_control_right.setVisibility(GONE);

        switch (show_index) {
            case 0:
                rl_control_top.setVisibility(VISIBLE);
                rl_control_bottom.setVisibility(VISIBLE);
                break;
            case 1:
                rl_control_bottom.setVisibility(VISIBLE);
                break;
            case 2:

                break;
            case 3:
                rl_control_top.setVisibility(VISIBLE);
                rl_control_bottom.setVisibility(VISIBLE);
                rl_control_right.setVisibility(VISIBLE);
                break;
            case 4:
                rl_control_top.setVisibility(VISIBLE);
                break;
            case 5:
                rl_control_top.setVisibility(VISIBLE);
                rl_control_right.setVisibility(VISIBLE);
                break;
            case 6:
                rl_control_bottom.setVisibility(VISIBLE);
                rl_control_right.setVisibility(VISIBLE);
                break;
        }
    }

}
