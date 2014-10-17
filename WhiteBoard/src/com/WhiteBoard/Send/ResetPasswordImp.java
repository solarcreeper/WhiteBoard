package com.WhiteBoard.Send;

import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import android.util.Log;

import com.WhiteBoard.BaseClasses.GlobalParameter;

public class ResetPasswordImp {
	//发送修改密码消息
	public static String resetPasswordImp(String uid, String oldPassword, String newPassword){
		String      resetResult = "";
		ArrayList<String> keysName   		  = new ArrayList<String>();
		ArrayList<String> keysValue  		  = new ArrayList<String>();
		keysName.add("uId");
		keysName.add("OldPasswordMD5");
		keysName.add("NewPasswordMD5");
		keysValue.add(uid);
		keysValue.add(oldPassword);
		keysValue.add(newPassword);
		String url    = GlobalParameter.ALTER_PASSWORD;
		String result = BaseFunctions.httpConnGBK(keysName, keysValue, url);
	
		try{
			JSONTokener jsonParser  = new JSONTokener(result);  
			JSONObject  temp        = (JSONObject) jsonParser.nextValue();  
		    resetResult             = temp.getString("ErrCode");
			Log.d("resetResult", resetResult);
		}
		catch(JSONException e){
			Log.d("json error",e.getMessage().toString());
		}

		return resetResult;
	}
}
