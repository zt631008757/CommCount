package vip.mengqin.diandezhun.ui.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;

import androidx.annotation.Nullable;

import com.android.baselibrary.interface_.CommCallBack;
import com.android.baselibrary.interface_.OkHttpCallBack;
import com.android.baselibrary.responce.BaseResponce;
import com.android.baselibrary.tool.CommLoading;
import com.android.baselibrary.tool.CommToast;
import com.android.baselibrary.tool.Log;
import com.android.baselibrary.tool.SPUtil;

import vip.mengqin.diandezhun.R;
import vip.mengqin.diandezhun.constant.SPConstants;
import vip.mengqin.diandezhun.manager.API_Manager;
import vip.mengqin.diandezhun.manager.UserManager;
import vip.mengqin.diandezhun.responce.TimestampResponce;

/**
 * Created by Administrator on 2018/7/21.
 */

public class LoadingActivity extends Activity {

    Handler handler = new Handler();
    Context mContext;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        setContentView(R.layout.activity_loading);

        boolean hasOk = SPUtil.getBoolValue(mContext, SPConstants.HasAccept, true);
        if (hasOk) {
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    goToMainActivity();
                }
            }, 1500);
        } else {
//            XieYi_Dialog dialog = new XieYi_Dialog(mContext);
//            dialog.show();
//            dialog.setIntputCallBack(new CommCallBack() {
//                @Override
//                public void onResult(Object obj) {
//                    SPUtil.putValue(mContext, SPConstants.HasAccept, true);
//                    goToMainActivity();
//                }
//            });
        }
        timestamp();
    }

    private void goToMainActivity() {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN, WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
        Intent intent = null;
//        if (UserManager.isLogin(mContext)) {
//            intent = new Intent(mContext, MainActivity.class);
//        } else {
//            intent = new Intent(mContext, LoginActivity.class);
//        }
        intent = new Intent(mContext, MainActivity.class);
        mContext.startActivity(intent);
        overridePendingTransition(R.anim.main_activity_in, R.anim.splash_out);
        finish();
    }

    //获取时间差
    private void timestamp() {
        API_Manager.timestamp(mContext, new OkHttpCallBack<BaseResponce<TimestampResponce>>() {
            @Override
            public void onSuccess(BaseResponce<TimestampResponce> baseResponce) {
                //计算时间差
                Log.i("timestamp:" + baseResponce.getData().timestamp + "   System.currentTimeMillis() :" + System.currentTimeMillis());

                long timeDelta = (System.currentTimeMillis() / 1000 - baseResponce.getData().timestamp);
                SPUtil.putValue(mContext, SPConstants.SysTimeDelta, timeDelta + "");
                Log.i("时间差:" + timeDelta);
            }

            @Override
            public void onFailure(BaseResponce<TimestampResponce> baseResponce) {
                CommLoading.dismissLoading();
                CommToast.showToast(mContext, baseResponce.message);
            }
        });
    }

}
