package com.coship.multimediaplayer.viewpager;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

import com.coship.multimediaplayer.R;

import java.util.ArrayList;


public class PicViewerActivity extends AppCompatActivity {
    private ViewPager viewPager;
    private TextView tv_indicator;
    private ArrayList<String> urlList;
    private int pos;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pic_viewer);

        Intent intent = this.getIntent();
        Bundle bundle = intent.getExtras();
        urlList = bundle.getStringArrayList("imagepath");
        pos = bundle.getInt("position");

        viewPager = (ViewPager) findViewById(R.id.viewpager);
        tv_indicator = (TextView) findViewById(R.id.tv_indicator);

        viewPager.setAdapter(new PictureSlidePagerAdapter(getSupportFragmentManager()));
        viewPager.setCurrentItem(pos);//设置初始页面
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener(){
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
               tv_indicator.setText(String.valueOf(position+1)+"/"+urlList.size()); //在当前页面滑动至其他页面后，获得position值
            }
            @Override
            public void onPageSelected(int position) {
            }
            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
    }

    private class PictureSlidePagerAdapter extends FragmentStatePagerAdapter {
        public PictureSlidePagerAdapter(FragmentManager fm){
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return PictureSlideFragment.newInstance(urlList.get(position)); //返回展示不同网络图片的PictureSlideFragment
        }

        @Override
        public int getCount() {
            return urlList.size();  //指定ViewPager的总页数
        }
    }


}



















