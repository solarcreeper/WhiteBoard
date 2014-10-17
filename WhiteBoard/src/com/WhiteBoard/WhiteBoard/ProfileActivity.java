package com.WhiteBoard.WhiteBoard;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.WhiteBoard.BaseClasses.DefaultSettings;
import com.WhiteBoard.Media.ImageDecoder;
import com.WhiteBoard.Media.ImageLoader;
import com.WhiteBoard.Media.MediaFile;
import com.WhiteBoard.Send.CommunityFactory;

public class ProfileActivity extends Activity {
	private ImageButton buttonmapProfile;
	private ImageButton buttonfollowProfile;
	private ImageButton buttonsetProfile;
	private ImageButton backProfile;
	
	private ImageButton followButton;
	private ImageView avatar, background;
	private ImageButton listFavorite, listPublished, listCommented;
	
	private ProgressBar indicator;

	private ImageLoader imageLoader; 
	
	private static final int CAPTURE_IMAGE_FOR_AVATAR_ACTIVITY_REQUEST_CODE = 111;
	private static final int CAPTURE_IMAGE_FOR_BACKGROUND_ACTIVITY_REQUEST_CODE = 112;
	private static final int GET_IMAGE_FOR_AVATAR_ACTIVITY_REQUEST_CODE = 211;
	private static final int GET_IMAGE_FOR_BACKGROUND_ACTIVITY_REQUEST_CODE = 212;
	private Uri fileUri;

	private AlertDialog dialog;
	
	private static boolean preInitialized = false;
	private static String targetUID;
	private static String targetUserName;
	private boolean followable, followed;
	
	private static final String TAG = "ProfileActivity";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		if (targetUID.equals("0")) {
			Log.d(TAG, "[Shouldn't be here] onCreate : Not logged in");
			Intent intent = new Intent();
			intent.setClass(ProfileActivity.this, LogInActivity.class);
			startActivity(intent);
			finish();
		}
		
		setContentView(R.layout.activity_profile);
		
		backProfile = (ImageButton)findViewById(R.id.backProfile);
		buttonmapProfile = (ImageButton)findViewById(R.id.buttonmapProfile);
		buttonfollowProfile = (ImageButton)findViewById(R.id.buttonfollowProfile);
		buttonsetProfile = (ImageButton)findViewById(R.id.buttonsetProfile);
		
		followButton = (ImageButton)findViewById(R.id.followButton);
		
		listFavorite = (ImageButton)findViewById(R.id.button_favorite);
		listPublished = (ImageButton)findViewById(R.id.button_published);
		listCommented = (ImageButton)findViewById(R.id.button_commented);
		
		avatar = (ImageView)findViewById(R.id.usertitleProfile);
		background = (ImageView)findViewById(R.id.mybackgroundProfile);
		indicator = (ProgressBar)findViewById(R.id.indicator);
		
		backProfile.setOnClickListener(new TitleBarListener());
		buttonmapProfile.setOnClickListener(new NavigationListener());
		buttonfollowProfile.setOnClickListener(new NavigationListener());
		buttonsetProfile.setOnClickListener(new NavigationListener());
		
		followButton.setOnClickListener(new FollowListener());
		
		listFavorite.setOnClickListener(new ListListener());
		listPublished.setOnClickListener(new ListListener());
		listCommented.setOnClickListener(new ListListener());
		
		avatar.setOnClickListener(new ChangePhotoListener());
		background.setOnClickListener(new ChangePhotoListener());
		
		imageLoader = new ImageLoader(getApplicationContext());
		imageLoader.clearCache();
		
		
	}
	
	
	@Override
	protected void onResume() {
		super.onResume();
		if (preInitialized)
			initialize();
		Log.d(TAG, "onResume");
	}


	public static void preInitialize(String targetUID, String targetUserName) {
		Log.d(TAG, "preInit Called");
		ProfileActivity.targetUID = targetUID;
		Log.d(TAG, targetUID);
		ProfileActivity.targetUserName = targetUserName;
		ProfileActivity.preInitialized = true;
	}
	
	private void initialize() {
		Log.d(TAG, DefaultSettings.getLocalUid());
		if (targetUID.equals(DefaultSettings.getLocalUid())) {
			Log.d(TAG, "init : My Profile");
			followButton.setVisibility(View.GONE);
			backProfile.setVisibility(View.GONE);
			findViewById(R.id.navigation).setVisibility(View.VISIBLE);
			((TextView)findViewById(R.id.label_favorite)).setText(R.string.profile_my_favorite);
			((TextView)findViewById(R.id.label_published)).setText(R.string.profile_my_published);
			((TextView)findViewById(R.id.label_commented)).setText(R.string.profile_my_commented);
			avatar.setClickable(true);
			background.setClickable(true);
		} else {
			Log.d(TAG, "init : Others's Profile");
			followButton.setVisibility(View.VISIBLE);
			backProfile.setVisibility(View.VISIBLE);
			findViewById(R.id.navigation).setVisibility(View.GONE);
			((TextView)findViewById(R.id.label_favorite)).setText(R.string.profile_ta_favorite);
			((TextView)findViewById(R.id.label_published)).setText(R.string.profile_ta_published);
			((TextView)findViewById(R.id.label_commented)).setText(R.string.profile_ta_commented);
			avatar.setClickable(false);
			background.setClickable(false);
			followButton.setClickable(false);
			Log.d(TAG, "init : Pull follow status");
			new GetFollowStatus().execute();
			
		}
		preInitialized = false;
		((TextView)findViewById(R.id.nameProfile)).setText(targetUserName);
		Log.d(TAG, "init : Pull avatar & background");
		String urlAvatar = new String(DefaultSettings.REMOTE_STORE_URL_PREFIX_AVATAT + targetUID + ".jpg");
		imageLoader.DisplayImage(urlAvatar, (ImageView)avatar, ImageLoader.REQUIRE_SIZE_AVATAR);  
		String urlBackground = new String(DefaultSettings.REMOTE_STORE_URL_PREFIX_BACKGROUND + targetUID + ".jpg");
		imageLoader.DisplayImage(urlBackground, (ImageView)background, ImageLoader.REQUIRE_SIZE_BACKGROUND);  
	}
	
	private class TitleBarListener implements OnClickListener {

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.backProfile:
				finish();
				break;
			}
		}
	}
	
	private class NavigationListener implements OnClickListener {

		@Override
		public void onClick(View v) {
			Intent  intent = new Intent();
			switch (v.getId()) {
			case R.id.buttonmapProfile:
				intent.setClass(ProfileActivity.this,MyMapActivity.class);
				break;
			case R.id.buttonfollowProfile:
				intent.setClass(ProfileActivity.this, FollowActivity.class);
				break;
			case R.id.buttonsetProfile:
				intent.setClass(ProfileActivity.this, SetActivity.class);
				break;
			}
			ProfileActivity.this.startActivity(intent);
			finish();		
			
		}
	}

	private class FollowListener implements OnClickListener {

		@Override
		public void onClick(View v) {
			Log.d(TAG, "Follow Button Called");
			if (!DefaultSettings.isLoggedIn()) {
				Toast.makeText(getApplicationContext(), "你还没有登陆哟", Toast.LENGTH_SHORT).show();
			} else if (!followable && !followed) {
				Toast.makeText(getApplicationContext(), "对方很低调，不想被关注", Toast.LENGTH_SHORT).show();
			} else if (followed) {
				followButton.setClickable(false);
				new SetFollow().execute("0");
			} else {
				followButton.setClickable(false);
				new SetFollow().execute("1");
			}
			
		}
	}
	
	private class ListListener implements OnClickListener {

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.button_favorite:
				Log.d(TAG, "Favorite List Called");
				RMListActivity.setFavoriteList(targetUID);
				break;
			case R.id.button_published:
				Log.d(TAG, "Published List Called");
				RMListActivity.setPublishedList(targetUID);
				break;
			case R.id.button_commented:
				Log.d(TAG, "Commented List Called");
				RMListActivity.setCommentedList(targetUID);
				break;
			}
			Log.d(TAG, "List preInitialized, Intent ->");
			Intent intent = new Intent();
			intent.setClass(ProfileActivity.this, RMListActivity.class);
			ProfileActivity.this.startActivity(intent);
		}
	}
	
	private class ChangePhotoListener implements OnClickListener {

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.usertitleProfile:
				Log.d(TAG, "Change avatar called");
				
				dialog = new AlertDialog.Builder(ProfileActivity.this).setItems(new String[] { "相机", "从相册选择" }, new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						if (which == 0) {
							// create Intent to take a picture and return control to the calling application
						    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
						    // create a file to save the image
						    fileUri = MediaFile.getOutputMediaFileUri(MediaFile.MEDIA_TYPE_PROFILE); 
						    // set the image file name
						    //intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri); 
						    // start the image capture Intent
						    startActivityForResult(intent, CAPTURE_IMAGE_FOR_AVATAR_ACTIVITY_REQUEST_CODE);
						} else {
							Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
							fileUri = MediaFile.getOutputMediaFileUri(MediaFile.MEDIA_TYPE_PROFILE); 
							intent.setType("image/*");
							startActivityForResult(intent, GET_IMAGE_FOR_AVATAR_ACTIVITY_REQUEST_CODE);

						}
					}
				}).setTitle("修改头像").create();
				if (!dialog.isShowing()) {
					dialog.show();
				}
				
				break;
			case R.id.mybackgroundProfile:
				Log.d(TAG, "Change background called");
				
				dialog = new AlertDialog.Builder(ProfileActivity.this).setItems(new String[] { "相机", "从相册选择" }, new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						if (which == 0) {
							// create Intent to take a picture and return control to the calling application
						    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
						    // create a file to save the image
							fileUri = MediaFile.getOutputMediaFileUri(MediaFile.MEDIA_TYPE_PROFILE); 
							// set the image file name
							//intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri); 
							// start the image capture Intent
							startActivityForResult(intent, CAPTURE_IMAGE_FOR_BACKGROUND_ACTIVITY_REQUEST_CODE);
						} else {
							Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
							fileUri = MediaFile.getOutputMediaFileUri(MediaFile.MEDIA_TYPE_PROFILE); 
							intent.setType("image/*");
							startActivityForResult(intent, GET_IMAGE_FOR_BACKGROUND_ACTIVITY_REQUEST_CODE);
						}
					}
				}).setTitle("修改背景").create();
				if (!dialog.isShowing()) {
					dialog.show();
				}
				
				break;
			}
			
		}
		
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		
		String filePath;
		
		if (requestCode == CAPTURE_IMAGE_FOR_AVATAR_ACTIVITY_REQUEST_CODE || requestCode == GET_IMAGE_FOR_AVATAR_ACTIVITY_REQUEST_CODE || requestCode == CAPTURE_IMAGE_FOR_BACKGROUND_ACTIVITY_REQUEST_CODE || requestCode == GET_IMAGE_FOR_BACKGROUND_ACTIVITY_REQUEST_CODE) {
		       if (resultCode == RESULT_OK) {
		        	/********************************************
		             *   RESERVED CODE AREA. MODIFY WITH CARE   *
		             ********************************************/
		        	Uri originalUri = data.getData();   
	                Log.d(TAG, "Image original path : " + originalUri);
		            
		            ImageDecoder imageDecoder = new ImageDecoder(this);
		            if (requestCode == CAPTURE_IMAGE_FOR_AVATAR_ACTIVITY_REQUEST_CODE || requestCode == GET_IMAGE_FOR_AVATAR_ACTIVITY_REQUEST_CODE) {
		            	imageDecoder.compressBitmap(originalUri, fileUri, ImageDecoder.DIMENSIONS_AVATAR);
		            } else {
		            	imageDecoder.compressBitmap(originalUri, fileUri);
					}
	                filePath = fileUri.getPath();
		            Log.d(TAG, "Image saved to " + filePath);
		            
		            /********************************************/
		            
		            new UploadProfileImage().execute("" + requestCode, filePath);
	                
		        } else if (resultCode == RESULT_CANCELED) {
		            // User cancelled the image capture
		        } else {
		            // Image capture failed, advise user
		        	Toast.makeText(this, "出错了，请重试", Toast.LENGTH_SHORT).show();
		        }
		    }
		 
		/*if (requestCode == CAPTURE_IMAGE_FOR_AVATAR_ACTIVITY_REQUEST_CODE || requestCode == GET_IMAGE_FOR_AVATAR_ACTIVITY_REQUEST_CODE) {
			//TODO 更新AVATAR
			
		} else if (requestCode == CAPTURE_IMAGE_FOR_BACKGROUND_ACTIVITY_REQUEST_CODE || requestCode == GET_IMAGE_FOR_BACKGROUND_ACTIVITY_REQUEST_CODE) {
			//TODO 更新BACKGROUND
		}*/
		 
	}
	
	private class UploadProfileImage extends AsyncTask<String, Integer, Integer> {
		
		boolean avatarStatus;
		boolean backgroundStatus;
		
		@Override
		protected void onPreExecute() {
			indicator.setVisibility(View.VISIBLE);
			avatar.setClickable(false);
			background.setClickable(false);
			Toast.makeText(getApplicationContext(), "正在上传中...", Toast.LENGTH_LONG).show();
		}

		@Override
		protected Integer doInBackground(String... params) {
			String request = params[0];
			String filepath = params[1]; 
			if (request.equals("" + CAPTURE_IMAGE_FOR_AVATAR_ACTIVITY_REQUEST_CODE) || request.equals("" + GET_IMAGE_FOR_AVATAR_ACTIVITY_REQUEST_CODE)) {
				Log.d(TAG, "AT:Uploading avatar");
				String strAvatarStatus = CommunityFactory.sendAvatar(filepath);
				if(strAvatarStatus.equals("0")){
					backgroundStatus = true;
				}else{
					backgroundStatus = false;
				}
				return 1;
			} else if (request.equals("" + CAPTURE_IMAGE_FOR_BACKGROUND_ACTIVITY_REQUEST_CODE) || request.equals("" + GET_IMAGE_FOR_BACKGROUND_ACTIVITY_REQUEST_CODE)) {
				Log.d(TAG, "AT:Uploading background");
				String strBackgroundStatus = CommunityFactory.sendBackgroundFile(filepath);
				if(strBackgroundStatus.equals("0")){
					backgroundStatus = true;
				}else{
					backgroundStatus = false;
				}
				return 2;
			}
			return 0;
		}
		
		@Override
		protected void onProgressUpdate(Integer... values) {
			super.onProgressUpdate(values);
		}
		
		@Override
		protected void onPostExecute(Integer result) {
			indicator.setVisibility(View.GONE);
			avatar.setClickable(true);
			background.setClickable(true);
			if (avatarStatus || backgroundStatus) {
				avatarStatus = false;
				backgroundStatus = false;
				Toast.makeText(getApplicationContext(), "修改成功，请静候刷新哦", Toast.LENGTH_SHORT).show();
				imageLoader.clearCache();
				if (result == 1) {
					String urlAvatar = new String(DefaultSettings.REMOTE_STORE_URL_PREFIX_AVATAT + targetUID + ".jpg");
					imageLoader.DisplayImage(urlAvatar, (ImageView)avatar, ImageLoader.REQUIRE_SIZE_AVATAR);  
				} else if (result == 2) {
					String urlBackground = new String(DefaultSettings.REMOTE_STORE_URL_PREFIX_BACKGROUND + targetUID + ".jpg");
					imageLoader.DisplayImage(urlBackground, (ImageView)background, ImageLoader.REQUIRE_SIZE_BACKGROUND);  
				}
				Log.d(TAG, "Upload fine");
			} else {
				Toast.makeText(getApplicationContext(), "出错了，请重试", Toast.LENGTH_SHORT).show();
				Log.d(TAG, "Upload err");
			}
		}
		
	}
	
	private class GetFollowStatus extends AsyncTask<String, Integer, Integer> {
		
		String returnCode;
		
		@Override
		protected Integer doInBackground(String... params) {
			returnCode = CommunityFactory.getIsUserFollowed(targetUID, DefaultSettings.getLocalUid());
			return 0;
		}
		
		@Override
		protected void onProgressUpdate(Integer... values) {
			super.onProgressUpdate(values);
		}
		
		@Override
		protected void onPostExecute(Integer result) {
			if (CommunityFactory.getTargetUserFollowable().equals("TRUE")) {
				followable = true;
			} else {
				followable = false;
			}
			Log.d(TAG, "UID " + targetUID + ", followable: " +CommunityFactory.getTargetUserFollowable());
			if (returnCode.equals("TRUE")) {
				followed = true;
				followButton.setBackgroundResource(R.drawable.icon_button_follow_on);
			} else {
				followed = false;
				followButton.setBackgroundResource(R.drawable.icon_button_follow_off);
			}
			Log.d(TAG, "UID " + targetUID + ", followed: " + returnCode);
			followButton.setClickable(true);
		}
		
	}
	
	private class SetFollow extends AsyncTask<String, Integer, Integer> {

		String returnCode;
		
		@Override
		protected Integer doInBackground(String... params) {
			returnCode = CommunityFactory.sendFollowStatus(DefaultSettings.getLocalUid(), targetUID, params[0]);
			return 0;
		}
		
		@Override
		protected void onProgressUpdate(Integer... values) {
			super.onProgressUpdate(values);
		}
		
		@Override
		protected void onPostExecute(Integer result) {
			if (returnCode.equals("0")) {
				Log.d(TAG, "following status changed");
				new GetFollowStatus().execute();
			} else {
				Toast.makeText(getApplicationContext(), "出错了，请重试", Toast.LENGTH_SHORT);
				Log.d(TAG, "error occured when changing following status");
			}
		}
		
	}
	
}
