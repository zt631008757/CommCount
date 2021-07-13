package com.android.diandezhun.ui.fragment;

import android.app.Activity;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.baselibrary.dialog.BaseDialogFragment;
import com.android.baselibrary.dialog.CommList_DialogFragment;
import com.android.baselibrary.interface_.CommCallBack;
import com.android.baselibrary.interface_.OkHttpCallBack;
import com.android.baselibrary.responce.BaseResponce;
import com.android.baselibrary.tool.AndPermisionUtil;
import com.android.baselibrary.tool.CommLoading;
import com.android.baselibrary.tool.CommToast;
import com.android.baselibrary.tool.Log;
import com.android.baselibrary.ui.BaseFragment;
import com.android.baselibrary.util.GlideEngine;
import com.android.baselibrary.view.MultiStateView;
import com.android.diandezhun.R;
import com.android.diandezhun.adapter.CommCountTypeListAdapter;
import com.android.diandezhun.bean.CommCount_Type;
import com.android.diandezhun.manager.API_Manager;
import com.android.diandezhun.manager.UserManager;
import com.android.diandezhun.ui.activity.CustomCamare1Activity;
import com.android.diandezhun.ui.activity.CustomCamareActivity;
import com.android.diandezhun.ui.activity.ImageRecognitionActivity;
import com.gyf.immersionbar.ImmersionBar;
import com.luck.picture.lib.PictureSelectionModel;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;
import com.luck.picture.lib.listener.OnResultCallbackListener;
import com.scwang.smart.refresh.layout.SmartRefreshLayout;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.listener.OnRefreshListener;
import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.runtime.Permission;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

public class Home_Fragment_Tab1 extends BaseFragment {

    private static Home_Fragment_Tab1 fragment = null;

    public static Home_Fragment_Tab1 newInstance() {
        if (fragment == null) {
            fragment = new Home_Fragment_Tab1();
        }
        return fragment;
    }

    @BindView(R.id.multiplestatusView)
    MultiStateView multiplestatusView;
    @BindView(R.id.ll_head)
    LinearLayout ll_head;
    @BindView(R.id.recyclerview)
    RecyclerView recyclerview;
    @BindView(R.id.smartrefreshlayout)
    SmartRefreshLayout smartrefreshlayout;

    CommCountTypeListAdapter adapter;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_home_tab1;
    }

    @Override
    protected void initView() {
        ImmersionBar.with(this).titleBar(ll_head).statusBarDarkFont(true).navigationBarDarkIcon(true).init();

        multiplestatusView.setViewState(MultiStateView.ViewState.LOADING);
        multiplestatusView.setOnRetryListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                multiplestatusView.setViewState(MultiStateView.ViewState.LOADING);
                template();
            }
        });

        recyclerview.setLayoutManager(new GridLayoutManager(mContext, 4));
        adapter = new CommCountTypeListAdapter(mContext, callBack);
        recyclerview.setAdapter(adapter);

        smartrefreshlayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                template();
            }
        });
        smartrefreshlayout.setEnableLoadMore(false);
    }

    CommCallBack callBack = new CommCallBack() {
        @Override
        public void onResult(Object obj) {
            CommCount_Type commCount_type = (CommCount_Type) obj;
            if (AndPermission.hasPermissions(mContext, Permission.Group.CAMERA)) {
                toCamera(commCount_type);
            } else {
                AndPermisionUtil.requstPermision(mContext, new AndPermisionUtil.PermisionCallBack() {
                    @Override
                    public void onGranted() {
                        toCamera(commCount_type);
                    }

                    @Override
                    public void onDenied() {
                        CommToast.showToast(mContext, "相机权限被拒绝，无法使用相机");
                    }
                }, Permission.Group.CAMERA);
            }
        }
    };

    private void toCamera(CommCount_Type commCount_type) {
        Intent intent = new Intent(mContext, CustomCamare1Activity.class);
        intent.putExtra("commCount_type", commCount_type);
        startActivity(intent);
        getActivity().overridePendingTransition(R.anim.main_activity_in, R.anim.anim_activityout);
    }

    @Override
    protected void initData() {
        template();
    }

    private void template() {
        API_Manager.template(mContext, new OkHttpCallBack<BaseResponce<List<CommCount_Type>>>() {
            @Override
            public void onSuccess(BaseResponce<List<CommCount_Type>> baseResponce) {
                smartrefreshlayout.finishRefresh();
                smartrefreshlayout.finishLoadMore();

                adapter.setData(baseResponce.getData());
                if (adapter.getItemCount() == 0) {
                    multiplestatusView.setViewState(MultiStateView.ViewState.EMPTY);
                } else {
                    multiplestatusView.setViewState(MultiStateView.ViewState.CONTENT);
                }
            }

            @Override
            public void onFailure(BaseResponce<List<CommCount_Type>> baseResponce) {
                smartrefreshlayout.finishRefresh();
                smartrefreshlayout.finishLoadMore();
                multiplestatusView.setViewState(MultiStateView.ViewState.ERROR);
//                CommToast.showToast(mContext, baseResponce.message);
            }
        });
    }

//    @OnClick({R.id.tv_camare, R.id.tv_gallary})
//    public void onViewClicked(View view) {
//        switch (view.getId()) {
//            case R.id.tv_camare:
//                if (UserManager.isLoginAndShowLogin((Activity) mContext)) {
//                    selectImg(0);
//                }
//                break;
//            case R.id.tv_gallary:
//                if (UserManager.isLoginAndShowLogin((Activity) mContext)) {
//                    selectImg(1);
//                }
//                break;
//        }
//    }

    //选择相册
    private void selectImg(int type, CommCount_Type commCount_type) {
        AndPermisionUtil.requstPermision((Activity) mContext, new AndPermisionUtil.PermisionCallBack() {
            @Override
            public void onGranted() {
                PictureSelector pictureSelector = PictureSelector.create((Activity) mContext);
                PictureSelectionModel pictureSelectionModel;
                if (type == 0) {
                    pictureSelectionModel = pictureSelector.openCamera(PictureMimeType.ofImage());
                } else {
                    pictureSelectionModel = pictureSelector.openGallery(PictureMimeType.ofImage());
                }
                pictureSelectionModel.loadImageEngine(GlideEngine.createGlideEngine())
                        .selectionMode(PictureConfig.SINGLE)
                        .compress(true)//是否压缩

//                        .enableCrop(true)//是否开启裁剪
//                        .cropImageWideHigh(500, 500)// 裁剪宽高比，设置如果大于图片本身宽高则无效
//                        .withAspectRatio(500, 500)//裁剪比例
//                        .freeStyleCropEnabled(true)//裁剪框是否可拖拽
//                        .circleDimmedLayer(true)// 是否开启圆形裁剪
//                        .setCircleDimmedColor(0x555555)//设置圆形裁剪背景色值
//                        .setCircleStrokeWidth(10)//设置圆形裁剪边框粗细
//                        .showCropFrame(false)// 是否显示裁剪矩形边框 圆形裁剪时建议设为false
//                        .showCropGrid(false)//是否显示裁剪矩形网格 圆形裁剪时建议设为false
//                        .rotateEnabled(true)//裁剪是否可旋转图片
//                        .scaleEnabled(true)//裁剪是否可放大缩小图片
//                        .isDragFrame(true)//是否可拖动裁剪框(固定)
//                        .hideBottomControls(true)//显示底部uCrop工具栏
                        .forResult(new OnResultCallbackListener<LocalMedia>() {
                            @Override
                            public void onResult(List<LocalMedia> result) {
                                // onResult Callback
                                if (result.size() > 0) {
                                    LocalMedia media = result.get(0);
                                    String path = "";
                                    if (!TextUtils.isEmpty(media.getAndroidQToPath())) {
                                        path = media.getAndroidQToPath();
                                    } else if (!TextUtils.isEmpty(media.getRealPath())) {
                                        path = media.getRealPath();
                                    }
                                    File file = new File(path);
                                    Log.i("file.getUsableSpace:" + file.getUsableSpace());

                                    Intent intent = new Intent(mContext, ImageRecognitionActivity.class);
                                    intent.putExtra("file", file);
                                    intent.putExtra("commCount_type", commCount_type);
                                    startActivity(intent);
                                }
                            }

                            @Override
                            public void onCancel() {
                                // onCancel Callback
                            }
                        });
            }

            @Override
            public void onDenied() {

            }
        }, Permission.Group.STORAGE);
    }

}
