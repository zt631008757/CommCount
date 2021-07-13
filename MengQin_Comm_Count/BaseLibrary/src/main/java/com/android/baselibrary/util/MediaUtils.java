package com.android.baselibrary.util;

import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.os.AsyncTask;

import java.io.File;
import java.util.HashMap;

/**
 * Created by Administrator on 2019/2/20.
 */

public class MediaUtils {
    public static final int MEDIA_TYPE_IMAGE = 1;
    public static final int MEDIA_TYPE_VIDEO = 2;
    public static File file;

    /**
     * 获取视频的第一帧图片
     */
    public static void getImageForVideo(String videoPath, OnLoadVideoImageListener listener) {
        LoadVideoImageTask task = new LoadVideoImageTask(listener);
        task.execute(videoPath);
    }

    public static class LoadVideoImageTask extends AsyncTask<String, Integer, Bitmap> {
        private OnLoadVideoImageListener listener;

        public LoadVideoImageTask(OnLoadVideoImageListener listener) {
            this.listener = listener;
        }

        @Override
        protected Bitmap doInBackground(String... params) {
            try {
                MediaMetadataRetriever mmr = new MediaMetadataRetriever();
                String path = params[0];
                if (path.startsWith("http"))
                    //获取网络视频第一帧图片
                    mmr.setDataSource(path, new HashMap());
                else
                    //本地视频
                    mmr.setDataSource(path);
                Bitmap bitmap = mmr.getFrameAtTime();

                mmr.release();
                return bitmap;
            }
            catch (Exception e)
            {
                return null;
            }
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            super.onPostExecute(bitmap);
            if (listener != null) {
                listener.onLoadImage(bitmap);
            }
        }
    }

    public interface OnLoadVideoImageListener {
        void onLoadImage(Bitmap bitmap);
    }
}
