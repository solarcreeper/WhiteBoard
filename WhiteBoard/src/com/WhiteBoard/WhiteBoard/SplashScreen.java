package com.WhiteBoard.WhiteBoard;

import com.WhiteBoard.BaseClasses.DefaultSettings;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.app.Activity;
import android.content.Intent;

public class SplashScreen extends Activity {
	
	private static final String TAG = "SplashScreen";
	private static final int SHOW_TIME_MIN = 1200;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_splash);
		
		new AsyncTask<Void, Void, Integer>() {
			 
            @Override
            protected Integer doInBackground(Void... params) {
                long startTime = System.currentTimeMillis();
            	getInitialized();
            	long loadingTime = System.currentTimeMillis() - startTime;
                if (loadingTime < SHOW_TIME_MIN) {
                    try {
                        Thread.sleep(SHOW_TIME_MIN - loadingTime);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                return 0;
            }
 
            @Override
            protected void onPostExecute(Integer result) {
            	Intent intent = new Intent();
            	// Check is first use?
        		if (DefaultSettings.firstUse) {
        			Log.d(TAG, "App first use!");
        			//TODO 跳转新手指南	
        			intent.setClass(SplashScreen.this, RegisterActivity.class);
        		} else {
        			intent.setClass(SplashScreen.this, MyMapActivity.class);
        		}
        		startActivity(intent);
    			finish();
 
            };
        }.execute(new Void[]{});
    }
	
	private void getInitialized() {
		//额外的初始化工作
		// LoadPrefs
		if (!DefaultSettings.prefsLoaded) {
			DefaultSettings.loadPrefs(getApplicationContext());
			DefaultSettings.prefsLoaded = true;
			Log.d(TAG, "Loading prefs: OK");
		}
	}
		
}
