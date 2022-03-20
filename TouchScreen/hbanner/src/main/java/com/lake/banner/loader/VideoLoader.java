package com.lake.banner.loader;

import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;
import android.widget.VideoView;

import android.util.Log;

import com.lake.banner.uitls.MD5Util;
import com.lake.banner.view.CustomVideoView;

import java.io.File;

/**
 * 视频代理实现
 */
public class VideoLoader implements VideoViewLoaderInterface {
    VideoCompletInterface mInterface;

    public VideoLoader(VideoCompletInterface inter) {
        mInterface = inter;
    }

    @Override
    public VideoView createView(Context context, int gravity) {
        //全屏拉伸 CustomVideoView(context)；
        CustomVideoView customVideoView = new CustomVideoView(context);
        customVideoView.setGravityType(gravity);
        return customVideoView;
    }

    @Override
    public void onPrepared(Context context, Object path, VideoView videoView, String cachePath) {
        try {
            videoView.setOnPreparedListener(mp -> {
                Log.i("test", "videoView onPrepared");
            });
            videoView.setOnErrorListener((MediaPlayer mp, int what, int extra) -> {
                //视频读取失败！
                Log.i("test", "videoView error=" + what);
                return true;
            });
            if (path instanceof String) {
                videoView.setVideoPath((String) path);
            } else if (path instanceof Uri) {
                String pStr = path.toString();
                String type = pStr.substring(pStr.lastIndexOf("."));
                File file = new File(cachePath + File.separator + MD5Util.md5(path.toString()) + type);
                if (file.exists()) {
                    Log.i("lake", "onPrepared: isCache");
                    videoView.setVideoURI(Uri.parse(file.getPath()));
                } else {
                    Log.i("lake", "onPrepared: noCache");
                    videoView.setVideoURI((Uri) path);
                }
            }
            videoView.setOnCompletionListener((MediaPlayer mp) -> {
                Log.i("test", "setOnCompletionListener");
                mInterface.comlet();
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void displayView(Context context, VideoView videoView) {
        Log.i("test", "displayView: ");
        videoView.seekTo(0);
        videoView.start();
    }

    @Override
    public void onResume(VideoView view) {
        Log.i("test", "onResume: ");
        view.start();
    }

    @Override
    public void onStop(VideoView view) {
        Log.i("test", "onStop: ");
        view.pause();
    }

    @Override
    public void onDestroy(VideoView videoView) {
        Log.i("test", "onDestroy: ");
        videoView.stopPlayback();
        System.gc();
    }
}
