package com.WhiteBoard.WhiteBoard;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.WhiteBoard.Algorithm.MessageDivision;
import com.WhiteBoard.BaseClasses.DefaultSettings;
import com.WhiteBoard.BaseClasses.RoughMessage;
import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.BMapManager;
import com.baidu.mapapi.map.ItemizedOverlay;
import com.baidu.mapapi.map.LocationData;
import com.baidu.mapapi.map.MapController;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MyLocationOverlay;
import com.baidu.mapapi.map.OverlayItem;
import com.baidu.platform.comapi.basestruct.GeoPoint;

public class MyMapActivity extends Activity {

	private final String TAG = "MyMapActivity";

	private ImageButton buttonprofileMap;
	private ImageButton buttonfollowMap;
	private ImageButton buttonsetMap;
	private ImageButton searchMap;
	private ImageButton refreshMap;
	private ImageButton buttonaddMap;
	private ImageButton buttonLocateMap;

	private EditText searchBar;
	private boolean searchBarExpand = false;
	
	// 这个是地图的视图类
	private static MapView mMapView;

	// 重要！这个是百度定位SDK的定位客户端，没有他不行
	private LocationClient mLocationClient;

	// 第一次定位需要的boolean
	private boolean isFirst;
	
	// 判断是不是定位哈
	private boolean isLocated;

	// 这些是可以被访问的信息，具体看名字啦。
//	public static BDLocation mLocation; // 这个是百度提供的Location对象，含有各种信息。

	// 钉子的图片
	private Drawable marker;

	// 钉子们的覆盖物集合啦；
	private MyOverlay mOverlay;

	//一个是存储不同区域的纸条
	private ArrayList<ArrayList<RoughMessage>> mListOfRMList;
	
	//这个是存储刷新需要的经纬度坐标信息
	private ArrayList<GeoPoint> nailPoints = new ArrayList<GeoPoint>();
	
	//定位图标的数据
	private LocationData locData;
	private MyLocationOverlay mLocationOverlay;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_map);
		
		DefaultSettings.firstUse = false;

		// 初始化boolean
		isFirst = true;
		isLocated = false;

		// 下方的三个导航
		buttonprofileMap = (ImageButton) findViewById(R.id.buttonprofileMap);
		buttonfollowMap = (ImageButton) findViewById(R.id.buttonfollowMap);
		buttonsetMap = (ImageButton) findViewById(R.id.buttonsetMap);

		// 搜索 更新 发纸条 定位
		searchMap = (ImageButton) findViewById(R.id.searchMap);
		refreshMap = (ImageButton) findViewById(R.id.refreshMap);
		buttonaddMap = (ImageButton) findViewById(R.id.buttonaddMap);
		buttonLocateMap = (ImageButton)findViewById(R.id.buttonLocateMap);

		// 搜索栏
		searchBar = (EditText) findViewById(R.id.search_input);
		searchBar.setOnKeyListener(new SearchInputListener());

		// 点击事件
		buttonprofileMap.setOnClickListener(new NavigationListener());
		buttonfollowMap.setOnClickListener(new NavigationListener());
		buttonsetMap.setOnClickListener(new NavigationListener());
		buttonaddMap.setOnClickListener(new ButtonAddMapOnClickListener());
		buttonLocateMap.setOnClickListener(new LocateOnClickListener());
		refreshMap.setOnClickListener(new RefreshListener());
		searchMap.setOnClickListener(new SearchListener());

		// 这段代码的作用是开启地图管理类，然后实例化地图。
		WhiteBoardApp app = (WhiteBoardApp) (this.getApplication());
		if (app.mBMapMan == null) {
			app.mBMapMan = new BMapManager(getApplication());
			app.mBMapMan.init(app.mStrKey, new WhiteBoardApp.WhiteBoardListener());
		}
		app.mBMapMan.start();
		mMapView = (MapView) findViewById(R.id.mymap);
		mMapView.setBuiltInZoomControls(false);
		
		mLocationClient = new LocationClient(this);

		// 这个开启定位SDK
		this.start();

		// 用钉子图片去替换吧。
		marker = getResources().getDrawable(R.drawable.icon_marka);
		
		mOverlay = new MyOverlay(marker, mMapView);
	}

	@Override
	protected void onDestroy() {
		mMapView.destroy();
		mLocationClient.stop();
		super.onDestroy();
	}

	@Override
	protected void onPause() {
		mMapView.onPause();
		mLocationClient.stop();
		DefaultSettings.savePrefs(getApplicationContext()); // save prefs
		super.onPause();
	}

	@Override
	protected void onResume() {
		mMapView.onResume();
		mLocationClient.start();
		super.onResume();
	}

	// 重要！！start后即可获得数据，每十秒刷新一次。
	public void start() {
		if (mLocationClient != null) {
			// 需要一个Listener实现BDLocationListener接口并且覆写OnReceiveLocation方法就可以获得其地理坐标。
			mLocationClient.registerLocationListener(new BDLocationListener() {

				@Override
				public void onReceiveLocation(BDLocation location) {
					if (location == null) {
						Toast.makeText(MyMapActivity.this, "登陆信息不完整", Toast.LENGTH_SHORT).show();
					} else {
						// 这里就可以获得定位信息的一个打包类。
//						mLocation = location;
						// 这里会存储经纬度
						DefaultSettings.setLocalPosition(location.getLatitude(), location.getLongitude());
						
						// 第一次定位的话，就把地图挪到当前位置
						if (isFirst || isLocated) {
							MapController mc = mMapView.getController();
							mc.setCenter(new GeoPoint((int) (location.getLatitude() * 1E6), (int) (location.getLongitude() * 1E6)));
							//只在第一次才设置得到13
							if(isFirst){
								//第一次的缩放。
								mc.setZoom(DefaultSettings.zoomLevel);
								
								//添加上定位标志
								mLocationOverlay = new MyLocationOverlay(mMapView);
								locData = new LocationData();
								locData.latitude = location.getLatitude();
								locData.longitude = location.getLongitude();
								mLocationOverlay.setData(locData);
								mMapView.getOverlays().add(mLocationOverlay);
							}
							isFirst = false;
							isLocated = false;
						}
						
						// 每次定位都修改定位点的绑定数据
						locData.latitude = location.getLatitude();
						locData.longitude = location.getLongitude();
						mLocationOverlay.setData(locData);
						
						// 如果是网络定位，就可以获得如下信息。
						if (location.getLocType() == BDLocation.TypeNetWorkLocation) {
							StringBuffer sb = new StringBuffer(256);
							// 一行足矣
							/*
							 * sb.append("\n省：");
							 * sb.append(location.getProvince());
							 * sb.append("\n市："); sb.append(location.getCity());
							 * sb.append("\n区/县：");
							 * sb.append(location.getDistrict());
							 * sb.append("\naddr : ");
							 */
							sb.append(location.getAddrStr());

							// 这里储存位置信息
							DefaultSettings.setAddressCurrent(sb.toString());
						}
						
						mMapView.refresh();
					}
				}

				// 这个覆写不用管
				@Override
				public void onReceivePoi(BDLocation arg0) {}

			});

			// 打开客户端并且设置客户端的属性，包括定位的！！刷新频率！！
			setLocationOption(mLocationClient);
			mLocationClient.start();

		} else {
			// 程序员可以检查一下客户端是不是没有实例化啊？？？、
		}
	}

	// 这是一个辅助函数,用于设置定位信息。
	private void setLocationOption(LocationClient client) {
		LocationClientOption option = new LocationClientOption();
		option.setCoorType("bd09ll"); // 设置坐标类型
		option.setAddrType("all"); // 设置地址信息，仅设置为“all”时有地址信息，默认无地址信息
		option.setScanSpan(5000); // 设置定位模式，小于1秒则一次定位;大于等于1秒则定时定位
		client.setLocOption(option);
	}

	private class RefreshListener implements OnClickListener {

		@Override
		public void onClick(View v) {
			// Refresh tells this asynctask to refresh the map;
			new Display().execute("Refresh");
		}
	}

	private class SearchListener implements OnClickListener {

		@Override
		public void onClick(View v) {
			findViewById(R.id.searchLayout).setVisibility(
					searchBarExpand ? View.GONE : View.VISIBLE);
			searchBarExpand = !searchBarExpand;
		}

	}

	private class SearchInputListener implements OnKeyListener {

		@Override
		public boolean onKey(View v, int keyCode, KeyEvent event) {
			if (keyCode == KeyEvent.KEYCODE_ENTER
					&& event.getAction() == KeyEvent.ACTION_DOWN) {

				String searchKey = searchBar.getText().toString();
				double rangeLatitude = (mMapView.getLatitudeSpan() / 1E6) *10;
				double rangeLongitude = (mMapView.getLongitudeSpan() / 1E6) *10;
				
				GeoPoint center = mMapView.getMapCenter();
				
				if(rangeLatitude > rangeLongitude){
					RMListActivity.setSearchList(center.getLatitudeE6() / 1E6, center.getLongitudeE6() / 1E6, rangeLatitude / 2, searchKey);
				}else{
					RMListActivity.setSearchList(center.getLatitudeE6() / 1E6, center.getLongitudeE6() / 1E6, rangeLongitude / 2, searchKey);
				}
				Intent intent = new Intent();
				intent.setClass(MyMapActivity.this, RMListActivity.class);
				startActivity(intent);
				return true;
			}
			return false;
		}

	}

	// 这个是添加纸条的事件
	private class ButtonAddMapOnClickListener implements OnClickListener {

		@Override
		public void onClick(View v) {
			if(DefaultSettings.isLoggedIn() == true){
				
				//存入我的当前地图信息
				DefaultSettings.currentZoomLevel = mMapView.getZoomLevel();
				DefaultSettings.currentGeoPoint = mMapView.getMapCenter();
				
				PublishActivity.isPosted = false;
				PublishActivity.picFiles.clear();
				PublishActivity.voiFile = null;
				Intent intent = new Intent();
				intent.setClass(MyMapActivity.this, PublishActivity.class);
				startActivity(intent);	
			}
			else
			{
				Toast.makeText(getApplicationContext(), "还没有登录呐", Toast.LENGTH_SHORT).show();
			}
			
		}

	}
	
	// 定位按钮点击事件
	private class LocateOnClickListener implements OnClickListener {

		@Override
		public void onClick(View v) {
			isLocated = true;
			mLocationClient.requestLocation();
		}
		
	}

	/* 整合Navigation监听器，添加了finish()方法 */
	private class NavigationListener implements OnClickListener {
		@Override
		public void onClick(View v) {
			Intent intent = new Intent();
			switch (v.getId()) {
			case R.id.buttonprofileMap:
				if (DefaultSettings.isLoggedIn()) {
					ProfileActivity.preInitialize(DefaultSettings.getLocalUid(), DefaultSettings.getLocalUserName());
					intent.setClass(MyMapActivity.this, ProfileActivity.class);
				} else {
					intent.setClass(MyMapActivity.this, LogInActivity.class);
				}
				break;
			case R.id.buttonfollowMap:
				if (DefaultSettings.isLoggedIn()) {
					intent.setClass(MyMapActivity.this, FollowActivity.class);
				} else {
					intent.setClass(MyMapActivity.this, LogInActivity.class);
				}
				break;
			case R.id.buttonsetMap:
				intent.setClass(MyMapActivity.this, SetActivity.class);
				break;
			}
			MyMapActivity.this.startActivity(intent);
			if (DefaultSettings.isLoggedIn()) {
				finish();
			}
		}
	}

	class Display extends AsyncTask<String, Integer, String> {
		
		@Override
		protected String doInBackground(String... method) {
			
			mListOfRMList = MessageDivision.groupDisplay(
					getApplicationContext(), mMapView,
					DefaultSettings.getLocalUid(),
					mMapView.getMapCenter().getLatitudeE6() / 1E6 + "",
					mMapView.getMapCenter().getLongitudeE6() / 1E6 + "",
					DefaultSettings.getDefaultCategory(), method);
			return "Done";
		}

		@Override
		protected void onPostExecute(String result) {
			
			int blocks = mListOfRMList.size();
			mMapView.getOverlays().clear();
			mLocationOverlay = new MyLocationOverlay(mMapView);
			locData = new LocationData();
			locData.latitude = DefaultSettings.getLatitudeCurrent();
			locData.longitude = DefaultSettings.getLongitudeCurrent();
			mLocationOverlay.setData(locData);
			mMapView.getOverlays().add(mLocationOverlay);
			ArrayList<OverlayItem> mItems = new ArrayList<OverlayItem>();
			mOverlay.removeAll();
			
			nailPoints.clear();
			
			for (int i = 0; i < blocks; i++) {

				// Get geography statistis using random number to show chaotic
				// messages.
				
				//System.out.println("size of the " + i + "block is " + mListOfRMList.get(i).size());
				
				if(mListOfRMList.get(i).size() > 0){
					double longitude = mListOfRMList.get(i).get(0)
							.getLongitudeExist();
					double latitude = mListOfRMList.get(i).get(0)
							.getLatitudeExist();
					
					GeoPoint geo = new GeoPoint((int) (latitude * 1E6), (int) (longitude * 1E6));
					OverlayItem tempItem = new OverlayItem(geo, "", "");
					
					//不同类型用不同的钉子
					String strCategory = mListOfRMList.get(i).get(0).getIdCategory() + ",";
					
					if(strCategory.equals("1,")){
						tempItem.setMarker(getResources().getDrawable(R.drawable.icon_nail_blue));
					}
					if(strCategory.equals("2,")){
						tempItem.setMarker(getResources().getDrawable(R.drawable.icon_nail_green));
					}
					if(strCategory.equals("3,")){
						tempItem.setMarker(getResources().getDrawable(R.drawable.icon_nail_red));
					}
					if(strCategory.equals("4,")){
						tempItem.setMarker(getResources().getDrawable(R.drawable.icon_nail_orange));
					}
						
					mItems.add(tempItem);
					nailPoints.add(geo);
					
				}
				
			}
			
			for(int j = 0; j < mItems.size(); j++){
				mOverlay.addItem(mItems.get(j));
			}
			
			mMapView.getOverlays().add(mOverlay);
			
			mMapView.refresh();

			super.onPostExecute(result);
		}

	}

	public class MyOverlay extends ItemizedOverlay<OverlayItem> {
		
//		PopupOverlay pop = new PopupOverlay(mMapView, new PopClickListener());
		
		public MyOverlay(Drawable defaultMarker, MapView mapView) {
			super(defaultMarker, mapView);
		}

		@Override
		public boolean onTap(int index) {
/*
			View viewCache = getLayoutInflater().inflate(
					R.layout.custom_text_view, null);
			View popupInfo = (View) viewCache.findViewById(R.id.popinfo);
			View popupLeft = (View) viewCache.findViewById(R.id.popleft);
			View popupRight = (View) viewCache.findViewById(R.id.popright);
			TextView popupText = (TextView) viewCache
					.findViewById(R.id.textcache);

			OverlayItem item = getItem(index);
			popupText.setText(getItem(index).getTitle());
			Bitmap[] bitMaps = { BMapUtil.getBitmapFromView(popupLeft),
					BMapUtil.getBitmapFromView(popupInfo),
					BMapUtil.getBitmapFromView(popupRight) };
			pop.showPopup(bitMaps, item.getPoint(), 32);
*/
			GeoPoint geo = nailPoints.get(index);
			
			// get mapview arguments
			double rangeLatitude = (mMapView.getLatitudeSpan() / 1E6) * 10;
			double rangeLongitude = (mMapView.getLongitudeSpan() / 1E6) * 10;
			// get window arguments
			WindowManager wm = (WindowManager) getApplicationContext().getSystemService(Context.WINDOW_SERVICE);
			@SuppressWarnings("deprecation")
			int pxWidth = wm.getDefaultDisplay().getWidth();
			@SuppressWarnings("deprecation")
			int pxHeight = wm.getDefaultDisplay().getHeight();
			int dpWidth = (int) (pxWidth / 1.5 + 0.5);
			int dpHeight = (int) (pxHeight / 1.5 + 0.5) - 112;
			int widthBlock = (int) (dpWidth / 48);
			int heightBlock = (int) (dpHeight / 48);
			// get a block's range
			double lab = (rangeLatitude / heightBlock);
			double lob = (rangeLongitude / widthBlock);
			
			System.out.println("latitude is " + geo.getLatitudeE6() / 1E6);
			System.out.println("longitude is " + geo.getLongitudeE6() / 1E6);
			System.out.println("lab is " + lab);
			System.out.println("lob is " + lob);
			
			if(lab > lob){
				RMListActivity.setNormalList(geo.getLatitudeE6() / 1E6, geo.getLongitudeE6() / 1E6, lab / 2);
			}else{
				RMListActivity.setNormalList(geo.getLatitudeE6() / 1E6, geo.getLongitudeE6() / 1E6, lob / 2);
			}
			
			Intent intent = new Intent(MyMapActivity.this, RMListActivity.class);
			startActivity(intent);
			
			return true;
		}

		@Override
		public boolean onTap(GeoPoint pt, MapView mMapView) {
			return false;
		}
/*		
		class PopClickListener implements PopupClickListener{
			@Override
			public void onClickedPopup(int index) {
				if (index == 0) {
					// 更新item位置
					pop.hidePop();
					GeoPoint p = new GeoPoint(mCurItem.getPoint()
							.getLatitudeE6() + 5000, mCurItem.getPoint()
							.getLongitudeE6() + 5000);
					mCurItem.setGeoPoint(p);
					mOverlay.updateItem(mCurItem);
					mMapView.refresh();
				} else if (index == 2) {
					// 更新图标
					mCurItem.setMarker(getResources().getDrawable(
							R.drawable.nav_turn_via_1));
					mOverlay.updateItem(mCurItem);
					mMapView.refresh();
				}
			}
		}
*/
	}
}
