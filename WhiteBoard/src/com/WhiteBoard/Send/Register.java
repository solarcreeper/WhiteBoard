package com.WhiteBoard.Send;

import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import com.WhiteBoard.BaseClasses.GlobalParameter;

import android.util.Log;

public class Register{
	private static ArrayList<String> RegisterResult = new ArrayList<String>();
	
	public static ArrayList<String> getRegisterResult(){
		return Register.RegisterResult;
	}
	
	public static void register(String nickname, String password, String email, String phone, String updateStamp){
		ArrayList<String> RegisterReturn = new ArrayList<String>();
		ArrayList<String> keysName   	 = new ArrayList<String>();
		ArrayList<String> keysValue      = new ArrayList<String>();
		keysName.add("Nickname");
		keysName.add("Password");
		keysName.add("Email");
		keysName.add("Phone");
		keysName.add("UpdateStamp");
		keysValue.add(nickname);
		keysValue.add(password);
		keysValue.add(email);
		keysValue.add(phone);
		keysValue.add(updateStamp);
		String url    = GlobalParameter.REGISTER;
		String result = BaseFunctions.httpConnGBK(keysName, keysValue, url);	
	
		try{
			JSONTokener jsonParser = new JSONTokener(result);  
		    JSONObject  temp       = (JSONObject) jsonParser.nextValue();  
		    RegisterReturn.add(temp.getString("uId"));
		    RegisterReturn.add(temp.getString("ErrCode"));
		}
		catch(JSONException e){
			Log.d("json error",e.getMessage().toString());
		}
		Register.RegisterResult = RegisterReturn;
	}
}
