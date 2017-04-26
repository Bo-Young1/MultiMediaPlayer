package com.coship.multimediaplayer.main;

import java.util.List;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaMetadataRetriever;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import android.widget.Toast;

import com.coship.multimediaplayer.R;
import com.coship.multimediaplayer.model.MusicLoader;
import com.coship.multimediaplayer.model.MusicInfo;
import com.coship.multimediaplayer.main.MusicService.NatureBinder;
import com.coship.multimediaplayer.utils.FormatHelper;

public class MusicPlayActivity extends AppCompatActivity implements OnClickListener{

	public static final String CURRENT_POSITION = "MusicPlayActivity.CURRENT_POSITION";
	public static final String CURRENT_MUSIC = "MusicPlayActivity.CURRENT_MUSIC";

	private int currentMode = 3; //默认顺序播放
	public static final String[] MODE_DESC = {"单曲循环", "列表循环", "随机播放", "顺序播放"};
	public static final int MODE_ONE_LOOP = 0; //单曲循环
	public static final int MODE_ALL_LOOP = 1; //列表循环
	public static final int MODE_RANDOM = 2;   //随机播放
	public static final int MODE_SEQUENCE = 3; //顺序播放

	private SeekBar pbDuration;
	private TextView tvTitle,tvTimeElapsed, tvDuration;
	private ImageView iv_detail;
	private List<MusicInfo> musicList;
	private boolean flagstart;

	private int currentMusic;
	private int currentPosition;

	private FragmentManager manager;
	private FragmentTransaction transaction;
	ImageButton btnStartStop;

	private ProgressReceiver progressReceiver;

	private NatureBinder natureBinder;

	private ServiceConnection serviceConnection = new ServiceConnection() {
		@Override
		public void onServiceDisconnected(ComponentName name) {
		}
		@Override
		public void onServiceConnected(ComponentName name, IBinder service) {
			natureBinder = (NatureBinder) service;
			if(natureBinder.isPlaying()){
				flagstart = false;
				btnStartStop.setImageResource(R.drawable.stop);
			}
		}
	};
	private void connectToMusicService(){
		Intent intent = new Intent(MusicPlayActivity.this, MusicService.class);
		bindService(intent, serviceConnection, BIND_AUTO_CREATE);
	}


	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		overridePendingTransition(R.anim.push_right_in,R.anim.hold);//Activity的切换动画指的是从一个activity跳转到另外一个activity时的动画
		MusicLoader musicLoader = MusicLoader.instance(getContentResolver());
		musicList = musicLoader.getMusicList();
		setContentView(R.layout.music_play);

		connectToMusicService();
		initComponents();
	}

	@Override
	public void onResume(){
		super.onResume();
		initReceiver();
	}
	@Override
	public void onPause(){
		super.onPause();
		unregisterReceiver(progressReceiver);
		overridePendingTransition(R.anim.hold, R.anim.push_right_out);
	}
	public void onStop(){
		super.onStop();
	}
	public void onDestroy(){
		super.onDestroy();
		if(natureBinder != null){
			unbindService(serviceConnection);
		}
	}

	//得到AlbumImage
	private Bitmap createAlbumImage(String url){
		Bitmap bitmap = null;
		MediaMetadataRetriever retriever = new MediaMetadataRetriever();
		try{
			retriever.setDataSource(url);
			byte[] art = retriever.getEmbeddedPicture();
			bitmap = BitmapFactory.decodeByteArray(art,0,art.length);
		}catch (IllegalArgumentException e){
		}catch (RuntimeException ex){
		}finally {
			try {
				retriever.release();
			} catch (RuntimeException ex) {
			}
		}
		return bitmap;
	}

	private void initComponents(){
		tvTitle = (TextView) findViewById(R.id.tvTitle);
		currentMusic = getIntent().getIntExtra(CURRENT_MUSIC,0);
		tvTitle.setText(musicList.get(currentMusic).getTitle());

		iv_detail = (ImageView)findViewById(R.id.iv_detail);
		iv_detail.setImageBitmap(createAlbumImage(musicList.get(currentMusic).getUrl()));

		tvDuration = (TextView) findViewById(R.id.tvDuration);
		int max = musicList.get(currentMusic).getDuration();
		tvDuration.setText(FormatHelper.formatDuration(max));

		pbDuration = (SeekBar) findViewById(R.id.pbDuration);
		pbDuration.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {}
			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {}
			@Override
			public void onProgressChanged(SeekBar seekBar, int progress,boolean fromUser) {
				if(fromUser){
					natureBinder.changeProgress(progress);
				}
			}
		});
		pbDuration.setMax(max/1000);

		currentPosition = getIntent().getIntExtra(CURRENT_POSITION,0);
		pbDuration.setProgress(currentPosition / 1000);

		tvTimeElapsed = (TextView) findViewById(R.id.tvTimeElapsed);
		tvTimeElapsed.setText(FormatHelper.formatDuration(currentPosition));

		btnStartStop = (ImageButton)findViewById(R.id.btnStartStop);
		btnStartStop.setOnClickListener(this);
		btnStartStop.requestFocus();

		ImageButton btnNext = (ImageButton)findViewById(R.id.btnNext);
		btnNext.setOnClickListener(this);

		ImageButton btnPrevious = (ImageButton)findViewById(R.id.btnPrevious);
		btnPrevious.setOnClickListener(this);

		ImageButton btnMode = (ImageButton)findViewById(R.id.btnMode);
		btnMode.setOnClickListener(this);

		ImageButton btnDetails = (ImageButton)findViewById(R.id.btnDetails);
		btnDetails.setOnClickListener(this);
	}


//	public int getCurrentPosition(){
//		return currentPosition;
//	}
//
//	public int getCurrentMusic(){
//		return currentMusic;
//	}


	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if(keyCode == KeyEvent.KEYCODE_BACK){
//			MusicListFragment fragment1 = new MusicListFragment();
//			Bundle bundle = new Bundle();
//			bundle.putInt("currentMusic", currentMusic);
//			bundle.putInt("currentPosition", currentPosition);
//			fragment1.setArguments(bundle);
//			//如果transaction  commit（）过  那么我们要重新得到transaction
//			transaction = manager.beginTransaction();
//			transaction.replace(R.id.framelayout, fragment1);
//			transaction.commit();
		}
		return super.onKeyDown(keyCode, event);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.btnStartStop:
				play(currentMusic,R.id.btnStartStop);
				break;
			case R.id.btnNext:
				flagstart = false;
				btnStartStop.setImageResource(R.drawable.stop);
				playNext();
				break;
			case R.id.btnPrevious:
				flagstart = false;
				btnStartStop.setImageResource(R.drawable.stop);
				playPrevious();
				break;
			case R.id.btnMode:
				changeMode();
				break;
			case R.id.btnDetails:
				getDetails();
				break;
			default:
				break;
		}
	}

	//播放下一曲
	private void playNext(){
		switch(currentMode){
			case MODE_ONE_LOOP:
				natureBinder.startPlay(currentMusic, 0);
				break;
			case MODE_ALL_LOOP:
				if(currentMusic + 1 == musicList.size()){
					natureBinder.startPlay(0,0);
				}else{
					natureBinder.startPlay(currentMusic + 1, 0);
				}
				break;
			case MODE_SEQUENCE:
				if(currentMusic + 1 == musicList.size()){
					Toast.makeText(this, "最后一首了.", Toast.LENGTH_SHORT).show();
				}else{
					natureBinder.startPlay(currentMusic + 1, 0);
				}
				break;
			case MODE_RANDOM:
				natureBinder.startPlay(getRandomPosition(), 0);
				break;
		}
	}

	//获得随机位置
	private int getRandomPosition(){
		int random = (int)(Math.random() * (musicList.size() - 1));
		return random;
	}


	//播放上一曲
	private void playPrevious(){
		switch(currentMode){
			case MODE_ONE_LOOP:
				natureBinder.startPlay(currentMusic, 0);
				break;
			case MODE_ALL_LOOP:
				if(currentMusic - 1 < 0){
					natureBinder.startPlay(musicList.size() - 1, 0);
				}else{
					natureBinder.startPlay(currentMusic - 1, 0);
				}
				break;
			case MODE_SEQUENCE:
				if(currentMusic - 1 < 0){
					Toast.makeText(this, "这是第一首.", Toast.LENGTH_SHORT).show();
				}else{
					natureBinder.startPlay(currentMusic - 1, 0);
				}
				break;
			case MODE_RANDOM:
				natureBinder.startPlay(getRandomPosition(), 0);
				break;
		}
	}

	//改变播放方式
	public void changeMode(){
		currentMode = (currentMode + 1) % 4;
		natureBinder.setCurrentMode(currentMode);
		Toast.makeText(MusicPlayActivity.this, MODE_DESC[currentMode], Toast.LENGTH_SHORT).show();
	}

	public void getDetails(){
		AlertDialog.Builder builder = new AlertDialog.Builder(MusicPlayActivity.this,AlertDialog.THEME_DEVICE_DEFAULT_DARK);
		builder.setTitle("歌曲信息");
		builder.setMessage("歌手："+musicList.get(currentMusic).getArtist()+"\n"+
				"专辑："+musicList.get(currentMusic).getAlbum());
		builder.setPositiveButton("返回",new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		});
		builder.show();
	}

	private void play(int currentMusic, int resId){
		btnStartStop = (ImageButton)findViewById(R.id.btnStartStop);
		if(flagstart == false){
			natureBinder.stopPlay();
			flagstart = true;
			btnStartStop.setImageResource(R.drawable.musicplay);
		}else if(flagstart == true){
			flagstart = false;
			btnStartStop.setImageResource(R.drawable.stop);
			natureBinder.startPlay(currentMusic,currentPosition);
		}
	}

	private void initReceiver(){
		progressReceiver = new ProgressReceiver();
		IntentFilter intentFilter = new IntentFilter();
		intentFilter.addAction(MusicService.ACTION_UPDATE_PROGRESS);
		intentFilter.addAction(MusicService.ACTION_UPDATE_DURATION);
		intentFilter.addAction(MusicService.ACTION_UPDATE_CURRENT_MUSIC);
		registerReceiver(progressReceiver, intentFilter);
	}

	class ProgressReceiver extends BroadcastReceiver{
		@Override
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();
			if(MusicService.ACTION_UPDATE_PROGRESS.equals(action)){

				int progress = intent.getIntExtra(MusicService.ACTION_UPDATE_PROGRESS, currentPosition);
				if(progress > 0){
					currentPosition = progress;
					//Log.d("tiaoshi","position:"+currentPosition);
					tvTimeElapsed.setText(FormatHelper.formatDuration(progress));//更新播放时间
					pbDuration.setProgress(progress / 1000);
				}

			}else if(MusicService.ACTION_UPDATE_CURRENT_MUSIC.equals(action)){

				currentMusic = intent.getIntExtra(MusicService.ACTION_UPDATE_CURRENT_MUSIC, 0);
				tvTitle.setText(musicList.get(currentMusic).getTitle());
				iv_detail.setImageBitmap(createAlbumImage(musicList.get(currentMusic).getUrl()));

			}else if(MusicService.ACTION_UPDATE_DURATION.equals(action)){

				int duration = intent.getIntExtra(MusicService.ACTION_UPDATE_DURATION, 0);
				tvDuration.setText(FormatHelper.formatDuration(duration));  //更新播放时长
				pbDuration.setMax(duration / 1000);
			}
		}

	}

}