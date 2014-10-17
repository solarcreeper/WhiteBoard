package com.WhiteBoard.Media;

import java.io.InputStream;
import java.io.OutputStream;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;

public class ImageDecoder {
	
	private final String TAG = "ImageDecoder";
	
	private Context context;
	ExifInterface exifInterface;
	public final static int DIMENSIONS_AVATAR = 100;
	public final static int DIMENSIONS_DEFAULT = 500;
	private static int MAX_VALUE_OF_DIMENSIONS = 500;
	
	public ImageDecoder(Context context) {
		this.context = context;
	}
	
	public void compressBitmap(Uri original, Uri target) {
		compressBitmap(original, target, DIMENSIONS_DEFAULT);
	}

	public void compressBitmap(Uri original, Uri target, int max_value_of_dimensions) {
		MAX_VALUE_OF_DIMENSIONS = max_value_of_dimensions;
	    try {
	    	// Get original orientation
	    	int orientation = getOrientation(original);
	    	Log.d(TAG, "Orientation : " + orientation);
	    	Matrix matrix = new Matrix();
	    	matrix.setRotate(orientation);
	    	
	        InputStream in = context.getContentResolver().openInputStream(original);
	        
	        // Decode round 1, get original dimensions only
	        BitmapFactory.Options options = new BitmapFactory.Options();
	        options.inJustDecodeBounds = true;
	        BitmapFactory.decodeStream(in, null, options);
	        in.close();
	        
	        // Calculate target dimensions
	        options.inSampleSize = calculateInSampleSize(options);
	 
	        // Decode round 2, get the target small image
	        in = context.getContentResolver().openInputStream(original);
	        options.inJustDecodeBounds = false;
	        OutputStream os = context.getContentResolver().openOutputStream(target);
	        Bitmap bmp = BitmapFactory.decodeStream(in, null, options);
	        Bitmap.createBitmap(bmp, 0, 0, bmp.getWidth(), bmp.getHeight(), matrix, true).compress(CompressFormat.JPEG, 100, os);
	        in.close();
	        os.close();
	        // Save file in target uri
	        
	        
	 
	        
	    } catch (Exception err) {
	        
	    }
	}
	
	private static int calculateInSampleSize(BitmapFactory.Options options) {
	    // Raw height and width of image
	    final int height = options.outHeight;
	    final int width = options.outWidth;
	    int inSampleSize = 1;
	
	    if (height > MAX_VALUE_OF_DIMENSIONS || width > MAX_VALUE_OF_DIMENSIONS) {
	
	        // Calculate ratios of height and width to requested height and width
	        final int heightRatio = Math.round((float) height / (float) MAX_VALUE_OF_DIMENSIONS);
	        final int widthRatio = Math.round((float) width / (float) MAX_VALUE_OF_DIMENSIONS);
	
	        // Choose the largest ratio as inSampleSize value, this will guarantee
	        // a final image with both dimensions larger than or equal to the
	        // requested height and width.
	        inSampleSize = heightRatio > widthRatio ? heightRatio : widthRatio;
	    }
	
	    return inSampleSize;
	}
	
	public int getOrientation(Uri photoUri) {
	    /* it's on the external media. */
	    Cursor cursor = context.getContentResolver().query(photoUri,
	            new String[] { MediaStore.Images.ImageColumns.ORIENTATION }, null, null, null);

	    if (cursor.getCount() != 1) {
	        return -1;
	    }

	    cursor.moveToFirst();
	    return cursor.getInt(0);
	}

}
