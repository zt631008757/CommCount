package vip.mengqin.diandezhun.ui.activity;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;

import com.android.baselibrary.ui.AbsBaseActivity;
import com.android.baselibrary.view.Comm_HeadView;
import com.gyf.immersionbar.ImmersionBar;

import vip.mengqin.diandezhun.R;

public abstract class BaseActivity extends AbsBaseActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Comm_HeadView comm_title = findViewById(R.id.comm_title);
        if (comm_title != null) {
            comm_title.setLeftClickFinish(this);
        }
        //是否沉浸式
        if (isImmersion()) {
            if (getImmersionTitleBar() != null) {
                ImmersionBar.with(this).titleBar(getImmersionTitleBar());
            }
            ImmersionBar.with(this).statusBarDarkFont(isBarDarkFontAndIcon()).navigationBarDarkIcon(isBarDarkFontAndIcon()).init();
        }
        //设置全局背景颜色
        ((ViewGroup) getWindow().getDecorView()).getChildAt(0).setBackgroundColor(getResources().getColor(R.color.page_bg));
    }

    @Override
    protected boolean isRegistEventbus() {
        return false;
    }

    protected boolean isImmersion() {
        return true;
    }

    protected View getImmersionTitleBar() {
        Comm_HeadView comm_title = findViewById(R.id.comm_title);
        return comm_title;
    }

    protected boolean isBarDarkFontAndIcon() {
        return true;
    }
}
