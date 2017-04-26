package com.coship.multimediaplayer.viewpager;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.coship.multimediaplayer.R;

public class PictureSlideFragment extends Fragment implements View.OnClickListener{
    private String url;

    private ImageView imageView;
    ImageButton big;
    ImageButton small;
    ImageButton rotate;
    LinearLayout operator;
    private float scaleX = 1.0f;
    private float scaleY = 1.0f;
    private float angle = 0.0f;
    Bitmap bitmap;
    Matrix matrix = new Matrix();;

    public static PictureSlideFragment newInstance(String url){
        PictureSlideFragment fragment = new PictureSlideFragment();
        Bundle args = new Bundle();
        args.putString("url",url);
        fragment.setArguments(args);
        return fragment;  //获得一个包含图片url的PictureSlideFragment的实例
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null){
            url = getArguments().getString("url");
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_picture_slide,container,false);
        imageView = (ImageView) v.findViewById(R.id.iv_main_pic);
        big = (ImageButton)v.findViewById(R.id.big);
        small = (ImageButton)v.findViewById(R.id.small);
        rotate = (ImageButton)v.findViewById(R.id.rotate);
        operator = (LinearLayout)v.findViewById(R.id.operate);

        Glide.with(getActivity()).load(url).crossFade().into(imageView);

        bitmap = BitmapFactory.decodeFile(url);
        imageView.setFocusable(true);
        imageView.requestFocus();

        imageView.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus){
                }else{
                }
            }
        });
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (operator.getVisibility() == View.INVISIBLE || operator.getVisibility() == View.GONE){
                    operator.setVisibility(View.VISIBLE);
                    new Handler().postDelayed(new Runnable(){
                        public void run() {
                            operator.setVisibility(View.GONE);
                            imageView.setFocusable(true);
                        }
                    }, 30000);
                }
                imageView.setFocusable(false);
                big.setFocusable(true);
            }
        });
        big.setOnClickListener(this);
        small.setOnClickListener(this);
        rotate.setOnClickListener(this);

        return v;
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.big:
                if (scaleX > 1.5 && scaleY > 1.5){
                    Toast.makeText(getContext(),"不能再放大啦！",Toast.LENGTH_SHORT).show();
                }else{
                    scaleX +=0.1f;
                    scaleY +=0.1f;
                }
                imageView.setScaleX(scaleX);
                imageView.setScaleY(scaleY);
                break;
            case R.id.small:
                if (scaleX >= 0.5 && scaleY >=0.5){
                    scaleX -=0.1f;
                    scaleY -=0.1f;
                }else{
                    Toast.makeText(getContext(),"不能再缩小啦！",Toast.LENGTH_SHORT).show();
                }
                imageView.setScaleX(scaleX);
                imageView.setScaleY(scaleY);
                break;
            case R.id.rotate:
//                angle +=9.0f;
//                imageView.setRotation(angle);
////                matrix.reset();
                matrix.postRotate(90,bitmap.getWidth()/2,bitmap.getHeight()/2);
                Bitmap mbitmap = Bitmap.createBitmap(bitmap,0,0,bitmap.getWidth(),bitmap.getHeight(),matrix,true);
                imageView.setImageBitmap(mbitmap);
                break;
            default:
                break;
        }
    }

}





















