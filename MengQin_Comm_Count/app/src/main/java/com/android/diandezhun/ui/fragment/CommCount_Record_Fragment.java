package com.android.diandezhun.ui.fragment;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.android.baselibrary.ui.BaseFragment;
import com.android.baselibrary.view.Comm_SubmitBtnView;
import com.android.diandezhun.R;
import com.android.diandezhun.adapter.FragmentViewPagerAdapter;
import com.android.diandezhun.manager.SqlHelper;
import com.google.android.material.tabs.TabLayout;
import com.gyf.immersionbar.ImmersionBar;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 计数记录- 包括历史记录，历史表单
 */
public class CommCount_Record_Fragment extends BaseFragment {

    private static CommCount_Record_Fragment fragment = null;

    public static CommCount_Record_Fragment newInstance() {
        if (fragment == null) {
            fragment = new CommCount_Record_Fragment();
        }
        return fragment;
    }

    @BindView(R.id.tabs)
    TabLayout tabs;
    @BindView(R.id.viewpager)
    ViewPager viewpager;
    @BindView(R.id.ll_head)
    LinearLayout ll_head;
    @BindView(R.id.btn_manage)
    Comm_SubmitBtnView btn_manage;
    @BindView(R.id.btn_delete)
    Comm_SubmitBtnView btn_delete;
    @BindView(R.id.ll_manage_history)
    LinearLayout ll_manage_history;
    @BindView(R.id.ll_manage_form)
    LinearLayout ll_manage_form;


    List<Fragment> fragments = new ArrayList<>();
    List<String> titleList = new ArrayList<>();

    CommCount_Record_HistoryFragment fragment1;
    CommCount_Record_FormFragment fragment2;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_commcount_record;
    }

    @Override
    protected void initView() {
        ImmersionBar.with(this).titleBar(ll_head);
        ImmersionBar.with(this).statusBarDarkFont(true).navigationBarDarkIcon(true).init();

        titleList.add("历史记录");
        titleList.add("我的表单");

        fragment1 = new CommCount_Record_HistoryFragment();
        fragment2 = new CommCount_Record_FormFragment();
        fragments.add(fragment1);
        fragments.add(fragment2);

        FragmentViewPagerAdapter viewPagerAdapter = new FragmentViewPagerAdapter(getChildFragmentManager(), fragments, titleList);
        viewpager.setAdapter(viewPagerAdapter);
        tabs.setupWithViewPager(viewpager);
        viewpager.setOffscreenPageLimit(5);

        viewpager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                showBtn();
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        showBtn();
    }

    private void showBtn() {
        ll_manage_history.setVisibility(View.GONE);
        ll_manage_form.setVisibility(View.GONE);
        if (viewpager.getCurrentItem() == 0) {
            ll_manage_history.setVisibility(View.VISIBLE);
        } else {
            ll_manage_form.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected void initData() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SqlHelper.resetHistorySelectState();
        SqlHelper.resetFormSelectState();
    }

    @OnClick({R.id.btn_manage, R.id.btn_fillter, R.id.btn_delete})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_manage:   //管理
                if ("完成".equals(btn_manage.getText())) {
                    fragment1.setShowManage(false);
                    btn_manage.setText("删除/汇总");
                    SqlHelper.resetHistorySelectState();
                } else {
                    fragment1.setShowManage(true);
                    btn_manage.setText("完成");
                }
                break;
            case R.id.btn_fillter:   //筛选
                fragment1.toFillter();
                break;
            case R.id.btn_delete:
                if ("完成".equals(btn_delete.getText())) {
                    fragment2.setShowManage(false);
                    btn_delete.setText("删除");
                    SqlHelper.resetFormSelectState();
                } else {
                    fragment2.setShowManage(true);
                    btn_delete.setText("完成");
                }
                break;
        }
    }


}
