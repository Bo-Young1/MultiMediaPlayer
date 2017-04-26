package com.coship.multimediaplayer.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaMetadataRetriever;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.coship.multimediaplayer.R;
import com.coship.multimediaplayer.model.MusicInfo;
import com.coship.multimediaplayer.utils.FormatHelper;

import java.util.List;

/**
 * Created by 980559 on 2017/4/14.
 */
public class MusicAdapter extends BaseAdapter {

     private Context context;
     private List<MusicInfo> musicList;

    public MusicAdapter(Context context,List<MusicInfo> musicList){
        this.context = context;
        this.musicList = musicList;
    }

    @Override
    public int getCount() {
        return musicList.size();
    }
    @Override
    public Object getItem(int position) {
        return musicList.get(position);
    }
    @Override
    public long getItemId(int position) {
        return musicList.get(position).getId();
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if(convertView == null){
            convertView = LayoutInflater.from(context).inflate(R.layout.music_item, null);
            ImageView pImageView = (ImageView) convertView.findViewById(R.id.albumPhoto);
            TextView pTitle = (TextView) convertView.findViewById(R.id.title);
            TextView pDuration = (TextView) convertView.findViewById(R.id.duration);
            TextView pArtist = (TextView) convertView.findViewById(R.id.artist);
            viewHolder = new ViewHolder(pImageView, pTitle, pDuration, pArtist);
            convertView.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.imageView.setImageResource(R.drawable.audio);
        viewHolder.title.setText(musicList.get(position).getTitle());
        viewHolder.duration.setText(FormatHelper.formatDuration(musicList.get(position).getDuration()));
        viewHolder.artist.setText(musicList.get(position).getArtist());

        return convertView;
    }
    class ViewHolder{
        public ViewHolder(ImageView pImageView, TextView pTitle, TextView pDuration, TextView pArtist){
            imageView = pImageView;
            title = pTitle;
            duration = pDuration;
            artist = pArtist;
        }
        ImageView imageView;
        TextView title;
        TextView duration;
        TextView artist;
    }
}

