package com.android.diandezhun.ui.activity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.Point;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.baselibrary.interface_.CommCallBack;
import com.android.baselibrary.interface_.OkHttpCallBack;
import com.android.baselibrary.responce.BaseResponce;
import com.android.baselibrary.tool.CommLoading;
import com.android.baselibrary.tool.CommToast;
import com.android.baselibrary.tool.Log;
import com.android.baselibrary.util.FileUtil;
import com.android.baselibrary.util.GlideUtil;
import com.android.baselibrary.util.Util;
import com.android.baselibrary.view.MultiStateView;
import com.android.diandezhun.R;
import com.android.diandezhun.bean.CommCount_Type;
import com.android.diandezhun.bean.Count_DetailInfo;
import com.android.diandezhun.bean.ImageRec_CircleInfo;
import com.android.diandezhun.dialog.Count_AddDetails_DialogFragment;
import com.android.diandezhun.dialog.Count_EditSize_DialogFragment;
import com.android.diandezhun.event.Event_Count_BgChange;
import com.android.diandezhun.event.Event_Count_ChangeCircleRadius;
import com.android.diandezhun.event.Event_Count_LocalData_CountDataChange;
import com.android.diandezhun.event.Event_CuttingBox_Move;
import com.android.diandezhun.manager.API_Manager;
import com.android.diandezhun.manager.ImageRecConfig;
import com.android.diandezhun.responce.ImageRecognitionResponce;
import com.android.diandezhun.ui.view.CommCount_BottomBntView;
import com.android.diandezhun.ui.view.CommCount_CircleContainer;
import com.android.diandezhun.ui.view.CommCount_ControlView;
import com.android.diandezhun.ui.view.CommCount_DetailView;
import com.android.diandezhun.ui.view.DragView;
import com.android.diandezhun.ui.view.DrawView;
import com.android.diandezhun.ui.view.MoveLayout;
import com.android.diandezhun.ui.view.PaletteView;
import com.android.diandezhun.util.FileSizeUtil;
import com.android.diandezhun.util.CommCountUtil;
import com.google.gson.Gson;
import com.luck.picture.lib.photoview.OnMatrixChangedListener;
import com.luck.picture.lib.photoview.OnPhotoTapListener;
import com.luck.picture.lib.photoview.OnViewDragListener;
import com.luck.picture.lib.photoview.PhotoView;
import com.nanchen.compresshelper.CompressHelper;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

public class ImageRecognitionActivity extends BaseActivity {

    @BindView(R.id.multiplestatusView)
    MultiStateView multiplestatusView;
    @BindView(R.id.ll_head)
    LinearLayout ll_head;
    @BindView(R.id.iv_img)
    PhotoView iv_img;
    @BindView(R.id.rl_content)
    RelativeLayout rl_content;
    @BindView(R.id.rl_view_container)
    RelativeLayout rl_view_container;

    @BindView(R.id.dragview_container)
    DragView dragview_container;
    @BindView(R.id.dragview_container_draw)
    DragView dragview_container_draw;

    @BindView(R.id.rl_circle_container)
    CommCount_CircleContainer rl_circle_container;
    @BindView(R.id.ll_btn_default)
    LinearLayout ll_btn_default;
    @BindView(R.id.ll_btn_edit)
    LinearLayout ll_btn_edit;
    @BindView(R.id.count_detailview)
    CommCount_DetailView count_detailview;
    @BindView(R.id.btn_border_type_rect)
    CommCount_BottomBntView btn_border_type_rect;  //??????
    @BindView(R.id.btn_border_type_draw)
    CommCount_BottomBntView btn_border_type_draw;  //??????
    @BindView(R.id.paletteview)
    PaletteView paletteview;    //?????????
    @BindView(R.id.ll_show_control_bar)
    LinearLayout ll_show_control_bar;   //??????????????????
    @BindView(R.id.control_view)
    CommCount_ControlView control_view;   //??????
    @BindView(R.id.iv_eyes)
    ImageView iv_eyes;
    @BindView(R.id.iv_showhiden)
    TextView iv_showhiden;


    CommCount_Type commCount_type;
    File sourceFile;   //????????????
    File uploadFile;   //???????????????
    ImageRecognitionResponce responce;
    int border_type = 0; //????????????  0????????????   1???????????????
    Count_DetailInfo countDetailInfo;   //???????????????
    boolean needCrop = false;

    int contentWidth = 0;   //???????????????
    int contentHeight = 0;  //???????????????
    int container_Width = 0;
    int container_Height = 0;
    double scale = 0;
    Bitmap bitmap;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_image_recognition;
    }

    @Override
    protected View getImmersionTitleBar() {
        return ll_head;
    }

    @Override
    protected boolean isRegistEventbus() {
        return true;
    }

    @Override
    protected void initView() {
        countDetailInfo = new Count_DetailInfo();
        commCount_type = (CommCount_Type) getIntent().getSerializableExtra("commCount_type");
        sourceFile = (File) getIntent().getSerializableExtra("file");
        needCrop = (boolean) getIntent().getBooleanExtra("needCrop", false);

        if (commCount_type != null) {
            countDetailInfo.countType = commCount_type.title;
            countDetailInfo.countType_Gson = new Gson().toJson(commCount_type);
        }

//        if (needCrop) {
//            iv_img.setScaleType(ImageView.ScaleType.CENTER_CROP);
//        } else {
//            iv_img.setScaleType(ImageView.ScaleType.FIT_CENTER);
//        }

        //???????????????4???3
//        int perviewWidth = getResources().getDisplayMetrics().widthPixels;
//        int perviewHeight = (int) (perviewWidth * 4d / 3d);
//        Log.i("perviewWidth:" + perviewWidth + "  perviewHeight:" + perviewHeight);
//        rl_content.setLayoutParams(new LinearLayout.LayoutParams(perviewWidth, perviewHeight));

        GlideUtil.displayImage(mContext, sourceFile, iv_img, -1);
//        iv_img.setImageURI(Uri.fromFile(sourceFile));

        rl_content.post(new Runnable() {
            @Override
            public void run() {
                Log.i("???????????????????????????");

//                Log.i("iv_img.getDrawable().getIntrinsicWidth():"+ iv_img.getDrawable().getIntrinsicWidth());
                //????????????
                uploadFile = compare(sourceFile);
                //????????????
//                if (needCrop) {
//                    CommCountUtil.crop(mContext, uploadFile, rl_content.getWidth(), rl_content.getHeight());
//                }

                Log.i("sourceFile???" + sourceFile.getPath());
                Log.i("uploadFile???" + uploadFile.getPath());

                initSize();
                getData();
            }
        });

        count_detailview.setData(countDetailInfo);
        rl_circle_container.init(countDetailInfo);
        rl_circle_container.setCallBack(onCircleChange);
        showBtn();
        showHideControl();

        iv_img.setOnMatrixChangeListener(new OnMatrixChangedListener() {
            @Override
            public void onMatrixChanged(Matrix matrix) {
                float transX = iv_img.attacher.getValue(iv_img.attacher.mBaseMatrix, Matrix.MTRANS_X);
                float transY = iv_img.attacher.getValue(iv_img.attacher.mBaseMatrix, Matrix.MTRANS_Y);
//                Log.i("transX:" + transX + " transY:" + transY);
                float transX_Img = iv_img.attacher.getValue(matrix, Matrix.MTRANS_X);
                float transY_Img = iv_img.attacher.getValue(matrix, Matrix.MTRANS_Y);
                float scaleX_Img = iv_img.attacher.getValue(matrix, Matrix.MSCALE_X);
                float scaleY_Img = iv_img.attacher.getValue(matrix, Matrix.MSCALE_Y);

                float MSKEW_X = iv_img.attacher.getValue(matrix, Matrix.MSKEW_X);
                float MSKEW_Y = iv_img.attacher.getValue(matrix, Matrix.MSKEW_Y);
                float MPERSP_0 = iv_img.attacher.getValue(matrix, Matrix.MPERSP_0);
                float MPERSP_1 = iv_img.attacher.getValue(matrix, Matrix.MPERSP_1);
                float MPERSP_2 = iv_img.attacher.getValue(matrix, Matrix.MPERSP_2);

                Matrix matrix1 = new Matrix();
                matrix1.setValues(new float[]{scaleX_Img, MSKEW_X, transX_Img - transX, MSKEW_Y, scaleY_Img, transY_Img - transY, MPERSP_0, MPERSP_1, MPERSP_2});
                rl_view_container.setAnimationMatrix(matrix1);
            }
        });

        iv_img.setAllowParentInterceptOnEdge(false);
        //???????????????
        iv_img.setOnViewDragListener(new OnViewDragListener() {
            @Override
            public void onDrag(float x, float y, float dx, float dy) {
//                Log.i("onDrag.x:" + x + " dx:" + dx);
                float transX = iv_img.attacher.getValue(iv_img.attacher.mBaseMatrix, Matrix.MTRANS_X);
                float transY = iv_img.attacher.getValue(iv_img.attacher.mBaseMatrix, Matrix.MTRANS_Y);
                x = x - transX;
                y = y - transY;

                //??????????????????????????????????????? ??????????????????????????????
                if (border_type == 0) {
                    MoveLayout moveLayout = dragview_container.getMoveLayout();
                    if (moveLayout != null) {
                        Point leftTop = new Point(moveLayout.getLeft(), moveLayout.getTop());
                        Point rightBottom = new Point(moveLayout.getRight(), moveLayout.getBottom());
                        Point target = new Point((int) x, (int) y);
                        if (CommCountUtil.isInRect(leftTop, rightBottom, target)) {
                            if (iv_img.getScale() < 1.01) {
                                dragview_container.onDrag((int) x, (int) y, (int) dx, (int) dy);
                            }
                        }
                    }
                } else {
                    MoveLayout moveLayout = dragview_container_draw.getMoveLayout();
                    if (moveLayout != null) {
                        Point leftTop = new Point(moveLayout.getLeft(), moveLayout.getTop());
                        Point rightBottom = new Point(moveLayout.getRight(), moveLayout.getBottom());
                        Point target = new Point((int) x, (int) y);
                        if (CommCountUtil.isInRect(leftTop, rightBottom, target)) {
                            if (iv_img.getScale() < 1.01) {
                                dragview_container_draw.onDrag((int) x, (int) y, (int) dx, (int) dy);
                            }
                        }
                    }
                }
            }
        });

        //??????????????????
        iv_img.setOnPhotoTapListener(new OnPhotoTapListener() {
            @Override
            public void onPhotoTap(ImageView view, float x, float y) {
                Log.i("onPhotoTap.x:" + x + " y:" + y);
                //????????????
                rl_circle_container.click(x * container_Width, y * container_Height);
            }
        });
        //???????????????
        paletteview.finishCallback = new PaletteView.FinishCallback() {
            @Override
            public void drawFinish() {
                //????????????
                List<Point> points = paletteview.pointList;
                Bitmap bitmap = Bitmap.createBitmap(paletteview.mBufferBitmap);
                drawFrame_Draw(points, bitmap);
            }
        };
    }

    //????????????
    private File compare(File oldFile) {
        Log.i("oldFile:" + FileSizeUtil.getAutoFileOrFilesSize(oldFile.getPath()));
        File newFile = new CompressHelper.Builder(this)
                .setMaxWidth(1600)  // ?????????????????????720
                .setMaxHeight(1600) // ?????????????????????960
                .setQuality(98)    // ?????????????????????80
                .setCompressFormat(Bitmap.CompressFormat.JPEG) // ?????????????????????jpg??????
                .setDestinationDirectoryPath(FileUtil.getDiskCacheRootDir(mContext))
                .setFileNamePrefix("Upload_")
                .build()
                .compressToFile(oldFile);
        Log.i("newFile:" + FileSizeUtil.getAutoFileOrFilesSize(newFile.getPath()));
        return newFile;
    }

    //??????????????????
    private void initSize() {
        //??????????????????
        contentWidth = rl_content.getWidth();
        contentHeight = rl_content.getHeight();

        bitmap = BitmapFactory.decodeFile(uploadFile.getPath());
        Log.i("bitmap.getWidth:" + bitmap.getWidth() + "   bitmap.getHeight:" + bitmap.getHeight());

        double imgWidth = bitmap.getWidth();
        double imgHeight = bitmap.getHeight();

        //????????????????????????
        double xScale = imgWidth / contentWidth;
        double yScale = imgHeight / contentHeight;
        Log.i("xScale:" + xScale + " yScale:" + yScale);
        if (xScale > yScale) {
            //X?????????
            scale = xScale;
            container_Width = contentWidth;
            container_Height = (int) (imgHeight / scale);
        } else {
            scale = yScale;
            container_Width = (int) (imgWidth / scale);
            container_Height = contentHeight;
        }
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(container_Width, container_Height);
        params.addRule(RelativeLayout.CENTER_IN_PARENT);
        rl_view_container.setLayoutParams(params);

        Log.i(" ??????scale:" + (scale > 0 ? "??????" : "??????") + scale + "  container_Width:" + container_Width + "  container_Height:" + container_Height);
        countDetailInfo.scale = scale;

        countDetailInfo.filePath = sourceFile.getAbsolutePath();
    }

    @Override
    protected void initData() {

    }

    //??????????????????
    private void getData() {
        Log.i("????????????");
        API_Manager.imgCompute(mContext, commCount_type.type, uploadFile, new OkHttpCallBack<BaseResponce<ImageRecognitionResponce>>() {
            @Override
            public void onSuccess(BaseResponce<ImageRecognitionResponce> baseResponce) {
                Log.i("????????????");
                responce = baseResponce.getData();

                countDetailInfo.circles = responce.circles;
                countDetailInfo.frame = responce.frame;
                //????????????????????????
                for (ImageRec_CircleInfo circleInfo : responce.circles) {
                    if (countDetailInfo.radius_min == 0) {
                        countDetailInfo.radius_min = circleInfo.r;
                    }
                    if (countDetailInfo.radius_max == 0) {
                        countDetailInfo.radius_max = circleInfo.r;
                    }
                    if (circleInfo.r > countDetailInfo.radius_max) {
                        countDetailInfo.radius_max = circleInfo.r;
                    }
                    if (circleInfo.r < countDetailInfo.radius_min) {
                        countDetailInfo.radius_min = circleInfo.r;
                    }
                }
                countDetailInfo.mostRadius = countDetailInfo.radius_min;  //??????
                countDetailInfo.radius = (int) (countDetailInfo.mostRadius / scale);

                control_view.setCountDetailInfo(countDetailInfo);
                count_detailview.setData(countDetailInfo);
                rl_circle_container.init(countDetailInfo);
                if (responce.circles != null && responce.circles.size() > 0) {
                    drawFrame();
                } else {
                    CommToast.showToast(mContext, "???????????????" + commCount_type.title + "????????????????????????");
                }
            }

            @Override
            public void onFailure(BaseResponce<ImageRecognitionResponce> baseResponce) {
                CommLoading.dismissLoading();
                CommToast.showToast(mContext, baseResponce.getMessage());
            }
        });
    }

    CommCallBack onCircleChange = new CommCallBack() {
        @Override
        public void onResult(Object obj) {
            //????????????
            int count = (int) obj;
            countDetailInfo.count = count;
            count_detailview.setData(countDetailInfo);
        }
    };

    //??????
    private void drawCircles() {
        if (border_type == 0) {
            MoveLayout moveLayout = dragview_container.getMoveLayout();
            if (moveLayout != null) {
                Point leftTop = moveLayout.getLeftTop();
                Point rightBottom = moveLayout.getRightBottom();
                rl_circle_container.drawCircles(leftTop, rightBottom);
            }
        } else {
            MoveLayout moveLayout = dragview_container_draw.getMoveLayout();
            if (moveLayout != null) {
                DrawView drawView = (DrawView) moveLayout.getSelfView();
                rl_circle_container.drawCircles(drawView.getCurrentPointList());
            }
        }
    }

    //????????????
    private void drawFrame() {
        int padding = ImageRecConfig.getFramePadding(mContext);
        int left = (int) ((responce.frame.ltx) / scale) - padding;
        int top = (int) ((responce.frame.lty) / scale) - padding;
        int right = (int) ((responce.frame.rbx) / scale) + padding;
        int bottom = (int) ((responce.frame.rby) / scale) + padding;
        dragview_container.addDragView(null, left, top, right, bottom, false);
        dragview_container.post(new Runnable() {
            @Override
            public void run() {
                drawCircles();
            }
        });
    }

    //???????????????
    private void drawFrame_Draw(List<Point> points, Bitmap mBufferBitmap) {
        Point[] points1 = CommCountUtil.getRect(points);
        int margin = Util.dip2px(mContext, 13);
        int left = points1[0].x - margin;
        int top = points1[0].y - margin;
        int right = points1[1].x + margin;
        int bottom = points1[1].y + margin;

        DrawView drawView = new DrawView(mContext, points, points1[0].x, points1[0].y, points1[1].x - points1[0].x, points1[1].y - points1[0].y);
        dragview_container_draw.clearView();
        dragview_container_draw.addDragView(drawView, left, top, right, bottom, false);
        dragview_container_draw.post(new Runnable() {
            @Override
            public void run() {
                drawCircles();
            }
        });
        paletteview.clear();
        paletteview.setVisibility(View.GONE);
        dragview_container_draw.setVisibility(View.VISIBLE);

    }

    //????????????
    private void showBtn() {
        btn_border_type_rect.setVisibility(View.GONE);
        btn_border_type_draw.setVisibility(View.GONE);
        dragview_container.setVisibility(View.GONE);
        dragview_container_draw.setVisibility(View.GONE);
        paletteview.setVisibility(View.GONE);
        if (border_type == 0) {
            //??????????????? ??????????????????
            btn_border_type_draw.setVisibility(View.VISIBLE);
            dragview_container.setVisibility(View.VISIBLE);
        } else if (border_type == 1) {
            //??????????????? ??????????????????
            btn_border_type_rect.setVisibility(View.VISIBLE);
            paletteview.setVisibility(View.VISIBLE);
        }

    }

    @OnClick({R.id.btn_pre_step, R.id.btn_buchongxiangqing, R.id.btn_jixupaizhao, R.id.ll_save, R.id.btn_border_type_rect, R.id.btn_border_type_draw, R.id.ll_show_control_bar})
    public void onViewClicked(View view) {
        Map<String, Object> map = new HashMap<>();
        map.put("countDetailInfo", countDetailInfo);
        switch (view.getId()) {
            case R.id.btn_pre_step:     //?????????
                if (border_type == 0) {
                    finish();
                } else {
                    border_type = 0;
                    showBtn();
                    drawCircles();
                }
                break;
            case R.id.btn_tiaojiejingdu:   //????????????
                Count_EditSize_DialogFragment editSizeDialogFragment = (Count_EditSize_DialogFragment) Count_EditSize_DialogFragment.showDialog(getSupportFragmentManager(), map);

                break;
            case R.id.btn_buchongxiangqing:     //????????????
                Count_AddDetails_DialogFragment dialogFragment = (Count_AddDetails_DialogFragment) Count_AddDetails_DialogFragment.showDialog(getSupportFragmentManager(), map);
                dialogFragment.setCallback(new CommCallBack() {
                    @Override
                    public void onResult(Object obj) {
                        count_detailview.setData(countDetailInfo);
                        dialogFragment.dismissWithAnim();
                    }
                });
                break;
            case R.id.btn_jixupaizhao:  //????????????
                countDetailInfo.save();
                finish();
                break;
            case R.id.ll_save:          //??????
                countDetailInfo.save();
                Event_Count_LocalData_CountDataChange event = new Event_Count_LocalData_CountDataChange();
                event.countType = countDetailInfo.countType;
                EventBus.getDefault().post(event);
                finish();
                break;
            case R.id.btn_border_type_rect:  //?????????????????? ??????
                border_type = 0;
                drawCircles();
                showBtn();
                break;
            case R.id.btn_border_type_draw:  //?????????????????? ??????
                border_type = 1;
                rl_circle_container.clearCircle();
                showBtn();
                break;
            case R.id.ll_show_control_bar:  //?????????????????????
                ImageRecConfig.addShowIndex(mContext);
                showHideControl();
                break;
        }
    }

    //??????????????? ????????????
    private void showHideControl() {
        int show_index = ImageRecConfig.getShowIndex(mContext);
        switch (show_index) {
            case 2:
                iv_eyes.setImageResource(R.drawable.ico_commcount_eyes_show);
                iv_showhiden.setText("??????");
                break;
            default:
                iv_eyes.setImageResource(R.drawable.ico_commcount_eyes_hide);
                iv_showhiden.setText("??????");
                break;
        }
        control_view.checkHiden();
    }

    //???????????????
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(Event_CuttingBox_Move event) {
        drawCircles();
    }

    //??????????????????
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(Event_Count_ChangeCircleRadius event) {
        drawCircles();
    }

    //??????????????????
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(Event_Count_BgChange event) {
        Log.i("??????????????????:" + ImageRecConfig.getFrame_BgColor(mContext));

        dragview_container.invalidate();
        dragview_container_draw.invalidate();

        MoveLayout moveLayout = dragview_container_draw.getMoveLayout();
        if (moveLayout != null) {
            DrawView drawView = (DrawView) moveLayout.getSelfView();
            //            drawView.mBufferBitmap = null;
            drawView.invalidate();
        }
    }
}
