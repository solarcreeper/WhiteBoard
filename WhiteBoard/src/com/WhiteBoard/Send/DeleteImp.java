package com.WhiteBoard.Send;

import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import android.util.Log;

import com.WhiteBoard.BaseClasses.GlobalParameter;

public class DeleteImp {
	public static String deleteComment(String commentId, String messageId){
		String rec = "";
		ArrayList<String> keysName    = new ArrayList<String>();
		ArrayList<String> keysValue   = new ArrayList<String>();
		keysName.add("cId");
		keysName.add("mId");
		keysValue.add(commentId);
		keysValue.add(messageId);
		String url = GlobalParameter.DeleteComment;
		String result = BaseFunctions.httpConnGBK(keysName, keysValue, url);
		
		try{
			JSONTokener jsonParser = new JSONTokener(result);  
		    JSONObject temp = (JSONObject) jsonParser.nextValue(); 
		    rec = temp.getString("ErrCode");
		}
		catch(JSONException e){
			Log.d("json error",e.getMessage().toString());
		}
		return rec;
	}
	
	public static String deleteMessage(String messageId, String uid){
		String rec = "";
		ArrayList<String> keysName    = new ArrayList<String>();
		ArrayList<String> keysValue   = new ArrayList<String>();
		keysName.add("mId");
		keysName.add("uId");
		keysValue.add(messageId);
		keysValue.add(uid);
		String url = GlobalParameter.DeleteMessage;
		String result = BaseFunctions.httpConnGBK(keysName, keysValue, url);
		
		try{
			JSONTokener jsonParser = new JSONTokener(result);  
		    JSONObject temp = (JSONObject) jsonParser.nextValue(); 
		    rec = temp.getString("ErrCode");
		}
		catch(JSONException e){
			Log.d("json error",e.getMessage().toString());
		}
		return rec;
	}
}
