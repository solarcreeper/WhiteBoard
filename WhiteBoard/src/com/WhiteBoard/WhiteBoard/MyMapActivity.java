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
	
	// ����ǵ�ͼ����ͼ��
	private static MapView mMapView;

	// ��Ҫ������ǰٶȶ�λSDK�Ķ�λ�ͻ��ˣ�û��������
	private LocationClient mLocationClient;

	// ��һ�ζ�λ��Ҫ��boolean
	private boolean isFirst;
	
	// �ж��ǲ��Ƕ�λ��
	private boolean isLocated;

	// ��Щ�ǿ��Ա����ʵ���Ϣ�����忴��������
//	public static BDLocation mLocation; // ����ǰٶ��ṩ��Location���󣬺��и�����Ϣ��

	// ���ӵ�ͼƬ
	private Drawable marker;

	// �����ǵĸ����Ｏ������
	private MyOverlay mOverlay;

	//һ���Ǵ洢��ͬ�����ֽ��
	private ArrayList<ArrayList<RoughMessage>> mListOfRMList;
	
	//����Ǵ洢ˢ����Ҫ�ľ�γ��������Ϣ
	private ArrayList<GeoPoint> nailPoints = new ArrayList<GeoPoint>();
	
	//��λͼ�������
	private LocationData locData;
	private MyLocationOverlay mLocationOverlay;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_map);
		
		DefaultSettings.firstUse = false;

		// ��ʼ��boolean
		isFirst = true;
		isLocated = false;

		// �·�����������
		buttonprofileMap = (ImageButton) findViewById(R.id.buttonprofileMap);
		buttonfollowMap = (ImageButton) findViewById(R.id.buttonfollowMap);
		buttonsetMap = (ImageButton) findViewById(R.id.buttonsetMap);

		// ���� ���� ��ֽ�� ��λ
		searchMap = (ImageButton) findViewById(R.id.searchMap);
		refreshMap = (ImageButton) findViewById(R.id.refreshMap);
		buttonaddMap = (ImageButton) findViewById(R.id.buttonaddMap);
		buttonLocateMap = (ImageButton)findViewById(R.id.buttonLocateMap);

		// ������
		searchBar = (EditText) findViewById(R.id.search_input);
		searchBar.setOnKeyListener(new SearchInputListener());

		// ����¼�
		buttonprofileMap.setOnClickListener(new NavigationListener());
		buttonfollowMap.setOnClickListener(new NavigationListener());
		buttonsetMap.setOnClickListener(new NavigationListener());
		buttonaddMap.setOnClickListener(new ButtonAddMapOnClickListener());
		buttonLocateMap.setOnClickListener(new LocateOnClickListener());
		refreshMap.setOnClickListener(new RefreshListener());
		searchMap.setOnClickListener(new SearchListener());

		// ��δ���������ǿ�����ͼ�����࣬Ȼ��ʵ������ͼ��
		WhiteBoardApp app = (WhiteBoardApp) (this.getApplication());
		if (app.mBMapMan == null) {
			app.mBMapMan = new BMapManager(getApplication());
			app.mBMapMan.init(app.mStrKey, new WhiteBoardApp.WhiteBoardListener());
		}
		app.mBMapMan.start();
		mMapView = (MapView) findViewById(R.id.mymap);
		mMapView.setBuiltInZoomControls(false);
		
		mLocationClient = new LocationClient(this);

		// ���������λSDK
		this.start();

		// �ö���ͼƬȥ�滻�ɡ�
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

	// ��Ҫ����start�󼴿ɻ�����ݣ�ÿʮ��ˢ��һ�Ρ�
	public void start() {
		if (mLocationClient != null) {
			// ��Ҫһ��Listenerʵ��BDLocationListener�ӿڲ��Ҹ�дOnReceiveLocation�����Ϳ��Ի����������ꡣ
			mLocationClient.registerLocationListener(new BDLocationListener() {

				@Override
				public void onReceiveLocation(BDLocation location) {
					if (location == null) {
						Toast.makeText(MyMapActivity.this, "��½��Ϣ������", Toast.LENGTH_SHORT).show();
					} else {
						// ����Ϳ��Ի�ö�λ��Ϣ��һ������ࡣ
//						mLocation = location;
						// �����洢��γ��
						DefaultSettings.setLocalPosition(location.getLatitude(), location.getLongitude());
						
						// ��һ�ζ�λ�Ļ����Ͱѵ�ͼŲ����ǰλ��
						if (isFirst || isLocated) {
							MapController mc = mMapView.getController();
							mc.setCenter(new GeoPoint((int) (location.getLatitude() * 1E6), (int) (location.getLongitude() * 1E6)));
							//ֻ�ڵ�һ�β����õõ�13
							if(isFirst){
								//��һ�ε����š�
								mc.setZoom(DefaultSettings.zoomLevel);
								
								//����϶�λ��־
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
						
						// ÿ�ζ�λ���޸Ķ�λ��İ�����
						locData.latitude = location.getLatitude();
						locData.longitude = location.getLongitude();
						mLocationOverlay.setData(locData);
						
						// ��������綨λ���Ϳ��Ի��������Ϣ��
						if (location.getLocType() == BDLocation.TypeNetWorkLocation) {
							StringBuffer sb = new StringBuffer(256);
							// һ������
							/*
							 * sb.append("\nʡ��");
							 * sb.append(location.getProvince());
							 * sb.append("\n�У�"); sb.append(location.getCity());
							 * sb.append("\n��/�أ�");
							 * sb.append(location.getDistrict());
							 * sb.append("\naddr : ");
							 */
							sb.append(location.getAddrStr());

							// ���ﴢ��λ����Ϣ
							DefaultSettings.setAddressCurrent(sb.toString());
						}
						
						mMapView.refresh();
					}
				}

				// �����д���ù�
				@Override
				public void onReceivePoi(BDLocation arg0) {}

			});

			// �򿪿ͻ��˲������ÿͻ��˵����ԣ�������λ�ģ���ˢ��Ƶ�ʣ���
			setLocationOption(mLocationClient);
			mLocationClient.start();

		} else {
			// ����Ա���Լ��һ�¿ͻ����ǲ���û��ʵ��������������
		}
	}

	// ����һ����������,�������ö�λ��Ϣ��
	private void setLocationOption(LocationClient client) {
		LocationClientOption option = new LocationClientOption();
		option.setCoorType("bd09ll"); // ������������
		option.setAddrType("all"); // ���õ�ַ��Ϣ��������Ϊ��all��ʱ�е�ַ��Ϣ��Ĭ���޵�ַ��Ϣ
		option.setScanSpan(5000); // ���ö�λģʽ��С��1����һ�ζ�λ;���ڵ���1����ʱ��λ
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

	// ��������ֽ�����¼�
	private class ButtonAddMapOnClickListener implements OnClickListener {

		@Override
		public void onClick(View v) {
			if(DefaultSettings.isLoggedIn() == true){
				
				//�����ҵĵ�ǰ��ͼ��Ϣ
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
				Toast.makeText(getApplicationContext(), "��û�е�¼��", Toast.LENGTH_SHORT).show();
			}
			
		}

	}
	
	// ��λ��ť����¼�
	private class LocateOnClickListener implements OnClickListener {

		@Override
		public void onClick(View v) {
			isLocated = true;
			mLocationClient.requestLocation();
		}
		
	}

	/* ����Navigation�������������finish()���� */
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
					
					//��ͬ�����ò�ͬ�Ķ���
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
					// ����itemλ��
					pop.hidePop();
					GeoPoint p = new GeoPoint(mCurItem.getPoint()
							.getLatitudeE6() + 5000, mCurItem.getPoint()
							.getLongitudeE6() + 5000);
					mCurItem.setGeoPoint(p);
					mOverlay.updateItem(mCurItem);
					mMapView.refresh();
				} else if (index == 2) {
					// ����ͼ��
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
