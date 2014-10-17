package com.WhiteBoard.Send;

import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import android.util.Log;


import com.WhiteBoard.BaseClasses.GlobalParameter;

public class SettingsImp {
	
	public static String followStatus(String uid, String tid, String status){
		String rec = "";
		ArrayList<String> keysName  = new ArrayList<String>();
		ArrayList<String> keysValue = new ArrayList<String>();
		keysName.add("uId");
		keysName.add("tId");
		keysName.add("FollowStatus");
		keysValue.add(uid);
		keysValue.add(tid);
		keysValue.add(status);
		String url    = GlobalParameter.ALTER_FOLLOWSTATUS;
		String result = BaseFunctions.httpConnGBK(keysName, keysValue, url);
		
		try{
			JSONTokener jsonParser = new JSONTokener(result);  
			JSONObject temp = (JSONObject) jsonParser.nextValue();  
			rec				= temp.getString("ErrCode").toUpperCase();
			Log.d("result", rec);
		}
		catch(JSONException e){
			Log.d("json error",e.getMessage().toString());
		}
		return rec;
	}
	
	//更改自己是否可被关注
	private static String followable;
	
	public static String followable(){
		return SettingsImp.followable;
	}
	
	public static String followable(String uid, String followable, String updateStamp){
		String rec = "";
		ArrayList<String> keysName  = new ArrayList<String>();
		ArrayList<String> keysValue = new ArrayList<String>();
		keysName.add("uId");
		keysName.add("Followable");
		keysName.add("UpdateStamp");
		keysValue.add(uid);
		keysValue.add(followable);
		keysValue.add(updateStamp);
		String url    = GlobalParameter.ALTER_FOLLOWATTRIBUTE;
		String result = BaseFunctions.httpConnGBK(keysName, keysValue, url);
		
		try{
			JSONTokener jsonParser  = new JSONTokener(result);  
			JSONObject temp 		= (JSONObject) jsonParser.nextValue();  
			rec      	    		= temp.getString("ErrCode").toUpperCase();
			SettingsImp.followable  = temp.getString("Followable");
		}
		catch(JSONException e){
			Log.d("json error",e.getMessage().toString());
		}
		return rec;
	}
	
	//发送修改privacylevel消息
	private static String privacyLevel;
	
	public static String privacyLevel(){
		return SettingsImp.privacyLevel;
	}
	
	public static String privacyLevel(String uid, String privacylevel, String updateStamp){
		String rec = "";
		ArrayList<String> keysName  = new ArrayList<String>();
		ArrayList<String> keysValue = new ArrayList<String>();
		keysName.add("uId");
		keysName.add("PrivacyLevel");
		keysName.add("UpdateStamp");
		keysValue.add(uid);
		keysValue.add(privacylevel);
		keysValue.add(updateStamp);
		String 			  url 		= GlobalParameter.ALTER_PRIVACYLEVEL;
		String 			  result    = BaseFunctions.httpConnGBK(keysName, keysValue, url);
		
		try{
			JSONTokener jsonParser = new JSONTokener(result);  
			JSONObject temp			 = (JSONObject) jsonParser.nextValue();  
			rec  					 = temp.getString("ErrCode");
			SettingsImp.privacyLevel = temp.getString("PrivacyLevel");
		}
		catch(JSONException e){
			Log.d("json error",e.getMessage().toString());
		}
		return rec;
	}
	
	//修改默认显示字条类型消息
	public static String defaultMsgType(String uid, String defaultmsgtype, String updateStamp){
		String rec = "";
		ArrayList<String> keysName  = new ArrayList<String>();
		ArrayList<String> keysValue = new ArrayList<String>();
		keysName.add("uId");
		keysName.add("DefaultMsgType");
		keysName.add("UpdateStamp");
		keysValue.add(uid);
		keysValue.add(defaultmsgtype);
		keysValue.add(updateStamp);
		String            url       = GlobalParameter.SET_DEFAULTCATEGORY;
		String 			  result 	= BaseFunctions.httpConnGBK(keysName, keysValue, url);
		try{
			JSONTokener jsonParser = new JSONTokener(result);  
			JSONObject temp = (JSONObject) jsonParser.nextValue();  
			rec 			= temp.getString("ErrCode").toUpperCase();
			Log.d("status", rec);
		}
		catch(JSONException e){
			Log.d("json error",e.getMessage().toString());
		}
		return rec;
	}
	
	//添加/取消收藏纸条
	public static String favorite(String uid, String mid, boolean favorited){
		String rec = "";
		ArrayList<String> keysName  = new ArrayList<String>();
		ArrayList<String> keysValue = new ArrayList<String>();
		keysName.add("uId");
		keysName.add("mId");
		keysName.add("Favorited");
		keysValue.add(uid);
		keysValue.add(mid);
		keysValue.add(String.valueOf(favorited));
		String 			  url  		= GlobalParameter.ADDFAVOURITE;
		String 			  result 	= BaseFunctions.httpConnGBK(keysName, keysValue, url);
		
		try{
			JSONTokener jsonParser = new JSONTokener(result);  
			JSONObject temp = (JSONObject) jsonParser.nextValue();  
			rec             = temp.getString("ErrCode").toUpperCase();
			Log.d("status", rec);
		}
		catch(JSONException e){
			Log.d("json error",e.getMessage().toString());
		}
		return rec;
	}
	
	//支持&反对
	public static String evaluation(String uid, String mid, String evaluation){
		String rec = "";
		ArrayList<String> keysName  = new ArrayList<String>();
		ArrayList<String> keysValue = new ArrayList<String>();
		keysName.add("uId");
		keysName.add("mId");
		keysName.add("Evaluation");
		keysValue.add(uid);
		keysValue.add(mid);
		keysValue.add(evaluation);
		String 			  url 		= GlobalParameter.USEREVALUATION;
		String 			  result 	= BaseFunctions.httpConnGBK(keysName, keysValue, url);
		
		try{
			JSONTokener jsonParser = new JSONTokener(result);  
			JSONObject temp = (JSONObject) jsonParser.nextValue();  
			rec			    = temp.getString("ErrCode").toUpperCase();
			Log.d("status", rec);
		}
		catch(JSONException e){
			Log.d("json error",e.getMessage().toString());
		}
		return rec;
	}
}
