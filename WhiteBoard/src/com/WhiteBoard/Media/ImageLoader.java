package com.WhiteBoard.Media;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Collections;
import java.util.Map;
import java.util.WeakHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.widget.ImageView;

/**
 * @author mzalive
 * ImageLoader�࣬���ڼ�������ͼ��֧���ļ����棬�ڴ滺�棬�̳߳�
 * �����FileCache�࣬MemoryCache��
 * 
 * ʹ��Demo:
 * 
 *   import com.WhiteBoard.Media.ImageLoader; //���������δ�Զ����룩
 *   private ImageLoader imageLoader; //������������
 *   ...
 *   imageLoader = new ImageLoader(getApplicationContext()); //ʵ��������ʹ��Application������
 *   imageLoader.clearCache(); //��ջ���
 *   ...
 *   String url = new String("https://www.google.com.hk/images/srpr/logo4w.png"); //׼��������ͼ���url
 *   imageLoader.DisplayImage(url, imageView, requiredSize); //����DisplayImage����
 * 
 * DisplayImage����������
 *   
 *   String url������ͼ��ĵ�ַ
 *   ImageView imageView��������ʾͼ���ImageView����
 *   int requiredSize��ͼ����ʾ�����ߴ磨���أ�
 *   
 * Ԥ���requiredSizeֵ��
 *   REQUIRE_SIZE_AVATAR��ͷ��
 *   REQUIRE_SIZE_BACKGROUND��profile����
 *   
 */

public class ImageLoader {
	public final static int REQUIRE_SIZE_AVATAR = 100;
	public final static int REQUIRE_SIZE_BACKGROUND = 700;
	
	private int REQUIRED_SIZE = REQUIRE_SIZE_AVATAR;

	MemoryCache memoryCache = new MemoryCache();
	FileCache fileCache;
	private Map<ImageView, String> imageViews = Collections
			.synchronizedMap(new WeakHashMap<ImageView, String>());
	// �̳߳�
	ExecutorService executorService;

	public ImageLoader(Context context) {
		fileCache = new FileCache(context);
		executorService = Executors.newFixedThreadPool(5);
	}

	// ������listviewʱĬ�ϵ�ͼƬ���ɻ������Լ���Ĭ��ͼƬ
	//final int stub_id = R.drawable.default_avatar;

	// ����Ҫ�ķ���
	public void DisplayImage(String url, ImageView imageView, int requiredSize) {
		this.REQUIRED_SIZE = requiredSize;
		
		imageViews.put(imageView, url);
		// �ȴ��ڴ滺���в���

		Bitmap bitmap = memoryCache.get(url);
		if (bitmap != null)
			imageView.setImageBitmap(bitmap);
		else {
			// ��û�еĻ��������̼߳���ͼƬ
			queuePhoto(url, imageView);
			//imageView.setImageResource(stub_id);
		}
	}

	private void queuePhoto(String url, ImageView imageView) {
		PhotoToLoad p = new PhotoToLoad(url, imageView);
		executorService.submit(new PhotosLoader(p));
	}

	private Bitmap getBitmap(String url) {
		File f = fileCache.getFile(url);

		// �ȴ��ļ������в����Ƿ���
		Bitmap b = decodeFile(f);
		if (b != null)
			return b;

		// ����ָ����url������ͼƬ
		try {
			Bitmap bitmap = null;
			URL imageUrl = new URL(url);
			HttpURLConnection conn = (HttpURLConnection) imageUrl
					.openConnection();
			conn.setConnectTimeout(30000);
			conn.setReadTimeout(30000);
			conn.setInstanceFollowRedirects(true);
			InputStream is = conn.getInputStream();
			OutputStream os = new FileOutputStream(f);
			CopyStream(is, os);
			os.close();
			bitmap = decodeFile(f);
			return bitmap;
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		}
	}

	// decode���ͼƬ���Ұ����������Լ����ڴ����ģ��������ÿ��ͼƬ�Ļ����СҲ�������Ƶ�
	private Bitmap decodeFile(File f) {
		try {
			// decode image size
			BitmapFactory.Options o = new BitmapFactory.Options();
			o.inJustDecodeBounds = true;
			BitmapFactory.decodeStream(new FileInputStream(f), null, o);

			// Find the correct scale value. It should be the power of 2.
			int width_tmp = o.outWidth, height_tmp = o.outHeight;
			int scale = 1;
			while (true) {
				if (width_tmp / 2 < REQUIRED_SIZE
						|| height_tmp / 2 < REQUIRED_SIZE)
					break;
				width_tmp /= 2;
				height_tmp /= 2;
				scale *= 2;
			}

			// decode with inSampleSize
			BitmapFactory.Options o2 = new BitmapFactory.Options();
			o2.inSampleSize = scale;
			return BitmapFactory.decodeStream(new FileInputStream(f), null, o2);
		} catch (FileNotFoundException e) {
		}
		return null;
	}

	// Task for the queue
	private class PhotoToLoad {
		public String url;
		public ImageView imageView;

		public PhotoToLoad(String u, ImageView i) {
			url = u;
			imageView = i;
		}
	}

	class PhotosLoader implements Runnable {
		PhotoToLoad photoToLoad;

		PhotosLoader(PhotoToLoad photoToLoad) {
			this.photoToLoad = photoToLoad;
		}

		@Override
		public void run() {
			if (imageViewReused(photoToLoad))
				return;
			Bitmap bmp = getBitmap(photoToLoad.url);
			memoryCache.put(photoToLoad.url, bmp);
			if (imageViewReused(photoToLoad))
				return;
			BitmapDisplayer bd = new BitmapDisplayer(bmp, photoToLoad);
			// ���µĲ�������UI�߳���
			Activity a = (Activity) photoToLoad.imageView.getContext();
			a.runOnUiThread(bd);
		}
	}

	/**
	 * ��ֹͼƬ��λ
	 * 
	 * @param photoToLoad
	 * @return
	 */
	boolean imageViewReused(PhotoToLoad photoToLoad) {
		String tag = imageViews.get(photoToLoad.imageView);
		if (tag == null || !tag.equals(photoToLoad.url))
			return true;
		return false;
	}

	// ������UI�߳��и��½���
	class BitmapDisplayer implements Runnable {
		Bitmap bitmap;
		PhotoToLoad photoToLoad;

		public BitmapDisplayer(Bitmap b, PhotoToLoad p) {
			bitmap = b;
			photoToLoad = p;
		}

		public void run() {
			if (imageViewReused(photoToLoad))
				return;
			if (bitmap != null)
				photoToLoad.imageView.setImageBitmap(bitmap);
			/*else
				photoToLoad.imageView.setImageResource(stub_id);*/
		}
	}

	public void clearCache() {
		memoryCache.clear();
		fileCache.clear();
	}

	public static void CopyStream(InputStream is, OutputStream os) {
		final int buffer_size = 1024;
		try {
			byte[] bytes = new byte[buffer_size];
			for (;;) {
				int count = is.read(bytes, 0, buffer_size);
				if (count == -1)
					break;
				os.write(bytes, 0, count);
			}
		} catch (Exception ex) {
		}
	}
}