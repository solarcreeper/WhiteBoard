package com.WhiteBoard.WhiteBoard;

import android.os.Bundle;
import android.app.Activity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;

public class AboutActivity extends Activity {
	
	private ImageButton naviBack;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_about);
		
		naviBack = (ImageButton)findViewById(R.id.backAbout);
		naviBack.setOnClickListener(new TitleBarListener());
	}
	
	private class TitleBarListener implements OnClickListener {

		@Override
		public void onClick(View v) {
			finish();		
		}
	}
}
