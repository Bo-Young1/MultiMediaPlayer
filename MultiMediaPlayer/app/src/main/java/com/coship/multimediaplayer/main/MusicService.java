package com.coship.multimediaplayer.main;

import java.util.List;

import android.app.Service;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.widget.Toast;

import com.coship.multimediaplayer.model.MusicInfo;
import com.coship.multimediaplayer.model.MusicLoader;

public class MusicService extends Service{

	private MediaPlayer mediaPlayer;
	private boolean isPlaying = false;
	private List<MusicInfo> musicList;
	private Binder natureBinder = new NatureBinder();
	private int currentMusic;
	private int currentPosition;
	private int currentMode = 3;
	private static final int updateProgress = 1;
	private static final int updateCurrentMusic = 2;
	private static final int updateDuration = 3;
	public static final String ACTION_UPDATE_PROGRESS = "UPDATE_PROGRESS";
	public static final String ACTION_UPDATE_DURATION = "UPDATE_DURATION";
	public static final String ACTION_UPDATE_CURRENT_MUSIC = "UPDATE_CURRENT_MUSIC";

	private Handler handler = new Handler(){
		public void handleMessage(Message msg){
			switch(msg.what){
				case updateProgress:
					toUpdateProgress();
					break;
				case updateDuration:
					toUpdateDuration();
					break;
				case updateCurrentMusic:
					toUpdateCurrentMusic();
					break;
			}
		}
	};

	//更新进度
	private void toUpdateProgress(){
		if(mediaPlayer != null && isPlaying){
			int progress = mediaPlayer.getCurrentPosition();
			//int progress = currentPosition；
			Intent intent = new Intent();
			intent.setAction(ACTION_UPDATE_PROGRESS);
			intent.putExtra(ACTION_UPDATE_PROGRESS,progress);
			sendBroadcast(intent);
			handler.sendEmptyMessageDelayed(updateProgress, 1000);
		}
	}

	//更新时长
	private void toUpdateDuration(){
		if(mediaPlayer != null){
			int duration = mediaPlayer.getDuration();
			Intent intent = new Intent();
			intent.setAction(ACTION_UPDATE_DURATION);
			intent.putExtra(ACTION_UPDATE_DURATION,duration);
			sendBroadcast(intent);
		}
	}

	//更新当前音乐
	private void toUpdateCurrentMusic(){
		Intent intent = new Intent();
		intent.setAction(ACTION_UPDATE_CURRENT_MUSIC);
		intent.putExtra(ACTION_UPDATE_CURRENT_MUSIC,currentMusic);
		sendBroadcast(intent);
	}

	// onCreate->
	public void onCreate(){
		initMediaPlayer();
		musicList = MusicLoader.instance(getContentResolver()).getMusicList();
		super.onCreate();

	}

	public void onDestroy(){
		if(mediaPlayer != null){
			mediaPlayer.release();
			mediaPlayer = null;
		}
	}

	//初始化MediaPlayer
	private void initMediaPlayer(){
		mediaPlayer = new MediaPlayer();
		mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);//设置音频流的类型

		// 当MediaPlayer调用prepare()方法时触发该监听器
		mediaPlayer.setOnPreparedListener(new OnPreparedListener() {
			@Override
			public void onPrepared(MediaPlayer mp) {
				mediaPlayer.start();  //开始或恢复播放
				mediaPlayer.seekTo(currentPosition);  //寻找指定的时间位置
				handler.sendEmptyMessage(updateDuration);
			}
		});


		//为MediaPlayer的播放完成事件绑定事件监听器
		mediaPlayer.setOnCompletionListener(new OnCompletionListener() {
			@Override
			public void onCompletion(MediaPlayer mp) {
				if(isPlaying){
					switch (currentMode) {
						case MusicPlayActivity.MODE_ONE_LOOP:
							mediaPlayer.start();
							break;
						case MusicPlayActivity.MODE_ALL_LOOP:
							play((currentMusic+1) % musicList.size(), 0);
							break;
						case MusicPlayActivity.MODE_RANDOM:
							play(getRandomPosition(), 0);
							break;
						case MusicPlayActivity.MODE_SEQUENCE:
							if(currentMusic < musicList.size()-1){
								next();
							}else if(currentMusic == musicList.size() - 1){
								Toast.makeText(MusicService.this, "最后一首了.", Toast.LENGTH_SHORT).show();
							}
							break;
						default:
							break;
					}
				}
			}
		});


	}

	//设置当前音乐
	private void setCurrentMusic(int pCurrentMusic){
		currentMusic = pCurrentMusic;
		handler.sendEmptyMessage(updateCurrentMusic);
	}
	//获得随机位置
	private int getRandomPosition(){
		int random = (int)(Math.random() * (musicList.size() - 1));
		return random;
	}

	//播放
	private void play(int currentMusic, int pCurrentPosition) {
		currentPosition = pCurrentPosition;
		setCurrentMusic(currentMusic);
		mediaPlayer.reset();//重置MediaPlayer进入未初始化状态
		try {
			//指定装载path路径所代表的文件
			mediaPlayer.setDataSource(musicList.get(currentMusic).getUrl());
		} catch (Exception e) {
			e.printStackTrace();
		}
		mediaPlayer.prepareAsync();
		handler.sendEmptyMessage(updateProgress);

		isPlaying = true;
	}

	//播放
	private void playContinueMy(int currentMusic, int pCurrentPosition) {
		currentPosition = pCurrentPosition;
		setCurrentMusic(currentMusic);
		try {
			//指定装载path路径所代表的文件
			mediaPlayer.setDataSource(musicList.get(currentMusic).getUrl());
		} catch (Exception e) {
			e.printStackTrace();
		}
		mediaPlayer.prepareAsync();
		handler.sendEmptyMessage(updateProgress);

		isPlaying = true;
	}

	private void next(){
		switch(currentMode){
			case MusicPlayActivity.MODE_ONE_LOOP:
				play(currentMusic, 0);
				break;
			case MusicPlayActivity.MODE_ALL_LOOP:
				if(currentMusic + 1 == musicList.size()){
					play(0,0);
				}else{
					play(currentMusic + 1, 0);
				}
				break;
			case MusicPlayActivity.MODE_SEQUENCE:
				if(currentMusic + 1 == musicList.size()){
					Toast.makeText(this, "最后一首了.", Toast.LENGTH_SHORT).show();
				}else{
					play(currentMusic + 1, 0);
				}
				break;
			case MusicPlayActivity.MODE_RANDOM:
				play(getRandomPosition(), 0);
				break;
		}
	}

	//停止
	private void stop(){
		mediaPlayer.stop();
		isPlaying = false;
	}

	public int getMode(){
		return currentMode;
	}


	@Override
	public IBinder onBind(Intent intent) {
		return natureBinder;
	}

	class NatureBinder extends Binder{

		public void startPlay(int currentMusic, int currentPosition){
			play(currentMusic,currentPosition);
		}
		public void playContinue(int currentMusic, int currentPosition){
			playContinueMy(currentMusic,currentPosition);
		}

		public void stopPlay(){
			stop();
		}


		public void setCurrentMode(int mode){
			currentMode = mode;
		}

		public int getCurrentMode(){
			return currentMode;
		}

		//这个service在播放音乐
		public boolean isPlaying(){
			return isPlaying;
		}

		public void notifyActivity(){
			toUpdateCurrentMusic();
			toUpdateDuration();
		}

		public void changeProgress(int progress){
			if(mediaPlayer != null){
				currentPosition = progress * 1000;
				if(isPlaying){
					mediaPlayer.seekTo(currentPosition);
				}else{
					play(currentMusic, currentPosition);
				}
			}
		}
	}
}