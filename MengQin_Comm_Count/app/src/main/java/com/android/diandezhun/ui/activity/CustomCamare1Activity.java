package com.android.diandezhun.ui.activity;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.PointF;
import android.graphics.Rect;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.hardware.camera2.params.StreamConfigurationMap;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.text.TextUtils;
import android.util.Rational;
import android.util.Size;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.OrientationEventListener;
import android.view.Surface;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.camera.core.AspectRatio;
import androidx.camera.core.Camera;
import androidx.camera.core.CameraControl;
import androidx.camera.core.CameraInfo;
import androidx.camera.core.CameraInfoUnavailableException;
import androidx.camera.core.CameraProvider;
import androidx.camera.core.CameraSelector;
import androidx.camera.core.FocusMeteringAction;
import androidx.camera.core.FocusMeteringResult;
import androidx.camera.core.ImageAnalysis;
import androidx.camera.core.ImageCapture;
import androidx.camera.core.ImageCaptureException;
import androidx.camera.core.ImageProxy;
import androidx.camera.core.MeteringPoint;
import androidx.camera.core.MeteringPointFactory;
import androidx.camera.core.Preview;
import androidx.camera.core.SurfaceOrientedMeteringPointFactory;
import androidx.camera.core.ZoomState;
import androidx.camera.core.impl.PreviewConfig;
import androidx.camera.core.internal.utils.ImageUtil;
import androidx.camera.lifecycle.ProcessCameraProvider;
import androidx.camera.view.LifecycleCameraController;
import androidx.camera.view.PreviewView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LifecycleRegistry;
import androidx.lifecycle.Observer;

import com.android.baselibrary.tool.AndPermisionUtil;
import com.android.baselibrary.tool.CommLoading;
import com.android.baselibrary.tool.CommToast;
import com.android.baselibrary.tool.Log;
import com.android.baselibrary.util.FileUtil;
import com.android.baselibrary.util.GlideEngine;
import com.android.diandezhun.R;
import com.android.diandezhun.bean.CommCount_Type;
import com.android.diandezhun.manager.UserManager;
import com.android.diandezhun.util.CommCountUtil;
import com.android.diandezhun.util.FileSizeUtil;
import com.google.common.util.concurrent.ListenableFuture;
import com.luck.picture.lib.PictureSelectionModel;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.camera.listener.CameraListener;
import com.luck.picture.lib.camera.listener.ImageCallbackListener;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.config.PictureSelectionConfig;
import com.luck.picture.lib.entity.LocalMedia;
import com.luck.picture.lib.listener.OnResultCallbackListener;
import com.luck.picture.lib.permissions.PermissionChecker;
import com.luck.picture.lib.tools.BitmapUtils;
import com.luck.picture.lib.tools.DateUtils;
import com.luck.picture.lib.tools.MediaUtils;
import com.luck.picture.lib.tools.PictureFileUtils;
import com.luck.picture.lib.tools.SdkVersionUtils;
import com.luck.picture.lib.tools.StringUtils;
import com.nanchen.compresshelper.CompressHelper;
import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.runtime.Permission;
import com.youth.banner.util.LogUtils;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * ???????????????
 */
public class CustomCamare1Activity extends BaseActivity {

    @BindView(R.id.ll_head)
    LinearLayout ll_head;
    @BindView(R.id.iv_flash)
    ImageView iv_flash;
    @BindView(R.id.iv_take_photo)
    ImageView iv_take_photo;
    @BindView(R.id.seekbar)
    SeekBar seekbar;
    @BindView(R.id.previewview)
    PreviewView previewview;

    CommCount_Type commCount_type;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_custom_camare;
    }

    /**
     * ???????????????
     */
    private static final int TYPE_FLASH_AUTO = 0x021;
    private static final int TYPE_FLASH_ON = 0x022;
    private static final int TYPE_FLASH_OFF = 0x023;

    private int type_flash = TYPE_FLASH_AUTO;
    File mOutMediaFile;
    PictureSelectionConfig mConfig;


    @Override
    protected View getImmersionTitleBar() {
        return ll_head;
    }

    @Override
    protected boolean isBarDarkFontAndIcon() {
        return false;
    }


    ImageCapture imageCapture;
    Camera camera;
    CameraControl cameraControl;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void initView() {
        mConfig = PictureSelectionConfig.getCleanInstance();
        seekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser) {
                    cameraControl.setLinearZoom((float) progress / 100f);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        //???????????????4???3
        int perviewWidth = getResources().getDisplayMetrics().widthPixels;
        int perviewHeight = (int) (perviewWidth * 4d / 3d);
        Log.i("perviewWidth:" + perviewWidth + "  perviewHeight:" + perviewHeight);
        previewview.setLayoutParams(new LinearLayout.LayoutParams(perviewWidth, perviewHeight));

        previewview.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // ??????onTouchEvent
                return mGestureDetector.onTouchEvent(event);
            }
        });
        mGestureDetector = new GestureDetector(mContext, onGestureListener);
        mGestureDetector.setOnDoubleTapListener(onDoubleTapListener);

        previewview.post(new Runnable() {
            @Override
            public void run() {
                initCamera();
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void initCamera() {
        ListenableFuture<ProcessCameraProvider> cameraProviderFuture = ProcessCameraProvider.getInstance(this);
        cameraProviderFuture.addListener(() -> {
            try {
                // ?????????????????????????????????
                ProcessCameraProvider cameraProvider = cameraProviderFuture.get();

                // ???????????????????????????????????????
                Preview preview = new Preview.Builder()
//                        .setTargetResolution(new Size(previewview.getWidth(), previewview.getHeight()))
//                        .setTargetResolution(new Size(3311, 4608))
                        .build();

                // ???????????????????????????????????????
                imageCapture = new ImageCapture.Builder()
                        .setCaptureMode(ImageCapture.CAPTURE_MODE_MINIMIZE_LATENCY)
//                        .setTargetAspectRatio(AspectRatio.RATIO_4_3)
//                        .setTargetResolution(new Size(3311, 4608))
                        .build();
                //        imageCapture.setCropAspectRatio(new Rational(previewview.getWidth(), previewview.getHeight()));
                Log.i("previewview.getWidth():" + previewview.getWidth() + "  previewview.getHeight():" + previewview.getHeight());

                // ?????????????????????????????????
                CameraSelector cameraSelector = new CameraSelector.Builder().requireLensFacing(CameraSelector.LENS_FACING_BACK).build();

                ImageAnalysis mImageAnalysis = new ImageAnalysis.Builder()
                        // ?????????
//                        .setTargetResolution(new Size(previewview.getWidth(), previewview.getHeight()))
                        // ???????????????????????????????????????????????????????????????????????????
                        .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                        .build();

                // ???????????????????????????????????????????????????????????????
                camera = cameraProvider.bindToLifecycle(((LifecycleOwner) this), cameraSelector, preview, mImageAnalysis, imageCapture);
                cameraControl = camera.getCameraControl(); //???????????????

                // ????????????????????????????????????
                preview.setSurfaceProvider(previewview.getSurfaceProvider());

                camera.getCameraInfo().getZoomState().observeForever(new Observer<ZoomState>() {
                    @Override
                    public void onChanged(ZoomState zoomState) {
                        seekbar.setProgress((int) (zoomState.getLinearZoom() * 100));
                    }
                });
                setFlashRes();

//                OrientationEventListener orientationEventListener = new OrientationEventListener(mContext) {
//                    @Override
//                    public void onOrientationChanged(int orientation) {
//                        int rotation;
//                        // Monitors orientation values to determine the target rotation value
//                        if (orientation >= 45 && orientation < 135) {
//                            rotation = Surface.ROTATION_270;
//                        } else if (orientation >= 135 && orientation < 225) {
//                            rotation = Surface.ROTATION_180;
//                        } else if (orientation >= 225 && orientation < 315) {
//                            rotation = Surface.ROTATION_90;
//                        } else {
//                            rotation = Surface.ROTATION_0;
//                        }
//                        Log.i("orientation:"+ orientation);
//                        imageCapture.setTargetRotation(rotation);
//                    }
//                };
//                orientationEventListener.enable();
                imageCapture.setTargetRotation(Surface.ROTATION_0);

            } catch (InterruptedException | ExecutionException e) {
            }
        }, ContextCompat.getMainExecutor(this));
    }


    private GestureDetector mGestureDetector;
    /**
     * ????????????
     */
    private float currentDistance = 0;
    private float lastDistance = 0;
    GestureDetector.OnGestureListener onGestureListener = new GestureDetector.OnGestureListener() {
        @Override
        public boolean onDown(MotionEvent e) {
            LogUtils.i("onDown: ??????");
            return true;
        }

        @Override
        public void onShowPress(MotionEvent e) {
            LogUtils.i("onShowPress: ?????????????????????");
        }

        @Override
        public boolean onSingleTapUp(MotionEvent e) {
            LogUtils.i("onSingleTapUp: ???????????????????????????");
            return true;
        }

        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            LogUtils.i("onScroll: ???????????????");
            // ?????????????????????
            if (e2.getPointerCount() >= 2) {

                //event???????????????????????????????????????????????????????????????????????????????????????event.getX(0)/getY(0)??????
                float offSetX = e2.getX(0) - e2.getX(1);
                float offSetY = e2.getY(0) - e2.getY(1);
                //??????????????????????????????????????????X,Y??????????????????????????????????????????
                currentDistance = (float) Math.sqrt(offSetX * offSetX + offSetY * offSetY);
                if (lastDistance == 0) {//??????????????????????????????
                    lastDistance = currentDistance;
                } else {
                    if (currentDistance - lastDistance > 10) {
                        // ??????
                        if (mCustomTouchListener != null) {
                            mCustomTouchListener.zoom();
                        }
                    } else if (lastDistance - currentDistance > 10) {
                        // ??????
                        if (mCustomTouchListener != null) {
                            mCustomTouchListener.ZoomOut();
                        }
                    }
                }
                //????????????????????????????????????????????????????????????lastDistance????????????????????????
                //?????????????????????move??????????????????????????????????????????????????????????????????????????????????????????10
                //???????????????????????????????????????????????????????????????????????????????????????
                //??????????????????????????????????????????????????????????????????????????????????????????
                lastDistance = currentDistance;
            }
            return true;
        }

        @Override
        public void onLongPress(MotionEvent e) {
            LogUtils.i("onLongPress: ????????????");
            if (mCustomTouchListener != null) {
                mCustomTouchListener.longClick(e.getX(), e.getY());
            }
        }

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            LogUtils.i("onFling: ???????????????");
            currentDistance = 0;
            lastDistance = 0;
            return true;
        }
    };

    CustomTouchListener mCustomTouchListener = new CustomTouchListener() {
        @Override
        public void zoom() {
            float linearZoom = camera.getCameraInfo().getZoomState().getValue().getLinearZoom();
//            Log.i("?????? linearZoom:" + linearZoom);
            linearZoom += 0.1;
            if (linearZoom > 1) linearZoom = 1;
            cameraControl.setLinearZoom(linearZoom);
        }

        @Override
        public void ZoomOut() {
            float linearZoom = camera.getCameraInfo().getZoomState().getValue().getLinearZoom();
//            Log.i("?????? linearZoom:" + linearZoom);
            linearZoom -= 0.1;
            if (linearZoom < 0) linearZoom = 0;
            cameraControl.setLinearZoom(linearZoom);
        }

        @Override
        public void click(float x, float y) {
//            Log.i("??????: x" + x + " y:" + y);
            MeteringPointFactory factory = new SurfaceOrientedMeteringPointFactory(10, 10);
            MeteringPoint point = factory.createPoint(x, y);
            FocusMeteringAction action = new FocusMeteringAction.Builder(point, FocusMeteringAction.FLAG_AF)
//                    .addPoint(point2, FocusMeteringAction.FLAG_AE) // could have many
                    .setAutoCancelDuration(5, TimeUnit.SECONDS)
                    .build();

            ListenableFuture future = cameraControl.startFocusAndMetering(action);
            future.addListener(() -> {
                try {
                    FocusMeteringResult result = (FocusMeteringResult) future.get();
                    Log.i("result:" + result.toString());
                    // process the result
                } catch (Exception e) {
                }
            }, ContextCompat.getMainExecutor(mContext));
        }

        @Override
        public void doubleClick(float x, float y) {
            Log.i("??????");
            // ??????????????????
            float zoomRatio = camera.getCameraInfo().getZoomState().getValue().getZoomRatio();
            float minZoomRatio = camera.getCameraInfo().getZoomState().getValue().getMinZoomRatio();
            if (zoomRatio > minZoomRatio) {
                cameraControl.setLinearZoom(0f);
            } else {
                cameraControl.setLinearZoom(0.5f);
            }
        }

        @Override
        public void longClick(float x, float y) {

        }
    };

    GestureDetector.OnDoubleTapListener onDoubleTapListener = new GestureDetector.OnDoubleTapListener() {
        @Override
        public boolean onSingleTapConfirmed(MotionEvent e) {
            LogUtils.i("onSingleTapConfirmed: ???????????????");
            if (mCustomTouchListener != null) {
                mCustomTouchListener.click(e.getX(), e.getY());
            }
            return true;
        }

        @Override
        public boolean onDoubleTap(MotionEvent e) {
            LogUtils.i("onDoubleTap: ??????");
            if (mCustomTouchListener != null) {
                mCustomTouchListener.doubleClick(e.getX(), e.getY());
            }
            return true;
        }

        @Override
        public boolean onDoubleTapEvent(MotionEvent e) {
            LogUtils.i("onDoubleTapEvent: ????????????????????????");
            return true;
        }
    };

    public interface CustomTouchListener {
        /**
         * ??????
         */
        void zoom();

        /**
         * ??????
         */
        void ZoomOut();

        /**
         * ??????
         */
        void click(float x, float y);

        /**
         * ??????
         */
        void doubleClick(float x, float y);

        /**
         * ??????
         */
        void longClick(float x, float y);
    }

    //???????????????
    private void setFlashRes() {
        switch (type_flash) {
            case TYPE_FLASH_AUTO:
                iv_flash.setImageResource(R.drawable.ico_customcamare_light_auto);
                imageCapture.setFlashMode(ImageCapture.FLASH_MODE_AUTO);
                break;
            case TYPE_FLASH_ON:
                iv_flash.setImageResource(R.drawable.ico_customcamare_light_open);
                imageCapture.setFlashMode(ImageCapture.FLASH_MODE_ON);
                break;
            case TYPE_FLASH_OFF:
                iv_flash.setImageResource(R.drawable.ico_customcamare_light_close);
                imageCapture.setFlashMode(ImageCapture.FLASH_MODE_OFF);
                break;
        }
    }

    @Override
    protected void initData() {
        commCount_type = (CommCount_Type) getIntent().getSerializableExtra("commCount_type");
    }

    @OnClick({R.id.iv_flash, R.id.iv_take_photo, R.id.iv_photo, R.id.iv_camare, R.id.iv_back})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_flash:   //???????????????
                type_flash++;
                if (type_flash > 0x023)
                    type_flash = TYPE_FLASH_AUTO;
                setFlashRes();
                break;
            case R.id.iv_take_photo:   //??????
                mOutMediaFile = createImageFile();
                takePicture();
                break;
            case R.id.iv_photo:   //??????
                if (UserManager.isLoginAndShowLogin((Activity) mContext)) {
                    selectImg(1);
                }
                break;
            case R.id.iv_camare:   //?????????
                if (UserManager.isLoginAndShowLogin((Activity) mContext)) {
                    selectImg(0);
                }
                break;
            case R.id.iv_back:
                finish();
                break;
        }
    }

    public void takePicture() {
        long beginTime = System.currentTimeMillis();
        ImageCapture.OutputFileOptions outputFileOptions = new ImageCapture.OutputFileOptions.Builder(mOutMediaFile).build();
        imageCapture.takePicture(outputFileOptions, ContextCompat.getMainExecutor(mContext),
                new ImageCapture.OnImageSavedCallback() {
                    @Override
                    public void onImageSaved(ImageCapture.OutputFileResults outputFileResults) {
                        Log.i("????????????????????????" + ((System.currentTimeMillis() - beginTime) / 1000d));

                        //????????????
//                        CommCountUtil.crop(mContext, mOutMediaFile, previewview.getWidth(), previewview.getHeight());

                        //????????????????????????
                        Log.i("mOutMediaFile???" + mOutMediaFile.getPath());
                        Intent intent = new Intent(mContext, ImageRecognitionActivity.class);
                        intent.putExtra("file", mOutMediaFile);
                        intent.putExtra("commCount_type", commCount_type);
                        intent.putExtra("needCrop", true);
                        startActivity(intent);
                    }

                    @Override
                    public void onError(ImageCaptureException error) {
                        // insert your code here.
                        Log.i("onError:" + error.getMessage());
                    }
                }
        );
//        imageCapture.takePicture(ContextCompat.getMainExecutor(mContext), new ImageCapture.OnImageCapturedCallback() {
//            @Override
//            public void onCaptureSuccess(@NonNull ImageProxy image) {
//                super.onCaptureSuccess(image);
//                Log.i("????????????????????????" + ((System.currentTimeMillis() - beginTime) / 1000d));
//                Log.i("image:"+ image.getWidth()  + " * " + image.getHeight());
//
//                try {
//                    byte[] data = ImageUtil.imageToJpegByteArray(image);
//                    Bitmap bitmap =  BitmapFactory.decodeByteArray(data,0,data.length);
//
//                    Log.i("bitmap:"+ bitmap.getWidth()  + " * "+ bitmap.getHeight());
//                    compare(bitmap);
//                }
//                catch (Exception e)
//                {
//
//                }
//
//            }
//
//            @Override
//            public void onError(@NonNull ImageCaptureException exception) {
//                super.onError(exception);
//            }
//        });
    }

    //???????????? ??? ??????
    private void selectImg(int type) {
        if (AndPermission.hasPermissions(mContext, Permission.Group.STORAGE)) {
            openGalleryOrCamera(type);
        } else {
            AndPermisionUtil.requstPermision((Activity) mContext, new AndPermisionUtil.PermisionCallBack() {
                @Override
                public void onGranted() {
                    openGalleryOrCamera(type);
                }

                @Override
                public void onDenied() {
                    CommToast.showToast(mContext, "????????????????????????????????????");
                }
            }, Permission.Group.STORAGE);
        }
    }

    //???????????? ??? ??????
    private void openGalleryOrCamera(int type) {
        PictureSelector pictureSelector = PictureSelector.create((Activity) mContext);
        PictureSelectionModel pictureSelectionModel;
        if (type == 0) {
            pictureSelectionModel = pictureSelector.openCamera(PictureMimeType.ofImage());
        } else {
            pictureSelectionModel = pictureSelector.openGallery(PictureMimeType.ofImage());
        }
        pictureSelectionModel.loadImageEngine(GlideEngine.createGlideEngine())
                .selectionMode(PictureConfig.SINGLE)
                .compress(false)//????????????
                .isCamera(false)
                .forResult(new OnResultCallbackListener<LocalMedia>() {
                    @Override
                    public void onResult(List<LocalMedia> result) {
                        // ??????????????????
                        if (result.size() > 0) {
                            LocalMedia media = result.get(0);
                            String path = "";
                            if (!TextUtils.isEmpty(media.getAndroidQToPath())) {
                                path = media.getAndroidQToPath();
                            } else if (!TextUtils.isEmpty(media.getRealPath())) {
                                path = media.getRealPath();
                            }
                            File file = new File(path);

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

    //????????????
    private void compare(Bitmap bitmap_old) {
        CommLoading.showLoading((AppCompatActivity) mContext, "???????????????");
        Log.i("????????????");
        long beginTime = System.currentTimeMillis();
        //????????????
        Log.i("????????????");
        Matrix matrix = new Matrix();
//        ExifInterface exif;
//        try {
//            exif = new ExifInterface(oldFile.getPath());
//            int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, 0);
//            Log.i("???????????????" + orientation);
//            if (orientation == 6) {
//                matrix.postRotate(90);
//            } else if (orientation == 3) {
//                matrix.postRotate(180);
//            } else if (orientation == 8) {
//                matrix.postRotate(270);
//            }
////            bitmap_old = Bitmap.createBitmap(bitmap_old, 0, 0,
////                    bitmap_old.getWidth(), bitmap_old.getHeight(),
////                    matrix, true);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
        Log.i("????????????");

        //????????????
        int old_width = Math.min(bitmap_old.getWidth(), bitmap_old.getHeight());
        int old_height = Math.max(bitmap_old.getWidth(), bitmap_old.getHeight());

        //????????????????????????????????????
        double scale_width = (double) old_width / previewview.getWidth();
        double scale_height = (double) old_height / previewview.getHeight();

        double scale = Math.min(scale_width, scale_height);
        int new_Width = (int) (previewview.getWidth() * scale);
        int new_Height = (int) (previewview.getHeight() * scale);

        int left = (old_width - new_Width) / 2;
        int top = (int) previewview.getX();
        int right = left + new_Width;
        int bottom = top + new_Height;

        Log.i("?????????  getWidth:" + old_width + " getHeight:" + old_height + " scale:" + scale);

        //??????????????????
//        Bitmap bmp = Bitmap.createBitmap((int) new_Width, (int) new_Height, Bitmap.Config.ARGB_8888);
//        Canvas canvas = new Canvas(bmp);
//
//        Rect rect1 = new Rect((int) left, (int) top, (int) right, (int) bottom);
//        Rect rect2 = new Rect(0, 0, bmp.getWidth(), bmp.getHeight());
//        canvas.drawBitmap(bitmap_old, rect1, rect2, null);

        Bitmap bmp = Bitmap.createBitmap(bitmap_old, left, top, new_Width, new_Height, matrix, false);


        BitmapUtils.saveBitmapFile(bmp, mOutMediaFile);
        Log.i("????????????????????????" + ((System.currentTimeMillis() - beginTime) / 1000d));
        CommLoading.dismissLoading();

        Intent intent = new Intent(mContext, ImageRecognitionActivity.class);
        intent.putExtra("file", mOutMediaFile);
        intent.putExtra("commCount_type", commCount_type);
        startActivity(intent);

        Log.i("?????????  getWidth:" + bmp.getWidth() + "  getHeight???" + bmp.getHeight());
    }

    public File createImageFile() {
        if (SdkVersionUtils.checkedAndroid_Q()) {
            String diskCacheDir = PictureFileUtils.getDiskCacheDir(mContext);
            File rootDir = new File(diskCacheDir);
            if (!rootDir.exists()) {
                rootDir.mkdirs();
            }
            boolean isOutFileNameEmpty = TextUtils.isEmpty(mConfig.cameraFileName);
            String suffix;
            if (mConfig.suffixType.startsWith("image/")) {
                suffix = mConfig.suffixType.replaceAll("image/", ".");
            } else {
                suffix = PictureMimeType.PNG;
            }
            String newFileImageName = isOutFileNameEmpty ? DateUtils.getCreateFileName("Source_") + suffix : mConfig.cameraFileName;
            File cameraFile = new File(rootDir, newFileImageName);
            Uri outUri = getOutUri(PictureMimeType.ofImage());
            if (outUri != null) {
                mConfig.cameraPath = outUri.toString();
            }
            return cameraFile;
        } else {
            String cameraFileName = "";
            if (!TextUtils.isEmpty(mConfig.cameraFileName)) {
                boolean isSuffixOfImage = PictureMimeType.isSuffixOfImage(mConfig.cameraFileName);
                mConfig.cameraFileName = !isSuffixOfImage ? StringUtils.renameSuffix(mConfig.cameraFileName, PictureMimeType.JPEG) : mConfig.cameraFileName;
                cameraFileName = mConfig.camera ? mConfig.cameraFileName : StringUtils.rename(mConfig.cameraFileName);
            }
            File cameraFile = PictureFileUtils.createCameraFile(mContext,
                    PictureMimeType.ofImage(), cameraFileName, mConfig.suffixType, mConfig.outPutCameraPath);
            mConfig.cameraPath = cameraFile.getAbsolutePath();
            return cameraFile;
        }
    }

    private Uri getOutUri(int type) {
        return MediaUtils.createImageUri(mContext, mConfig.cameraFileName, mConfig.suffixType);
    }

    //???????????????
    private void cropImg() {
        Bitmap bitmap_old = BitmapFactory.decodeFile(mOutMediaFile.getPath());

        double old_width = bitmap_old.getWidth();
        double old_height = bitmap_old.getWidth();


        if (old_height > old_width) {
            //????????????
            //????????????????????????????????????
            double scale = (double) old_height / previewview.getHeight();

            double width = previewview.getWidth() * scale;

            double left = (old_width - width) / 2;
            double top = 0;
            double right = left + width;
            double bottom = old_height;


        } else {
            //????????????


        }


        //????????????????????????????????????
        double scale = (double) old_height / previewview.getHeight();

        double width = previewview.getWidth() * scale;
        double left = (old_width - width) / 2;
        double top = 0;
        double right = left + width;
        double bottom = old_height;

        Log.i("old_width:" + old_width + " old_height:" + old_height + " scale:" + scale);
        //??????????????????
        Bitmap bmp = Bitmap.createBitmap((int) width, (int) old_height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bmp);

        Rect rect1 = new Rect((int) left, (int) top, (int) right, (int) bottom);
        Rect rect2 = new Rect(0, 0, bmp.getWidth(), bmp.getHeight());
        canvas.drawBitmap(bitmap_old, rect1, rect2, null);

        BitmapUtils.saveBitmapFile(bmp, mOutMediaFile);

        Intent intent = new Intent(mContext, ImageRecognitionActivity.class);
        intent.putExtra("file", mOutMediaFile);
        intent.putExtra("commCount_type", commCount_type);
        startActivity(intent);
    }
}
