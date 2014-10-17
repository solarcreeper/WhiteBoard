package com.WhiteBoard.WhiteBoard;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.WhiteBoard.BaseClasses.DefaultSettings;
import com.WhiteBoard.BaseClasses.Md5;
import com.WhiteBoard.BaseClasses.User;
import com.WhiteBoard.Send.CommunityFactory;

public class RegisterActivity extends Activity {
	
	//装载控件的对象
	private EditText phoneOrMail;
	private EditText nickname;
	private EditText password;
	private EditText checkPassword;
	private ImageButton registerBtn;
	private TextView turnToLoginBtn;
	private ImageButton backBtn;
	private ImageButton ignoreFirstUse;
	private TextView buttonLabel;
	private ProgressBar indicator;
	
	private final static String TAG = "RegisterActivity";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_register);
		
		//几个注册需要填写的值
		phoneOrMail = (EditText) findViewById(R.id.firstbuttonRegister);
		nickname = (EditText) findViewById(R.id.secondbuttonRegister);
		password = (EditText) findViewById(R.id.thirdbuttonRegister);
		checkPassword = (EditText) findViewById(R.id.fourthbuttonRegister);
		
		//返回，注册，转向登陆的按钮
		registerBtn = (ImageButton) findViewById(R.id.registerbuttonRegister);
		turnToLoginBtn = (TextView) findViewById(R.id.linktextRegister);
		backBtn = (ImageButton) findViewById(R.id.backRegister);
		ignoreFirstUse = (ImageButton) findViewById(R.id.ignorRegisterFirstUse);
		
		buttonLabel = (TextView)findViewById(R.id.registercontentRegister);
		indicator = (ProgressBar)findViewById(R.id.indicator);
		
		//首次使用
		backBtn.setVisibility(DefaultSettings.firstUse ? View.INVISIBLE : View.VISIBLE);
		ignoreFirstUse.setVisibility(DefaultSettings.firstUse ? View.VISIBLE : View.INVISIBLE);
		
		
		//注册Button的点击事件。匿名内部类实现。
		registerBtn.setOnClickListener(new OnClickListener(){
			
			@Override
			public void onClick(View arg0) {
				new Register().execute();
			}
		});
		
		
		//这里有一些问题，程序员们来解决一下吧。
		backBtn.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				RegisterActivity.this.finish();
			}
			
		});
		
		//转向注册的按钮
		turnToLoginBtn.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				Intent intent = new Intent();
				intent.setClass(RegisterActivity.this, LogInActivity.class);
				RegisterActivity.this.startActivity(intent);
				RegisterActivity.this.finish();
			}
			
		});
		
		ignoreFirstUse.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				if(DefaultSettings.firstUse == true){
					Intent intent = new Intent(RegisterActivity.this, MyMapActivity.class);
					startActivity(intent);
					RegisterActivity.this.finish();
				}
				
			}
			
		});
		
		if(DefaultSettings.firstUse == true){
			Intent intent = new Intent(RegisterActivity.this, WelcomeActivity.class);
			startActivity(intent);
		}
	}
	
	//这是实现异步注册的类
	class Register extends AsyncTask<Integer, Integer, String>{

		private int response;
		
		@Override
		protected void onPreExecute() {
			registerBtn.setClickable(false);
			buttonLabel.setText("正在注册...");
			indicator.setVisibility(View.VISIBLE);
			
		}
		
		//申请并处理注册
		@Override
		protected String doInBackground(Integer... params) {
			String strNickname = nickname.getText().toString();
			Log.d(TAG, "Info Collected, Username: "+strNickname);
			String strPhoneOrMail = phoneOrMail.getText().toString();
			Log.d(TAG, "Info Collected, Phone or Mail: "+strPhoneOrMail);
			String strPassword = password.getText().toString();
			Log.d(TAG, "Info Collected, Password: "+strPassword);
			String strCheckPassword = checkPassword.getText().toString();
			Log.d(TAG, "Info Collected, Password 2nd: "+strCheckPassword);
			response = register(strNickname, strPassword, strCheckPassword, strPhoneOrMail);
			
			//存放本地User给系统。
			if(response == 1){
				String uId = CommunityFactory.getRegisterId();
				Log.d(TAG, "Result, UID: "+uId);
				User user = CommunityFactory.getLocalUserInfo(uId, "0");
				DefaultSettings.setLoggedIn(uId);
				DefaultSettings.setLocalUser(user);
				DefaultSettings.lastCredential = strNickname;
			}
			
			return "Done";
		}
		
		//处理注册的结果
		@Override
		protected void onPostExecute(String result) {
			registerBtn.setClickable(true);
			buttonLabel.setText("注册");
			indicator.setVisibility(View.GONE);
			
			switch(response){
			case 0:
				Toast.makeText(RegisterActivity.this, "注册信息不完整",Toast.LENGTH_SHORT).show();
				break;
			case 1:
				Toast.makeText(RegisterActivity.this, "注册成功",Toast.LENGTH_SHORT).show();
				//条状到我的地图
				Intent intent = new Intent();
				intent.setClass(RegisterActivity.this, MyMapActivity.class);
				RegisterActivity.this.startActivity(intent);
				RegisterActivity.this.finish();
				break;
			case 1004:
				Toast.makeText(RegisterActivity.this, "密码错误",Toast.LENGTH_SHORT).show();
				break;
			case 1005:
				Toast.makeText(RegisterActivity.this, "用户不存在",Toast.LENGTH_SHORT).show();
				break;
			case -1:
				Toast.makeText(RegisterActivity.this, "密码不一致",Toast.LENGTH_SHORT).show();
				break;
			case -3:
				Toast.makeText(RegisterActivity.this, "不能使用null作为用户名",Toast.LENGTH_SHORT).show();
				break;
			default:
				Toast.makeText(RegisterActivity.this, "程序有问题啦",Toast.LENGTH_SHORT).show();
			}
			super.onPostExecute(result);
		}
		
	}
	
	//这个是把注册的函数给封装了
	public static int register(String nickname, String password, String checkPassword, String emailOrPhone){
		if(nickname.equals("null")){
			return -3;
		}else{
			if(nickname.equals("")||password.equals("")||checkPassword.equals("")||emailOrPhone.equals("")){
				return 0;
			}else{
				if(!(password.equals(checkPassword))){
					return -1;
				}else{
					if(emailOrPhone.indexOf(String.valueOf("@")) == -1){
						String returnCode = CommunityFactory.sendRegister(nickname, Md5.getMd5(password), null, emailOrPhone,"" + DefaultSettings.newUpdateStamp());
						if(returnCode.equals("1004")){
							Log.d(TAG, "Result : FAILED");
							return 1004;
						}else if(returnCode.equals("1005")){
							Log.d(TAG, "Result : FAILED");
							return 1005;
						}
						else{
							Log.d(TAG, "Result : SUCCESSED");
							return 1;
						}
					}else{
						String returnCode = CommunityFactory.sendRegister(nickname, Md5.getMd5(password), emailOrPhone, null, "" + DefaultSettings.newUpdateStamp());
						if(returnCode.equals("1004")){
							Log.d(TAG, "Result : FAILED");
							return 1004;
						}else if(returnCode.equals("1005")){
							Log.d(TAG, "Result : FAILED");
							return 1005;
						}
						else{
							Log.d(TAG, "Result : SUCCESSED");
							return 1;
						}
					}	
				}
			}
		}
	}

}
