package com.android.diandezhun.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StyleRes;
import com.android.baselibrary.dialog.Comm_Dialog;
import com.android.baselibrary.interface_.CommCallBack;
import com.android.baselibrary.tool.AndPermisionUtil;
import com.android.baselibrary.tool.CommToast;
import com.android.baselibrary.tool.Log;
import com.android.baselibrary.util.AnimUtil;
import com.android.baselibrary.util.NetUtils;
import com.android.baselibrary.util.StatusBarUtil_Dialog;
import com.android.baselibrary.util.Util;
import com.android.diandezhun.R;
import com.android.diandezhun.bean.VersionInfo;
import com.android.diandezhun.manager.Update_NotificationManager;
import com.android.diandezhun.util.AppUtil;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.db.DownloadManager;
import com.lzy.okgo.model.Progress;
import com.lzy.okgo.request.GetRequest;
import com.lzy.okserver.OkDownload;
import com.lzy.okserver.download.DownloadListener;
import com.lzy.okserver.download.DownloadTask;
import com.yanzhenjie.permission.runtime.Permission;
import java.io.File;

/**
 * Created by Administrator on 2018/8/29.
 */

public class Update_Dialog extends Dialog implements View.OnClickListener {
    public Update_Dialog(@NonNull Context context) {
        super(context, R.style.myDialog);
        mContext = context;
    }

    public Update_Dialog(@NonNull Context context, @StyleRes int themeResId) {
        super(context, themeResId);
        mContext = context;
    }

    protected Update_Dialog(@NonNull Context context, boolean cancelable, @Nullable OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
        mContext = context;
    }

    Context mContext;
    View view_bg;
    ImageView iv_close;

    TextView tv_download, tv_progress, tv_finish, tv_new_version, tv_tips;
    ProgressBar progressbar;
    RelativeLayout rl_loading;
    RelativeLayout rl_content;
    ScrollView scrollview;

    VersionInfo info;
    CommCallBack callBack;

    //????????????
    public void setCallBack(CommCallBack callBack) {
        this.callBack = callBack;
    }

    public void setData(VersionInfo info) {
        this.info = info;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_update);
        initView();
        StatusBarUtil_Dialog.setImmersiveStatusBar(this, false);
    }

    private void initView() {
        rl_content = findViewById(R.id.rl_content);
        view_bg = findViewById(R.id.view_bg);

        view_bg.setOnClickListener(this);
        rl_content.setOnClickListener(this);

        AnimUtil.fadeIn(view_bg);
        AnimUtil.fadeIn(rl_content);

        iv_close = findViewById(R.id.iv_close);
        tv_download = findViewById(R.id.tv_download);
        progressbar = findViewById(R.id.progressbar);
        tv_progress = findViewById(R.id.tv_progress);
        rl_loading = findViewById(R.id.rl_loading);
        tv_finish = findViewById(R.id.tv_finish);
        tv_new_version = findViewById(R.id.tv_new_version);
        tv_tips = findViewById(R.id.tv_tips);
        scrollview = findViewById(R.id.scrollview);

        iv_close.setOnClickListener(this);

        tv_new_version.setText("???????????????v" + info.version);
        tv_tips.setText(info.mark);

        if (info.isConstraint == 1) {       //????????????
            iv_close.setVisibility(View.GONE);
        } else {
            iv_close.setVisibility(View.VISIBLE);
        }

        initDownLoad();

        //view?????????????????????
        scrollview.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                // TODO Auto-generated method stub
                if (scrollview.getHeight() > Util.dip2px(mContext, 150)) {
                    LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) scrollview.getLayoutParams();
                    params.height = Util.dip2px(mContext, 150);
                    scrollview.setLayoutParams(params);
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.view_bg:
//                dismissWithAnim();
                break;
            case R.id.rl_content:
                //?????????????????????????????????
                break;
            case R.id.tv_cancel:
                dismissWithAnim();
                break;
            case R.id.iv_close:
                dismissWithAnim();
                break;
        }
    }

    public void dismissWithAnim() {
        if (view_bg.getAnimation() != null) return;
        AnimUtil.fadeOut(rl_content, null);
        AnimUtil.fadeOut(view_bg, new CommCallBack() {
            @Override
            public void onResult(Object obj) {
                dismiss();
            }
        });

    }

    @Override
    public void onBackPressed() {
        if (info.isConstraint == 1) {       //????????????
            Util.closeApp();
        } else {
            dismissWithAnim();
        }
    }

    String tagStr;

    private void initDownLoad() {
        tagStr = info.version;

        //???????????????????????????
        OkDownload.restore(DownloadManager.getInstance().getAll());

        if (OkDownload.getInstance().hasTask(tagStr)) {
            DownloadTask task = OkDownload.getInstance().getTask(tagStr);
            task.register(listener);
            setTask(task);
            setTag(tagStr);
            Progress progress = task.progress;
            refresh(progress);
            start();

        } else {
            setTask(null);
            setTag(null);
            rl_loading.setVisibility(View.GONE);  //?????????
            tv_download.setVisibility(View.VISIBLE);  //??????
            tv_finish.setVisibility(View.GONE);  //?????????
        }

        tv_download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!NetUtils.isWifiConnected(getContext()))  //wifi?????????
                {
                    Comm_Dialog.showCommDialog(getContext(), "", "????????????2G/3G/4G???????????????????????????????????????????????????", "????????????", "????????????",  new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            requestPermissions();
                        }
                    },  null);
                } else {
//                    startDownLoad();
                    requestPermissions();
                }
            }
        });
        tv_finish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    AppUtil.install(getContext(), task.progress.filePath);
                } catch (Exception e) {
                    CommToast.showToast(getContext(), e.getMessage());
                }
            }
        });
    }

    //??????????????? ???????????????
    private void requestPermissions() {
        AndPermisionUtil.requstPermision(mContext, new AndPermisionUtil.PermisionCallBack() {
            @Override
            public void onGranted() {
                startDownLoad();
            }

            @Override
            public void onDenied() {
                CommToast.showToast(mContext, "????????????????????????????????????");
            }
        }, Permission.Group.STORAGE);
    }

    //????????????
    private void startDownLoad() {
        if (info == null) return;

        if (info.isConstraint == 1) {       //????????????

        } else {   //???????????????
            Intent intent = new Intent(getContext(), Update_NotificationManager.class);
            intent.putExtra("tagStr", tagStr);
            getContext().startService(intent);
        }
        OkDownload.getInstance().setFolder(mContext.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS).getAbsolutePath());
        DownloadTask task;
        if (!OkDownload.getInstance().hasTask(info.version)) {
            GetRequest<File> request = OkGo.get(info.downloadUrl);
            task = OkDownload.request(info.version, request).save().register(listener);
            task.extra1(info);
            setTask(task);
            setTag(info.version);
            start();
        } else {
            start();
        }
        if (info.isConstraint == 1) {       //????????????
        } else    //???????????????
        {
//            dismissWithAnim();
        }
    }


    public DownloadTask task;
    public String tag;

    public void setTask(DownloadTask task) {
        this.task = task;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getTag() {
        return tag;
    }

    public void refresh(Progress progress) {
        rl_loading.setVisibility(View.GONE);  //?????????
        tv_download.setVisibility(View.GONE);  //??????
        tv_finish.setVisibility(View.GONE);  //?????????
        switch (progress.status) {
            case Progress.NONE:
                tv_download.setText("??????");
                tv_download.setVisibility(View.VISIBLE);  //??????
                break;
            case Progress.PAUSE:
                tv_download.setText("??????");
                tv_download.setVisibility(View.VISIBLE);  //??????
                break;
            case Progress.ERROR:
                tv_download.setText("??????");
                tv_download.setVisibility(View.VISIBLE);  //??????
                break;
            case Progress.WAITING:
                tv_progress.setText("?????????");
                rl_loading.setVisibility(View.VISIBLE);  //?????????
                break;
            case Progress.FINISH:
                tv_finish.setText("??????");
                tv_finish.setVisibility(View.VISIBLE);   //????????????

                boolean hasInstall = false;
                if (progress.extra2 != null) {
                    hasInstall = (boolean) progress.extra2;
                }
                if (!hasInstall) {
                    try {
                        AppUtil.install(getContext(), progress.filePath);
                    } catch (Exception e) {
                        CommToast.showToast(getContext(), e.getMessage());
                    }

                    task.extra2(true);
                    task.save();
                }
                break;
            case Progress.LOADING:    //?????????
                progressbar.setProgress((int) (progress.fraction * 100));
                Log.i("?????????" + progress.fraction * 100);
                rl_loading.setVisibility(View.VISIBLE);  //?????????
                tv_progress.setText(Util.numDecimalFormat(progress.fraction * 100 + "", 2) + "%");
                break;
        }
    }

    public void start() {
        Log.i("start");
        Progress progress = task.progress;
        switch (progress.status) {
            case Progress.PAUSE:
            case Progress.NONE:
            case Progress.ERROR:
                task.start();
                break;
            case Progress.LOADING:
                task.pause();
                break;
            case Progress.FINISH:
//                    MusicPlayUtil.loadR(progress.filePath);
                break;
        }
        refresh(progress);
    }

    DownloadListener listener = new DownloadListener(tag) {
        @Override
        public void onStart(Progress progress) {
            Log.i("????????????");
            refresh(progress);
        }

        @Override
        public void onProgress(Progress progress) {
            refresh(progress);
        }

        @Override
        public void onError(Progress progress) {
            refresh(progress);
        }

        @Override
        public void onFinish(File file, Progress progress) {
            refresh(progress);
        }

        @Override
        public void onRemove(Progress progress) {

        }
    };

}
