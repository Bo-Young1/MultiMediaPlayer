package com.coship.multimediaplayer.main;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.media.ThumbnailUtils;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;

import com.coship.multimediaplayer.R;
import com.coship.multimediaplayer.adapter.VideoAdapter;
import com.coship.multimediaplayer.model.GetVideoInfoUtils;
import com.coship.multimediaplayer.model.VideoInfo;

import java.util.List;

/**
 * Created by 980559 on 2017/4/20.
 */
public class VideoListFragment extends Fragment {

    private GridView gridview;
    VideoAdapter mAdapter;
    List<VideoInfo> listVideos;
    int videoSize;

    Button btnVideo;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.video_list,container,false);
        GetVideoInfoUtils provider = new GetVideoInfoUtils(getContext());
        listVideos = provider.getList();
        videoSize = listVideos.size();
        gridview = (GridView)view.findViewById(R.id.gridview);

        mAdapter = new VideoAdapter(getActivity(), listVideos);
        gridview.setAdapter(mAdapter);
        gridview.setSelector(new ColorDrawable(Color.TRANSPARENT));

//        gridview.setSelection(4);

        gridview.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                  mAdapter.notifyDataSetChanged(position);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent();
                intent.setClass(getActivity(), VideoPlayActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("videoInfo", listVideos.get(position));
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });

        gridview.setOnKeyListener(backListenr);
        loadImages();
        return view;

    }

    View.OnKeyListener backListenr = new View.OnKeyListener(){
        @Override
        public boolean onKey(View v, int keyCode, KeyEvent event) {
            if (event.getAction() == KeyEvent.ACTION_DOWN){
                if (keyCode == KeyEvent.KEYCODE_BACK){
                    btnVideo = (Button) getActivity().findViewById(R.id.btnVideo);
                    btnVideo.setFocusable(true);
                    btnVideo.requestFocus();
                    return true;
                }
            }
            return false;
        }
    };

    private void loadImages() {
        final Object data = getActivity().getLastNonConfigurationInstance();
        if (data == null) {
            new LoadImagesFromSDCard().execute();//执行一个异步任务，需要我们在代码中调用此方法，触发异步任务的执行
        } else {
            final LoadedImage[] photos = (LoadedImage[]) data;
            if (photos.length == 0) {
                new LoadImagesFromSDCard().execute();
            }
            for (LoadedImage photo : photos) {
                addImage(photo);
            }
        }
    }

    private void addImage(LoadedImage... value) {
        for (LoadedImage image : value) {
            mAdapter.addPhoto(image);
            mAdapter.notifyDataSetChanged();
        }
    }

    private Bitmap getVideoThumbnail(String videoPath, int width, int height, int kind) {
        Bitmap bitmap = null;
        bitmap = ThumbnailUtils.createVideoThumbnail(videoPath, kind);
        bitmap = ThumbnailUtils.extractThumbnail(bitmap, width, height, ThumbnailUtils.OPTIONS_RECYCLE_INPUT);
        return bitmap;
    }

    class LoadImagesFromSDCard extends AsyncTask<Object, LoadedImage, Object> {
        @Override
        protected void onPreExecute() {
        }
        @Override
        protected Object doInBackground(Object... params) {
            //在onPreExecute()完成后立即执行，用于执行较为费时的操作，此方法将接收输入参数和返回计算结果。
            // 在执行过程中可以调用publishProgress(Progress... values)来更新进度信息
            Bitmap bitmap = null;
            for (int i = 0; i < videoSize; i++) {
                bitmap = getVideoThumbnail(listVideos.get(i).getPath(), 150, 200, MediaStore.Video.Thumbnails.MINI_KIND);
                if (bitmap != null) {
                    publishProgress(new LoadedImage(bitmap));
                }
            }
            return null;
        }
        @Override
        public void onProgressUpdate(LoadedImage... value) {
            addImage(value);
        }
        @Override
        protected void onPostExecute(Object result) {
        }
    }

    public class LoadedImage {
        Bitmap mBitmap;
        public LoadedImage(Bitmap bitmap) {
            mBitmap = bitmap;
        }
        public Bitmap getBitmap() {
            return mBitmap;
        }
    }
}
