package com.WhiteBoard.WhiteBoard;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.WhiteBoard.BaseClasses.DefaultSettings;
import com.WhiteBoard.BaseClasses.DetailMessage;
import com.WhiteBoard.Send.CommunityFactory;
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

public class PositionActivity extends Activity{

	private MapView mMapView;
	private double LongitudeExist = 300;
	private double LatitudeExist = 300;
	private String content;
	private double latitudeCreate = DefaultSettings.getLatitudeCurrent();
	private double longitudeCreate = DefaultSettings.getLongitudeCurrent();
	private int privacyLevel;
	private String category;

	private ImageButton backBtn;
	private ImageButton postBtn;
	private ImageButton locateBtn;
	private ProgressBar indicator;
	
	private LocationClient mLocationClient;
	
	private MapController mc;
	
	private Drawable marker = null;
	
	private LocationData locData;
	private MyLocationOverlay mLocationOverlay;
	
	private boolean isFirst = true;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_position);

		backBtn = (ImageButton) findViewById(R.id.PositionBack);
		postBtn = (ImageButton) findViewById(R.id.PositionPut);
		indicator = (ProgressBar)findViewById(R.id.indicator);
		locateBtn = (ImageButton) findViewById(R.id.buttonLocateMap);
		
		WhiteBoardApp app = (WhiteBoardApp) (this.getApplication());
		if (app.mBMapMan == null) {
			app.mBMapMan = new BMapManager(getApplication());
			app.mBMapMan.init(app.mStrKey, new WhiteBoardApp.WhiteBoardListener());
		}

		app.mBMapMan.start();
		mMapView = (MapView) findViewById(R.id.mymap2);
		mMapView.setBuiltInZoomControls(false);
		
		//单击覆盖物
		mc = mMapView.getController();
		mc.setZoom(DefaultSettings.currentZoomLevel);
		mc.setCenter(DefaultSettings.currentGeoPoint);
		
		//拿一下Publish2里面拿到的数据
		Intent intent = getIntent();
		Bundle bundle = intent.getExtras();
		content = bundle.getString("content");
		privacyLevel = bundle.getInt("privacyLevel");
		category = bundle.getString("category");
		
		if(category.equals("1,")){
			marker = getResources().getDrawable(R.drawable.icon_nail_blue);
		}else if(category.equals("2,")){
			marker = getResources().getDrawable(R.drawable.icon_nail_green);
		}else if(category.equals("3,")){
			marker = getResources().getDrawable(R.drawable.icon_nail_red);
		}else if(category.equals("4,")){
			marker = getResources().getDrawable(R.drawable.icon_nail_orange);
		}
		
		mMapView.getOverlays().add(new MyOverlay(marker, mMapView));
		
		mLocationClient = new LocationClient(this);
		
		this.start();
/*		
		mMapView.setOnTouchListener(new OnTouchListener(){
			
			private float xPrevious;
			private float yPrevious;
			private long previoustime = 0;
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				
				long time = System.currentTimeMillis();
				float x = event.getX();
				float y = event.getY();
				
				if(previoustime != 0){
					
					if((Math.abs(x - xPrevious) < 30)&&(Math.abs(y - yPrevious) < 30&&((time - previoustime) < 300)&&((time - previoustime) > 50))){
						
						ItemizedOverlay<?> mOverlay = new ItemizedOverlay<OverlayItem>(marker, mMapView);

						GeoPoint geo = mMapView.getProjection().fromPixels((int) x, (int) y);
				        OverlayItem item = new OverlayItem(geo, "item", "item");

				        mMapView.getOverlays().clear();
				        mOverlay.addItem(item);
				        mMapView.getOverlays().add(mOverlay);
				        mMapView.refresh();
				        
				        LatitudeExist = geo.getLatitudeE6() / 1E6;
				        LongitudeExist = geo.getLongitudeE6() / 1E6;
				        
					}
					
				}
				
				previoustime = time;
				xPrevious = x;
				yPrevious = y;
				
				return false;
			}
			
		});
*/	
		backBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				PositionActivity.this.finish();
			}

		});

		postBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				new RemotePublish().execute();
			}

		});
		
		locateBtn.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				mc.setCenter(new GeoPoint((int) (DefaultSettings.getLatitudeCurrent() * 1E6),(int) (DefaultSettings.getLongitudeCurrent() * 1E6) ));
			}
			
		});
		
	}
	
	class RemotePublish extends AsyncTask<Integer, Integer, Integer> {
		boolean havePos2 = false;
		String returnCode;
		@Override
		protected void onPreExecute() {
			postBtn.setVisibility(View.GONE);
			indicator.setVisibility(View.VISIBLE);
			if(LongitudeExist == 300 && LatitudeExist == 300){
				havePos2 = false;
				Toast.makeText(getApplicationContext(), "点击地图来选择一个投放地点", Toast.LENGTH_SHORT).show();
			} else {
				havePos2 = true;
				Toast.makeText(getApplicationContext(), "纸条正向目的地飞去~", Toast.LENGTH_LONG).show();
			}
		}

		@Override
		protected Integer doInBackground(Integer... params) {
			if(!havePos2){
				return 1;
			}else{
				DetailMessage dm = new DetailMessage();
				dm.setContent(content);
				dm.setPrivacyLevel(privacyLevel);
				dm.setLocationCreate(latitudeCreate, longitudeCreate);
				dm.setLocationExist(LatitudeExist, LongitudeExist);
				dm.setCategory(category);
				dm.setPictureFile(PublishActivity.picFiles);
				dm.setVoiceFile(PublishActivity.voiFile);
				
				returnCode = CommunityFactory.createMessage(dm);
				return 0;
			}
			
			
		}

		@Override
		protected void onPostExecute(Integer result) {
			postBtn.setVisibility(View.VISIBLE);
			indicator.setVisibility(View.GONE);
			
			if (result == 1) {
				
			} else if (returnCode.equals("-1")) {
				Toast.makeText(getApplicationContext(), "出错了，请重试",Toast.LENGTH_SHORT).show();
			} else {
				Toast.makeText(getApplicationContext(), "发送成功",	Toast.LENGTH_SHORT).show();
				
				//清空静态数据
				PublishActivity.picFiles.clear();
				PublishActivity.voiFile = null;
				
				
				String mId = CommunityFactory.getCreatedMsgId();
				Intent intent = new Intent();
				Bundle bundle = new Bundle();
				bundle.putString("mId", mId);
				intent.putExtras(bundle);
				intent.setClass(PositionActivity.this, DetailMessageActivity.class);
				PositionActivity.this.startActivity(intent);
				PublishActivity.isPosted = true;
				finish();
			}
			super.onPostExecute(result);
		}
		
	}
	
	class MyOverlay extends ItemizedOverlay<OverlayItem>{
		
		private boolean isFirstTap = true;
		
		private OverlayItem item = null;
		
		private ItemizedOverlay<?> mOverlay;
		
		public MyOverlay(Drawable arg0, MapView arg1) {
			super(arg0, arg1);
		}
		
		public boolean onTap(GeoPoint geo, MapView mapView){
			
			if(isFirstTap){
				mOverlay = new ItemizedOverlay<OverlayItem>(marker, mMapView);
				item = new OverlayItem(geo, "item", "item");
				mOverlay.addItem(item);
				mMapView.getOverlays().add(mOverlay);
				isFirstTap = false;
			}
			
			item = new OverlayItem(geo, "item", "item");
			mOverlay.removeAll();
			mOverlay.addItem(item);
	        mMapView.refresh();
	        
	        LatitudeExist = geo.getLatitudeE6() / 1E6;
	        LongitudeExist = geo.getLongitudeE6() / 1E6;
			
			return super.onTap(geo, mapView);
		}
		
	}
	
	public void start() {
		if (mLocationClient != null) {
			// 需要一个Listener实现BDLocationListener接口并且覆写OnReceiveLocation方法就可以获得其地理坐标。
			mLocationClient.registerLocationListener(new BDLocationListener() {

				@Override
				public void onReceiveLocation(BDLocation location) {
					if (location == null)
					{
						Toast.makeText(PositionActivity.this, "登陆信息不完整", Toast.LENGTH_SHORT).show();
					} 
					else 
					{
						DefaultSettings.setLocalPosition(location.getLatitude(), location.getLongitude());
								//添加上定位标志
						if(isFirst){
							mLocationOverlay = new MyLocationOverlay(mMapView);
							locData = new LocationData();
							locData.latitude = location.getLatitude();
							locData.longitude = location.getLongitude();
							mLocationOverlay.setData(locData);
							mMapView.getOverlays().add(mLocationOverlay);
							isFirst = false;
						}
						
						locData.latitude = location.getLatitude();
						locData.longitude = location.getLongitude();
						mLocationOverlay.setData(locData);	
					}
					
					mMapView.refresh();
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
		super.onPause();
	}

	@Override
	protected void onResume() {
		mMapView.onResume();
		mLocationClient.start();
		super.onResume();
	}
}
