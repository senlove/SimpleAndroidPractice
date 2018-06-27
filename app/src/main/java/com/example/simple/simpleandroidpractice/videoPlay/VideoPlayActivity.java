package com.example.simple.simpleandroidpractice.videoPlay;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.SurfaceTexture;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.TextureView;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.example.simple.simpleandroidpractice.R;

import java.io.IOException;
import java.lang.ref.WeakReference;


/**
 * 没有 stop() --> start()方式 stop()-->prepare()-->start()
 */
public class VideoPlayActivity extends Activity implements MediaPlayer.OnCompletionListener, MediaPlayer.OnPreparedListener,
        MediaPlayer.OnErrorListener, SurfaceHolder.Callback, SeekBar.OnSeekBarChangeListener,
        MediaPlayer.OnBufferingUpdateListener, MediaPlayer.OnSeekCompleteListener, TextureView.SurfaceTextureListener {

    private static final String TAG = "VideoPlayActivity";
    private static final int MSG_PLAY_CURR_TIME = 100;
    private static final int INVALID = -1;

    String liveUrl = "http://testqv.facebac.com/hls/6a29bfedd6ab0e356877/n8tgn9ilwasfgklxdwwe/n_n/49675ecc7365b54335d39e217872bf83.m3u8";
    private MediaPlayer mMediaPlayer;
//    private SurfaceView mSurfaceView;
    private TextureView mTextureView;
    private SeekBar mSeekBar;
    private TextView mTvCurrTime;
    private TextView mTvTotalTime;
    private WatchVideoHandler mHandler;

    public static Intent newIntent(Context context){
        Intent intent = new Intent(context, VideoPlayActivity.class);
        return intent;
    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_play);
//        mSurfaceView = findViewById(R.id.surface_view);
        mTextureView = findViewById(R.id.texture_view);
        mSeekBar = findViewById(R.id.seek_bar);
        mTvCurrTime = findViewById(R.id.tv_curr_time);
        mTvTotalTime = findViewById(R.id.tv_total_time);

        mSeekBar.setOnSeekBarChangeListener(this);
//        SurfaceHolder surfaceHolder = mSurfaceView.getHolder();
//        surfaceHolder.addCallback(this);
        mTextureView.setSurfaceTextureListener(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if(null != mMediaPlayer && mMediaPlayer.isPlaying()){
            mMediaPlayer.pause();
        }
        mHandler.removeMessages(MSG_PLAY_CURR_TIME);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mHandler.removeMessages(MSG_PLAY_CURR_TIME);
        if(null != mMediaPlayer && mMediaPlayer.isPlaying()){
            mMediaPlayer.stop();
        }
        mMediaPlayer.release();
        mMediaPlayer = null;

    }

    //surface holder
    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {
        Log.i(TAG, "surfaceCreated");
//        if(null == mMediaPlayer){
//            mMediaPlayer = new MediaPlayer();
//            try {
//                mMediaPlayer.setOnPreparedListener(this);
//                mMediaPlayer.setOnBufferingUpdateListener(this);
//                mMediaPlayer.setOnSeekCompleteListener(this);
//                mMediaPlayer.setOnCompletionListener(this);
//                mMediaPlayer.setOnErrorListener(this);
//                mMediaPlayer.setDataSource(liveUrl);
//
//                mMediaPlayer.setDisplay(surfaceHolder);
//                mMediaPlayer.prepareAsync();
//
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        } else {
//            mMediaPlayer.setDisplay(surfaceHolder);
//            mMediaPlayer.start();
//            mHandler.sendEmptyMessage(MSG_PLAY_CURR_TIME);
//        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {
        Log.i(TAG, "surfaceChanged");
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {//按home键回调到这个参数
        Log.i(TAG, "surfaceDestroyed");
        if(null != mMediaPlayer){
            mMediaPlayer.pause();
        }
    }

    //seek bar
    @Override
    public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
        mTvCurrTime.setText(WatchVideoTimeUtil.formatTime(seekBar.getProgress()));
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
        mHandler.removeMessages(MSG_PLAY_CURR_TIME);
    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        if(null != mMediaPlayer){
            mMediaPlayer.seekTo(seekBar.getProgress());
        }
    }

    //mediaplayer

    @Override
    public void onPrepared(MediaPlayer mediaPlayer) {
        Log.i(TAG, "onPrepared");
        if (mediaPlayer.getVideoWidth() > mediaPlayer.getVideoHeight()) {//横向视频
            RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) mTextureView.getLayoutParams();
            WindowManager windowManager = (WindowManager) VideoPlayActivity.this.getSystemService(Context.WINDOW_SERVICE);
            int windowWidth = windowManager.getDefaultDisplay().getWidth();
            layoutParams.width = windowWidth;
            layoutParams.height = (int) getResources().getDimension(R.dimen.dp_200);
            layoutParams.addRule(RelativeLayout.CENTER_IN_PARENT);
            mTextureView.setLayoutParams(layoutParams);
        }
        mTvCurrTime.setText(WatchVideoTimeUtil.formatTime(mediaPlayer.getCurrentPosition()));
        mTvTotalTime.setText(WatchVideoTimeUtil.formatTime(mediaPlayer.getDuration()));
        mSeekBar.setMax(mediaPlayer.getDuration());
        mSeekBar.setProgress(mediaPlayer.getCurrentPosition());

        mMediaPlayer.start();
        mHandler = new WatchVideoHandler(VideoPlayActivity.this, mMediaPlayer, mTvCurrTime, mSeekBar);
        mHandler.sendEmptyMessageDelayed(MSG_PLAY_CURR_TIME, 1000);
    }

    @Override
    public void onBufferingUpdate(MediaPlayer mediaPlayer, int i) {//mediaplayer pause stop 都会调用到这个回调方法
        Log.i(TAG, "onBufferingUpdate");
    }

    @Override
    public void onSeekComplete(MediaPlayer mediaPlayer) {
        Log.i(TAG, "onSeekComplete");
        mHandler.sendEmptyMessageDelayed(MSG_PLAY_CURR_TIME, 1000);
    }

    @Override
    public void onCompletion(MediaPlayer mediaPlayer) { //播放结束会调用这个方法
        Log.i(TAG, "onCompletion");
    }

    @Override
    public boolean onError(MediaPlayer mediaPlayer, int i, int i1) {
        Log.i(TAG, "onError");
        return false;
    }

    //texture view
    @Override
    public void onSurfaceTextureAvailable(SurfaceTexture surfaceTexture, int i, int i1) {
        Log.i(TAG, "onSurfaceTextureAvailable");
        Surface surface = new Surface(surfaceTexture);

        if(null == mMediaPlayer){
            mMediaPlayer = new MediaPlayer();
            try {
                mMediaPlayer.setOnPreparedListener(this);
                mMediaPlayer.setOnBufferingUpdateListener(this);
                mMediaPlayer.setOnSeekCompleteListener(this);
                mMediaPlayer.setOnCompletionListener(this);
                mMediaPlayer.setOnErrorListener(this);
                mMediaPlayer.setDataSource(liveUrl);
                mMediaPlayer.setSurface(surface);
                mMediaPlayer.prepareAsync();

            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            mMediaPlayer.setSurface(surface);
            mMediaPlayer.start();
            mHandler.sendEmptyMessage(MSG_PLAY_CURR_TIME);
        }


    }

    @Override
    public void onSurfaceTextureSizeChanged(SurfaceTexture surfaceTexture, int i, int i1) {
        Log.i(TAG, "onSurfaceTextureSizeChanged");
    }

    @Override
    public boolean onSurfaceTextureDestroyed(SurfaceTexture surfaceTexture) {
        Log.i(TAG, "onSurfaceTextureDestroyed");
        if(null != mMediaPlayer){
            mMediaPlayer.pause();
        }
        return false;
    }

    @Override
    public void onSurfaceTextureUpdated(SurfaceTexture surfaceTexture) {
        Log.i(TAG, "onSurfaceTextureUpdated");
    }

    static class WatchVideoHandler extends Handler {

        private static final int INVALID = -1;

        private WeakReference<Context> mReference;
        private WeakReference<MediaPlayer> mMediaPlayerWeakReference;
        private TextView mTvCurrTime;
        private SeekBar mSeekBar;

        public WatchVideoHandler(Context context, MediaPlayer mediaPlayer, TextView tvCurrTime, SeekBar seekBar) {
            mReference = new WeakReference<>(context);
            mMediaPlayerWeakReference = new WeakReference<>(mediaPlayer);
            this.mTvCurrTime = tvCurrTime;
            this.mSeekBar = seekBar;
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case MSG_PLAY_CURR_TIME:
                    if (null == mReference.get() || null == mMediaPlayerWeakReference.get()) {
                        return;
                    }
                    int currPlayTime = mMediaPlayerWeakReference.get().getCurrentPosition();
                    mTvCurrTime.setText(WatchVideoTimeUtil.formatTime(currPlayTime));
                    mSeekBar.setProgress(currPlayTime);
                    sendEmptyMessageDelayed(MSG_PLAY_CURR_TIME, 1000);
                    break;
            }
        }
    }
}
