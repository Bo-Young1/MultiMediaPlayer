package com.coship.multimediaplayer.adapter;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.coship.multimediaplayer.R;
import com.coship.multimediaplayer.custom.SquareLayout;
import com.coship.multimediaplayer.utils.SDCardImageLoader;
import com.coship.multimediaplayer.utils.ScreenUtils;

import java.util.ArrayList;

/**
 * Created by 980559 on 2017/4/10.
 */
public class PictureAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<String> imagePathList = null;
    private SDCardImageLoader loader;
    //private int seleted = -1;

    public PictureAdapter(Context context, ArrayList<String> imagePathList) {
        this.context = context;
        this.imagePathList = imagePathList;

        loader = new SDCardImageLoader(ScreenUtils.getScreenW(), ScreenUtils.getScreenH());
    }

    @Override
    public int getCount() {
        return imagePathList == null ? 0 : imagePathList.size();
    }

    @Override
    public Object getItem(int position) {
        return imagePathList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        String filePath = (String) getItem(position);

        final ViewHolder viewHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.thumbnail_item, null);
            viewHolder = new ViewHolder();
            viewHolder.imageView = (ImageView) convertView.findViewById(R.id.thunbnailitem);
            viewHolder.squarelayout = (SquareLayout)convertView.findViewById(R.id.squarelayout);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
//        if (seleted == position){
//            viewHolder.squarelayout.setBackgroundColor(Color.parseColor("#00FFFF"));
//            viewHolder.squarelayout.setPadding(4,4,4,4);
//        }else{
//            viewHolder.squarelayout.setBackgroundColor(Color.TRANSPARENT);
//            viewHolder.squarelayout.setPadding(0,0,0,0);
//        }

        viewHolder.imageView.setTag(filePath);
        loader.loadImage((int)0.1, filePath, viewHolder.imageView);
       // Glide.with(context).load(filePath).thumbnail(0.1f).into(viewHolder.imageView);

        return convertView;
    }

    private class ViewHolder {
        ImageView imageView;
        SquareLayout squarelayout;
    }

//    public void notifyDataSetChanged(int id) {
//        seleted = id;
//        super.notifyDataSetChanged();
//    }
}

































