package com.WhiteBoard.Send;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import android.util.Log;

import com.WhiteBoard.BaseClasses.RoughMessage;
import com.WhiteBoard.BaseClasses.User;
import com.WhiteBoard.BaseClasses.GlobalParameter;

public class UserInfoImp {
	//获取localuser数据
	public static User localUserInfo(String uid, String updatestamp){
		User user = null;
		ArrayList<String> keysName    = new ArrayList<String>();
		ArrayList<String> keysValue   = new ArrayList<String>();
		keysName.add("uId");
		keysName.add("UpdateStamp");
		keysValue.add(uid);
		keysValue.add(updatestamp);
		String url = GlobalParameter.GET_LOCALPROFILE;
		
		String result = BaseFunctions.httpConnUTF8(keysName, keysValue, url);
		
		try{
			JSONTokener jsonParser 			  = new JSONTokener(result);
			JSONObject temp 				  = (JSONObject)jsonParser.nextValue();
			String userName 				  = temp.getString("UserName");
			String email 					  = temp.getString("Email");
			String phone					  = temp.getString("Phone");
			String idDefaultCategory 		  = temp.getString("DefaultCategory");
			int followable_temp				  = Integer.parseInt(temp.getString("Followable"));
			boolean followable 		  		  = false;
			if(followable_temp > 0)
				followable = true;
			long updateStamp      			  = Long.parseLong(temp.getString("UpdateStamp"));
			user = new User(uid, userName, email, phone, followable, idDefaultCategory, updateStamp);
		}
		catch(JSONException e){
			Log.d("json error",e.getMessage().toString());
		}
		
		return user;
	}
	
	//获取用户关注状态
	private static String targetUserFollowable;
	
	public static String targetUserFollowable(){
		return UserInfoImp.targetUserFollowable;
	}
	
	public static String isUserFollowed(String targetuid, String localuid){
		String followed = "";
		ArrayList<String> keysName  = new ArrayList<String>();
		ArrayList<String> keysValue = new ArrayList<String>();
		keysName.add("TargetUid");
		keysName.add("LocalUid");
		keysValue.add(targetuid);
		keysValue.add(localuid);
		String url = GlobalParameter.GET_FOLLOWSTATUS;
		String result = BaseFunctions.httpConnUTF8(keysName, keysValue, url);
		try{
			JSONTokener jsonParser           = new JSONTokener(result);
			JSONObject  temp				 = (JSONObject)jsonParser.nextValue();
			followed  	                     = temp.getString("Followed").toUpperCase();
			UserInfoImp.targetUserFollowable = temp.getString("Followable").toUpperCase();
			Log.d("followed", followed);
		}
		catch(JSONException e){
			Log.d("json error",e.getMessage().toString());
		}
		return followed;
	}
	
	//获取某用户关注的人列表
	public static ArrayList<User> followingList(String uid, String page){
		ArrayList<User>   users     = new ArrayList<User>();
		ArrayList<String> keysName  = new ArrayList<String>();
		ArrayList<String> keysValue = new ArrayList<String>();
		keysName.add("uId");
		keysName.add("Page");
		keysValue.add(uid);
		keysValue.add(page);
		String url = GlobalParameter.GET_FOLLOWINGLIST;
		String result = BaseFunctions.httpConnUTF8(keysName, keysValue, url);
		try{
			JSONArray array = new JSONArray(result);
			for(int j = 0; j < array.length(); j++){
				JSONObject temp 	= (JSONObject)array.get(j);
				String     id 		= temp.getString("Id");
				String     userName = temp.getString("UserName");
				User       user     = new User(id, userName);
				users.add(user);
				Log.d("id", id);
				Log.d("username", userName);
			}
		}
		catch(JSONException e){
			Log.d("json error",e.getMessage().toString());
		}
		return users;
	}
	
	public static ArrayList<User> followerList(String uid, String page){
		ArrayList<User>   users     = new ArrayList<User>();
		ArrayList<String> keysName  = new ArrayList<String>();
		ArrayList<String> keysValue = new ArrayList<String>();
		keysName.add("uId");
		keysName.add("Page");
		keysValue.add(uid);
		keysValue.add(page);
		String url    = GlobalParameter.GET_FOLLOWERLIST;
		String result = BaseFunctions.httpConnUTF8(keysName, keysValue, url);
		
		try{
			JSONArray array = new JSONArray(result);
			for(int j = 0; j < array.length(); j++){
				JSONObject temp     = (JSONObject)array.get(j);
				String 	   id       = temp.getString("Id");
				String     userName = temp.getString("UserName");
				User 	   user     = new User(id, userName);
				users.add(user);
				Log.d("id", id);
				Log.d("username", userName);
			}
		}
		catch(JSONException e){
			Log.d("json error",e.getMessage().toString());
		}
		return users;
	}
	
	//获取某用户的收藏纸条列表
	public static ArrayList<RoughMessage> favoriteList(String targetuId, String localuId, String latitudeCurrent, String longitudeCurrent, String categorySet, String sortMethod, String messageFilter, String page ){
		ArrayList<RoughMessage> favoriteList = new ArrayList<RoughMessage>();
		ArrayList<String>       keysName    = new ArrayList<String>();
		ArrayList<String>       keysValue	= new ArrayList<String>();
		keysName.add("uId");
		keysName.add("LocalUid");
		keysName.add("LatitudeCurrent");
		keysName.add("LongitudeCurrent");
		keysName.add("CategorySet");
		keysName.add("SortMethod");
		keysName.add("MessageFilter");
		keysName.add("Page");
		keysValue.add(targetuId);
		keysValue.add(localuId);
		keysValue.add(latitudeCurrent);
		keysValue.add(longitudeCurrent);
		keysValue.add(categorySet);
		keysValue.add(sortMethod);
		keysValue.add(messageFilter);
		keysValue.add(page);
		String 					url			= GlobalParameter.GET_FAVORITELIST;
		String 					result      = BaseFunctions.httpConnUTF8(keysName, keysValue, url);
		try{
			JSONArray array = new JSONArray(result);
			for(int j = 0; j < array.length(); j++){
				JSONObject temp = (JSONObject)array.get(j);
				Log.d("temp", temp.toString());
				String  id            	 	 = temp.getString("mId");
				if(id != "null"){
					String  idAuthor 	 	 = temp.getString("IdAuthor");
					String  idCategory    	 = temp.getString("IdCategory");
					String  dateCreate    	 = temp.getString("DateCreate");
					String  content     	 = temp.getString("Content");
					int hasPicture_temp 	 = Integer.parseInt(temp.getString("HasPicture"));
					int hasVoice_temp 		 = Integer.parseInt(temp.getString("HasVoice"));
					boolean hasPicture 	  	 = false;
					boolean hasVoice  		 = false;
					if(hasPicture_temp > 0)
						hasPicture = true;
					if(hasVoice_temp > 0)
						hasVoice   = true;
					int     amountHelpful 	 = Integer.parseInt(temp.getString("AmountHelpful"));
					int     amountSpam 		 = Integer.parseInt(temp.getString("AmountSpam"));
					double  latitudeExist 	 = Double.parseDouble(temp.getString("LatitudeExist"));
					double  longitudeExist	 = Double.parseDouble(temp.getString("LongitudeExist"));
					double  distance 		 = Double.parseDouble(temp.getString("Distance"));

					RoughMessage message     = new RoughMessage(id, idAuthor, idCategory, dateCreate, content, hasPicture, hasVoice, amountHelpful, amountSpam, latitudeExist, longitudeExist, distance);
					favoriteList.add(message);
				}
				else{
					RoughMessage empty       = new RoughMessage();
					favoriteList.add(empty);
				}
			}
		}
		catch(JSONException e){
			Log.d("json error",e.getMessage().toString());
		}
		return favoriteList;
	} 
	
	//拉取某用户发布过的纸条列表
	public static ArrayList<RoughMessage> publishedList(String uId, String localuId, String latitudeCurrent, String longitudeCurrent, String categorySet, String sortMethod, String messageFilter, String page ){
		ArrayList<RoughMessage> publishedList = new ArrayList<RoughMessage>();
		ArrayList<String>       keysName      = new ArrayList<String>();
		ArrayList<String>       keysValue	  = new ArrayList<String>();
		keysName.add("uId");
		keysName.add("LocalUid");
		keysName.add("LatitudeCurrent");
		keysName.add("LongitudeCurrent");
		keysName.add("CategorySet");
		keysName.add("SortMethod");
		keysName.add("MessageFilter");
		keysName.add("Page");
		keysValue.add(uId);
		keysValue.add(localuId);
		keysValue.add(latitudeCurrent);
		keysValue.add(longitudeCurrent);
		keysValue.add(categorySet);
		keysValue.add(sortMethod);
		keysValue.add(messageFilter);
		keysValue.add(page);
		String url 	  = GlobalParameter.GET_PUBLISHEDLIST;
		String result = BaseFunctions.httpConnUTF8(keysName, keysValue, url);
		try{
			JSONArray array = new JSONArray(result);
			for(int j = 0; j < array.length(); j++){
				JSONObject temp 			 = (JSONObject)array.get(j);
				Log.d("temp", temp.toString());
				String  id            		 = temp.getString("mId");
				if(id != "null"){
					String  idAuthor 	 	 = temp.getString("IdAuthor");
					String  idCategory    	 = temp.getString("IdCategory");
					String  dateCreate    	 = temp.getString("DateCreate");
					String  content     	 = temp.getString("Content");
					int hasPicture_temp 	 = Integer.parseInt(temp.getString("HasPicture"));
					int hasVoice_temp 		 = Integer.parseInt(temp.getString("HasVoice"));
					boolean hasPicture 		 = false;
					boolean hasVoice   		 = false;
					if(hasPicture_temp > 0)
						hasPicture = true;
					if(hasVoice_temp > 0)
						hasVoice   = true;
					int     amountHelpful 	 = Integer.parseInt(temp.getString("AmountHelpful"));
					int     amountSpam 		 = Integer.parseInt(temp.getString("AmountSpam"));
					double  latitudeExist 	 = Double.parseDouble(temp.getString("LatitudeExist"));
					double  longitudeExist   = Double.parseDouble(temp.getString("LongitudeExist"));
					double  distance 		 = Double.parseDouble(temp.getString("Distance"));

					RoughMessage message     = new RoughMessage(id, idAuthor, idCategory, dateCreate, content, hasPicture, hasVoice, amountHelpful, amountSpam, latitudeExist, longitudeExist, distance);
					publishedList.add(message);
				}
				else{
					RoughMessage empty	     = new RoughMessage();
					publishedList.add(empty);
				}
			}
		}
		catch(JSONException e){
			Log.d("json error",e.getMessage().toString());
		}
		return publishedList;
	}
	
	//拉取某用户的评论过的纸条列表
	public static ArrayList<RoughMessage> commentedList(String uId, String localuId, String latitudeCurrent, String longitudeCurrent, String categorySet, String sortMethod, String messageFilter, String page){
		ArrayList<RoughMessage> commentList = new ArrayList<RoughMessage>();
		ArrayList<String>       keysName      = new ArrayList<String>();
		ArrayList<String>       keysValue	  = new ArrayList<String>();
		keysName.add("uId");
		keysName.add("LocalUid");
		keysName.add("LatitudeCurrent");
		keysName.add("LongitudeCurrent");
		keysName.add("CategorySet");
		keysName.add("SortMethod");
		keysName.add("MessageFilter");
		keysName.add("Page");
		keysValue.add(uId);
		keysValue.add(localuId);
		keysValue.add(latitudeCurrent);
		keysValue.add(longitudeCurrent);
		keysValue.add(categorySet);
		keysValue.add(sortMethod);
		keysValue.add(messageFilter);
		keysValue.add(page);
		String url    = GlobalParameter.GET_COMMENTEDLIST;
		String result = BaseFunctions.httpConnUTF8(keysName, keysValue, url);
		
		try{
			JSONArray array 				 = new JSONArray(result);
			for(int j = 0; j < array.length(); j++){
				JSONObject temp 		   	 = (JSONObject)array.get(j);
				Log.d("temp", temp.toString());
				String  id            	 	 = temp.getString("mId");
				if(id != "null"){
					String  idAuthor 	 	 = temp.getString("IdAuthor");
					String  idCategory    	 = temp.getString("IdCategory");
					String  dateCreate    	 = temp.getString("DateCreate");
					String  content     	 = temp.getString("Content");
					int hasPicture_temp 	 = Integer.parseInt(temp.getString("HasPicture"));
					int hasVoice_temp 		 = Integer.parseInt(temp.getString("HasVoice"));
					boolean hasPicture     	 = false;
					boolean hasVoice   		 = false;
					if(hasPicture_temp > 0)
						hasPicture = true;
					if(hasVoice_temp > 0)
						hasVoice   = true;
					int     amountHelpful 	 = Integer.parseInt(temp.getString("AmountHelpful"));
					int     amountSpam 		 = Integer.parseInt(temp.getString("AmountSpam"));
					double  latitudeExist 	 = Double.parseDouble(temp.getString("LatitudeExist"));
					double  longitudeExist   = Double.parseDouble(temp.getString("LongitudeExist"));
					double  distance 		 = Double.parseDouble(temp.getString("Distance"));

					RoughMessage message	 = new RoughMessage(id, idAuthor, idCategory, dateCreate, content, hasPicture, hasVoice, amountHelpful, amountSpam, latitudeExist, longitudeExist, distance);
					commentList.add(message);
				}
				else{
					RoughMessage empty		 = new RoughMessage();
					commentList.add(empty);
				}
			}
		}
		catch(JSONException e){
			Log.d("json error",e.getMessage().toString());
		}
		return commentList;
	}
	
	
}
