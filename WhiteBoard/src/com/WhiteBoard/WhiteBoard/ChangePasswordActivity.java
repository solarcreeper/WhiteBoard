package com.WhiteBoard.WhiteBoard;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.WhiteBoard.BaseClasses.DefaultSettings;
import com.WhiteBoard.BaseClasses.Md5;
import com.WhiteBoard.Send.CommunityFactory;

public class ChangePasswordActivity extends Activity {

	private ImageButton back;
	private EditText previous;
	private EditText current;
	private EditText checkCurrent;
	private ImageButton change;
	private TextView buttonLabel;
	private ProgressBar indicator;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_change_password);
		
		//返回按钮
		back = (ImageButton) findViewById(R.id.backChangePassword);
		
		//三个密码值
		previous = (EditText) findViewById(R.id.firstbuttonChangePassword);
		current = (EditText) findViewById(R.id.secondbuttonChangePassword);
		checkCurrent = (EditText) findViewById(R.id.thirdbuttonChangePassword);
		
		//改变按钮
		change = (ImageButton) findViewById(R.id.changepasswordbuttonChangePassword);
		buttonLabel = (TextView)findViewById(R.id.changepasswordcontentChangePassword);
		indicator = (ProgressBar)findViewById(R.id.indicator);
		
		//设置返回的按钮
		back.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				ChangePasswordActivity.this.finish();
			}
			
		});
		
		//设置改变密码按钮
		change.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				//1只是一个代入值
				new ChangePassword().execute();
			}
			
		});
	}
	
	//这个类用来实现异步修改密码
	class ChangePassword extends AsyncTask<Integer, Integer, String>{
		
		@Override
		protected void onPreExecute() {
			change.setClickable(false);
			buttonLabel.setText("正在修改...");
			indicator.setVisibility(View.VISIBLE);
			
		}
		
		//负责请求修改等。
		@Override
		protected String doInBackground(Integer... arg0) {
			
			String prePassword = previous.getText().toString();
			String currPassword = current.getText().toString();
			String checkPassword = checkCurrent.getText().toString();
			if(prePassword.equals("")||currPassword.equals("")||checkPassword.equals(""))
			{
				return "Incomplement";
			}
			else
			{
				if(currPassword.equals(checkPassword))
				{
					String returnCode = CommunityFactory.sendResetPassword(DefaultSettings.getLocalUid(), Md5.getMd5(prePassword), Md5.getMd5(currPassword));
					return returnCode;
				}
				else
				{
					return "Different";
				}
			}
		}
		
		//负责显示结果
		@Override
		protected void onPostExecute(String result) {
			change.setClickable(true);
			buttonLabel.setText("确认修改");
			indicator.setVisibility(View.GONE);
			
			if(result.equals("Incomplement"))
			{
				Toast.makeText(ChangePasswordActivity.this, "信息不完整啊", Toast.LENGTH_SHORT).show();
			}
			if(result.equals("-1"))
			{
				Toast.makeText(ChangePasswordActivity.this, "出错了，请重试", Toast.LENGTH_SHORT).show();
			}
			if(result.equals("0"))
			{
				Toast.makeText(ChangePasswordActivity.this, "修改成功", Toast.LENGTH_SHORT).show();
				ChangePasswordActivity.this.finish();
			}
			if(result.equals("1006"))
			{
				Toast.makeText(ChangePasswordActivity.this, "原密码错误", Toast.LENGTH_SHORT).show();
			}
			
			if(result.equals("Different"))
			{
				Toast.makeText(ChangePasswordActivity.this, "密码不一致", Toast.LENGTH_SHORT).show();
			}
			super.onPostExecute(result);
		}
		
	}
}
