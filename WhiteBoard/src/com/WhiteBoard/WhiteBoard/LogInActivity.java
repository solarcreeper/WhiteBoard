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
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.WhiteBoard.BaseClasses.DefaultSettings;
import com.WhiteBoard.BaseClasses.Md5;
import com.WhiteBoard.BaseClasses.User;
import com.WhiteBoard.Media.ImageLoader;
import com.WhiteBoard.Send.CommunityFactory;

public class LogInActivity extends Activity {
	
	private static final String TAG = "LogInActivity";
	
	//���ڴ洢�ؼ���Ϣ��
	private EditText mUsername;
	private EditText mPassword;
	private ImageButton mLogin;
	private TextView buttonLabel;
	private ProgressBar indicator;
	private TextView mTurntoRegister;
	private ImageButton mBack;
	private ImageView avatar;
	private ImageLoader imageLoader; 
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_log_in);
		
		//��½��Ҫ��ֵ
		mUsername = (EditText) findViewById(R.id.firstbuttonLogIn);
		mPassword = (EditText) findViewById(R.id.secondbuttonLogIn);
		
		//������ť
		mLogin = (ImageButton) findViewById(R.id.loginbuttonLogIn);
		mTurntoRegister = (TextView) findViewById(R.id.linktextLogIn);
		mBack = (ImageButton) findViewById(R.id.backLogIn);
		
		buttonLabel = (TextView)findViewById(R.id.logincontentLogIn);
		indicator = (ProgressBar)findViewById(R.id.indicator);
		
		avatar = (ImageView)findViewById(R.id.usertileLogIn);
		imageLoader = new ImageLoader(getApplicationContext());
		imageLoader.clearCache();
		
		//�״�ʹ��
		mBack.setVisibility(DefaultSettings.firstUse ? View.INVISIBLE : View.VISIBLE);
		
		mUsername.setText(DefaultSettings.lastCredential);
		String url = new String(DefaultSettings.REMOTE_STORE_URL_PREFIX_AVATAT + DefaultSettings.lastUid + ".jpg");
		imageLoader.DisplayImage(url, (ImageView)avatar, ImageLoader.REQUIRE_SIZE_AVATAR);  
		
		
		//���ǵ�½�ĵ���¼����������ڲ���ʵ�֡�
		mLogin.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				//������Ҫʵ���˵�½��
				new Login().execute();
			}
			
		});
		
		//��ת��ע�����ĵ����¼���
		mTurntoRegister.setOnClickListener(new OnClickListener(){
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent();
				intent.setClass(LogInActivity.this,RegisterActivity.class);
				LogInActivity.this.startActivity(intent);
				LogInActivity.this.finish();
			}
			
		});
		
		//���Ƿ��ذ�ť�¼�
		mBack.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				LogInActivity.this.finish();
			}
			
		});
	}
	
	class Login extends AsyncTask<Integer, Integer, String>{
		
		//��ʱ�洢����ֵ
		private int response;
		
		@Override
		protected void onPreExecute() {
			mLogin.setClickable(false);
			buttonLabel.setText("��¼��...");
			indicator.setVisibility(View.VISIBLE);
			
		}
		
		//���𷢳�����
		@Override
		protected String doInBackground(Integer... params) {
			
			String strUsername = mUsername.getText().toString();
			String strPassword = mPassword.getText().toString();
			String strPasswordMd5 = Md5.getMd5(strPassword);
			response = login(strUsername, strPasswordMd5);
			
			if(response == 0){
				//��������Ҫ�õ�һ��User�����ʵ���������setting�ĵط���
				String uId = CommunityFactory.getLoginId();
				Log.d(TAG, "Result, UID: "+uId);
				User user = CommunityFactory.getLocalUserInfo(uId, "0");
				DefaultSettings.setLoggedIn(uId);
				DefaultSettings.setLocalUser(user);
				DefaultSettings.lastCredential = strUsername;
			}
			
			return "Done";
		}
		
		//�����жϷ���ֵ
		@Override
		protected void onPostExecute(String result) {
			mLogin.setClickable(true);
			buttonLabel.setText("��½");
			indicator.setVisibility(View.GONE);
			
			switch(response){
			case 0:
				Toast.makeText(LogInActivity.this, "��½�ɹ�", Toast.LENGTH_SHORT).show();
				DefaultSettings.lastCredential = mUsername.getText().toString();
				Intent intent = new Intent();
				intent.setClass(LogInActivity.this,MyMapActivity.class);
				LogInActivity.this.startActivity(intent);
				LogInActivity.this.finish();
				break;
			case -2:
				Toast.makeText(LogInActivity.this, "��Ϣ������", Toast.LENGTH_SHORT).show();
				break;
			case -1:
				Toast.makeText(LogInActivity.this, "���ݿ����", Toast.LENGTH_SHORT).show();
				break;
			case 1004:
				Toast.makeText(LogInActivity.this, "�������", Toast.LENGTH_SHORT).show();
				break;
			case 1005:
				Toast.makeText(LogInActivity.this, "�û�������", Toast.LENGTH_SHORT).show();
				break;
			default:
				Toast.makeText(LogInActivity.this, "����Ա��Ĵ���д���˰�", Toast.LENGTH_SHORT).show();
			}
			super.onPostExecute(result);
		}
		
	}

	//����������ʾ��½�ĺ�����
	public static int login(String uId, String password){
		
		if(uId.equals("")||password.equals("")) {
			return -2;
		} else {
			String returnCode = CommunityFactory.sendLogin(uId, password);
			if(returnCode.equals("-1")) {
				return -1;
			}else if(returnCode.equals("1004")) {
				return 1004;
			}else if(returnCode.equals("1005")){
				return 1005;
			}else{
				return 0;
			}
			
		}
	}
}
