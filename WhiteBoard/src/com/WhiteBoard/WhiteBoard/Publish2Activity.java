package com.WhiteBoard.WhiteBoard;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.WhiteBoard.BaseClasses.DefaultSettings;
import com.WhiteBoard.BaseClasses.DetailMessage;
import com.WhiteBoard.Send.CommunityFactory;

public class Publish2Activity extends Activity {

	private ImageButton localPost;
	private ImageButton remotePost;
	private ImageButton backBtn;
	private ImageButton chooseCategory;
	private ImageButton choosePrivacy;
	private TextView publish_label;
	private ProgressBar indicator;

	private TextView categoryDisplay;
	private TextView privacyDisplay;
	private ImageView categoryLabel;

	private int privacyLevel = DefaultSettings.DEFAULT_PRIVACY_LEVEL;
	private String category = DefaultSettings.DEFAULT_PUBLISH_CATEGORY;

	private String content;
	private double longitude;
	private double latitude;
	private Bundle bundle;

	private AlertDialog categoryBtn;
	private AlertDialog privacyBtn;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_publish2);

		Intent intent = getIntent();
		bundle = intent.getExtras();
		longitude = DefaultSettings.getLongitudeCurrent();
		latitude = DefaultSettings.getLatitudeCurrent();
		content = bundle.getString("content");
		
		publish_label = (TextView)findViewById(R.id.publish_label);
		indicator = (ProgressBar)findViewById(R.id.indicator);

		localPost = (ImageButton) findViewById(R.id.buttonLocallyPublish2);
		remotePost = (ImageButton) findViewById(R.id.buttonOtherPlacePublish2);
		backBtn = (ImageButton) findViewById(R.id.backPublish2);
		chooseCategory = (ImageButton) findViewById(R.id.buttonClassesPublish2);
		choosePrivacy = (ImageButton) findViewById(R.id.buttonLimitPublish2);

		categoryDisplay = (TextView) findViewById(R.id.publish2_label_category);
		privacyDisplay = (TextView) findViewById(R.id.publish2_label_privacy);
		categoryLabel = (ImageView) findViewById(R.id.publish2_category_image);

		localPost.setOnClickListener(new LocalListener());
		remotePost.setOnClickListener(new RemoteListener());
		backBtn.setOnClickListener(new BackListener());
		chooseCategory.setOnClickListener(new CategoryListener());
		choosePrivacy.setOnClickListener(new PrivacyListener());

		refreshOptionDisplay();

	}

	private void refreshOptionDisplay() {
		if (category.equals(DefaultSettings.CATEGORY_ID_COMMERCIAL)) {
			categoryDisplay.setText("��ҵ��Ϣ");
			categoryLabel.setBackgroundResource(R.drawable.category_label1);
		} else if (category.equals(DefaultSettings.CATEGORY_ID_PERSONAL_STATUS)) {
			categoryDisplay.setText("����״̬");
			categoryLabel.setBackgroundResource(R.drawable.category_label2);
		} else if (category.equals(DefaultSettings.CATEGORY_ID_FRIEND_MAKING)) {
			categoryDisplay.setText("������Ϣ");
			categoryLabel.setBackgroundResource(R.drawable.category_label3);
		} else if (category.equals(DefaultSettings.CATEGORY_ID_MUTUAL_HELP)) {
			categoryDisplay.setText("������Ϣ");
			categoryLabel.setBackgroundResource(R.drawable.category_label4);
		}

		switch (privacyLevel) {
		case DefaultSettings.PRIVACY_PUBLIC:
			privacyDisplay.setText("����");
			break;
		case DefaultSettings.PRIVACY_FOLLOWING:
			privacyDisplay.setText("ֻ�����ҹ�ע���˲鿴");
			break;
		case DefaultSettings.PRIVACY_PRIVATE:
			privacyDisplay.setText("���Լ����Բ鿴");
			break;
		}
	}

	class CategoryListener implements OnClickListener {

		@Override
		public void onClick(View v) {
			if (categoryBtn == null) {
				categoryBtn = new AlertDialog.Builder(Publish2Activity.this).setItems(
						new String[] { "��ҵ��Ϣ", "����״̬", "������Ϣ", "������Ϣ" },
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface arg0,	int which) {
								switch (which) {
								case 0:
									category = DefaultSettings.CATEGORY_ID_COMMERCIAL;
									break;
								case 1:
									category = DefaultSettings.CATEGORY_ID_PERSONAL_STATUS;
									break;
								case 2:
									category = DefaultSettings.CATEGORY_ID_FRIEND_MAKING;
									break;
								case 3:
									category = DefaultSettings.CATEGORY_ID_MUTUAL_HELP;
									break;
								}
								refreshOptionDisplay();
							}

						}).create();
			}
			if (!categoryBtn.isShowing()) {
				categoryBtn.show();
			}
		}

	}

	class PrivacyListener implements OnClickListener {

		@Override
		public void onClick(View v) {
			if (privacyBtn == null) {
				privacyBtn = new AlertDialog.Builder(Publish2Activity.this).setItems(
						new String[] { "����", "ֻ�����ҹ�ע���˲鿴", "���Լ����Բ鿴" },
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface arg0,	int which) {
								switch (which) {
								case 0:
									privacyLevel = DefaultSettings.PRIVACY_PUBLIC;
									break;
								case 1:
									privacyLevel = DefaultSettings.PRIVACY_FOLLOWING;
									break;
								case 2:
									privacyLevel = DefaultSettings.PRIVACY_PRIVATE;
									break;
								default:
								}
								refreshOptionDisplay();
							}

						}).create();
			}
			if (!privacyBtn.isShowing()) {
				privacyBtn.show();
			}
		}

	}

	class LocalListener implements OnClickListener {

		@Override
		public void onClick(View v) {
			new LocalPublish().execute();
		}

	}

	// ѡ��Զ����ֽ���İ�ť
	class RemoteListener implements OnClickListener {

		@Override
		public void onClick(View v) {
			Intent intent = new Intent();
			Bundle bundle = new Bundle();
			bundle.putInt("privacyLevel", privacyLevel);
			bundle.putString("category", category);
			bundle.putString("content", content);
			intent.setClass(Publish2Activity.this, PositionActivity.class);
			intent.putExtras(bundle);
			Publish2Activity.this.startActivity(intent);
			finish();
		}

	}

	// ���ذ�ť�ļ����¼�
	class BackListener implements OnClickListener {

		@Override
		public void onClick(View v) {
			Publish2Activity.this.finish();
		}

	}

	class LocalPublish extends AsyncTask<Integer, Integer, String> {
		
		String returnCode;
		
		@Override
		protected void onPreExecute() {
			localPost.setClickable(false);
			remotePost.setClickable(false);
			publish_label.setText("���ڷ���...");
			indicator.setVisibility(View.VISIBLE);
			
		}

		@Override
		protected String doInBackground(Integer... params) {
			// ������ֽ��
			DetailMessage dm = new DetailMessage();
			dm.setContent(content);
			dm.setPrivacyLevel(privacyLevel);
			dm.setLocationCreate(latitude, longitude);
			dm.setLocationExist(latitude, longitude);
			dm.setCategory(category);
			dm.setPictureFile(PublishActivity.picFiles);
			dm.setVoiceFile(PublishActivity.voiFile);
			// ���뷢��Ϣ
			returnCode = CommunityFactory.createMessage(dm);
			
			return null;
		}

		@Override
		protected void onPostExecute(String result) {
			localPost.setClickable(true);
			remotePost.setClickable(true);
			publish_label.setText("���ط���");
			indicator.setVisibility(View.GONE);
			
			if (returnCode.equals("-1")) {
				// ʧ����
				Toast.makeText(getApplicationContext(), "�����ˣ�������",	Toast.LENGTH_SHORT).show();
			} else if(returnCode.equals("0")){
				// �ɹ���
				Toast.makeText(getApplicationContext(), "���ͳɹ�", Toast.LENGTH_SHORT).show();
				
				//��վ�̬����
				PublishActivity.picFiles.clear();
				PublishActivity.voiFile = null;
				
				// ��mId��ת
				String mId = CommunityFactory.getCreatedMsgId();
				Intent intent = new Intent();
				Bundle bundle = new Bundle();
				bundle.putString("mId", mId);
				intent.putExtras(bundle);
				intent.setClass(Publish2Activity.this, DetailMessageActivity.class);
				Publish2Activity.this.startActivity(intent);
				PublishActivity.isPosted = true;
				finish();
				
			}
			super.onPostExecute(result);
		}

	}

}
