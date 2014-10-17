package com.WhiteBoard.Media;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.WhiteBoard.BaseClasses.DefaultSettings;

import android.net.Uri;
import android.os.Environment;
import android.util.Log;

public class MediaFile {

	public static final int MEDIA_TYPE_IMAGE = 1;
	public static final int MEDIA_TYPE_VIDEO = 2;
	public static final int MEDIA_TYPE_PROFILE = 3;
	public static final int MEDIA_TYPE_AUDIO = 4;

	/** Create a file Uri for saving an image or video */
	public static Uri getOutputMediaFileUri(int type){
	      return Uri.fromFile(getOutputMediaFile(type));
	}

	/** Create a File for saving an image or video */
	private static File getOutputMediaFile(int type){
	    // To be safe, you should check that the SDCard is mounted
	    // using Environment.getExternalStorageState() before doing this.
		
		File mediaStorageDir;
		if (type == MEDIA_TYPE_IMAGE || type == MEDIA_TYPE_PROFILE){
			mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "WhiteBoard");
	    } else if(type == MEDIA_TYPE_VIDEO) {
	    	mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MOVIES), "WhiteBoard");
	    } else if(type == MEDIA_TYPE_AUDIO) {
	    	mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MUSIC), "WhiteBoard");
	    } else {
	    	mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "WhiteBoard");
	    }
	    // This location works best if you want the created images to be shared
	    // between applications and persist after your app has been uninstalled.

	    // Create the storage directory if it does not exist
	    if (! mediaStorageDir.exists()){
	        if (! mediaStorageDir.mkdirs()){
	            Log.d("WhiteBoard", "failed to create directory");
	            return null;
	        }
	    }

	    // Create a media file name
	    String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
	    File mediaFile;
	    if (type == MEDIA_TYPE_IMAGE){
	        mediaFile = new File(mediaStorageDir.getPath() + File.separator + "IMG_"+ DefaultSettings.getLocalUid() + "_" + timeStamp + ".jpg");
	    } else if(type == MEDIA_TYPE_VIDEO) {
	        mediaFile = new File(mediaStorageDir.getPath() + File.separator + "VID_"+ DefaultSettings.getLocalUid() + "_" + timeStamp + ".mp4");
	    } else if(type == MEDIA_TYPE_PROFILE) {
	        mediaFile = new File(mediaStorageDir.getPath() + File.separator + DefaultSettings.getLocalUid() + ".jpg");
	    } else if(type == MEDIA_TYPE_AUDIO) {
	        mediaFile = new File(mediaStorageDir.getPath() + File.separator + "AUD_" + DefaultSettings.getLocalUid() + "_" + timeStamp + ".3gp");
	    } else {
	        return null;
	    }
	    Log.d("MediaFile", "Done");
	    return mediaFile;
	}

}
