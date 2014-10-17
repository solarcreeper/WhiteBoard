package com.WhiteBoard.WhiteBoard;

import android.os.Bundle;
import android.app.Activity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;

public class HelpActivity extends Activity {
	
	private ImageButton naviBack;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_help);
		
		naviBack = (ImageButton)findViewById(R.id.backHelp);
		naviBack.setOnClickListener(new TitleBarListener());
	}
	
	private class TitleBarListener implements OnClickListener {

		@Override
		public void onClick(View v) {
			finish();		
		}
	}

}
