package com.WhiteBoard.WhiteBoard;

import android.app.Application;
import android.widget.Toast;

import com.baidu.mapapi.BMapManager;
import com.baidu.mapapi.MKGeneralListener;
import com.baidu.mapapi.map.MKEvent;


public class WhiteBoardApp extends Application {
	
	static WhiteBoardApp mApp;
	
	//�ٶ�MapAPI�Ĺ�����Ͱٶȶ�λSDK�Ĺ����ࡣ
	BMapManager mBMapMan = null;
	
	/**********************************************
	 *  ��ȨKey, ���������ʱ������key!!�����Ҫ  *
	 **********************************************/
	String mStrKey = "9D2AE8DA5FE9B9675399E6F0764E3193052BB4C8";
	boolean m_bKeyRight = true;	// ��ȨKey��ȷ����֤ͨ��
	
	//��ʼ��BMapManager��ĵڶ����������������������쳣�¼���
	static class WhiteBoardListener implements MKGeneralListener {
		@Override
		public void onGetNetworkState(int iError) {
			Toast.makeText(WhiteBoardApp.mApp.getApplicationContext(), "�����쳣������", Toast.LENGTH_SHORT).show();
		}

		@Override
		public void onGetPermissionState(int iError) {
			if (iError ==  MKEvent.ERROR_PERMISSION_DENIED) {
				// ��ȨKey���� 
				// ���������Ա���ú����ɾ��
				Toast.makeText(WhiteBoardApp.mApp.getApplicationContext(), "�������ȨKey",Toast.LENGTH_SHORT).show();
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
		    // ��ͼsdk��ʼ��ʧ�ܣ�����ʹ��sdk
			Toast.makeText(WhiteBoardApp.mApp.getApplicationContext(), "��ͼ��ʼ��ʧ��",Toast.LENGTH_SHORT).show();
		}
		super.onCreate();
	}

	//�����д��Ϊ�˴����ͼAPIʱ�õġ�
	@Override
	public void onTerminate() {
		if (mBMapMan != null) {
			mBMapMan.destroy();
			mBMapMan = null;
		}
		super.onTerminate();
	}
}
