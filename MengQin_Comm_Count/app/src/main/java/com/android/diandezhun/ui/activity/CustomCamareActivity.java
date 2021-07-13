package com.android.diandezhun.ui.activity;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.Image;
import android.net.Uri;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;

import androidx.annotation.NonNull;
import androidx.camera.core.ImageCapture;
import androidx.camera.core.ImageCaptureException;
import androidx.camera.core.ZoomState;
import androidx.camera.view.LifecycleCameraController;
import androidx.camera.view.PreviewView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;

import com.android.baselibrary.interface_.OkHttpCallBack;
import com.android.baselibrary.responce.BaseResponce;
import com.android.baselibrary.tool.AndPermisionUtil;
import com.android.baselibrary.tool.CommLoading;
import com.android.baselibrary.tool.CommToast;
import com.android.baselibrary.tool.Log;
import com.android.baselibrary.util.GlideEngine;
import com.android.baselibrary.view.Comm_HeadView;
import com.android.baselibrary.view.MultiStateView;
import com.android.diandezhun.R;
import com.android.diandezhun.bean.CommCount_Type;
import com.android.diandezhun.manager.API_Manager;
import com.android.diandezhun.manager.UserManager;
import com.luck.picture.lib.PictureSelectionModel;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.camera.CustomCameraView;
import com.luck.picture.lib.camera.listener.CameraListener;
import com.luck.picture.lib.camera.listener.ImageCallbackListener;
import com.luck.picture.lib.camera.view.CaptureLayout;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.config.PictureSelectionConfig;
import com.luck.picture.lib.entity.LocalMedia;
import com.luck.picture.lib.listener.OnResultCallbackListener;
import com.luck.picture.lib.permissions.PermissionChecker;
import com.luck.picture.lib.thread.PictureThreadUtils;
import com.luck.picture.lib.tools.AndroidQTransformUtils;
import com.luck.picture.lib.tools.BitmapUtils;
import com.luck.picture.lib.tools.DateUtils;
import com.luck.picture.lib.tools.MediaUtils;
import com.luck.picture.lib.tools.PictureFileUtils;
import com.luck.picture.lib.tools.SdkVersionUtils;
import com.luck.picture.lib.tools.StringUtils;
import com.nanchen.compresshelper.BitmapUtil;
import com.nanchen.compresshelper.CompressHelper;
import com.yanzhenjie.permission.runtime.Permission;

import java.io.File;
import java.lang.ref.WeakReference;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 自定义相机
 */
public class CustomCamareActivity extends BaseActivity implements SensorEventListener {

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


    SensorManager mSensorManager;
    Sensor mAccelerometer;

    CommCount_Type commCount_type;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_custom_camare;
    }

    /**
     * 闪关灯状态
     */
    private static final int TYPE_FLASH_AUTO = 0x021;
    private static final int TYPE_FLASH_ON = 0x022;
    private static final int TYPE_FLASH_OFF = 0x023;

    private int type_flash = TYPE_FLASH_AUTO;
    File mOutMediaFile;
    LifecycleCameraController mCameraController;    //相机控制
    PictureSelectionConfig mConfig;

    @Override
    protected void onResume() {
        super.onResume();
        mSensorManager.registerListener(this, mAccelerometer, SensorManager.SENSOR_DELAY_NORMAL);
    }

    protected void onPause() {
        super.onPause();
        mSensorManager.unregisterListener(this);
    }

    @Override
    protected View getImmersionTitleBar() {
        return ll_head;
    }

    @Override
    protected boolean isBarDarkFontAndIcon() {
        return false;
    }

    @Override
    protected void initView() {
        mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        mConfig = PictureSelectionConfig.getCleanInstance();

        // 验证相机权限和麦克风权限
        if (PermissionChecker.checkSelfPermission(this, Manifest.permission.CAMERA)) {
            boolean isRecordAudio = PermissionChecker.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO);
            if (isRecordAudio) {
            } else {
                PermissionChecker.requestPermissions(this,
                        new String[]{Manifest.permission.RECORD_AUDIO}, PictureConfig.APPLY_RECORD_AUDIO_PERMISSIONS_CODE);
            }
        } else {
            PermissionChecker.requestPermissions(this,
                    new String[]{Manifest.permission.CAMERA}, PictureConfig.APPLY_CAMERA_PERMISSIONS_CODE);
        }

        if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
            mCameraController = new LifecycleCameraController(mContext);
            mCameraController.bindToLifecycle((LifecycleOwner) mContext);
            previewview.setController(mCameraController);
            setFlashRes();
            mCameraController.getZoomState().observeForever(new Observer<ZoomState>() {
                @Override
                public void onChanged(ZoomState zoomState) {
                    Log.i("zoomState:" + zoomState.getZoomRatio());
                    seekbar.setProgress((int) (zoomState.getLinearZoom() * 100));
                }
            });

        }

        seekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser) {
                    mCameraController.setLinearZoom((float) progress / 100f);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    private void setFlashRes() {
        switch (type_flash) {
            case TYPE_FLASH_AUTO:
                iv_flash.setImageResource(R.drawable.ico_customcamare_light_auto);
                mCameraController.setImageCaptureFlashMode(ImageCapture.FLASH_MODE_AUTO);
                break;
            case TYPE_FLASH_ON:
                iv_flash.setImageResource(R.drawable.ico_customcamare_light_open);
                mCameraController.setImageCaptureFlashMode(ImageCapture.FLASH_MODE_ON);
                break;
            case TYPE_FLASH_OFF:
                iv_flash.setImageResource(R.drawable.ico_customcamare_light_close);
                mCameraController.setImageCaptureFlashMode(ImageCapture.FLASH_MODE_OFF);
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
            case R.id.iv_flash:   //切换闪光灯
                type_flash++;
                if (type_flash > 0x023)
                    type_flash = TYPE_FLASH_AUTO;
                setFlashRes();
                break;
            case R.id.iv_take_photo:   //拍照
                mOutMediaFile = createImageFile();
//                mCameraController.setEnabledUseCases(LifecycleCameraController.IMAGE_CAPTURE);
                ImageCapture.OutputFileOptions fileOptions = new ImageCapture.OutputFileOptions.Builder(mOutMediaFile).build();
                Log.i("拍照开始");
                long beginTime = System.currentTimeMillis();
                mCameraController.takePicture(fileOptions, ContextCompat.getMainExecutor(mContext), new ImageCapture.OnImageSavedCallback() {
                    @Override
                    public void onImageSaved(@NonNull ImageCapture.OutputFileResults outputFileResults) {
                        long endTime = System.currentTimeMillis();
                        Log.i("拍照完成,耗时:" + ((endTime - beginTime) / 1000d) + " 秒");
                        rotateImg(mOutMediaFile);
                        Intent intent = new Intent(mContext, ImageRecognitionActivity.class);
                        intent.putExtra("file", mOutMediaFile);
                        intent.putExtra("commCount_type", commCount_type);
                        startActivity(intent);
                    }

                    @Override
                    public void onError(@NonNull ImageCaptureException exception) {

                    }
                });
                break;
            case R.id.iv_photo:   //相册
                if (UserManager.isLoginAndShowLogin((Activity) mContext)) {
                    selectImg(1);
                }
                break;
            case R.id.iv_camare:   //原相机
                if (UserManager.isLoginAndShowLogin((Activity) mContext)) {
                    selectImg(0);
                }
                break;
            case R.id.iv_back:
                finish();
                break;
        }
    }

    //选择相册
    private void selectImg(int type) {
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
                        .isCamera(false)
                        .forResult(new OnResultCallbackListener<LocalMedia>() {
                            @Override
                            public void onResult(List<LocalMedia> result) {
                                // 相册选择返回
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

            @Override
            public void onDenied() {

            }
        }, Permission.Group.STORAGE);
    }

    private void rotateImg(File file) {
        //先旋转图片，按拍摄的方向
        int degree = PictureFileUtils.readPictureDegree(mContext, file.getPath());
        int rotate = 0;
        switch (degree) {
            case 0:   //横屏
                rotate = 90;
                break;
            case 90:
                rotate = 0;
                break;
            case 180:
                rotate = 90;
                break;
            case 270:
                rotate = 90;
                break;
        }
        Log.i("degree:" + degree + " rotate:" + rotate);
        BitmapUtils.rotateImage(rotate, file.getPath());
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            /**
             * 裁剪返回
             */
            case 200:
                if (resultCode == RESULT_OK) {
                    Bitmap bitmap = (Bitmap) data.getExtras().get("data");
                    BitmapUtils.saveBitmapFile(bitmap, mOutMediaFile);

                    rotateImg(mOutMediaFile);

                    Intent intent = new Intent(mContext, ImageRecognitionActivity.class);
                    intent.putExtra("file", mOutMediaFile);
                    intent.putExtra("commCount_type", commCount_type);
                    startActivity(intent);

                }
                break;
            default:
                break;
        }
    }


    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
//        Log.d("linc", "value size: " + sensorEvent.values.length);
        float xValue = sensorEvent.values[0];// Acceleration minus Gx on the x-axis
        float yValue = sensorEvent.values[1];//Acceleration minus Gy on the y-axis
        float zValue = sensorEvent.values[2];//Acceleration minus Gz on the z-axis
//        Log.i("x轴： " + xValue + "  y轴： " + yValue + "  z轴： " + zValue);

//        mTvInfo.setText("x轴： "+xValue+"  y轴： "+yValue+"  z轴： "+zValue);
//        if(xValue > mGravity) {
//            mTvInfo.append("\n重力指向设备左边");
//        } else if(xValue < -mGravity) {
//            mTvInfo.append("\n重力指向设备右边");
//        } else if(yValue > mGravity) {
//            mTvInfo.append("\n重力指向设备下边");
//        } else if(yValue < -mGravity) {
//            mTvInfo.append("\n重力指向设备上边");
//        } else if(zValue > mGravity) {
//            mTvInfo.append("\n屏幕朝上");
//        } else if(zValue < -mGravity) {
//            mTvInfo.append("\n屏幕朝下");
//        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

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
                suffix = PictureMimeType.JPEG;
            }
            String newFileImageName = isOutFileNameEmpty ? DateUtils.getCreateFileName("IMG_") + suffix : mConfig.cameraFileName;
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


    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
            Log.i("竖屏");
        }
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            Log.i("横屏");
        }
    }
}
