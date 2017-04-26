package com.coship.multimediaplayer.model;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;

import java.util.ArrayList;

/**
 * Created by 980559 on 2017/4/10.
 */
public class PictureDataOperation {

    public static ArrayList<String> getImagePaths(Context context){
        Uri mImageUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
        String key_DATA = MediaStore.Images.Media.DATA;

        ContentResolver mContentResolver =context.getContentResolver();
        Cursor cursor = mContentResolver.query(mImageUri, null, null,null, null);

        ArrayList<String> imagePaths = null;

        if (cursor != null){
            if (cursor.moveToFirst()){
                imagePaths = new ArrayList<String>();

                while (true){
                    String path = cursor.getString(cursor.getColumnIndex(key_DATA));
                    imagePaths.add(path);
                    if (!cursor.moveToNext()){
                        break;
                    }
                }
            }
            cursor.close();
        }
        return imagePaths;
    }

//    public static ArrayList<String> getThumbnailPath(Context context){
//        Uri thumbnailUri = MediaStore.Images.Thumbnails.EXTERNAL_CONTENT_URI;
//        String thumbnail_DATA = MediaStore.Images.Thumbnails.DATA;
//
//        ContentResolver mContentResolver = context.getContentResolver();
//        Cursor cursor1 = mContentResolver.query(thumbnailUri,null,null,null,null);
//
//        ArrayList<String> thumbnailPaths = null;
//
//        if (cursor1 != null){
//            if (cursor1.moveToFirst()){
//                thumbnailPaths = new ArrayList<String>();
//
//                while (true){
//                    String path = cursor1.getString(cursor1.getColumnIndex(thumbnail_DATA));
//
//                    thumbnailPaths.add(path);
//
//                    if (!cursor1.moveToNext()){
//                        break;
//                    }
//                }
//            }
//            cursor1.close();
//        }
//        return thumbnailPaths;
//    }
}
