package com.coship.multimediaplayer.main;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;

import com.coship.multimediaplayer.R;
import com.coship.multimediaplayer.main.MusicListFragment;
import com.coship.multimediaplayer.main.PictureListFragment;
import com.coship.multimediaplayer.main.VideoListFragment;
import com.coship.multimediaplayer.utils.ScreenUtils;

public class MainActivity extends AppCompatActivity{

    Button btnPicture;
    Button btnMusic;
    Button btnVideo;

    Fragment fragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }

    private void initView() {
        ScreenUtils.initScreen(this);

        btnPicture = (Button)findViewById(R.id.btnPicture);
        btnPicture.setFocusable(true);
        btnPicture.requestFocus();

        btnMusic = (Button)findViewById(R.id.btnMusic);
        btnVideo = (Button)findViewById(R.id.btnVideo);

//        btnPicture.setOnClickListener(this);
//        btnMusic.setOnClickListener(this);
//        btnVideo.setOnClickListener(this);
        replaceFragment(new PictureListFragment());

        btnPicture.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus){
                    replaceFragment(new PictureListFragment());
                }
            }
        });
        btnMusic.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus){
                    replaceFragment(new MusicListFragment());
                }
            }
        });
        btnVideo.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus){
                    replaceFragment(new VideoListFragment());
                }
            }
        });
    }

//    @Override
//    public void onClick(View v) {
//        switch(v.getId()){
//            case R.id.btnPicture:
//                replaceFragment(new PictureListFragment());
//                break;
//            case R.id.btnMusic:
//                replaceFragment(new MusicListFragment());
//                break;
//            case R.id.btnVideo:
//                replaceFragment(new VideoListFragment());
//                break;
//            default:
//                break;
//        }
//    }


    public void replaceFragment(Fragment fragment){
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.framelayout,fragment);
        transaction.commit();
    }
}
