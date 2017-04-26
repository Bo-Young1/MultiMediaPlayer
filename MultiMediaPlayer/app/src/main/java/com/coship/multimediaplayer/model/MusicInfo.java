package com.coship.multimediaplayer.model;

import android.os.Parcel;
import android.os.Parcelable;
import android.widget.ImageView;

public class MusicInfo implements Parcelable {
    private long id;// 歌曲ID
    private String title;// 歌曲名称
    private String album;// 专辑
    private int albumId;//专辑ID
    private int duration;// 歌曲时长
    private long size;// 歌曲大小
    private String artist;// 歌手名称
    private String url;// 歌曲路径
    private String albumart;

    private String lrcTitle; // 歌词名称
    private String lrcSize; // 歌词大小
    private String albumImageSrc; //专辑封面路径


    public String getAlbumart() {
        return albumart;
    }

    public void setAlbumart(String albumart) {
        this.albumart = albumart;
    }

    public MusicInfo(){

    }

    public int getAlbumId() {
        return albumId;
    }

    public void setAlbumId(int albumId) {
        this.albumId = albumId;
    }

    public MusicInfo(long pId, String pTitle){
        id = pId;
        title = pTitle;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAlbum() {
        return album;
    }

    public void setAlbum(String album) {
        this.album = album;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(id);
        dest.writeString(title);
        dest.writeString(album);
        dest.writeString(artist);
        dest.writeString(url);
        dest.writeInt(duration);
        dest.writeLong(size);
    }

    public static final Creator<MusicInfo>
            CREATOR = new Creator<MusicInfo>() {

        @Override
        public MusicInfo[] newArray(int size) {
            return new MusicInfo[size];
        }

        @Override
        public MusicInfo createFromParcel(Parcel source) {
            MusicInfo musicInfo = new MusicInfo();
            musicInfo.setId(source.readLong());
            musicInfo.setTitle(source.readString());
            musicInfo.setAlbum(source.readString());
            musicInfo.setArtist(source.readString());
            musicInfo.setUrl(source.readString());
            musicInfo.setDuration(source.readInt());
            musicInfo.setSize(source.readLong());
            return musicInfo;
        }
    };
}
