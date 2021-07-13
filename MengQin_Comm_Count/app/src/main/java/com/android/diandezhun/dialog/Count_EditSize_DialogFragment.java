package com.android.diandezhun.dialog;

import android.view.View;
import android.widget.SeekBar;

import androidx.fragment.app.FragmentManager;

import com.android.baselibrary.dialog.BaseDialogFragment;
import com.android.diandezhun.R;
import com.android.diandezhun.bean.Count_DetailInfo;
import com.android.diandezhun.event.Event_Count_ChangeCircleRadius;
import com.android.diandezhun.manager.ImageRecConfig;

import org.greenrobot.eventbus.EventBus;

import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by Administrator on 2018/8/29.
 */

public class Count_EditSize_DialogFragment extends BaseDialogFragment {

    @BindView(R.id.seekbar)
    SeekBar seekbar;   //圆心大小
    @BindView(R.id.seekbar1)
    SeekBar seekbar1;   //精度大小

    Count_DetailInfo countDetailInfo;

    @Override
    protected int getLayoutId() {
        return R.layout.dialog_editsize;
    }

    @Override
    protected void initData() {
        if (countDetailInfo != null) {
            seekbar.setMax(countDetailInfo.radius * 2);
            seekbar.setProgress(countDetailInfo.radius);

            seekbar1.setMax(100);
            seekbar1.setProgress(countDetailInfo.mostRadius);
        }

        seekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                //改变圆的大小
                countDetailInfo.radius = progress;
                EventBus.getDefault().post(new Event_Count_ChangeCircleRadius());
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        seekbar1.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                //改变圆的大小
                countDetailInfo.mostRadius = progress;
                EventBus.getDefault().post(new Event_Count_ChangeCircleRadius());
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    @Override
    protected void initView() {

    }

    @Override
    protected int getShowDirection() {
        return BaseDialogFragment.Direction_FromBottom;
    }

    @Override
    public void setData(Map<String, Object> params) {
        if (params != null) {
            countDetailInfo = (Count_DetailInfo) params.get("countDetailInfo");
        }
    }

    public static BaseDialogFragment showDialog(FragmentManager fragmentManager, Map<String, Object> params) {
        Count_EditSize_DialogFragment dialogFragment = new Count_EditSize_DialogFragment();
        dialogFragment.setData(params);
        dialogFragment.show(fragmentManager, "");
        return dialogFragment;
    }

    @OnClick({R.id.iv_jian, R.id.iv_jia, R.id.iv_jian1, R.id.iv_jia1, R.id.iv_shape_circle, R.id.iv_shape_jiahao, R.id.iv_color, R.id.iv_close})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_close:
                dismissWithAnim();
                break;
            case R.id.iv_jian: //减
                seekbar.setProgress(seekbar.getProgress() - 1);
                break;
            case R.id.iv_jia: //加
                seekbar.setProgress(seekbar.getProgress() + 1);
                break;
            case R.id.iv_jian1: //减
                seekbar1.setProgress(seekbar1.getProgress() - 1);
                break;
            case R.id.iv_jia1: //加
                seekbar1.setProgress(seekbar1.getProgress() + 1);
                break;
            case R.id.iv_shape_circle:  //切换圆形
                ImageRecConfig.setCircleStyle(mContext, 0);
                EventBus.getDefault().post(new Event_Count_ChangeCircleRadius());
                break;
            case R.id.iv_shape_jiahao:  //切换加号
                ImageRecConfig.setCircleStyle(mContext, 1);
                EventBus.getDefault().post(new Event_Count_ChangeCircleRadius());
                break;
            case R.id.iv_color:     //颜色
                Count_EditColor_DialogFragment.showDialog(getFragmentManager(), null);
                break;

        }
    }
}
