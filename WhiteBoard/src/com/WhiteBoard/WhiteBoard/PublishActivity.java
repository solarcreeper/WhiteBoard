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

	// ��������������������
	private TextView wordsCount;

	// ͼƬ��Ƶ��Ҫ�Ĳ���
	private static final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 110;
	private static final int CAPTURE_VIDEO_ACTIVITY_REQUEST_CODE = 120;
	private static final int CAPTURE_AUDIO_ACTIVITY_REQUEST_CODE = 130;
	private static final int GET_IMAGE_ACTIVITY_REQUEST_CODE = 210;
	private static final int GET_VIDEO_ACTIVITY_REQUEST_CODE = 220;
	private AlertDialog dialog;
	private Uri fileUri;

	// �жϵ�ַ�Ƿ���ʾ
	private boolean addrVisible;
	
	public static ArrayList<String> picFiles = new ArrayList<String>();
	
	public static String voiFile = null;
	private String voiName;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_publish);

		// һЩ�����ĳ�ʼ����
		addrVisible = true;

		// TitleBar��������ť
		advancePublish = (ImageButton) findViewById(R.id.advancePublish);
		backPublish = (ImageButton) findViewById(R.id.backPublish);

		// ����
		content = (EditText) findViewById(R.id.contentPublish);

		// �ϴ�ͼ�� ���� �����İ�ť
		uploadImage = (ImageButton) findViewById(R.id.buttonPicturePublish);
		uploadVoice = (ImageButton) findViewById(R.id.buttonSoundPublish);
		uploadFace = (ImageButton) findViewById(R.id.buttonFacePublish);

		// ��ʾ��ַ�İ�ť��TextView
		displayLocation = (ImageButton) findViewById(R.id.currentAddressPublish);
		address = (TextView) findViewById(R.id.addressMidPublish);

		// ��ʾ����
		wordsCount = (TextView) findViewById(R.id.wordNumberPublish);

		// ��ʾ��ַ��
		address.setText("��ǰ��ַ��" + DefaultSettings.getAddressCurrent());

		// ���ü�����
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

	//����������
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

	// ����
	class BackListener implements OnClickListener {

		@Override
		public void onClick(View v) {
			voiFile = null;
			picFiles.clear();
			PublishActivity.this.finish();
		}

	}

	// �����Ҫ�ϴ�ͼ��
	class ImageListener implements OnClickListener {

		@Override
		public void onClick(View v) {
			if (dialog == null) {
				dialog = new AlertDialog.Builder(PublishActivity.this).setItems(new String[] { "���", "�����ѡ��" },
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

	// ����ʵ��¼���Ĺ���(�ѷ���)
	class VoiceListener implements OnClickListener {

		@Override
		public void onClick(View v) {
			Intent intent = new Intent(Media.RECORD_SOUND_ACTION);
			fileUri = MediaFile.getOutputMediaFileUri(MediaFile.MEDIA_TYPE_AUDIO);
			startActivityForResult(intent, CAPTURE_AUDIO_ACTIVITY_REQUEST_CODE);
		}

	}

	// ������ʵ�ֱ�����ϴ�(�ѷ���)
	class FaceListener implements OnClickListener {

		@Override
		public void onClick(View v) {

		}

	}

	// �Ƿ���ʾ��ַ
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

	// ��һ������
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
	 * ���������Ѿ����
	 * 
	 * ͼ������ImageDecoder�� �ļ�������MediaFile��
	 * 
	 * ���� ���ֿ�߱� �Զ���ͼ��ѹ��Ϊ�����������MAX_VALUE_OF_DIMENSIONS����ֵ��ͼ����ֵ����ImageDecoder���޸�
	 * �����Զ�����ͼ��EXIF��Ϣ�е�ORIETATION���ԣ���֤ͼ������ȷ
	 * 
	 * ���ڹ���ͼ��洢·�������ҳ����ļ��в��洢ͼ�� ��Ϊ�����ʹ�õ����洢һ�ݣ������ԭͼ�����޸� �洢���ļ�����·��ΪfilePath
	 * 
	 * �����棡 ����Զֻʹ�������ļ�filePath����ý�����û��һ��ʹ�� ��Ҫ�����޸ı������ڵĴ��� �ڱ�����֮����Ӷ������
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
				
				//�����path��filename��
				int index = filePath.lastIndexOf("/");
				String fileName = filePath.substring(index + 1);
				picFiles.add(filePath);
				
				//��content��������index
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
				Toast.makeText(this, "�����ˣ�������", Toast.LENGTH_SHORT).show();
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
				Toast.makeText(this, "�����ˣ�������", Toast.LENGTH_SHORT).show();
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
