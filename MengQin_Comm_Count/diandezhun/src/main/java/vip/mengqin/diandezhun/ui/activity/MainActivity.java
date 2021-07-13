package vip.mengqin.diandezhun.ui.activity;

import android.graphics.Color;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.android.baselibrary.interface_.CommCallBack;
import com.android.baselibrary.interface_.OkHttpCallBack;
import com.android.baselibrary.responce.BaseResponce;
import com.android.baselibrary.tool.CommToast;
import com.android.baselibrary.view.MyBottomTabView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import vip.mengqin.diandezhun.R;
import vip.mengqin.diandezhun.bean.UserInfo;
import vip.mengqin.diandezhun.manager.API_Manager;
import vip.mengqin.diandezhun.manager.UserManager;
import vip.mengqin.diandezhun.ui.fragment.A_ModelFragment;
import vip.mengqin.diandezhun.ui.fragment.HomeMainFragment;
import vip.mengqin.diandezhun.ui.fragment.HomeMineFragment;

public class MainActivity extends BaseActivity {

    public final static int HOME_INDEX_0 = 0;
    public final static int HOME_INDEX_1 = 1;
    public final static int HOME_INDEX_2 = 2;
    public final static int HOME_INDEX_3 = 3;
    public final static int HOME_INDEX_4 = 4;

    public static int curruntIndex;

    @BindView(R.id.mybottomtabview)
    MyBottomTabView mybottomtabview;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    protected void initView() {
        initTab();
        chooseFragment(HOME_INDEX_0);
    }

    //设置tab参数
    private void initTab() {
        mybottomtabview.setOnTabSelectCallBack(onBottomTabSelect);
        mybottomtabview.setTextColor(Color.parseColor("#555555"), getResources().getColor(R.color.mainColor));
        List<String> tab_name = new ArrayList<String>();   //文字
        tab_name.add("计数");
        tab_name.add("记录");
        tab_name.add("我的");
        List<Integer> ico_normal = new ArrayList<>();       //默认图片
        ico_normal.add(R.drawable.ico_home_main_normal);
        ico_normal.add(R.drawable.ico_home_record_normal);
        ico_normal.add(R.drawable.ico_home_my_normal);
        List<Integer> ico_select = new ArrayList<>();
        ico_select.add(R.drawable.ico_home_main_select);       //选中图片
        ico_select.add(R.drawable.ico_home_my_select);
        ico_select.add(R.drawable.ico_home_my_select);
        mybottomtabview.setData(tab_name, ico_normal, ico_select, false, R.drawable.ico_home_bottom_center_);
    }

    @Override
    protected void initData() {
//        UpDateManager.getNewVersion(mContext, false, null);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(UserManager.isLogin(mContext)) {
            getUserInfo();
        }
    }

    //切换Fragment
    private void chooseFragment(int index) {
        Fragment currentFragment = getSupportFragmentManager().findFragmentByTag(String.valueOf(index));
        hideAllFrag();
        if (currentFragment == null) {
            switch (index) {
                case HOME_INDEX_0:
                    currentFragment = new HomeMainFragment();
                    break;
                case HOME_INDEX_1:
                    currentFragment = new A_ModelFragment();
                    break;
                case HOME_INDEX_2:
                    currentFragment = new HomeMineFragment();
                    break;
//                case HOME_INDEX_3:
//                    currentFragment = Home_Fragment_Mine.newInstance();
//                    break;
            }
            addFragment(currentFragment, index);
        } else {
            showFragment(currentFragment);
        }
        //切换刷新
        currentFragment.onResume();
        curruntIndex = index;
    }

    CommCallBack onBottomTabSelect = new CommCallBack() {
        @Override
        public void onResult(Object obj) {
            int index = (int) obj;
            if (index == mybottomtabview.centerIndex) {
                //点击加号回调
                CommToast.showToast(mContext, "点击加号");
//                Intent intent = new Intent(mContext, AddTaskActivity.class);
//                startActivity(intent);
            }else {
                chooseFragment(index);
                mybottomtabview.setSelect(index);
            }
        }
    };

    private void addFragment(Fragment currentFragment, int index) {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.add(R.id.framelayout, currentFragment, index + "");
        ft.commitAllowingStateLoss();
    }

    private void hideAllFrag() {
        for (int i = 0; i < 5; i++) {
            Fragment fragment = getSupportFragmentManager().findFragmentByTag(String.valueOf(i));
            if (fragment != null) {
                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                ft.hide(fragment);
                ft.commitAllowingStateLoss();
            }
        }
    }

    public void showFragment(Fragment fragment) {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        if (fragment.isDetached()) {
            ft.attach(fragment).show(fragment);
            ft.commitAllowingStateLoss();
        } else {
            ft.show(fragment);
            ft.commitAllowingStateLoss();
        }
    }

    //获取用户信息
    private void getUserInfo() {
        API_Manager.getUserInfo(mContext, new OkHttpCallBack<BaseResponce<UserInfo>>() {
            @Override
            public void onSuccess(BaseResponce<UserInfo> baseResponce) {
                UserManager.saveUserInfo(mContext, baseResponce.getData());
            }

            @Override
            public void onFailure(BaseResponce<UserInfo> baseResponce) {
            }
        });
    }

}