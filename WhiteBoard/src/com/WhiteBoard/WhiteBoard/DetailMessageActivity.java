package com.WhiteBoard.WhiteBoard;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.method.LinkMovementMethod;
import android.text.style.ImageSpan;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SimpleAdapter;
import android.widget.SimpleAdapter.ViewBinder;
import android.widget.TextView;
import android.widget.Toast;

import com.WhiteBoard.BaseClasses.Comment;
import com.WhiteBoard.BaseClasses.DefaultSettings;
import com.WhiteBoard.BaseClasses.DetailMessage;
import com.WhiteBoard.Media.ImageLoader;
import com.WhiteBoard.Send.CommunityFactory;
import com.baidu.mapapi.search.MKAddrInfo;
import com.baidu.mapapi.search.MKBusLineResult;
import com.baidu.mapapi.search.MKDrivingRouteResult;
import com.baidu.mapapi.search.MKPoiResult;
import com.baidu.mapapi.search.MKSearch;
import com.baidu.mapapi.search.MKSearchListener;
import com.baidu.mapapi.search.MKSuggestionResult;
import com.baidu.mapapi.search.MKTransitRouteResult;
import com.baidu.mapapi.search.MKWalkingRouteResult;
import com.baidu.platform.comapi.basestruct.GeoPoint;

public class DetailMessageActivity extends Activity {

	private static final String TAG = "DetailMessageActivity";

	private static String symble_play;
	private static String symble_stop;

	// 所有的控件都在这里。
	private ImageButton naviBack;
	private ImageButton showCommentList;
	private ImageButton sendComment;
	private ImageView userAvatar;
	private TextView userName;
	private TextView amountHelpful;
	private TextView amountSpam;
	private TextView addrCreated;
	private TextView addrExisted;
	private TextView content;
	private ProgressBar indicator;

	private ImageButton sound;
	private ImageButton writeComment;
	private ImageButton collect;
	private ImageButton markHelpful;
	private ImageButton markSpam;
	private TextView commentNbr;
	private TextView time;
	private TextView categoryLabel;
	private ImageView categoryImage;
	private ImageView ribbonDiscuss;
	private TextView voiceTime;
	private int duration;
	private boolean favorited;

	private boolean commentListExpand = false;

	// 这个两个是获得地址信息需要的管理类。
	private MKSearch mk = new MKSearch();

	private ListView list;
	private SimpleAdapter listAdapter;
	private View header;
	private View loadMoreView;
	private EditText commentContent;
	private ImageView commentAvatar;
	private int page = 0;
	private ArrayList<Map<String, Object>> items = new ArrayList<Map<String, Object>>();
	private TextView loadingLabel;
	private ProgressBar loadingIndicator;
	private boolean loadFinish = false;
	private boolean loading = false;

	// 这里是纸条的Id
	private String mId;

	// 评价信息
	private int markStatus = DefaultSettings.USER_MARK_NONE;
	private int nHelpful;
	private int nSpam;

	// 这两个变量都怪垃圾百度！
	private double longitudeExist;
	private double latitudeExist;

	// 这个叼东西是啥啊- -
	private ImageLoader imageLoader;

	// 作者信息
	private String strUserName;
	private String uId;

	private boolean isDisplayEdit = false;

	private MediaPlayer player;
	

	private String strContent;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_detail);
		
		symble_play = getString(R.string.symble_play);
		symble_stop = getString(R.string.symble_stop);

		// 由前一个Activity拿到纸条id
		Intent intent = getIntent();
		Bundle bundle = intent.getExtras();
		mId = bundle.getString("mId");

		// 获得Header和footer的View对象
		header = getLayoutInflater().inflate(R.layout.detailmessage_header, null);
		loadMoreView = getLayoutInflater().inflate( R.layout.comment_item_viewmore, null);

		loadingLabel = (TextView) loadMoreView.findViewById(R.id.text_loading);
		loadingIndicator = (ProgressBar) loadMoreView .findViewById(R.id.progressBar_loading);

		// 获得控件实例化
		naviBack = (ImageButton) findViewById(R.id.back);
		userAvatar = (ImageView) header.findViewById(R.id.dm_avatar);
		userName = (TextView) header.findViewById(R.id.usernameInformation);
		amountHelpful = (TextView) header .findViewById(R.id.helpfulNumberInformation);
		amountSpam = (TextView) header .findViewById(R.id.unhelpfulNumberInformation);
		addrCreated = (TextView) header.findViewById(R.id.addr_create);
		addrExisted = (TextView) header.findViewById(R.id.addr_exist);
		content = (TextView) header.findViewById(R.id.informationInformation);
		sound = (ImageButton) header.findViewById(R.id.soundInformation);
		voiceTime = (TextView) header.findViewById(R.id.voiceTimeInformation);
		writeComment = (ImageButton) header .findViewById(R.id.buttonWriteDiscussInformation);
		markHelpful = (ImageButton) header.findViewById(R.id.buttonMarkHelpful);
		markSpam = (ImageButton) header.findViewById(R.id.buttonMarkSpam);
		time = (TextView) header.findViewById(R.id.timeInformation);
		categoryLabel = (TextView) header.findViewById(R.id.label_category);
		categoryImage = (ImageView) header.findViewById(R.id.colorInformation);
		ribbonDiscuss = (ImageView) header .findViewById(R.id.colorDiscussInformation);
		commentAvatar = (ImageView) findViewById(R.id.comment_list_avatar_addcomment);
		commentContent = (EditText) findViewById(R.id.comment_list_content_addcomment);
		collect = (ImageButton) findViewById(R.id.buttonMuneCollectInformation);
		sendComment = (ImageButton)findViewById(R.id.buttonSendComment);
		indicator = (ProgressBar)findViewById(R.id.indicator);
		commentNbr = (TextView) findViewById(R.id.discussNumberInformation);
		showCommentList = (ImageButton) findViewById(R.id.buttonMuneDiscussInformation);
		list = (ListView) findViewById(R.id.wholelist);

		// 设置好footer和header
		list.addHeaderView(header);
		list.addFooterView(loadMoreView);
		loadMoreView.setVisibility(View.GONE);

		// 处理ListView的Adapter
		listAdapter = new SimpleAdapter(this, items, R.layout.comment_list_item, 
				new String[] { "AVATAR", "USERNANE", "DATE", "CONTENT" }, 
				new int[] {	R.id.comment_list_avatar, R.id.comment_list_username, R.id.comment_list_date, R.id.comment_list_content });
		listAdapter.setViewBinder(VIEW_BINDER);
		list.setAdapter(listAdapter);

		// 初始化图片载入器且清空缓存
		imageLoader = new ImageLoader(getApplicationContext());
		imageLoader.clearCache();
		String myAvatarUrl = DefaultSettings.REMOTE_STORE_URL_PREFIX_AVATAT + DefaultSettings.getLocalUid() + ".jpg";
		imageLoader.DisplayImage(myAvatarUrl, commentAvatar, ImageLoader.REQUIRE_SIZE_AVATAR);

		// 读取完成前收藏按钮不可点击。
		collect.setClickable(false);

		// 初始化获得地址信息的监听器
		WhiteBoardApp app = (WhiteBoardApp) (this.getApplication());
		mk.init(app.mBMapMan, new MKListener());

		// 设置点击事件。
		markHelpful.setOnClickListener(new MarkListener());
		markSpam.setOnClickListener(new MarkListener());
		collect.setOnClickListener(new CollectListener());
		showCommentList.setOnClickListener(new ShowCommentListener());
		writeComment.setOnClickListener(new CommentListener());
		sound.setOnClickListener(new SoundListener());
		userAvatar.setOnClickListener(new AvatarListener());
		commentContent.setOnKeyListener(new CommentInputListener());
		naviBack.setOnClickListener(new BackListener());
		sendComment.setOnClickListener(new SendCommentListener());

		// 滚动监听
		list.setOnScrollListener(new ScrollListener());
		list.setOnItemClickListener(new ItemListener());

		// 执行更新纸条信息；
		resetList();
		new MessageDetail().execute();

	}

	@Override
	protected void onPause() {
		super.onPause();
		if (player != null && player.isPlaying()) {
			player.stop();
		}
	}

	// 重置列表，销毁列表后，首次加载列表前均需调用
	private void resetList() {
		page = 0;
		loadFinish = false;
		loading = false;
		items.clear();
		listAdapter.notifyDataSetChanged();
	}

	private class ItemListener implements OnItemClickListener {

		@Override
		public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
			Log.d(TAG, "Item Clicked");
			// 取到name，然后跳转
			if (v != loadMoreView) {
				ListView listView = (ListView)parent;
				HashMap<String, Object> map = (HashMap<String, Object>) listView.getItemAtPosition(position);
				String userid = (String) map.get("AVATAR");
				String username = (String) map.get("USERNAME");
				ProfileActivity.preInitialize(userid, username);
				Intent intent = new Intent();
				intent.setClass(DetailMessageActivity.this, ProfileActivity.class);
				DetailMessageActivity.this.startActivity(intent);
			}
		}

	}

	private class ScrollListener implements OnScrollListener {

		private int lastVisibleIndex;

		@Override
		public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
			lastVisibleIndex = firstVisibleItem + visibleItemCount - 1;
		}

		@Override
		public void onScrollStateChanged(AbsListView view, int scrollState) {
			/* 不在加载		未加载完全		滚动停止时		最后可见项目为最后一项（加载View) */
			if (!loading && !loadFinish && scrollState == OnScrollListener.SCROLL_STATE_IDLE && lastVisibleIndex == listAdapter.getCount()) {
				if (items != null)
					items.clear();
				new LoadMoreComment().execute(++page);
			}

		}
	}

	private class MarkListener implements OnClickListener {

		@Override
		public void onClick(View v) {
			if (DefaultSettings.isLoggedIn()) {
				Runnable runnable = new Runnable() {
					@Override
					public void run() {
						CommunityFactory.sendEvaluation(DefaultSettings.getLocalUid(), mId, "" + markStatus);
					}
				};

				boolean modified = false;
				switch (v.getId()) {
				case R.id.buttonMarkHelpful:
					if (markStatus == DefaultSettings.USER_MARK_HELPFUL)
						break;
					Log.d(TAG, "MarkListener: MarkHelpful");
					if (markStatus == DefaultSettings.USER_MARK_SPAM)
						--nSpam;
					markStatus = DefaultSettings.USER_MARK_HELPFUL;
					++nHelpful;
					modified = true;
					break;
				case R.id.buttonMarkSpam:
					if (markStatus == DefaultSettings.USER_MARK_SPAM)
						break;
					Log.d(TAG, "MarkListener: MarkSpam");
					if (markStatus == DefaultSettings.USER_MARK_HELPFUL)
						--nHelpful;
					markStatus = DefaultSettings.USER_MARK_SPAM;
					++nSpam;
					modified = true;
					break;
				}
				if (modified) {
					new Thread(runnable).start();
					modified = false;
					refreshMarkDisplay();
					Toast.makeText(getApplicationContext(), "评价成功", Toast.LENGTH_SHORT).show();
				}

			} else {
				Toast.makeText(getApplicationContext(), "还没有登录呐", Toast.LENGTH_SHORT).show();
				Intent intent = new Intent(DetailMessageActivity.this, LogInActivity.class);
				startActivity(intent);
			}

		}

	}

	private void refreshMarkDisplay() {
		switch (markStatus) {
		case DefaultSettings.USER_MARK_NONE:
			amountHelpful.setText("△" + nHelpful);
			amountSpam.setText("▽" + nSpam);
			break;
		case DefaultSettings.USER_MARK_HELPFUL:
			amountHelpful.setText("▲" + nHelpful);
			amountSpam.setText("▽" + nSpam);
			break;
		case DefaultSettings.USER_MARK_SPAM:
			amountHelpful.setText("△" + nHelpful);
			amountSpam.setText("▼" + nSpam);
			break;
		}
	}

	// 收藏的按键功能。调用Collect类来实现。
	private class CollectListener implements OnClickListener {

		@Override
		public void onClick(View v) {
			if (DefaultSettings.isLoggedIn()) {
				new Collect().execute();
			} else {
				Toast.makeText(getApplicationContext(), "还没有登录呐", Toast.LENGTH_SHORT).show();
				Intent intent = new Intent(DetailMessageActivity.this, LogInActivity.class);
				startActivity(intent);
			}

		}
	}

	private class ShowCommentListener implements OnClickListener {

		@Override
		public void onClick(View v) {
			resetList();
			if (commentListExpand) {
				findViewById(R.id.message_layout_bottom_with_comment).setVisibility(View.GONE);
				findViewById(R.id.message_layout_bottom_without_comment).setVisibility(View.VISIBLE);
				loadMoreView.setVisibility(View.GONE);
				findViewById(R.id.comment_layout_add).setVisibility(View.GONE);
				isDisplayEdit = false;

			} else {
				findViewById(R.id.message_layout_bottom_with_comment).setVisibility(View.VISIBLE);
				findViewById(R.id.message_layout_bottom_without_comment).setVisibility(View.GONE);
				loadMoreView.setVisibility(View.VISIBLE);
				// 重新读取数据
				resetList();
				new LoadMoreComment().execute(++page);
			}

			commentListExpand = !commentListExpand;

		}

	}

	// 返回上个Activity的按键功能
	private class BackListener implements OnClickListener {

		@Override
		public void onClick(View v) {
			finish();
		}

	}

	private class CommentListener implements OnClickListener {

		@Override
		public void onClick(View arg0) {
			if (!DefaultSettings.isLoggedIn()) {
				Toast.makeText(getApplicationContext(), "还没有登录呐", Toast.LENGTH_SHORT).show();
				Intent intent = new Intent(DetailMessageActivity.this, LogInActivity.class);
				startActivity(intent);
				finish();
			} else if (!isDisplayEdit) {
				findViewById(R.id.comment_layout_add).setVisibility(View.VISIBLE);
				sendComment.setVisibility(View.VISIBLE);
				commentContent.requestFocus();
				isDisplayEdit = !isDisplayEdit;
			} else {
				findViewById(R.id.comment_layout_add).setVisibility(View.GONE);
				sendComment.setVisibility(View.GONE);
				isDisplayEdit = !isDisplayEdit;
			}
		}
	}
	
	private class SendCommentListener implements OnClickListener {

		@Override
		public void onClick(View v) {
			new CommentSend().execute();
		}
		
	}

	private class CommentInputListener implements OnKeyListener {

		@Override
		public boolean onKey(View v, int keyCode, KeyEvent event) {
			if (keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_DOWN) {
				new CommentSend().execute();
				return true;
			}
			return false;
		}

	}

	// 这是播放媒体问题的
	private class SoundListener implements OnClickListener {

		@Override
		public void onClick(View v) {
			if (player != null) {
				if (player.isPlaying()) {
					player.stop();
					voiceTime.setText(symble_play + duration + "s");
				} else {
					try {
						player.prepare();
					} catch (IllegalStateException e) {
						e.printStackTrace();
					} catch (IOException e) {
						e.printStackTrace();
					} finally {
						player.start();
						voiceTime.setText(symble_stop + duration + "s");
					}
				}
			}

		}

	}

	// 这是头像的点击事件
	private class AvatarListener implements OnClickListener {

		@Override
		public void onClick(View v) {
			ProfileActivity.preInitialize(uId, strUserName);
			Intent intent = new Intent();
			intent.setClass(DetailMessageActivity.this, ProfileActivity.class);
			startActivity(intent);
		}

	}

	// 这两个是获得地址信息的回调。
	private class MKListener implements MKSearchListener {

		private boolean isCreatedPositionLocated = true;

		@Override
		public void onGetAddrResult(MKAddrInfo arg0, int arg1) {
			if (isCreatedPositionLocated) {
				addrCreated.setText(arg0.strAddr);
				isCreatedPositionLocated = false;
				mk.reverseGeocode(new GeoPoint((int) (latitudeExist * 1E6), (int) (longitudeExist * 1E6)));
			} else {
				addrExisted.setText(arg0.strAddr);
			}
		}

		@Override
		public void onGetBusDetailResult(MKBusLineResult arg0, int arg1) {}
		@Override
		public void onGetDrivingRouteResult(MKDrivingRouteResult arg0, int arg1) {}
		@Override
		public void onGetPoiResult(MKPoiResult arg0, int arg1, int arg2) {}
		@Override
		public void onGetSuggestionResult(MKSuggestionResult arg0, int arg1) {}
		@Override
		public void onGetTransitRouteResult(MKTransitRouteResult arg0, int arg1) {}
		@Override
		public void onGetWalkingRouteResult(MKWalkingRouteResult arg0, int arg1) {}
		@Override
		public void onGetPoiDetailSearchResult(int arg0, int arg1) {}
	}


	// 这个是异步更新更多评论的方法
	private class LoadMoreComment extends AsyncTask<Integer, Integer, String> {

		private ArrayList<Comment> listComment;
		private int size;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			Log.d(TAG, "AT：PreExcute Start");
			loading = true;
			loadingIndicator.setVisibility(View.VISIBLE);
			loadingLabel.setText("努力加载中...");
			Log.d(TAG, "AT：PreExcute Done");
		}

		@Override
		protected String doInBackground(Integer... page) {
			String strPage = "" + page[0];
			listComment = CommunityFactory.getCommentList(mId, strPage);
			size = listComment.size();
			return null;
		}

		@Override
		protected void onPostExecute(String result) {
			// 更新纸条信息
			for (int i = 0; i < size; i++) {
				if (!(listComment.get(i).getContent().equals("null"))) {
					HashMap<String, Object> map = new HashMap<String, Object>();
					map.put("AVATAR", (listComment.get(i)).getIdAuthor());
					map.put("CONTENT", (listComment.get(i)).getContent());
					map.put("DATE", (listComment.get(i)).getDateCreate());
					map.put("USERNAME", (listComment.get(i)).getAuthorUserName());
					items.add(map);
				} else {
					loadFinish = true;
				}
			}
			loading = false;
			loadingLabel.setText(loadFinish ? "已经是最后一页" : "加载更多");
			loadingIndicator.setVisibility(View.GONE);
			listAdapter.notifyDataSetChanged();

			super.onPostExecute(result);
		}

	}

	private final ViewBinder VIEW_BINDER = new ViewBinder() {

		@Override
		public boolean setViewValue(View view, Object data, String textRepresentation) {
			if (view.getId() == R.id.comment_list_avatar) {
				ImageLoader imageLoader = new ImageLoader(getApplicationContext());
				imageLoader.clearCache();
				String urlAvatar = new String(DefaultSettings.REMOTE_STORE_URL_PREFIX_AVATAT + ((String) data) + ".jpg");
				imageLoader.DisplayImage(urlAvatar, ((ImageView) view), ImageLoader.REQUIRE_SIZE_AVATAR);
				return true;
			}
			return false;
		}
	};

	// 这是异步更新整个纸条详情的方法
	private class MessageDetail extends AsyncTask<Integer, Integer, String> {

		private DetailMessage msg;

		private int nComment;
		private String date;
		private String strCategory;
		
		@Override
		protected String doInBackground(Integer... params) {

			// 向老板要来详细纸条信息
			msg = CommunityFactory.getDetailMessage(DefaultSettings.getLocalUid(), mId);

			// 拉地址信息
			double latitudeCreate = msg.getLatitudeCreate();
			double longitudeCreate = msg.getLongitudeCreate();
			latitudeExist = msg.getLatitudeExist();
			longitudeExist = msg.getLongitudeExist();
			mk.reverseGeocode(new GeoPoint((int) (latitudeCreate * 1E6), (int) (longitudeCreate * 1E6)));

			return "Done";
		}
		
		@Override
		protected void onPostExecute(String result) {

			// 拿数据
			uId = msg.getIdAuthor();
			nHelpful = msg.getAmountHelpful();
			nSpam = msg.getAmountSpam();
			nComment = msg.getAmountComment();
			strContent = msg.getContent();
			date = msg.getDateCreate();
			strCategory = msg.getIdCategory() + ",";
			strUserName = msg.getAuthorUserName();
			markStatus = msg.getLocalUserMarkStatus();
			favorited = msg.isFavotited();
			Log.d(TAG, "MSG MarkStatus : " + markStatus);

			// 设置控件的值
			refreshMarkDisplay();
			commentNbr.setText("" + nComment);
			userName.setText(strUserName);
			time.setText(date);

			Drawable marker = null;
			String categoryName = "";

			if (strCategory.equals(DefaultSettings.CATEGORY_ID_COMMERCIAL)) {
				marker = getResources().getDrawable(R.drawable.icon_color_bule_discuss_information);
				categoryName = getString(R.string.category_name_commercial);
			} else if (strCategory.equals(DefaultSettings.CATEGORY_ID_PERSONAL_STATUS)) {
				marker = getResources().getDrawable(R.drawable.icon_color_green_discuss_information);
				categoryName = getString(R.string.category_name_personal);
			} else if (strCategory.equals(DefaultSettings.CATEGORY_ID_FRIEND_MAKING)) {
				marker = getResources().getDrawable(R.drawable.icon_color_red_discuss_information);
				categoryName = getString(R.string.category_name_friend);
			} else if (strCategory.equals(DefaultSettings.CATEGORY_ID_MUTUAL_HELP)) {
				marker = getResources().getDrawable(R.drawable.icon_color_orange_discuss_information);
				categoryName = getString(R.string.category_name_help);
			}
			categoryImage.setImageDrawable(marker);
			ribbonDiscuss.setImageDrawable(marker);
			categoryLabel.setText(categoryName);

			new LoadPictureAndVoice().execute();
			
			setFavoriteButton(favorited);
			
			//读取头像
			String urlAvatar = new String(DefaultSettings.REMOTE_STORE_URL_PREFIX_AVATAT + uId + ".jpg");
			imageLoader.DisplayImage(urlAvatar, userAvatar, ImageLoader.REQUIRE_SIZE_AVATAR);
			
			super.onPostExecute(result);
		}
	}
	
	private void setFavoriteButton(boolean favorited) {
		this.favorited = favorited;
		int resIdNonFav = R.drawable.icon_button_menu_collect_collected;
		int resIdFaved = R.drawable.icon_button_menu_collect_information;
		collect.setBackgroundResource(favorited ? resIdNonFav : resIdFaved);
	}

	// 一个用来实现收藏异步操作的函数。
	private class Collect extends AsyncTask<Integer, Integer, String> {
		
		String returnCode;
		
		@Override
		protected void onPreExecute() {
			collect.setClickable(false);
		}

		@Override
		protected String doInBackground(Integer... params) {
			String strUId = "" + DefaultSettings.getLocalUid();
			returnCode = CommunityFactory.setFavorite(strUId, mId, !favorited);
			return null;
		}

		@Override
		protected void onPostExecute(String result) {
			collect.setClickable(true);
			if(returnCode.equals("-1")) {
				Toast.makeText(getApplicationContext(), "出错了，请重试", Toast.LENGTH_SHORT).show();
			} else {
				setFavoriteButton(!favorited);
			}
			super.onPostExecute(result);
		}

	}

	private class CommentSend extends AsyncTask<Integer, Integer, Integer> {

		private String response;
		
		@Override
		protected void onPreExecute() {
			indicator.setVisibility(View.VISIBLE);
			sendComment.setVisibility(View.GONE);
		}

		@Override
		protected Integer doInBackground(Integer... arg0) {
			response = CommunityFactory.createComment(DefaultSettings.getLocalUid(), mId, commentContent.getText().toString());
			return null;
		}

		@Override
		protected void onPostExecute(Integer result) {
			indicator.setVisibility(View.GONE);
			sendComment.setVisibility(View.VISIBLE);

			if (!(response.equals("0"))) {
				Toast.makeText(DetailMessageActivity.this, "发送成功", Toast.LENGTH_SHORT).show();
				commentContent.setText("");
				findViewById(R.id.comment_layout_add).setVisibility(View.GONE);
				sendComment.setVisibility(View.GONE);
				isDisplayEdit = false;

				// 清空数据
				items.clear();
				listAdapter.notifyDataSetChanged();
				list.invalidate();
				
				page = 0;
				new LoadMoreComment().execute(page + 1);
				page = page + 1;
				
			} else {
				Toast.makeText(DetailMessageActivity.this, "出错了，请重试", Toast.LENGTH_SHORT).show();
			}
			
			super.onPostExecute(result);
		}

	}

	
	private class LoadPictureAndVoice extends AsyncTask<Integer, Integer, Integer> {

		public String fileName;
		public Bitmap bm;
		
		private SpannableString spannableString;
		
		@Override
		protected Integer doInBackground(Integer... arg0) {
			int indexB = strContent.indexOf("<audio", 0);
			int indexE = strContent.indexOf("/>", indexB);

			if (indexB != -1 && indexE != -1) {
				String voiceName = strContent.substring(indexB + 7, indexE);
				Uri uri = Uri.parse(DefaultSettings.REMOTE_STORE_URL_PREFIX_VOICE + voiceName);
				player = MediaPlayer.create(getApplicationContext(), uri);
				player.setOnCompletionListener(new audioPlaybackCompletionListener());
			}

			if (indexB != -1) {
				strContent = strContent.substring(0, indexB - 1);
			}

			spannableString = new SpannableString(strContent);
			int indexBegin = strContent.indexOf("<image", 0);
			int indexEnd = strContent.indexOf("/>", indexBegin);

			while (indexBegin != -1 && indexEnd != -1) {

				fileName = strContent.substring(indexBegin + 7, indexEnd);
	
				try {
					// prepare data source
					URL url = new URL(DefaultSettings.REMOTE_STORE_URL_PREFIX_PICTURE + fileName);
					InputStream in = url.openStream();
					// decode image size
					BitmapFactory.Options o = new BitmapFactory.Options();
					o.inJustDecodeBounds = true;
					BitmapFactory.decodeStream(in, null, o);
					in.close();
					// Find the correct scale value. It should be the power of 2.
					double REQUIRED_SIZE = content.getWidth(); 
					Log.d(TAG, ""+REQUIRED_SIZE);
					double width_tmp = o.outWidth, height_tmp = o.outHeight;
					int scale = 1;
					if (width_tmp > REQUIRED_SIZE || height_tmp > REQUIRED_SIZE) {
						double width_scale = Math.ceil(width_tmp / REQUIRED_SIZE);
						Log.d(TAG, ""+width_scale);
						double height_scale = Math.ceil(height_tmp / REQUIRED_SIZE);
						Log.d(TAG, ""+height_scale);
						if (width_scale > height_scale)
							scale = (int)width_scale;
						else
							scale = (int)height_scale;
					}
					Log.d(TAG, ""+scale);
					// decode with inSampleSize
					in = url.openStream();
					BitmapFactory.Options o2 = new BitmapFactory.Options();
					o2.inSampleSize = scale;
					bm = BitmapFactory.decodeStream(in, null, o2);
					in.close();
					
				} catch (MalformedURLException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
				
				if (bm != null) 	{
					ImageSpan imageSpan = new ImageSpan(getApplicationContext(), bm);
					spannableString.setSpan(imageSpan, indexBegin,indexEnd + 2, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
				} else {
					// TODO prompt if image failed to load
				}
				
				
				indexBegin = strContent.indexOf("<image", indexBegin + 1);
				indexEnd = strContent.indexOf("/>", indexBegin);
			}
			return null;
		}

		@Override
		protected void onPostExecute(Integer result) {
			
			if(player != null) {
				duration = (int)Math.ceil(1.0 * player.getDuration() / 1000);
				voiceTime.setText(symble_play + duration + "s");
				findViewById(R.id.audio_tool_layout).setVisibility(View.VISIBLE);
			}
			
			content.setText(spannableString);
			content.setMovementMethod(LinkMovementMethod.getInstance());
			super.onPostExecute(result);
		}

	}
	
	private class audioPlaybackCompletionListener implements OnCompletionListener {

		@Override
		public void onCompletion(MediaPlayer mp) {
			voiceTime.setText(symble_play + duration + "s");
		}
		
	}
}
