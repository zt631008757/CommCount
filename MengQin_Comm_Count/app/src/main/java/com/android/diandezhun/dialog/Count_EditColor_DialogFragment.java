package com.android.diandezhun.dialog;

import android.widget.RadioGroup;

import androidx.fragment.app.FragmentManager;

import com.android.baselibrary.dialog.BaseDialogFragment;
import com.android.diandezhun.R;
import com.android.diandezhun.event.Event_Count_ChangeCircleRadius;
import com.android.diandezhun.manager.ImageRecConfig;

import org.greenrobot.eventbus.EventBus;

import java.util.Map;

import butterknife.BindView;

/**
 * Created by Administrator on 2018/8/29.
 */

public class Count_EditColor_DialogFragment extends BaseDialogFragment {

    @BindView(R.id.radiogroup)
    RadioGroup radiogroup;

    @Override
    protected int getLayoutId() {
        return R.layout.dialog_count_edit_color;
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initView() {
        radiogroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int checkedId) {
                int color = 0;
                switch (checkedId) {
                    case R.id.tb_color1:
                        color = 0;
                        break;
                    case R.id.tb_color2:
                        color = 1;
                        break;
                    case R.id.tb_color3:
                        color = 2;
                        break;
                    case R.id.tb_color4:
                        color = 3;
                        break;
                    case R.id.tb_color5:
                        color = 4;
                        break;
                }
                ImageRecConfig.setCircleColor(mContext,color);
                EventBus.getDefault().post(new Event_Count_ChangeCircleRadius());
            }
        });
    }

    @Override
    protected int getShowDirection() {
        return BaseDialogFragment.Direction_FromBottom;
    }

    @Override
    public void setData(Map<String, Object> params) {
        if (params != null) {
        }
    }

    public static BaseDialogFragment showDialog(FragmentManager fragmentManager, Map<String, Object> params) {
        Count_EditColor_DialogFragment dialogFragment = new Count_EditColor_DialogFragment();
        dialogFragment.setData(params);
        dialogFragment.show(fragmentManager, "");
        return dialogFragment;
    }


//    @OnClick({R.id.ll_share_wx })
//    public void onViewClicked(View view) {
//        switch (view.getId()) {
//            case R.id.ll_share_wx:
//
//                break;
//
//        }
//    }


}
