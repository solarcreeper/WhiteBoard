/**
 * @author solarcreeper;
 * 登陆实现
 * 
 */
package com.WhiteBoard.Send;

import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import android.util.Log;


import com.WhiteBoard.BaseClasses.GlobalParameter;


public class LoginImp {
	//发送登陆消息
	private static ArrayList<String> loginResult;
	
	public static ArrayList<String> getSendLogin(){
		return LoginImp.loginResult;
	}

	public static void login(String Identifier, String passwordMD5){
		ArrayList<String> loginReturn = new ArrayList<String>();
		ArrayList<String> keysName    = new ArrayList<String>();
		ArrayList<String> keysValue   = new ArrayList<String>();
		keysName.add("Identifier");
		keysName.add("PasswordMD5");
		keysValue.add(Identifier);
		keysValue.add(passwordMD5);
		String url = GlobalParameter.LOGIN;
		String result = BaseFunctions.httpConnGBK(keysName, keysValue, url, 1);
		
		try{
			JSONTokener jsonParser = new JSONTokener(result);  
		    JSONObject temp = (JSONObject) jsonParser.nextValue(); 
		    loginReturn.add(temp.getString("uId"));
		    loginReturn.add(temp.getString("ErrCode"));
		}
		catch(JSONException e){
			Log.d("json error",e.getMessage().toString());
		}
		LoginImp.loginResult = loginReturn;
	}
}
