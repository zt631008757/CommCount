package com.android.baselibrary.dialog;

import android.graphics.Color;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.fragment.app.FragmentManager;

import com.android.baselibrary.R;
import com.android.baselibrary.util.Util;

import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2018/8/29.
 */

public class CommList_DialogFragment extends BaseDialogFragment {

    @Override
    protected int getLayoutId() {
        return R.layout.dialog_list_comm;
    }

    @Override
    protected void initData() {

    }

    LinearLayout list_content;
    TextView tv_cancel;
    ScrollView scrollview;

    List<String> strList;
    boolean isShowCancel = false;  //是否显示取消按钮

    @Override
    protected void initView() {

        list_content = mRootView.findViewById(R.id.list_content);
        scrollview = mRootView.findViewById(R.id.scrollview);
        tv_cancel = mRootView.findViewById(R.id.tv_cancel);
        tv_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismissWithAnim();
            }
        });


        if (strList != null) {
            for (int i = 0; i < strList.size(); i++) {
                if (!TextUtils.isEmpty(strList.get(i))) {
                    TextView textView = new TextView(getContext());
                    textView.setText(strList.get(i));
                    textView.setTextSize(14);
                    textView.setGravity(Gravity.CENTER);
                    textView.setTextColor(Color.parseColor("#292929"));
                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, Util.dip2px(getContext(), 50));
                    params.gravity = Gravity.CENTER;
                    textView.setLayoutParams(params);

                    final int finalI = i;
                    textView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Util.unallowQuickClick(textView);
                            //将position回传
                            if (callBack != null)
                                callBack.onResult(finalI);
                            dismiss();
                        }
                    });
                    //加分割线
                    if (i != 0) {
                        View view = new View(getContext());
                        view.setBackgroundColor(Color.parseColor("#eeeeee"));
                        LinearLayout.LayoutParams paramsView = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 1);
                        view.setLayoutParams(paramsView);
                        list_content.addView(view);
                    }
                    list_content.addView(textView);
                }
            }
        }

        if (strList.size() > 8) {
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, Util.dip2px(getContext(), 400));
            scrollview.setLayoutParams(params);
        }

        if (isShowCancel) {
            tv_cancel.setVisibility(View.VISIBLE);
        } else {
            tv_cancel.setVisibility(View.GONE);
        }
    }

    @Override
    public void setData(Map<String, Object> params) {
        if (params != null) {
            {
                this.strList = (List<String>) params.get("strList");
                this.isShowCancel = (boolean) params.get("isShowCancel");
            }
        }
    }

    public static BaseDialogFragment showDialog(FragmentManager fragmentManager, Map<String, Object> params) {
        CommList_DialogFragment dialogFragment = new CommList_DialogFragment();
        dialogFragment.setData(params);
        dialogFragment.show(fragmentManager, "");
        return dialogFragment;
    }


}
