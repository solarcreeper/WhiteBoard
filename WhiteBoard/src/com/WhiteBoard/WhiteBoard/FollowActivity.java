package com.WhiteBoard.WhiteBoard;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SimpleAdapter;
import android.widget.SimpleAdapter.ViewBinder;
import android.widget.TextView;

import com.WhiteBoard.BaseClasses.DefaultSettings;
import com.WhiteBoard.BaseClasses.User;
import com.WhiteBoard.Media.ImageLoader;
import com.WhiteBoard.Send.CommunityFactory;

public class FollowActivity extends Activity implements OnScrollListener, OnItemClickListener {
	
	private final String TAG = "FollowActivity";
	
	private ListView userListView;
	private SimpleAdapter userListAdapter;
	private ArrayList<Map<String, Object>> userSource = new ArrayList<Map<String,Object>>();
	private ArrayList<User> userTerm = new ArrayList<User>();
	/*加载更多*/
	private View loadMoreView; 
	private TextView loadingLabel;
	private ProgressBar loadingIndicator;
	private boolean loadFinish = false;
	private boolean loading = false;
	private int lastVisibleIndex;
	
	private ImageButton  buttonmapFollow = null;
	private ImageButton  buttonprofileFollow = null;
	private ImageButton buttonsetFollow = null;
	
	private ImageButton buttonFollowing;
	private ImageButton buttonFollower;
	
	private ImageLoader imageLoader; 
	
	private final int FOLLOWING = 1;
	private final int FOLLOWER = 2;
	private int currentView = FOLLOWING;
	private int page = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		if (!DefaultSettings.isLoggedIn()) {
			Log.d(TAG, "[Shouldn't be here] onCreate : Not logged in");
			Intent intent = new Intent();
			intent.setClass(FollowActivity.this, LogInActivity.class);
			startActivity(intent);
			finish();
		}
		
		setContentView(R.layout.activity_follow);
		
		buttonmapFollow = (ImageButton)findViewById(R.id.buttonmapFollow);
		buttonprofileFollow = (ImageButton)findViewById(R.id.buttonprofileFollow);
		buttonsetFollow = (ImageButton)findViewById(R.id.buttonsetFollow);
		
		buttonFollowing = (ImageButton)findViewById(R.id.followingFollow);
		buttonFollower = (ImageButton)findViewById(R.id.followedFollow);
		
		buttonmapFollow.setOnClickListener(new NavigationListener());
		buttonprofileFollow.setOnClickListener(new NavigationListener());
		buttonsetFollow.setOnClickListener(new NavigationListener());
		
		buttonFollowing.setOnClickListener(new ListSelectListener());
		buttonFollower.setOnClickListener(new ListSelectListener());
		
		userListView = (ListView)findViewById(R.id.list_view_user);
		userSource = new ArrayList<Map<String, Object>>();
		
		loadMoreView = getLayoutInflater().inflate(R.layout.list_item_viewmore, null);
		loadingLabel = (TextView) loadMoreView.findViewById(R.id.text_loading);
		loadingIndicator = (ProgressBar) loadMoreView.findViewById(R.id.progressBar_loading);
		
		userListAdapter = new SimpleAdapter(this, userSource, R.layout.user_list_item,
				new String[] { "AVATAR", "NAME", "ID" }, 
				new int[] {R.id.image_avatar, R.id.text_user_name, R.id.text_user_id });
		
		userListAdapter.setViewBinder(VIEW_BINDER);
		// 加上底部View，注意要放在setAdapter方法前
		userListView.addFooterView(loadMoreView);
		userListView.setAdapter(userListAdapter);
        // 绑定监听器
		userListView.setOnScrollListener(this);
		userListView.setOnItemClickListener(this); 
		
		imageLoader = new ImageLoader(getApplicationContext());
		imageLoader.clearCache();
		resetList();
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		resetList();
		new LoadMore().execute(++page);
		Log.d(TAG, "onResume");
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		resetList();
		Log.d(TAG, "onDestroy");
	}
	
	private void resetList() {
		page = 0;
		loadFinish = false;
		loading = false;
		userSource.clear();
		userTerm.clear();
		userListAdapter.notifyDataSetChanged();
	}

	private class NavigationListener implements OnClickListener{
		
		@Override
		public  void onClick(View v){
			Intent  intent = new Intent();
			switch (v.getId()) {
			case R.id.buttonmapFollow:
				intent.setClass(FollowActivity.this,MyMapActivity.class);
				break;
			case R.id.buttonprofileFollow:
				ProfileActivity.preInitialize(DefaultSettings.getLocalUid(), DefaultSettings.getLocalUserName());
				intent.setClass(FollowActivity.this, ProfileActivity.class);
				break;
			case R.id.buttonsetFollow:
				intent.setClass(FollowActivity.this, SetActivity.class);
				break;
			}
			FollowActivity.this.startActivity(intent);
			finish();
		}
	}
	
	private class ListSelectListener implements OnClickListener {
		
		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.followingFollow:
				if (currentView != FOLLOWING) {
					currentView = FOLLOWING;
					buttonFollowing.setBackgroundResource(R.drawable.icon_button_of_follow_hov);
					buttonFollower.setBackgroundResource(R.drawable.icon_button_of_follow_act);
				}
				break;
			case R.id.followedFollow:
				if (currentView != FOLLOWER) {
					currentView = FOLLOWER;
					buttonFollowing.setBackgroundResource(R.drawable.icon_button_of_follow_act);
					buttonFollower.setBackgroundResource(R.drawable.icon_button_of_follow_hov);
				}
				break;
			}
			resetList();
			new LoadMore().execute(++page);
		}
	}
	
	
	@Override
	public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
		lastVisibleIndex = firstVisibleItem + visibleItemCount -1;
		
	}


	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
		// 滑到底部后自动加载，判断listview已经停止滚动并且最后可视的条目等于adapter的条目
        if (!loading && !loadFinish && scrollState == OnScrollListener.SCROLL_STATE_IDLE && lastVisibleIndex == userListAdapter.getCount()) {
        	Log.d("ListTest","lastVisibleIndex = "+lastVisibleIndex + ";Count = " + userListAdapter.getCount());
            // 当滑到底部时自动加载
            new LoadMore().execute(++page);
        }
		
	}


	@SuppressWarnings("unchecked")
	@Override
	public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
		/*要先过滤掉奇怪的Item*/
		if (v != loadMoreView) {
			ListView listView = (ListView)parent;
			HashMap<String, Object> map = (HashMap<String, Object>) listView.getItemAtPosition(position);
			String userid = (String) map.get("ID");
			String username = (String) map.get("NAME");
			ProfileActivity.preInitialize(userid, username);
			Intent intent = new Intent();
			intent.setClass(FollowActivity.this, ProfileActivity.class);
			startActivity(intent);
		}
	}
	
	
	private final ViewBinder VIEW_BINDER = new ViewBinder() {

		@Override
		public boolean setViewValue(View view, Object data,	String textRepresentation) {
			if (view.getId() == R.id.image_avatar) {
				String uid = (String) data;
				((ImageView) view).setImageResource(R.drawable.default_avatar);
				//处理头像
				String url = new String(DefaultSettings.REMOTE_STORE_URL_PREFIX_AVATAT + uid + ".jpg");
				imageLoader.DisplayImage(url, (ImageView)view, ImageLoader.REQUIRE_SIZE_AVATAR);  
				return true;
			}
			return false;
		}
		
	};

	
	/* 这里是AsyncTask的部分
	 * 将工作抽象为一个Task，拥有4种状态，新建类继承AsyncTask, 覆盖4四种状态对应的方法
	 */
	private class LoadMore extends AsyncTask<Integer, Integer, Integer> {
		
		@Override
		protected void onPreExecute() {
			Log.d("Async Task", "PreExcute Start");
			loading = true;
			loadingIndicator.setVisibility(View.VISIBLE);
            loadingLabel.setText("努力加载中...");
            Log.d("Async Task", "PreExcute Done");
		}
		
		@Override
		protected Integer doInBackground(Integer... parms) {
			Log.d("Async Task", "doInBackground Start");
			if (currentView == FOLLOWING) {
				userTerm = CommunityFactory.getFollowingList(DefaultSettings.getLocalUid(), "" + parms[0]);
			}
			else if (currentView == FOLLOWER) {
				userTerm = CommunityFactory.getFollowerList(DefaultSettings.getLocalUid(), "" + parms[0]);
			}
			Log.d("Async Task", "doInBackground Done");
			return currentView;
		}
		
		@Override
		protected void onProgressUpdate(Integer... values) {
			super.onProgressUpdate(values);
		}
		
		@Override
		protected void onPostExecute(Integer result) {
			Log.d("Async Task", "onPostExecute Start");

			for (User userItem : userTerm) {
	        	if (!userItem.getId().equals("null")) {
	        		HashMap<String, Object> map = new HashMap<String, Object>();
	        		map.put("AVATAR", userItem.getId());
	        		map.put("NAME", userItem.getUserName());
	        		map.put("ID", userItem.getId());
	        		userSource.add(map);
	        	} else {
	        		loadFinish = true;
	        	}
			}
			Log.d("Async Task", "onPostExecute: Reload Data Done");
			loading = false;
			loadingLabel.setText(loadFinish ? "所有的朋友都在这了哟" : "加载更多");
            loadingIndicator.setVisibility(View.GONE);
            userListAdapter.notifyDataSetChanged();
            Log.d("Async Task", "onPostExecute: Refresh UI Done");
			Log.d("Async Task", "onPostExecute Done");
		}
	}

}
