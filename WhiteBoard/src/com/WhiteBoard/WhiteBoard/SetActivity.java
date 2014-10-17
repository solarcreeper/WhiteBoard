package com.WhiteBoard.WhiteBoard;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.Toast;

import com.WhiteBoard.BaseClasses.DefaultSettings;
import com.WhiteBoard.Send.CommunityFactory;

public class SetActivity extends Activity {
	
	private final String TAG = "SetActivity";
	
	private boolean listFollowableExpand = false;
	private boolean listCategoryExpand = false;

	private ImageButton  buttonmapSet = null;
	private ImageButton buttonfollowSet = null;
	private ImageButton buttonprofileSet = null;
	
	private ImageButton buttonChangePwd;
	private ImageButton buttonChangeFollowable;
	private ImageButton buttonChangeDefaultCategory;
	private ImageButton buttonShowNew;
	private ImageButton buttonHelp;
	private ImageButton buttonAbout;
	
	private ImageButton buttonListAllow;
	private ImageButton buttonListDisallow;
	
	private ImageButton buttonListCategory1;
	private ImageButton buttonListCategory2;
	private ImageButton buttonListCategory3;
	private ImageButton buttonListCategory4;
	
	private ImageButton buttonLogin;
	private ImageButton buttonLogout;
	
	private boolean settingsModified = false;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_set);
		
		buttonmapSet = (ImageButton)findViewById(R.id.buttonmapSet);
		buttonfollowSet = (ImageButton)findViewById(R.id.buttonfollowSet);
		buttonprofileSet = (ImageButton)findViewById(R.id.buttonprofileSet);
		
		buttonChangePwd = (ImageButton)findViewById(R.id.buttonChangePwd);
		buttonChangeFollowable = (ImageButton)findViewById(R.id.buttonChangeFollowable);
		buttonChangeDefaultCategory = (ImageButton)findViewById(R.id.buttonChangeDefaultCategory);
		buttonShowNew = (ImageButton)findViewById(R.id.buttonShowNew);
		buttonHelp = (ImageButton)findViewById(R.id.buttonHelp);
		buttonAbout = (ImageButton)findViewById(R.id.buttonAbout);
		
		buttonListAllow = (ImageButton)findViewById(R.id.buttonAllow);
		buttonListDisallow = (ImageButton)findViewById(R.id.buttonDisallow);
		
		buttonListCategory1 = (ImageButton)findViewById(R.id.buttonListCategory1);
		buttonListCategory2 = (ImageButton)findViewById(R.id.buttonListCategory2);
		buttonListCategory3 = (ImageButton)findViewById(R.id.buttonListCategory3);
		buttonListCategory4 = (ImageButton)findViewById(R.id.buttonListCategory4);
		
		buttonLogin = (ImageButton)findViewById(R.id.buttonLogin);
		buttonLogout = (ImageButton)findViewById(R.id.buttonLogout);
		
		buttonmapSet.setOnClickListener(new NavigationListener());
		buttonfollowSet.setOnClickListener(new NavigationListener());
		buttonprofileSet.setOnClickListener(new NavigationListener());
		
		buttonChangePwd.setOnClickListener(new ListButtonListener());
		buttonChangeFollowable.setOnClickListener(new ListButtonListener());
		buttonChangeDefaultCategory.setOnClickListener(new ListButtonListener());
		buttonShowNew.setOnClickListener(new ListButtonListener());
		buttonHelp.setOnClickListener(new ListButtonListener());
		buttonAbout.setOnClickListener(new ListButtonListener());
		
		buttonListAllow.setOnClickListener(new ListFollowableListener());
		buttonListDisallow.setOnClickListener(new ListFollowableListener());
		
		buttonListCategory1.setOnClickListener(new ListCategoryListener());
		buttonListCategory2.setOnClickListener(new ListCategoryListener());
		buttonListCategory3.setOnClickListener(new ListCategoryListener());
		buttonListCategory4.setOnClickListener(new ListCategoryListener());
		
		buttonLogin.setOnClickListener(new LogButtonListener());
		buttonLogout.setOnClickListener(new LogButtonListener());
		
		loadPreferenceContent();
	}
	
	
	@Override
	public void onPause() {
		super.onPause();
		DefaultSettings.savePrefs(getApplicationContext());
		if (settingsModified) {
			new ModifySettings().execute();
		}
	}
	
	
	@Override
	protected void onResume() {
		super.onResume();
		loadPreferenceContent();
	}
	
	
	private class NavigationListener implements OnClickListener {
		@Override
		public void onClick(View v) {
			Intent  intent = new Intent();
			switch (v.getId()) {
			case R.id.buttonmapSet:
				intent.setClass(SetActivity.this, MyMapActivity.class);	
				break;
			case R.id.buttonprofileSet:
				if (DefaultSettings.isLoggedIn()) {
					ProfileActivity.preInitialize(DefaultSettings.getLocalUid(), DefaultSettings.getLocalUserName());
					intent.setClass(SetActivity.this, ProfileActivity.class);
				} else {
					intent.setClass(SetActivity.this, LogInActivity.class);
				}
				break;
			case R.id.buttonfollowSet:
				if (DefaultSettings.isLoggedIn()) {
					intent.setClass(SetActivity.this, FollowActivity.class);
				} else {
					intent.setClass(SetActivity.this, LogInActivity.class);
				}
				break;
			}
			SetActivity.this.startActivity(intent);
			if (DefaultSettings.isLoggedIn()) {
				finish();
			}
		}
	}
	
	private class ListButtonListener implements OnClickListener {
		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.buttonChangePwd:
				Intent intent = new Intent();
				intent.setClass(SetActivity.this, ChangePasswordActivity.class);
				startActivity(intent);
				break;
			case R.id.buttonChangeFollowable:
				//change followable
				if (listFollowableExpand) {
					buttonChangeFollowable.setImageDrawable(getResources().getDrawable(R.drawable.style_list_button_normal));
					findViewById(R.id.preference_chancge_followable).setBackgroundResource(R.drawable.icon_navigation_menu);
					findViewById(R.id.layout_set_followable).setVisibility(View.GONE);
					listFollowableExpand = false;
				}
				else {
					buttonChangeFollowable.setImageDrawable(getResources().getDrawable(R.drawable.style_list_button_top));
					findViewById(R.id.layout_set_followable).setVisibility(View.VISIBLE);
					findViewById(R.id.preference_chancge_followable).setBackgroundResource(R.drawable.navigation_collapse);
					listFollowableExpand = true;
				}
				break;
			case R.id.buttonChangeDefaultCategory:
				//change default category
				if (listCategoryExpand) {
					buttonChangeDefaultCategory.setImageDrawable(getResources().getDrawable(R.drawable.style_list_button_normal));
					findViewById(R.id.preference_change_category).setBackgroundResource(R.drawable.icon_navigation_menu);
					findViewById(R.id.layout_set_category).setVisibility(View.GONE);
					listCategoryExpand = false;
				}
				else {
					buttonChangeDefaultCategory.setImageDrawable(getResources().getDrawable(R.drawable.style_list_button_top));
					findViewById(R.id.preference_change_category).setBackgroundResource(R.drawable.navigation_collapse);
					findViewById(R.id.layout_set_category).setVisibility(View.VISIBLE);
					listCategoryExpand = true;
				}
				break;
			case R.id.buttonShowNew:
				//show new
				//Toast.makeText(getApplicationContext(), "Show new", Toast.LENGTH_SHORT).show();
				Intent intentRegist = new Intent();
				intentRegist.setClass(SetActivity.this, RegisterActivity.class);
				startActivity(intentRegist);
				break;
			case R.id.buttonHelp:
				//help
				Intent intentHelp = new Intent();
				intentHelp.setClass(SetActivity.this, HelpActivity.class);
				startActivity(intentHelp);
				break;
			case R.id.buttonAbout:
				//about
				Intent intentAbout = new Intent();
				intentAbout.setClass(SetActivity.this, AboutActivity.class);
				startActivity(intentAbout);
				break;
			}
		}
	}
	
	private class ListFollowableListener implements OnClickListener {
		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.buttonAllow:
				DefaultSettings.setFollowable(true);
				break;
			case R.id.buttonDisallow:
				DefaultSettings.setFollowable(false);
				break;
			}
			settingsModified = true;
			refreshFollowableCheckStatus();
			Log.d(TAG, "FollowableListener : "+String.valueOf(DefaultSettings.isFollowable()));
		}
	}
	
	private class ListCategoryListener implements OnClickListener {
		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.buttonListCategory1:
				//Commercial
				DefaultSettings.setCategoryCommercialChecked(!DefaultSettings.isCategoryCommercialChecked());
				break;
			case R.id.buttonListCategory2:
				//Personal
				DefaultSettings.setCategoryPersonalChecked(!DefaultSettings.isCategoryPersonalChecked());
				break;
			case R.id.buttonListCategory3:
				//friend
				DefaultSettings.setCategoryFriendChecked(!DefaultSettings.isCategoryFriendChecked());
				break;
			case R.id.buttonListCategory4:
				//help
				DefaultSettings.setCategoryHelpChecked(!DefaultSettings.isCategoryHelpChecked());
				break;
			}
			DefaultSettings.setDefaultCategory(DefaultSettings.encodeCategoryState());
			settingsModified = true;
			refreshCategoryCheckStatus();
			Log.d(TAG, DefaultSettings.encodeCategoryState());
		}
	}
	
	private class LogButtonListener implements OnClickListener {
		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.buttonLogin:
				Intent intent = new Intent();
				intent.setClass(SetActivity.this, LogInActivity.class);
				startActivity(intent);
				break;
			case R.id.buttonLogout:
				DefaultSettings.setLoggedOut();
				break;
			}
			loadPreferenceContent();
			
		}
	}
	
	private void loadPreferenceContent() {
		if (DefaultSettings.isLoggedIn()) {
			findViewById(R.id.set_button_logout).setVisibility(View.VISIBLE);
			findViewById(R.id.set_button_login).setVisibility(View.GONE);
			findViewById(R.id.layoutLoggedInPreference).setVisibility(View.VISIBLE);
			refreshCategoryCheckStatus();
			refreshFollowableCheckStatus();
		}
		else {
			findViewById(R.id.set_button_logout).setVisibility(View.GONE);
			findViewById(R.id.set_button_login).setVisibility(View.VISIBLE);
			findViewById(R.id.layoutLoggedInPreference).setVisibility(View.GONE);
		}
	}
	
	private void refreshCategoryCheckStatus() {
		DefaultSettings.decodeCategoryState();
		findViewById(R.id.preference_category1_check)
		.setVisibility(DefaultSettings.isCategoryCommercialChecked() ? View.VISIBLE : View.INVISIBLE);
		findViewById(R.id.preference_category2_check)
		.setVisibility(DefaultSettings.isCategoryPersonalChecked() ? View.VISIBLE : View.INVISIBLE);
		findViewById(R.id.preference_category3_check)
		.setVisibility(DefaultSettings.isCategoryFriendChecked() ? View.VISIBLE : View.INVISIBLE);
		findViewById(R.id.preference_category4_check)
		.setVisibility(DefaultSettings.isCategoryHelpChecked() ? View.VISIBLE : View.INVISIBLE);
	}
	
	private void refreshFollowableCheckStatus() {
		if (DefaultSettings.isFollowable()) {
			findViewById(R.id.preference_allow_follow).setVisibility(View.VISIBLE);
			findViewById(R.id.preference_Disallow_follow).setVisibility(View.INVISIBLE);
		}
		else {
			findViewById(R.id.preference_allow_follow).setVisibility(View.INVISIBLE);
			findViewById(R.id.preference_Disallow_follow).setVisibility(View.VISIBLE);
		}
	}
	
	private class ModifySettings extends AsyncTask<Integer, Integer, String> {

		@Override
		protected String doInBackground(Integer... params) {
			
			String uid = DefaultSettings.getLocalUid();
			String followable = DefaultSettings.isFollowable() ? "1" : "0";
			String category = DefaultSettings.encodeCategoryState();
			
			Log.d(TAG, "Modify UID: "+uid);
			Log.d(TAG, "Modify Followable: "+followable);
			Log.d(TAG, "Modify Category: "+category);
			
			CommunityFactory.sendFollowable(uid, followable, "" + DefaultSettings.newUpdateStamp());
			Log.d(TAG, "Followable changed");
			CommunityFactory.sendDefaultMsgType(uid, category, "" + DefaultSettings.newUpdateStamp());
			Log.d(TAG, "Category changed");
			
			return "Done";
		}
		
		//负责判断返回值
		@Override
		protected void onPostExecute(String result) {
			
			Toast.makeText(getApplicationContext(), "修改操作完成", Toast.LENGTH_SHORT).show();
			settingsModified = false;
			super.onPostExecute(result);
		}
		
	}

}
