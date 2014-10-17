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
import com.WhiteBoard.BaseClasses.RoughMessage;
import com.WhiteBoard.Media.ImageLoader;
import com.WhiteBoard.Send.CommunityFactory;

public class RMListActivity extends Activity {
	
	private enum ListType { NORMAL, SEARCH, FAVORITE, PUBLISHED, COMMENTED };
	
	
	private static final String SORT_BY_TIME = "1";
	private static final String SORT_BY_HEAT = "2";
	private static final String SORT_BY_DISTANCE = "3";

	private ImageButton naviBack, naviTool;
	private ImageButton applyTool;
	private ImageButton toolType, toolFilter, toolSort;
	private ImageButton typeCommercial, typePersonal, typeFriend, typeHelp;
	private ImageButton filterPicture, filterVoice;
	private ImageButton sortDistance, sortTime, sortHeat;
	
	private boolean toolbarExpand, typeExpand, filterExpand, sortExpand;
	private boolean filterPictureChecked, filterVoiceChecked;
	private String sortMethod = SORT_BY_TIME;
	private String messageFilter = ",";
	
	private static ListType listType;
	private static String targetUID;
	private static String index;
	private static double latitudeTarget, longitudeTarget, latitudeCurrent, longitudeCurrent, geoShift;
	
	private ListView rmListView;
	private SimpleAdapter rmListAdapter;
	private ArrayList<Map<String, Object>> rmData = new ArrayList<Map<String,Object>>();
	private ArrayList<RoughMessage> rmSource = new ArrayList<RoughMessage>();
	private ArrayList<RoughMessage> rmListTerm;
	private View loadMoreView;
	private TextView loadStatus;
	private ProgressBar loadingIndicator;
	
	private ImageLoader imageLoader; 
	
	private int page = 0;
	private boolean loadFinish;
	private boolean loading;
	
	private final static String TAG = "RMListAvtivity";

	
	public static void setNormalList(double latitudeTarget, double longitudeTarget, double geoShift) {
		RMListActivity.setList(ListType.NORMAL, latitudeTarget, longitudeTarget, geoShift, null, "");
	}
	
	public static void setSearchList(double latitudeTarget, double longitudeTarget, double geoShift, String index) {
		RMListActivity.setList(ListType.SEARCH, latitudeTarget, longitudeTarget, geoShift, null, index);
	}
	
	public static void setFavoriteList(String targetUID) {
		RMListActivity.setList(ListType.FAVORITE, 0, 0, 0, targetUID, null);
	}
	
	public static void setPublishedList(String targetUID) {
		RMListActivity.setList(ListType.PUBLISHED, 0, 0, 0, targetUID, null);
	}
	
	public static void setCommentedList(String targetUID) {
		RMListActivity.setList(ListType.COMMENTED, 0, 0, 0, targetUID, null);
	}
	
	private static void setList(ListType listType, double latitudeTarget, double longitudeTarget, double geoShift, String targetUID, String index) {
		RMListActivity.listType = listType;
		RMListActivity.latitudeCurrent = DefaultSettings.getLatitudeCurrent();
		RMListActivity.longitudeCurrent = DefaultSettings.getLongitudeCurrent();
		RMListActivity.latitudeTarget = latitudeTarget;
		RMListActivity.longitudeTarget = longitudeTarget;
		RMListActivity.geoShift = geoShift;
		RMListActivity.targetUID = targetUID;
		RMListActivity.index = index;
	}
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_rm_list);
		
		naviBack = (ImageButton)findViewById(R.id.backInfolist);
		naviTool = (ImageButton)findViewById(R.id.advanceInfolist);
		
		applyTool = (ImageButton)findViewById(R.id.option_panel_background_button);
		
		toolType = (ImageButton)findViewById(R.id.rm_list_button_option_type);
		toolFilter = (ImageButton)findViewById(R.id.rm_list_button_option_filter);
		toolSort = (ImageButton)findViewById(R.id.rm_list_button_option_sort);
		
		typeCommercial = (ImageButton)findViewById(R.id.rm_list_button_option_type_commercial);
		typePersonal = (ImageButton)findViewById(R.id.rm_list_button_option_type_personal);
		typeFriend = (ImageButton)findViewById(R.id.rm_list_button_option_type_friend);
		typeHelp = (ImageButton)findViewById(R.id.rm_list_button_option_type_help);
		
		filterPicture = (ImageButton)findViewById(R.id.rm_list_button_option_filter_picture);
		filterVoice = (ImageButton)findViewById(R.id.rm_list_button_option_filter_voice);
		
		sortDistance = (ImageButton)findViewById(R.id.rm_list_button_option_sort_distance);
		sortHeat = (ImageButton)findViewById(R.id.rm_list_button_option_sort_heat);
		sortTime = (ImageButton)findViewById(R.id.rm_list_button_option_sort_time);
		
		naviBack.setOnClickListener(new TitleBarListener());
		naviTool.setOnClickListener(new TitleBarListener());
		applyTool.setOnClickListener(new TitleBarListener());
		
		toolType.setOnClickListener(new ToolBarListener());
		toolFilter.setOnClickListener(new ToolBarListener());
		toolSort.setOnClickListener(new ToolBarListener());
		
		typeCommercial.setOnClickListener(new TypeListener());
		typePersonal.setOnClickListener(new TypeListener());
		typeFriend.setOnClickListener(new TypeListener());
		typeHelp.setOnClickListener(new TypeListener());
		
		filterPicture.setOnClickListener(new FilterListener());
		filterVoice.setOnClickListener(new FilterListener());
		
		sortDistance.setOnClickListener(new SortListener());
		sortHeat.setOnClickListener(new SortListener());
		sortTime.setOnClickListener(new SortListener());
		
		rmListView = (ListView)findViewById(R.id.list_view_rm);
		rmListAdapter = new SimpleAdapter(this, rmData, R.layout.roughmessage_list_item, 
				new String[] {"AVATAR", "UID", "MID", "CATEGORY", "CONTENT", "DATE", "HAS_PICTURE", "HAS_VOICE", "AMOUNT_HELPFUL", "DISTANCE"},
				new int[] {R.id.rm_list_avatar, R.id.rm_list_uid, R.id.rm_list_mid, R.id.rm_list_ribbon, R.id.rm_list_content, R.id.rm_list_date, R.id.rm_list_hasPicture, R.id.rm_list_hasVoice, R.id.rm_list_amount_helpful, R.id.rm_list_distance});
		
		loadMoreView = getLayoutInflater().inflate(R.layout.roughmessage_list_viewmore, null);
		loadStatus = (TextView) loadMoreView.findViewById(R.id.rm_list_loading_label);
		loadingIndicator = (ProgressBar) loadMoreView.findViewById(R.id.rm_list_loading_progressBar);
		
		imageLoader = new ImageLoader(getApplicationContext());
		
		rmListView.setOnItemClickListener(new RMItemOnClickListener());
		rmListView.setOnScrollListener(new RMListOnScrollListener());
		
		rmListAdapter.setViewBinder(VIEW_BINDER);
		rmListView.addFooterView(loadMoreView);
		rmListView.setAdapter(rmListAdapter);
		
		DefaultSettings.decodeCategoryState();
		imageLoader.clearCache();
		resetList();
		//new LoadRMList().execute(++page);
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		resetList();
		new LoadRMList().execute(++page);
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
		rmData.clear();
		rmSource.clear();
		if (rmListTerm != null)
			rmListTerm.clear();
		rmListAdapter.notifyDataSetChanged();
	}

	private class TitleBarListener implements OnClickListener {

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.backInfolist:
				Log.d(TAG, "Finished");
				RMListActivity.this.finish();
				break;
			case R.id.advanceInfolist:
			case R.id.option_panel_background_button:
				Log.d(TAG, "Called Options Panel");
				if (toolbarExpand) {
					collapseAllSubPanel();
					findViewById(R.id.option_panel_background_button).setVisibility(View.GONE);
					findViewById(R.id.rm_list_layout_option).setVisibility(View.GONE);
					toolbarExpand = false;
					// 刷新列表
					resetList();
					new LoadRMList().execute(++page);
				} else {
					findViewById(R.id.option_panel_background_button).setVisibility(View.VISIBLE);
					findViewById(R.id.rm_list_layout_option).setVisibility(View.VISIBLE);
					toolbarExpand = true;
				}
				break;
			}
		}
	}
	
	private void collapseAllSubPanel() {
		findViewById(R.id.rm_list_layout_type).setVisibility(View.GONE);
		findViewById(R.id.rm_list_layout_filter).setVisibility(View.GONE);
		findViewById(R.id.rm_list_layout_sort).setVisibility(View.GONE);
		typeExpand = false;
		filterExpand = false;
		sortExpand = false;
	}
	
	private void refreshTypeDisplay() {
		findViewById(R.id.rm_list_checked_type_commercial).setVisibility(DefaultSettings.isCategoryCommercialChecked() ? View.VISIBLE : View.INVISIBLE);
		findViewById(R.id.rm_list_checked_type_personal).setVisibility(DefaultSettings.isCategoryPersonalChecked() ? View.VISIBLE : View.INVISIBLE);
		findViewById(R.id.rm_list_checked_type_friend).setVisibility(DefaultSettings.isCategoryFriendChecked() ? View.VISIBLE : View.INVISIBLE);
		findViewById(R.id.rm_list_checked_type_help).setVisibility(DefaultSettings.isCategoryHelpChecked() ? View.VISIBLE : View.INVISIBLE);
		Log.d(TAG, "refreshTypeDisp:accessed");
	}
	
	private void refreshFilterDisplay() {
		findViewById(R.id.rm_list_checked_filter_picture).setVisibility(filterPictureChecked ? View.VISIBLE : View.INVISIBLE);
		findViewById(R.id.rm_list_checked_filter_voice).setVisibility(filterVoiceChecked ? View.VISIBLE : View.INVISIBLE);
	}
	
	private void refreshSortDisplay() {
		findViewById(R.id.rm_list_checked_sort_distance).setVisibility(View.INVISIBLE);
		findViewById(R.id.rm_list_checked_sort_heat).setVisibility(View.INVISIBLE);
		findViewById(R.id.rm_list_checked_sort_time).setVisibility(View.INVISIBLE);
		if (sortMethod.equals(SORT_BY_DISTANCE))
			findViewById(R.id.rm_list_checked_sort_distance).setVisibility(View.VISIBLE);
		else if (sortMethod.equals(SORT_BY_HEAT))
			findViewById(R.id.rm_list_checked_sort_heat).setVisibility(View.VISIBLE);
		else if (sortMethod.equals(SORT_BY_TIME))
			findViewById(R.id.rm_list_checked_sort_time).setVisibility(View.VISIBLE);
	}
	
	private class ToolBarListener implements OnClickListener {

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.rm_list_button_option_type:
				if (!typeExpand) {
					collapseAllSubPanel();
					findViewById(R.id.rm_list_layout_type).setVisibility(View.VISIBLE);
					typeExpand = true;
					refreshTypeDisplay();
				} else 
					collapseAllSubPanel();
				break;
			case R.id.rm_list_button_option_filter:
				if (!filterExpand) {
					collapseAllSubPanel();
					findViewById(R.id.rm_list_layout_filter).setVisibility(View.VISIBLE);
					filterExpand = true;
					refreshFilterDisplay();
				} else 
					collapseAllSubPanel();
				break;
			case R.id.rm_list_button_option_sort:
				if (!sortExpand) {
					collapseAllSubPanel();
					findViewById(R.id.rm_list_layout_sort).setVisibility(View.VISIBLE);
					sortExpand = true;
					refreshSortDisplay();
				} else 
					collapseAllSubPanel();
				break;
			}
		}
	}
	
	private class TypeListener implements OnClickListener {

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.rm_list_button_option_type_commercial:
				DefaultSettings.setCategoryCommercialChecked(!DefaultSettings.isCategoryCommercialChecked());
				Log.d(TAG, "TypeListener:commercial"+String.valueOf(DefaultSettings.isCategoryCommercialChecked()));
				break;
			case R.id.rm_list_button_option_type_personal:
				DefaultSettings.setCategoryPersonalChecked(!DefaultSettings.isCategoryPersonalChecked());
				break;
			case R.id.rm_list_button_option_type_friend:
				DefaultSettings.setCategoryFriendChecked(!DefaultSettings.isCategoryFriendChecked());
				break;
			case R.id.rm_list_button_option_type_help:
				DefaultSettings.setCategoryHelpChecked(!DefaultSettings.isCategoryHelpChecked());
				break;
			}
			refreshTypeDisplay();
		}
	}
	
	private class FilterListener implements OnClickListener {

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.rm_list_button_option_filter_picture:
				filterPictureChecked = !filterPictureChecked;
				break;
			case R.id.rm_list_button_option_filter_voice:
				filterVoiceChecked = !filterVoiceChecked;
				break;
			}
			messageFilter = ",";
			if (filterPictureChecked)
				messageFilter += DefaultSettings.HAS_PICTURE;
			if (filterVoiceChecked)
				messageFilter += DefaultSettings.HAS_VOICE;
			refreshFilterDisplay();
		}
	}
	
	private class SortListener implements OnClickListener {

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.rm_list_button_option_sort_distance:
				sortMethod = SORT_BY_DISTANCE;
				break;
			case R.id.rm_list_button_option_sort_heat:
				sortMethod = SORT_BY_HEAT;
				break;
			case R.id.rm_list_button_option_sort_time:
				sortMethod = SORT_BY_TIME;
				break;
			}
			refreshSortDisplay();
		}
	}
	
	private class RMItemOnClickListener implements OnItemClickListener {

		@SuppressWarnings("unchecked")
		@Override
		public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
			if (v != loadMoreView) {
				ListView listView = (ListView)parent;
				HashMap<String, Object> map = (HashMap<String, Object>) listView.getItemAtPosition(position);
				String mid = (String) map.get("MID");
				// 已获取MID，连接DM
				Log.d(TAG, "MID:" + mid + " Intent -> DM");
				Intent intent = new Intent();
				Bundle bundle = new Bundle();
				bundle.putString("mId", mid);
				intent.putExtras(bundle);
				intent.setClass(RMListActivity.this, DetailMessageActivity.class);
				RMListActivity.this.startActivity(intent);
				//Toast.makeText(getApplicationContext(), messageID, Toast.LENGTH_SHORT).show();
			}
		}
	}
	
	private class RMListOnScrollListener implements OnScrollListener {
		
		private int lastVisibleIndex;
		
		@Override
		public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
			lastVisibleIndex = firstVisibleItem + visibleItemCount - 1;
		}

		@Override
		public void onScrollStateChanged(AbsListView view, int scrollState) {
			/*  不在加载 	 未加载完全		滚动停止时		最后可见项目为最后一项（加载View)  */
			if (!loading && !loadFinish && scrollState == OnScrollListener.SCROLL_STATE_IDLE && lastVisibleIndex == rmListAdapter.getCount()) {
				if (rmListTerm != null) 
					rmListTerm.clear();
				new LoadRMList().execute(++page);
			}
			
		}
	}
	
	private final ViewBinder VIEW_BINDER = new ViewBinder() {
		
		@Override
		public boolean setViewValue(View view, Object data, String textRepresentation) {
			
			if (view.getId() == R.id.rm_list_avatar) {
				String uid = (String) data;
				((ImageView) view).setImageResource(R.drawable.default_avatar);
				//处理头像
				String url = new String(DefaultSettings.REMOTE_STORE_URL_PREFIX_AVATAT + uid + ".jpg");
				imageLoader.DisplayImage(url, (ImageView)view, ImageLoader.REQUIRE_SIZE_AVATAR);  
				return true;
				
			} else if (view.getId() == R.id.rm_list_hasPicture || view.getId() == R.id.rm_list_hasVoice) {
				String flag = (String) data;
				if (flag.equals("true"))
					((ImageView) view).setVisibility(View.VISIBLE);
				else 
					((ImageView) view).setVisibility(View.INVISIBLE);
				return true;
				
			} else if (view.getId() == R.id.rm_list_ribbon) {
				String category = (String) data;
				if (category.equals("1,")) 
					((ImageView) view).setBackgroundResource(R.drawable.icon_color_bule_info_infolist);
				else if (category.equals("2,")) 
					((ImageView) view).setBackgroundResource(R.drawable.icon_color_green_info_infolist);
				else if (category.equals("3,")) 
					((ImageView) view).setBackgroundResource(R.drawable.icon_color_red_info_infolist);
				else if (category.equals("4,")) 
					((ImageView) view).setBackgroundResource(R.drawable.icon_color_orange_info_infolist);
				return true;
				
			} else if (view.getId() == R.id.rm_list_distance) {
				String distance = (String)data;
				Log.d(TAG, "Got Distance "+ distance);
				((TextView) view).setText(distance + "公里");
				return true;
			}
			
			return false;
		}
	};
	

	
	private class LoadRMList extends AsyncTask<Integer, Integer, String> {
			
			@Override
			protected void onPreExecute() {
				Log.d(TAG, "(AT-Pre) : Start");
				loading = true;
				loadingIndicator.setVisibility(View.VISIBLE);
	            loadStatus.setText("努力加载中...");
			}
			
			@Override
			protected String doInBackground(Integer... parms) {
				Log.d(TAG, "(AT-Main) ListType : " + listType);
				rmListTerm = null;
				switch (listType) {
				case FAVORITE:
					rmListTerm = CommunityFactory.getFavoriteList(targetUID, DefaultSettings.getLocalUid(), String.valueOf(latitudeCurrent), String.valueOf(longitudeCurrent), DefaultSettings.encodeCategoryState(), sortMethod, messageFilter, String.valueOf(parms[0]));
					break;
				case PUBLISHED:
					rmListTerm = CommunityFactory.getPublishedList(targetUID, DefaultSettings.getLocalUid(), String.valueOf(latitudeCurrent), String.valueOf(longitudeCurrent), DefaultSettings.encodeCategoryState(), sortMethod, messageFilter, String.valueOf(parms[0]));
					break;
				case COMMENTED:
					rmListTerm = CommunityFactory.getCommentedList(targetUID, DefaultSettings.getLocalUid(), String.valueOf(latitudeCurrent), String.valueOf(longitudeCurrent), DefaultSettings.encodeCategoryState(), sortMethod, messageFilter, String.valueOf(parms[0]));
					break;
				case SEARCH:
					rmListTerm = CommunityFactory.getMessageList(DefaultSettings.getLocalUid(), String.valueOf(latitudeCurrent), String.valueOf(longitudeCurrent), String.valueOf(latitudeTarget), String.valueOf(longitudeTarget), String.valueOf(geoShift), DefaultSettings.getDefaultCategory(), index, sortMethod, messageFilter, String.valueOf(parms[0]));
					break;
				default:
					rmListTerm = CommunityFactory.getMessageList(DefaultSettings.getLocalUid(), String.valueOf(latitudeCurrent), String.valueOf(longitudeCurrent), String.valueOf(latitudeTarget), String.valueOf(longitudeTarget), String.valueOf(geoShift), DefaultSettings.getDefaultCategory(), index, sortMethod, messageFilter, String.valueOf(parms[0]));
					break; 
				}
				Log.d(TAG, "(AT-Main) Data Got");
				return "Done";
			}
			
			@Override
			protected void onProgressUpdate(Integer... values) {
				super.onProgressUpdate(values);
			}
			
			@Override
			protected void onPostExecute(String result) {
				Log.d(TAG, "(AT-Post) : Start");
				rmSource.addAll(rmListTerm);
				for (RoughMessage item : rmListTerm) {
					if (!(item.getId() == null)) {
						HashMap<String, Object> map = new HashMap<String, Object>();
						String content = item.getContent();
						int indexBegin = content.indexOf("<image", 0);
						int indexEnd = content.indexOf("/>", indexBegin) + 2;
						while (indexBegin != -1 && indexEnd != -1) {
							int pos_start = (indexBegin == 0) ? 0 : (indexBegin - 1);
							int pos_end = (content.length() == indexEnd) ? indexEnd : indexEnd + 1;
							String label = content.substring(pos_start, pos_end);
							content = content.replace(label, "");
							indexBegin = content.indexOf("<image", 0);
							indexEnd = content.indexOf("/>", indexBegin) + 2;
						}
						indexBegin = content.indexOf("<audio", 0);
						indexEnd = content.indexOf("/>", indexBegin) + 2;
						if (indexBegin != -1 && indexEnd != -1) {
							String label = content.substring(indexBegin, indexEnd);
							content = content.replace(label, "");
						}
						map.put("AVATAR", item.getIdAuthor());
						map.put("UID", item.getIdAuthor());
						map.put("MID", item.getId());
						map.put("CATEGORY", item.getIdCategory()+",");
						map.put("CONTENT", content);
						map.put("DATE", item.getDateCreate());
						map.put("HAS_PICTURE", String.valueOf(item.hasPicture()));
						map.put("HAS_VOICE", String.valueOf(item.hasVoice()));
						map.put("AMOUNT_HELPFUL", String.valueOf(item.getAmountHelpful()));
						map.put("DISTANCE", String.valueOf((int)item.getDitance()));
						rmData.add(map);
					} else 
						loadFinish = true;
				}
				Log.d(TAG, "(AT-Post) : Reload Data Done");
				loading = false;
				loadStatus.setText(loadFinish ? "已经是最后一页" : "加载更多");
	            loadingIndicator.setVisibility(View.GONE);
	            rmListAdapter.notifyDataSetChanged();
				Log.d(TAG, "(AT-Post) : Done");
			}
	}
}
