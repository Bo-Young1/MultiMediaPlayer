package com.coship.multimediaplayer.main;

import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.MediaController;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.VideoView;

import com.coship.multimediaplayer.R;
import com.coship.multimediaplayer.model.VideoInfo;
import com.coship.multimediaplayer.utils.CloseActivityClass;
import com.coship.multimediaplayer.utils.FormatHelper;

import java.io.IOException;

/**
 * Created by 980559 on 2017/4/17.
 */
public class VideoPlayActivity extends AppCompatActivity implements View.OnClickListener,MediaPlayer.OnSeekCompleteListener {

    private String urlPath ;
    private Intent intent;

    private SurfaceView surface1;
    private ImageButton restart;
    private ImageButton start;
    private ImageButton stop;
    private MediaPlayer mediaPlayer1;
    private SeekBar videoDuration;
    private TextView tvVideoDuration;
    private TextView tvVideoTimeElapsed;
    private LinearLayout linear1;
    private FrameLayout framelayout1;
    private ImageButton kuaitui;
    private ImageButton kuaijin;
    private int postion = 0;
    private static final int updateProgress = 1;
    private static final int faster = 2;
    private static final int slower = 3;

    private Handler mHandler = new Handler();
    private Handler handler = new Handler(){
       @Override
       public void handleMessage(Message msg) {
           switch(msg.what){
               case updateProgress:
                   toUpdateProgress();
                   break;
               case faster:
                   toFaster();
                   break;
               case slower:
                   toSlower();
                   break;
               default:
                   break;

           }
       }
    };
    private void toUpdateProgress(){
        if (mediaPlayer1 != null){
            int progress = mediaPlayer1.getCurrentPosition();
            tvVideoTimeElapsed.setText(FormatHelper.formatDuration(progress));
            videoDuration.setProgress(progress);
            handler.sendEmptyMessageDelayed(updateProgress, 1000);
        }
    }

    private void toFaster(){
        if (mediaPlayer1 != null){
            int progress = mediaPlayer1.getCurrentPosition();
            int thisProgress = progress + 10000;
            changeProgress(thisProgress);
            tvVideoTimeElapsed.setText(FormatHelper.formatDuration(thisProgress));
            videoDuration.setProgress(thisProgress);
            handler.sendEmptyMessageDelayed(faster, 500);
        }
    }

    private void toSlower(){
        if (mediaPlayer1 != null){
            int progress = mediaPlayer1.getCurrentPosition();
            int thisProgress = progress - 10000;
            if (thisProgress > 0){
                changeProgress(thisProgress);
                tvVideoTimeElapsed.setText(FormatHelper.formatDuration(thisProgress));
                videoDuration.setProgress(thisProgress);
                handler.sendEmptyMessageDelayed(slower, 500);
            }else{
                thisProgress = 0;
                handler.removeMessages(slower);
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.video_play);

        surface1 = (SurfaceView) findViewById(R.id.surface1);
        restart = (ImageButton) findViewById(R.id.restart);
        start = (ImageButton) findViewById(R.id.start);
        stop = (ImageButton) findViewById(R.id.stop);
        kuaitui = (ImageButton)findViewById(R.id.kuaitui);
        kuaijin = (ImageButton)findViewById(R.id.kuaijin);
        linear1 = (LinearLayout)findViewById(R.id.linear1);
        framelayout1 = (FrameLayout)findViewById(R.id.framelayout1);
        framelayout1.setFocusable(true);
        framelayout1.requestFocus();

        mediaPlayer1 = new MediaPlayer();
        //设置播放时打开屏幕
        surface1.getHolder().setKeepScreenOn(true);
        surface1.getHolder().addCallback(new SurfaceViewLis());

        framelayout1.setOnClickListener(this);
        restart.setOnClickListener(this);
        start.setOnClickListener(this);
        stop.setOnClickListener(this);
        kuaitui.setOnClickListener(this);
        kuaijin.setOnClickListener(this);

        intent = this.getIntent();
        Bundle bundle = intent.getExtras();
        VideoInfo video = (VideoInfo) bundle.getSerializable("videoInfo");
        urlPath = video.getPath();

        tvVideoDuration = (TextView)findViewById(R.id.tvVideoDuration);
        int max = video.getDuration();
        tvVideoDuration.setText(FormatHelper.formatDuration(max));

        videoDuration = (SeekBar)findViewById(R.id.videoDuration);
        videoDuration.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser){
                    changeProgress(progress);
                }
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });
        videoDuration.setMax(max);

        tvVideoTimeElapsed = (TextView)findViewById(R.id.tvVideoTimeElapsed);
        tvVideoTimeElapsed.setText(FormatHelper.formatDuration(postion));

        mediaPlayer1.setOnSeekCompleteListener(this);

    }

    private void changeProgress(int progress){
            postion = progress;
            mediaPlayer1.seekTo(postion);
            handler.sendEmptyMessage(updateProgress);
    }
    @Override
    public void onSeekComplete(MediaPlayer mp) {
        mp.start();
    }

    public void play() throws IllegalArgumentException, SecurityException,IllegalStateException, IOException {
        mediaPlayer1.reset();
        mediaPlayer1.setAudioStreamType(AudioManager.STREAM_MUSIC);
        mediaPlayer1.setDataSource(urlPath);
        // 把视频输出到SurfaceView上
        mediaPlayer1.setDisplay(surface1.getHolder());
        mediaPlayer1.prepare();
        mediaPlayer1.start();
        mediaPlayer1.seekTo(postion);
        handler.sendEmptyMessage(updateProgress);
    }

//    @Override
//    public boolean onKeyDown(int keyCode, KeyEvent event) {
//        if (keyCode == KeyEvent.KEYCODE_BACK){
//           // finish();
//            //System.exit(0);
//            //this.onDestroy();
//            //android.os.Process.killProcess(android.os.Process.myPid());
//        }
//        return super.onKeyDown(keyCode, event);
//    }

    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            try{
                linear1.setVisibility(View.GONE);
                framelayout1.setFocusable(true);
                framelayout1.requestFocus();
                mHandler.postDelayed(this,30000);
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    };

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.framelayout1:
                if (linear1.getVisibility() == View.INVISIBLE || linear1.getVisibility() == View.GONE) {
                    linear1.setVisibility(View.VISIBLE);
                    start.setFocusable(true);
                    start.requestFocus();
                    mHandler.postDelayed(runnable, 30000);
                }
                break;
            case R.id.restart:
                try {
                    postion = 0;
                    play();
                    start.setImageResource(R.drawable.pause1);
                } catch (IllegalArgumentException e) {
                    e.printStackTrace();
                } catch (SecurityException e) {
                    e.printStackTrace();
                } catch (IllegalStateException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
            case R.id.kuaitui:
               // changeProgress(mediaPlayer1.getCurrentPosition() - 5000);
                start.setImageResource(R.drawable.pause1);
                handler.sendEmptyMessage(slower);

                break;
            case R.id.start:
                if (mediaPlayer1.isPlaying()) {
                    mediaPlayer1.pause();
                    start.setImageResource(R.drawable.playmy);
                    handler.removeMessages(faster);
                } else {
                    mediaPlayer1.start();
                    start.setImageResource(R.drawable.pause1);
                    handler.sendEmptyMessageDelayed(updateProgress,1000);
                }
                break;
            case R.id.kuaijin:
               // changeProgress(mediaPlayer1.getCurrentPosition() + 5000);
                start.setImageResource(R.drawable.pause1);
                handler.sendEmptyMessage(faster);
                break;
            case R.id.stop:
                if (mediaPlayer1.isPlaying()){
                    mediaPlayer1.stop();
                    handler.sendEmptyMessage(updateProgress);
                }

                break;
            default:
                break;
        }
    }

    private class SurfaceViewLis implements SurfaceHolder.Callback {
        @Override
        public void surfaceChanged(SurfaceHolder holder, int format, int width,int height) {
        }
        @Override
        public void surfaceCreated(SurfaceHolder holder) {
            if (postion == 0) {
                try {
                    play();
                    mediaPlayer1.seekTo(postion);
                } catch (IllegalArgumentException e) {
                    e.printStackTrace();
                } catch (SecurityException e) {
                    e.printStackTrace();
                } catch (IllegalStateException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        @Override
        public void surfaceDestroyed(SurfaceHolder holder) {
        }
    }

    @Override
    protected void onPause() {
        if (mediaPlayer1.isPlaying()) {
            // 保存当前播放的位置
            postion = mediaPlayer1.getCurrentPosition();
            mediaPlayer1.stop();
        }
        super.onPause();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (runnable != null){
            mHandler.removeCallbacks(runnable);
            runnable = null;
        }


        if (mediaPlayer1.isPlaying()){
            mediaPlayer1.stop();
        }
        mediaPlayer1.release();

        if (mediaPlayer1 != null){
            mediaPlayer1 =null;
        }
    }
}

