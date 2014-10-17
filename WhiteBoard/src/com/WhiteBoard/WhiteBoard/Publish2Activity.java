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
			categoryDisplay.setText("商业信息");
			categoryLabel.setBackgroundResource(R.drawable.category_label1);
		} else if (category.equals(DefaultSettings.CATEGORY_ID_PERSONAL_STATUS)) {
			categoryDisplay.setText("个人状态");
			categoryLabel.setBackgroundResource(R.drawable.category_label2);
		} else if (category.equals(DefaultSettings.CATEGORY_ID_FRIEND_MAKING)) {
			categoryDisplay.setText("交友信息");
			categoryLabel.setBackgroundResource(R.drawable.category_label3);
		} else if (category.equals(DefaultSettings.CATEGORY_ID_MUTUAL_HELP)) {
			categoryDisplay.setText("互助信息");
			categoryLabel.setBackgroundResource(R.drawable.category_label4);
		}

		switch (privacyLevel) {
		case DefaultSettings.PRIVACY_PUBLIC:
			privacyDisplay.setText("公开");
			break;
		case DefaultSettings.PRIVACY_FOLLOWING:
			privacyDisplay.setText("只允许我关注的人查看");
			break;
		case DefaultSettings.PRIVACY_PRIVATE:
			privacyDisplay.setText("仅自己可以查看");
			break;
		}
	}

	class CategoryListener implements OnClickListener {

		@Override
		public void onClick(View v) {
			if (categoryBtn == null) {
				categoryBtn = new AlertDialog.Builder(Publish2Activity.this).setItems(
						new String[] { "商业信息", "个人状态", "交友信息", "互助信息" },
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
						new String[] { "公开", "只允许我关注的人查看", "仅自己可以查看" },
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

	// 选择远处贴纸条的按钮
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

	// 返回按钮的监听事件
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
			publish_label.setText("正在发布...");
			indicator.setVisibility(View.VISIBLE);
			
		}

		@Override
		protected String doInBackground(Integer... params) {
			// 创建新纸条
			DetailMessage dm = new DetailMessage();
			dm.setContent(content);
			dm.setPrivacyLevel(privacyLevel);
			dm.setLocationCreate(latitude, longitude);
			dm.setLocationExist(latitude, longitude);
			dm.setCategory(category);
			dm.setPictureFile(PublishActivity.picFiles);
			dm.setVoiceFile(PublishActivity.voiFile);
			// 申请发消息
			returnCode = CommunityFactory.createMessage(dm);
			
			return null;
		}

		@Override
		protected void onPostExecute(String result) {
			localPost.setClickable(true);
			remotePost.setClickable(true);
			publish_label.setText("本地发布");
			indicator.setVisibility(View.GONE);
			
			if (returnCode.equals("-1")) {
				// 失败了
				Toast.makeText(getApplicationContext(), "出错了，请重试",	Toast.LENGTH_SHORT).show();
			} else if(returnCode.equals("0")){
				// 成功了
				Toast.makeText(getApplicationContext(), "发送成功", Toast.LENGTH_SHORT).show();
				
				//清空静态数据
				PublishActivity.picFiles.clear();
				PublishActivity.voiFile = null;
				
				// 拿mId跳转
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
