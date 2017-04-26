package com.coship.multimediaplayer.main;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.coship.multimediaplayer.R;
import com.coship.multimediaplayer.adapter.MusicAdapter;
import com.coship.multimediaplayer.model.MusicInfo;
import com.coship.multimediaplayer.model.MusicLoader;
import com.coship.multimediaplayer.main.MusicService.NatureBinder;
import java.util.List;

/**
 * Created by 980559 on 2017/4/20.
 */
public class MusicListFragment extends Fragment {

    private MusicAdapter adapter;
    ListView lvSongs;
    private List<MusicInfo> musicList;
    private int currentMusic;
    private int currentPosition;
    private boolean isPlaying;

    Button btnMusic;

    private NatureBinder natureBinder;

    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceDisconnected(ComponentName name) {
        }
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            //统调用这个来传送在service的onBind()中返回的IBinder．
            natureBinder = (NatureBinder) service;
        }
    };

    private void connectToMusicService(){
        Intent intent = new Intent(getActivity(), MusicService.class);
        //bindService()绑定到一个service．系统之后调用service的onBind()方法，它返回一个用来与service交互的IBinder．
        //绑定是异步的．bindService()会立即返回，它不会返回IBinder给客户端．
        // 要接收IBinder，客户端必须创建一个ServiceConnection的实例并传给bindService()．
        // ServiceConnection包含一个回调方法，系统调用这个方法来传递要返回的IBinder．
        getActivity().bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.music_list,container,false);

        MusicLoader musicLoader = MusicLoader.instance(getActivity().getContentResolver());
        musicList = musicLoader.getMusicList();
        adapter = new MusicAdapter(getActivity(),musicList);
        lvSongs = (ListView) view.findViewById(R.id.lvSongs);
        lvSongs.setAdapter(adapter);
        connectToMusicService();
        lvSongs.setOnKeyListener(backListenr);

        initListener();

        return view;
    }

    View.OnKeyListener backListenr = new View.OnKeyListener(){
        @Override
        public boolean onKey(View v, int keyCode, KeyEvent event) {
            if (event.getAction() == KeyEvent.ACTION_DOWN){
                if (keyCode == KeyEvent.KEYCODE_BACK){
                    btnMusic = (Button) getActivity().findViewById(R.id.btnMusic);
                    btnMusic.setFocusable(true);
                    btnMusic.requestFocus();
                    return true;
                }
            }
            return false;
        }
    };

    public void initListener(){
        lvSongs.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,int position, long id) {
                currentMusic = position;
                natureBinder.startPlay(currentMusic,0);
                Intent intent = new Intent(getActivity(),MusicPlayActivity.class);
                intent.putExtra(MusicPlayActivity.CURRENT_MUSIC, currentMusic);
                getActivity().startActivity(intent);
            }
        });
    }

    public void onResume(){
        super.onResume();
        if(natureBinder != null){
            if(natureBinder.isPlaying()){
//                currentMusic = MusicPlayActivity.currentMusic;
//                currentPosition = MusicPlayActivity.currentPosition;

           //     Log.d("tiaoshi","currentMusic:"+currentMusic+" currentPosition:"+currentPosition);
//                lvSongs.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//                    @Override
//                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                        if (position == currentMusic){
//                            natureBinder.playContinue(currentMusic,currentPosition);
//                            Intent intent = new Intent(getActivity(),MusicPlayActivity.class);
//                            intent.putExtra(MusicPlayActivity.CURRENT_MUSIC, currentMusic);
//                            getActivity().startActivity(intent);
//                        }
//
//                    }
//                });
            }else{
                Log.d("tiaoshi","bcd");
            }
            natureBinder.notifyActivity();
        }
    }

    public void onPause(){
        super.onPause();
    }
    public void onStop(){
        super.onStop();
    }
    public void onDestroy(){
        super.onDestroy();
        if(natureBinder != null){
            getActivity().unbindService(serviceConnection);
        }
    }

}
