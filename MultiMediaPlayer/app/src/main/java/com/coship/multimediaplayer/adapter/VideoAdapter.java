package com.coship.multimediaplayer.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.coship.multimediaplayer.R;
import com.coship.multimediaplayer.custom.SquareLayout;
import com.coship.multimediaplayer.model.VideoInfo;
import com.coship.multimediaplayer.main.VideoListFragment.LoadedImage;
import com.coship.multimediaplayer.utils.FormatHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 980559 on 2017/4/17.
 */
public class VideoAdapter extends BaseAdapter {
    private List<VideoInfo> list;
    private Context context;
    private LayoutInflater mInflater;
    private ArrayList<LoadedImage> photos = new ArrayList<LoadedImage>();
    private int seleted = -1;


    public VideoAdapter(Context context ,List<VideoInfo> list) {
        this.context = context;
        this.list = list;
        mInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return photos.size();
    }

    public void addPhoto(LoadedImage image){
        photos.add(image);
    }
    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder hold = null;
        if (convertView==null) {
            hold = new ViewHolder();
            convertView = mInflater.inflate(R.layout.video_list_item, null);
            hold.linearlayout = (LinearLayout)convertView.findViewById(R.id.linearlayout);
            hold.img = (ImageView) convertView.findViewById(R.id.videoitem);
            hold.tv_title = (TextView) convertView.findViewById(R.id.tv_title);
            hold.tv_time = (TextView) convertView.findViewById(R.id.tv_time);
            convertView.setTag(hold);
        }else{
            hold = (ViewHolder) convertView.getTag();
        }
//        if (seleted == position){
//            hold.linearlayout.setBackgroundColor(Color.parseColor("#00FFFF"));
//            hold.linearlayout.setPadding(4,4,4,4);
//        }else{
//            hold.linearlayout.setBackgroundColor(Color.TRANSPARENT);
//            hold.linearlayout.setPadding(0,0,0,0);
//        }

        hold.tv_title.setText(list.get(position).getTitle());
        hold.tv_time.setText(FormatHelper.formatDuration(list.get(position).getDuration()));
        hold.img.setImageBitmap(photos.get(position).getBitmap());
        return convertView;
    }

    private class ViewHolder{
        LinearLayout linearlayout;
        ImageView img;
        TextView tv_title;
        TextView tv_time;

    }
    public void notifyDataSetChanged(int id) {
        seleted = id;
        super.notifyDataSetChanged();
    }
}