package com.android.diandezhun.ui.activity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Build;
import android.view.View;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.android.baselibrary.interface_.OkHttpCallBack;
import com.android.baselibrary.responce.BaseResponce;
import com.android.baselibrary.tool.CommLoading;
import com.android.baselibrary.tool.CommToast;
import com.android.baselibrary.tool.Log;
import com.android.baselibrary.util.DateUtil;
import com.android.baselibrary.view.Comm_HeadView;
import com.android.baselibrary.view.MultiStateView;
import com.android.diandezhun.R;
import com.android.diandezhun.bean.History_FillterInfo;
import com.android.diandezhun.manager.API_Manager;

import java.util.Calendar;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 筛选历史记录
 */
public class CommCount_History_FillterActivity extends BaseActivity {

    @BindView(R.id.multiplestatusView)
    MultiStateView multiplestatusView;
    @BindView(R.id.comm_title)
    Comm_HeadView comm_title;
    @BindView(R.id.ll_start_time)
    LinearLayout ll_start_time;
    @BindView(R.id.tv_start_time)
    TextView tv_start_time;
    @BindView(R.id.tv_endtime)
    TextView tv_endtime;
    @BindView(R.id.ll_ruku)
    LinearLayout ll_ruku;
    @BindView(R.id.ll_chuku)
    LinearLayout ll_chuku;
    @BindView(R.id.iv_chuku)
    ImageView iv_chuku;
    @BindView(R.id.iv_ruku)
    ImageView iv_ruku;
    @BindView(R.id.recyclerview)
    RecyclerView recyclerview;

    History_FillterInfo fillterInfo;   //筛选条件

    @Override
    protected int getLayoutId() {
        return R.layout.activity_commcount_history_fillter;
    }

    @Override
    protected void initView() {
        comm_title.setTitle("筛选");
        comm_title.setRightText("确定", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.putExtra("fillterInfo", fillterInfo);
                setResult(200, intent);
                finish();
            }
        });
    }

    @Override
    protected void initData() {
        fillterInfo = (History_FillterInfo) getIntent().getSerializableExtra("fillterInfo");
        if (fillterInfo == null) {
            fillterInfo = new History_FillterInfo();
            Calendar myCalendar = Calendar.getInstance();
            fillterInfo.startTime = myCalendar.getTime().getTime();
            myCalendar.add(Calendar.DATE, 1);
            fillterInfo.endTime = myCalendar.getTime().getTime();
        }

        tv_start_time.setText(DateUtil.getDateFromTimeLine(fillterInfo.startTime, "yyyy-MM-dd"));
        tv_endtime.setText(DateUtil.getDateFromTimeLine(fillterInfo.endTime, "yyyy-MM-dd"));
        checkInAndOutType();
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @OnClick({R.id.ll_start_time, R.id.ll_end_time, R.id.ll_ruku, R.id.ll_chuku})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ll_start_time:
                DatePickerDialog dialog = new DatePickerDialog(mContext, DatePickerDialog.THEME_HOLO_LIGHT);
                dialog.show();
                dialog.setOnDateSetListener(new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                        Log.i(i + "-" + i1 + "-" + i2);
                        Calendar myCalendar = Calendar.getInstance();
                        myCalendar.set(Calendar.YEAR, i);
                        myCalendar.set(Calendar.MONTH, i1);
                        myCalendar.set(Calendar.DATE, i2);

                        long startTimeTemp = myCalendar.getTime().getTime();
                        if (fillterInfo.endTime == 0 || startTimeTemp < fillterInfo.endTime) {
                            fillterInfo.startTime = startTimeTemp;
                            tv_start_time.setText(DateUtil.getDateFromTimeLine(fillterInfo.startTime, "yyyy-MM-dd"));
                        } else {
                            CommToast.showToast(mContext, "结束时间不能早于开始时间");
                        }
                    }
                });
                break;
            case R.id.ll_end_time:
                DatePickerDialog dialog1 = new DatePickerDialog(mContext, DatePickerDialog.THEME_HOLO_LIGHT);
                dialog1.show();
                dialog1.setOnDateSetListener(new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                        Log.i(i + "-" + i1 + "-" + i2);
                        Calendar myCalendar = Calendar.getInstance();
                        myCalendar.set(Calendar.YEAR, i);
                        myCalendar.set(Calendar.MONTH, i1);
                        myCalendar.set(Calendar.DATE, i2);
                        myCalendar.add(Calendar.DATE, 1);

                        long endTimeTimeTemp = myCalendar.getTime().getTime();
                        if (fillterInfo.startTime == 0 || fillterInfo.startTime < endTimeTimeTemp) {
                            fillterInfo.endTime = endTimeTimeTemp;
                            tv_endtime.setText(DateUtil.getDateFromTimeLine(fillterInfo.endTime, "yyyy-MM-dd"));
                        } else {
                            CommToast.showToast(mContext, "结束时间不能早于开始时间");
                        }
                    }
                });
                break;
            case R.id.ll_ruku:      //入库
                fillterInfo.inAndOutType = 1;
                checkInAndOutType();
                break;
            case R.id.ll_chuku:     //出库
                fillterInfo.inAndOutType = 0;
                checkInAndOutType();
                break;
        }
    }

    private void checkInAndOutType() {
        iv_chuku.setImageResource(R.drawable.ico_unselect);
        iv_ruku.setImageResource(R.drawable.ico_unselect);
        if (fillterInfo.inAndOutType == 0) {
            iv_chuku.setImageResource(R.drawable.ico_select);
        } else {
            iv_ruku.setImageResource(R.drawable.ico_select);
        }
    }

}
