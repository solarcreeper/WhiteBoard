package com.WhiteBoard.WhiteBoard;

import java.util.ArrayList;
import java.io.FileNotFoundException;
import java.io.InputStream;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.MediaStore.Audio.Media;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.WhiteBoard.BaseClasses.DefaultSettings;
import com.WhiteBoard.Media.AudioSolver;
import com.WhiteBoard.Media.ImageDecoder;
import com.WhiteBoard.Media.MediaFile;

public class PublishActivity extends Activity {
	
	public static boolean isPosted = false;

	private final String TAG = "PublishActivity";

	private ImageButton advancePublish;
	private ImageButton backPublish;
	private ImageButton uploadImage;
	private ImageButton uploadVoice;
	private ImageButton uploadFace;
	private ImageButton displayLocation;
	private EditText content;
	private TextView address;

	// 这个是下面的字数计数。
	private TextView wordsCount;

	// 图片音频需要的参数
	private static final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 110;
	private static final int CAPTURE_VIDEO_ACTIVITY_REQUEST_CODE = 120;
	private static final int CAPTURE_AUDIO_ACTIVITY_REQUEST_CODE = 130;
	private static final int GET_IMAGE_ACTIVITY_REQUEST_CODE = 210;
	private static final int GET_VIDEO_ACTIVITY_REQUEST_CODE = 220;
	private AlertDialog dialog;
	private Uri fileUri;

	// 判断地址是否显示
	private boolean addrVisible;
	
	public static ArrayList<String> picFiles = new ArrayList<String>();
	
	public static String voiFile = null;
	private String voiName;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_publish);

		// 一些常量的初始化。
		addrVisible = true;

		// TitleBar的两个按钮
		advancePublish = (ImageButton) findViewById(R.id.advancePublish);
		backPublish = (ImageButton) findViewById(R.id.backPublish);

		// 正文
		content = (EditText) findViewById(R.id.contentPublish);

		// 上传图像， 表情 声音的按钮
		uploadImage = (ImageButton) findViewById(R.id.buttonPicturePublish);
		uploadVoice = (ImageButton) findViewById(R.id.buttonSoundPublish);
		uploadFace = (ImageButton) findViewById(R.id.buttonFacePublish);

		// 显示地址的按钮和TextView
		displayLocation = (ImageButton) findViewById(R.id.currentAddressPublish);
		address = (TextView) findViewById(R.id.addressMidPublish);

		// 显示字数
		wordsCount = (TextView) findViewById(R.id.wordNumberPublish);

		// 显示地址。
		address.setText("当前地址：" + DefaultSettings.getAddressCurrent());

		// 设置监听器
		advancePublish.setOnClickListener(new AdvanceListener());
		backPublish.setOnClickListener(new BackListener());
		uploadImage.setOnClickListener(new ImageListener());
		uploadVoice.setOnClickListener(new VoiceListener());
		uploadFace.setOnClickListener(new FaceListener());
		displayLocation.setOnClickListener(new DisplayListener());
				
		content.addTextChangedListener(new WordsCounter());

	}
	
	@Override
	protected void onResume() {
		if(isPosted == true){
			finish();
		}
		super.onResume();
	}

	//计算字数的
	class WordsCounter implements TextWatcher {
		
		@Override
		public void beforeTextChanged(CharSequence s, int start, int count, int after) {
		}

		@Override
		public void onTextChanged(CharSequence s, int start, int before, int count) {
			String strContent = content.getText().toString();
			
			int length = strContent.length();
			
			if(length <= 280){
				wordsCount.setText(length + "/280");
			}
			else
			{
				strContent = strContent.substring(0, 279);
				
				content.setText(strContent);
				content.setSelection(279);
				
				wordsCount.setText("280/280");
			}
		}

		@Override
		public void afterTextChanged(Editable s) {
			
		}

	}

	// 返回
	class BackListener implements OnClickListener {

		@Override
		public void onClick(View v) {
			voiFile = null;
			picFiles.clear();
			PublishActivity.this.finish();
		}

	}

	// 这个需要上传图像
	class ImageListener implements OnClickListener {

		@Override
		public void onClick(View v) {
			if (dialog == null) {
				dialog = new AlertDialog.Builder(PublishActivity.this).setItems(new String[] { "相机", "从相册选择" },
					new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							if (which == 0) {
								// create Intent to take a picture
								// and return control to the calling
								// application
								Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
								// create a file to save the image
								fileUri = MediaFile.getOutputMediaFileUri(MediaFile.MEDIA_TYPE_IMAGE);
								// set the image file name
								// intent.putExtra(MediaStore.EXTRA_OUTPUT,
								// fileUri);
								// start the image capture Intent
								startActivityForResult(intent,CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);
							} else {
								Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
								fileUri = MediaFile.getOutputMediaFileUri(MediaFile.MEDIA_TYPE_IMAGE);
								intent.setType("image/*");
								startActivityForResult(intent,GET_IMAGE_ACTIVITY_REQUEST_CODE);
							}
						}
					}).create();
			}
			if (!dialog.isShowing()) {
				dialog.show();
			}
		}

	}

	// 这里实现录音的功能(已放弃)
	class VoiceListener implements OnClickListener {

		@Override
		public void onClick(View v) {
			Intent intent = new Intent(Media.RECORD_SOUND_ACTION);
			fileUri = MediaFile.getOutputMediaFileUri(MediaFile.MEDIA_TYPE_AUDIO);
			startActivityForResult(intent, CAPTURE_AUDIO_ACTIVITY_REQUEST_CODE);
		}

	}

	// 这里是实现表情的上传(已放弃)
	class FaceListener implements OnClickListener {

		@Override
		public void onClick(View v) {

		}

	}

	// 是否显示地址
	class DisplayListener implements OnClickListener {

		@Override
		public void onClick(View v) {
			if (addrVisible) {
				addrVisible = false;
				findViewById(R.id.linearLayout1Publish).setVisibility(View.GONE);
			} else {
				addrVisible = true;
				findViewById(R.id.linearLayout1Publish).setVisibility(View.VISIBLE);
			}
		}

	}

	// 进一步发表
	class AdvanceListener implements OnClickListener {
		@Override
		public void onClick(View v) {
			String c = content.getText().toString();
			if(voiName != null) {
				c = c + "\n<audio " + voiName + "/>";
			}
			content.setText(c);
			content.setSelection(content.length());
			String str = content.getText().toString();
			Bundle bundle = new Bundle();
			bundle.putString("content", str);
			Intent intent = new Intent();
			intent.putExtras(bundle);
			intent.setClass(PublishActivity.this, Publish2Activity.class);
			PublishActivity.this.startActivity(intent);
		}
	}

	/*
	 * 相机与相册已经完成
	 * 
	 * 图像处理在ImageDecoder类 文件创建在MediaFile类
	 * 
	 * 将会 保持宽高比 自动将图像压缩为长宽均不超过MAX_VALUE_OF_DIMENSIONS像素值的图像，其值可在ImageDecoder中修改
	 * 将会自动处理图像EXIF信息中的ORIETATION属性，保证图像方向正确
	 * 
	 * 会在公共图像存储路径创建我程序文件夹并存储图像 会为程序的使用单独存储一份，不会对原图进行修改 存储的文件绝对路径为filePath
	 * 
	 * ！警告！ 请永远只使用最终文件filePath进行媒体引用或进一步使用 不要擅自修改保留区内的代码 在保留区之外添加额外代码
	 */
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE || requestCode == GET_IMAGE_ACTIVITY_REQUEST_CODE) {
			if (resultCode == RESULT_OK) {
				/********************************************
				 * RESERVED CODE AREA. MODIFY WITH CARE *
				 ********************************************/
				Uri originalUri = data.getData();
				Log.d(TAG, "Image original path : " + originalUri);

				ImageDecoder imageDecoder = new ImageDecoder(this);
				imageDecoder.compressBitmap(originalUri, fileUri);
				String filePath = fileUri.getPath();
				Log.d(TAG, "Image saved to " + filePath);

				/********************************************/
				
				//添加了path和filename；
				int index = filePath.lastIndexOf("/");
				String fileName = filePath.substring(index + 1);
				picFiles.add(filePath);
				
				//在content中增加了index
				String c = content.getText().toString();
				if (c.length() != 0 && c.lastIndexOf("\n") != c.length()-1) {
					c += "\n";
				}
				c = c + "<image " + fileName + "/>\n";
				content.setText(c);
				content.setSelection(content.length());
				

			} else if (resultCode == RESULT_CANCELED) {
				// User cancelled the image capture
			} else {
				// Image capture failed, advise user
				Toast.makeText(this, "出错了，请重试", Toast.LENGTH_SHORT).show();
			}
		}
		/*if (requestCode == GET_IMAGE_ACTIVITY_REQUEST_CODE) {
			if (resultCode == RESULT_OK) {
				*//********************************************
				 * RESERVED CODE AREA. MODIFY WITH CARE *
				 ********************************************//*
				Uri originalUri = data.getData();
				Log.d(TAG, "Image original path : " + originalUri);

				ImageDecoder imageDecoder = new ImageDecoder(this);
				imageDecoder.compressBitmap(originalUri, fileUri);
				String filePath = fileUri.getPath();
				Log.d(TAG, "Image saved to " + filePath);

				*//********************************************//*

				testView.setImageBitmap(BitmapFactory.decodeFile(fileUri
						.getPath()));

			} else if (resultCode == RESULT_CANCELED) {
				// User cancelled the image browsing
			} else {
				// Image browsing failed, advise user
				Toast.makeText(this, "出错了，请重试", Toast.LENGTH_SHORT).show();
			}
		}*/
		if (requestCode == CAPTURE_AUDIO_ACTIVITY_REQUEST_CODE) {
			if (resultCode == RESULT_OK) {
				/********************************************
				 * RESERVED CODE AREA. MODIFY WITH CARE *
				 ********************************************/
				Uri originalUri = data.getData();
				String dataFile=data.getDataString() ;
				String filePath = fileUri.getPath();
				Log.d(TAG, "AUDIO "+dataFile);
				try {
					InputStream in = getContentResolver().openInputStream(originalUri);
					AudioSolver.copyfile(in, filePath);
					Log.d(TAG, "Audio: Saved to " + filePath);
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				}
				/********************************************/
				
				int index = filePath.lastIndexOf("/");
				voiName = filePath.substring(index + 1);
				voiFile = filePath;
			}
		}
	}
}
