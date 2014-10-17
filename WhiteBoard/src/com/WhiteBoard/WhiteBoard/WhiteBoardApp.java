package com.WhiteBoard.WhiteBoard;

import android.app.Application;
import android.widget.Toast;

import com.baidu.mapapi.BMapManager;
import com.baidu.mapapi.MKGeneralListener;
import com.baidu.mapapi.map.MKEvent;


public class WhiteBoardApp extends Application {
	
	static WhiteBoardApp mApp;
	
	//百度MapAPI的管理类和百度定位SDK的管理类。
	BMapManager mBMapMan = null;
	
	/**********************************************
	 *  授权Key, 请更换电脑时更换此key!!万分重要  *
	 **********************************************/
	String mStrKey = "9D2AE8DA5FE9B9675399E6F0764E3193052BB4C8";
	boolean m_bKeyRight = true;	// 授权Key正确，验证通过
	
	//初始化BMapManager类的第二个参数，用来处理网络异常事件。
	static class WhiteBoardListener implements MKGeneralListener {
		@Override
		public void onGetNetworkState(int iError) {
			Toast.makeText(WhiteBoardApp.mApp.getApplicationContext(), "网络异常，请检查", Toast.LENGTH_SHORT).show();
		}

		@Override
		public void onGetPermissionState(int iError) {
			if (iError ==  MKEvent.ERROR_PERMISSION_DENIED) {
				// 授权Key错误 
				// 方便调试人员，用后可以删除
				Toast.makeText(WhiteBoardApp.mApp.getApplicationContext(), "错误的授权Key",Toast.LENGTH_SHORT).show();
				WhiteBoardApp.mApp.m_bKeyRight = false;
			}
		}
	}
	
	@Override
    public void onCreate() {
		mApp = this;
		mBMapMan = new BMapManager(this);
		boolean isSuccess = mBMapMan.init(this.mStrKey, new WhiteBoardListener());
		if (isSuccess) {
		}
		else {
		    // 地图sdk初始化失败，不能使用sdk
			Toast.makeText(WhiteBoardApp.mApp.getApplicationContext(), "地图初始化失败",Toast.LENGTH_SHORT).show();
		}
		super.onCreate();
	}

	//这个覆写是为了处理地图API时用的。
	@Override
	public void onTerminate() {
		if (mBMapMan != null) {
			mBMapMan.destroy();
			mBMapMan = null;
		}
		super.onTerminate();
	}
}
