package com.coship.multimediaplayer.main;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.Toast;

import com.coship.multimediaplayer.R;
import com.coship.multimediaplayer.adapter.PictureAdapter;
import com.coship.multimediaplayer.model.PictureDataOperation;
import com.coship.multimediaplayer.viewpager.PicViewerActivity;

import java.util.ArrayList;

/**
 * Created by 980559 on 2017/4/20.
 */
public class PictureListFragment extends Fragment{

    private PictureAdapter mPictureAdapter;
    private ArrayList<String> imagePathList;

    GridView picture_thumbnail;
    Button btnPicture;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.picture_list,container,false);
        picture_thumbnail = (GridView)view.findViewById(R.id.picture_thumbnail);

        btnPicture = (Button) getActivity().findViewById(R.id.btnPicture);

        imagePathList = PictureDataOperation.getImagePaths(getContext());
        mPictureAdapter = new PictureAdapter(getContext(),imagePathList);
        picture_thumbnail.setAdapter(mPictureAdapter);
        picture_thumbnail.setSelector(new ColorDrawable(Color.TRANSPARENT));
        picture_thumbnail.setOnKeyListener(backListenr);
        initListener();
        return view;
    }

    View.OnKeyListener backListenr = new View.OnKeyListener(){
        @Override
        public boolean onKey(View v, int keyCode, KeyEvent event) {
            if (event.getAction() == KeyEvent.ACTION_DOWN){
                if (keyCode == KeyEvent.KEYCODE_BACK){
                    btnPicture.setFocusable(true);
                    btnPicture.requestFocus();
                    return true;
                }
            }
           return false;
        }
    };


    public void initListener() {

        picture_thumbnail.setOnFocusChangeListener(new View.OnFocusChangeListener(){
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus){
                }else{
//                    btnPicture.setFocusable(true);
//                    btnPicture.requestFocus();
//                    ((MainActivity)getActivity()).replaceFragment(new PictureListFragment());
                }
            }
        });
        picture_thumbnail.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
               // mPictureAdapter.notifyDataSetChanged(position);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        picture_thumbnail.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity(), PicViewerActivity.class);
                Bundle bundle = new Bundle();
                bundle.putStringArrayList("imagepath",imagePathList);
                bundle.putInt("position",position);
                intent.putExtras(bundle);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                getActivity().startActivity(intent);
            }
        });
    }
}
