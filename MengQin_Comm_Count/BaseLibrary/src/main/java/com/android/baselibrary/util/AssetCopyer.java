package com.android.baselibrary.util;

import android.content.Context;
import android.content.res.AssetManager;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2018/7/19.
 */

public class AssetCopyer {
    private String asset_list_fileName;

    private final Context mContext;
    private final AssetManager mAssetManager;
    public File mAppDirectory;
    public String targetPath;

    public AssetCopyer(Context context, String asset_list_fileName, String targetPath) {
        mContext = context;
        mAssetManager = context.getAssets();
        this.asset_list_fileName = asset_list_fileName;
        this.targetPath = targetPath;
    }

    public boolean copy() throws IOException {

        List<String> srcFiles = new ArrayList<>();

        //获取系统在SDCard中为app分配的目录，eg:/sdcard/Android/data/$(app's package)
        //该目录存放app相关的各种文件(如cache，配置文件等)，unstall app后该目录也会随之删除
//        mAppDirectory = mContext.getExternalFilesDir(null) +"/BaiduMapSDKNew";
        mAppDirectory = new File(targetPath);

        if (null == mAppDirectory) {
            mAppDirectory.createNewFile();
        }

        //读取assets/$(subDirectory)目录下的assets.lst文件，得到需要copy的文件列表
        List<String> assets = getAssetsList();
        for (String asset : assets) {
            //如果不存在，则添加到copy列表
            if (!new File(mAppDirectory, asset).exists()) {
                srcFiles.add(asset);
            }
        }

        //依次拷贝到App的安装目录下
        for (String file : srcFiles) {
            copy(file);
        }
        return true;
    }

    protected List<String> getAssetsList() throws IOException {
        List<String> files = new ArrayList<>();
        InputStream listFile = mAssetManager.open(new File(asset_list_fileName).getPath());
        BufferedReader br = new BufferedReader(new InputStreamReader(listFile));
        String path;
        while (null != (path = br.readLine())) {
            files.add(path);
        }
        return files;
    }

    protected File copy(String asset) throws IOException {
        InputStream source = mAssetManager.open(new File(asset).getPath());
        File destinationFile = new File(mAppDirectory, asset);

        if (destinationFile.exists()) {
            return destinationFile;
        }

        destinationFile.getParentFile().mkdirs();
        OutputStream destination = new FileOutputStream(destinationFile);
        byte[] buffer = new byte[1024];
        int nread;

        while ((nread = source.read(buffer)) != -1) {
            if (nread == 0) {
                nread = source.read();
                if (nread < 0)
                    break;
                destination.write(nread);
                continue;
            }
            destination.write(buffer, 0, nread);
        }
        destination.close();
        return destinationFile;
    }
}
