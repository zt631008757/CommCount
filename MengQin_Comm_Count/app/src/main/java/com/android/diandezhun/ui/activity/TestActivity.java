package com.android.diandezhun.ui.activity;

import android.net.Uri;
import android.os.Environment;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;

import androidx.camera.core.Camera;
import androidx.camera.core.CameraSelector;
import androidx.camera.core.CameraX;
import androidx.camera.core.ImageCapture;
import androidx.camera.core.ImageCaptureException;
import androidx.camera.core.Preview;
import androidx.camera.core.impl.PreviewConfig;
import androidx.camera.lifecycle.ProcessCameraProvider;
import androidx.camera.view.PreviewView;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.LifecycleOwner;

import com.android.baselibrary.tool.Log;
import com.android.baselibrary.view.Comm_HeadView;
import com.android.diandezhun.R;
import com.google.common.util.concurrent.ListenableFuture;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.config.PictureSelectionConfig;
import com.luck.picture.lib.tools.DateUtils;
import com.luck.picture.lib.tools.MediaUtils;
import com.luck.picture.lib.tools.PictureFileUtils;
import com.luck.picture.lib.tools.SdkVersionUtils;
import com.luck.picture.lib.tools.StringUtils;

import java.io.File;
import java.util.concurrent.ExecutionException;

import butterknife.BindView;
import butterknife.OnClick;

public class TestActivity extends BaseActivity{

    @BindView(R.id.comm_title)
    Comm_HeadView comm_title;
    @BindView(R.id.previewview)
    PreviewView previewview;

    PictureSelectionConfig mConfig;


    @Override
    protected int getLayoutId() {
        return R.layout.activity_test;
    }

    private ImageCapture imageCapture;
    @Override
    protected void initView() {
        comm_title.setTitle("测试");
        mConfig = PictureSelectionConfig.getCleanInstance();

        ListenableFuture<ProcessCameraProvider> cameraProviderFuture =
                ProcessCameraProvider.getInstance(this);

        cameraProviderFuture.addListener(() -> {
            try {
                // 相机供应商现在保证可用
                ProcessCameraProvider cameraProvider = cameraProviderFuture.get();

                // 设置取景器用例显示相机预览
                Preview preview = new Preview.Builder().build();

                // 设置捕获用例以允许用户拍照
                imageCapture = new ImageCapture.Builder().setCaptureMode(ImageCapture.CAPTURE_MODE_MINIMIZE_LATENCY).build();

                // 选择需要面对镜头的相机
                CameraSelector cameraSelector = new CameraSelector.Builder().requireLensFacing(CameraSelector.LENS_FACING_BACK).build();

                // 将具有相同生命周期所有者的用例附加到摄像机
                Camera camera = cameraProvider.bindToLifecycle(((LifecycleOwner) this), cameraSelector, preview, imageCapture);

                // 将预览用例连接到预览视图
                preview.setSurfaceProvider(previewview.getSurfaceProvider());


            } catch (InterruptedException | ExecutionException e) {
                // Currently no exceptions thrown. cameraProviderFuture.get()
                // shouldn't block since the listener is being called, so no need to
                // handle InterruptedException.
            }
        }, ContextCompat.getMainExecutor(this));
    }


    @Override
    protected void initData() {


    }


    @OnClick({R.id.tv_takephoto})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_takephoto:
                takePicture();
                break;
        }
    }

    public void takePicture() {
        Log.i("拍照开始");
        File file = createImageFile();
        Log.i("file:"+ file.getPath());

        ImageCapture.OutputFileOptions outputFileOptions = new ImageCapture.OutputFileOptions.Builder(file).build();
        imageCapture.takePicture(outputFileOptions, ContextCompat.getMainExecutor(mContext),
                new ImageCapture.OnImageSavedCallback() {
                    @Override
                    public void onImageSaved(ImageCapture.OutputFileResults outputFileResults) {
                        // insert your code here.
                        Log.i("拍照完成");
                    }
                    @Override
                    public void onError(ImageCaptureException error) {
                        // insert your code here.
                        Log.i("onError:"+ error.getMessage());
                    }
                }
        );
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
}
