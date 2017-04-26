package com.coship.multimediaplayer.model;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore.Audio.Media;

public class MusicLoader {

	private static List<MusicInfo> musicList = new ArrayList<MusicInfo>();
	private static MusicLoader musicLoader;
	private static ContentResolver contentResolver;
	private Uri contentUri = Media.EXTERNAL_CONTENT_URI;

	public static MusicLoader instance(ContentResolver pContentResolver){
		if(musicLoader == null){
			contentResolver = pContentResolver;
			musicLoader = new MusicLoader();			
		}
		return musicLoader;
	}
	
	private MusicLoader(){
		Cursor cursor = contentResolver.query(contentUri, null, null, null, null);

		if(cursor == null){
		}else if(!cursor.moveToFirst()){
		}else{			
			int displayNameCol = cursor.getColumnIndex(Media.TITLE);
			int albumCol = cursor.getColumnIndex(Media.ALBUM);
			int albumid = cursor.getColumnIndex(Media.ALBUM_ID);
			int idCol = cursor.getColumnIndex(Media._ID);
			int durationCol = cursor.getColumnIndex(Media.DURATION);
			int sizeCol = cursor.getColumnIndex(Media.SIZE);
			int artistCol = cursor.getColumnIndex(Media.ARTIST);
			int urlCol = cursor.getColumnIndex(Media.DATA);			
			do{
				String title = cursor.getString(displayNameCol);
				String album = cursor.getString(albumCol);
				int albumId = cursor.getInt(albumid);
				long id = cursor.getLong(idCol);
				int duration = cursor.getInt(durationCol);
				long size = cursor.getLong(sizeCol);
				String artist = cursor.getString(artistCol);
				String url = cursor.getString(urlCol);
				String album_art = getThisAlbumArt(albumId);

				MusicInfo musicInfo = new MusicInfo(id, title);
				musicInfo.setAlbum(album);
				musicInfo.setAlbumId(albumId);
				musicInfo.setDuration(duration);
				musicInfo.setSize(size);
				musicInfo.setArtist(artist);
				musicInfo.setUrl(url);
				musicInfo.setAlbumart(album_art);

				musicList.add(musicInfo);

			}while(cursor.moveToNext());
		}
	}

	private String getThisAlbumArt(int album_id) {
		String mUriAlbums = "content://media/external/audio/albums";
		String[] projection = new String[] { "album_art" };
		Cursor cur = contentResolver.query(
				Uri.parse(mUriAlbums + "/" + Integer.toString(album_id)),
				projection, null, null, null);
		String album_art = null;
		if (cur.getCount() > 0 && cur.getColumnCount() > 0) {
			cur.moveToNext();
			album_art = cur.getString(0);
		}
		cur.close();
		cur = null;
		return album_art;
	}


	public List<MusicInfo> getMusicList(){
		return musicList;
	}
	
	public Uri getMusicUriById(long id){
		Uri uri = ContentUris.withAppendedId(contentUri, id);
		return uri;
	}	

}
