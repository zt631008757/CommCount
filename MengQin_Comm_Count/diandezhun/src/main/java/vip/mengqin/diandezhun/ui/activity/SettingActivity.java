package vip.mengqin.diandezhun.ui.activity;

import android.view.View;

import com.android.baselibrary.util.GlideUtil;
import com.android.baselibrary.view.Comm_HeadView;
import com.android.baselibrary.view.MultiStateView;
import com.bumptech.glide.Glide;

import butterknife.BindView;
import butterknife.OnClick;
import vip.mengqin.diandezhun.R;

/**
 * 设置
 */
public class SettingActivity extends BaseActivity {

    @BindView(R.id.multiplestatusView)
    MultiStateView multiplestatusView;
    @BindView(R.id.comm_title)
    Comm_HeadView comm_title;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_setting;
    }

    @Override
    protected void initView() {
        comm_title.setTitle("设置");


    }

    @Override
    protected void initData() {

    }

    @OnClick({R.id.ctv_qingchuhuancun, R.id.ctv_cunchiweizhi, R.id.ctv_zhezhaotoumingdu})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ctv_qingchuhuancun:   //清除缓存
                Glide.get(mContext).clearDiskCache();
                break;
            case R.id.ctv_cunchiweizhi:   //照片存储位置


                break;
            case R.id.ctv_zhezhaotoumingdu:   //遮罩透明度


                break;
        }
    }

}
