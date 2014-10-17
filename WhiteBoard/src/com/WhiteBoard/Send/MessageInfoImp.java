package com.WhiteBoard.Send;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import android.util.Log;

import com.WhiteBoard.BaseClasses.DetailMessage;
import com.WhiteBoard.BaseClasses.RoughMessage;
import com.WhiteBoard.BaseClasses.GlobalParameter;

public class MessageInfoImp {
	//地图视野内的钉子
	public static ArrayList<RoughMessage> pinnedList(String uId, String  latitudeTarget, String longitudeTarget,String latitudeShift, String longitudeShift, String defaultCategory ){
		 ArrayList<RoughMessage> pinnedList = new ArrayList<RoughMessage>();
		 ArrayList<String> keysName  = new ArrayList<String>();
		 ArrayList<String> keysValue = new ArrayList<String>();
		 keysName.add("uId");
		 keysName.add("LatitudeTarget");
		 keysName.add("LongitudeTarget");
		 keysName.add("LatitudeShift");
		 keysName.add("LongitudeShift");
		 keysName.add("DefaultCategory");
		 keysValue.add(uId);
		 keysValue.add(latitudeTarget);
		 keysValue.add(longitudeTarget);
		 keysValue.add(latitudeShift);
		 keysValue.add(longitudeShift);
		 keysValue.add(defaultCategory);
		 String url    = GlobalParameter.MAPINFO;
		 String result =  BaseFunctions.httpConnUTF8(keysName, keysValue, url);
		 
		 try{
				JSONArray array = new JSONArray(result);
				for(int j = 0; j < array.length(); j++){
					JSONObject temp 	 	   = (JSONObject)array.get(j);
					String     id              = temp.getString("mId");
					String     idCategory      = temp.getString("mIdCategory");
					double     latitudeExist   = Double.parseDouble(temp.getString("LatitudeExist"));
					double     longitudeExist  = Double.parseDouble(temp.getString("LongitudeExist"));

					RoughMessage message 	   = new RoughMessage(id, idCategory, latitudeExist, longitudeExist);
					pinnedList.add(message);
				}
			}
			catch(JSONException e){
				Log.d("json error",e.getMessage().toString());
			}
		 return pinnedList;
	}
	
	//纸条列表数据
	public static ArrayList<RoughMessage> messageList(String uId, String latitudeCurrent, String longitudeCurrent,String latitudeTarget, String longitudeTarget,String geoShift, String categorySet, String index, String sortMethod, String messageFilter, String page){
		ArrayList<RoughMessage> messageList = new ArrayList<RoughMessage>();
		ArrayList<String>       keysName 	= new ArrayList<String>();
		ArrayList<String> 	    keysValue 	= new ArrayList<String>();
		keysName.add("uId");
		keysName.add("LatitudeCurrent");
		keysName.add("LongitudeCurrent");
		keysName.add("LatitudeTarget");
		keysName.add("LongitudeTarget");
		keysName.add("GeoShift");
		keysName.add("CategorySet");
		keysName.add("Index");
		keysName.add("SortMethod");
		keysName.add("MessageFilter");
		keysName.add("Page");
		keysValue.add(uId);
		keysValue.add(latitudeCurrent);
		keysValue.add(longitudeCurrent);
		keysValue.add(latitudeTarget);
		keysValue.add(longitudeTarget);
		keysValue.add(geoShift);
		keysValue.add(categorySet);
		keysValue.add(index);
		keysValue.add(sortMethod);
		keysValue.add(messageFilter);
		keysValue.add(page);
		
		
		String url = GlobalParameter.GET_LIST;
		String result = BaseFunctions.httpConnUTF8(keysName, keysValue, url);
		
		try{
			JSONArray array = new JSONArray(result);
			for(int j = 0; j < array.length(); j++){
				JSONObject temp = (JSONObject)array.get(j);
				String  id            		 = temp.getString("mId");
				if(id != "null"){
					String  idAuthor 	 	 = temp.getString("IdAuthor");
					String  idCategory    	 = temp.getString("IdCategory");
					String  dateCreate    	 = temp.getString("DateCreate");
					String  content     	 = temp.getString("Content");
					int     hasPicture_temp  = Integer.parseInt(temp.getString("HasPicture"));
					int     hasVoice_temp 	 = Integer.parseInt(temp.getString("HasVoice"));
					boolean hasPicture       = false;
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
					messageList.add(message);
				}
				else{
					 RoughMessage empty      = new RoughMessage();
					 messageList.add(empty);
				}
			}
		}
		catch(JSONException e){
			Log.d("json error",e.getMessage().toString());
		}
		return messageList;
	}
	
	
	//纸条详细信息
	public  static DetailMessage detailMessage(String uid, String mid){
		DetailMessage     message   = null;
		ArrayList<String> keysName  = new ArrayList<String>();
		ArrayList<String> keysValue = new ArrayList<String>();
		keysName.add("uId");
		keysName.add("mId");
		keysValue.add(uid);
		keysValue.add(mid);
		
		String url = GlobalParameter.GET_DETAILEDMESSAGE;
		String result = BaseFunctions.httpConnUTF8(keysName, keysValue, url);
		
		try{
			JSONTokener jsonParser 			= new JSONTokener(result);  
			JSONObject  temp 				= (JSONObject) jsonParser.nextValue();  
			String      id			  		= temp.getString("Id");
			String 		idAuthor			= temp.getString("IdAuthor");
			if(idAuthor != "null"){
				String  authorUserName 		= temp.getString("AuthorUserName");
				String 	idCategory			= temp.getString("IdCategory");
				String  dateCreate			= temp.getString("DateCreate");
				String  content 			= temp.getString("Content");
				int 	amountComment 		= Integer.parseInt(temp.getString("AmountComment"));
				int 	amountHelpful		= Integer.parseInt(temp.getString("AmountHelpful"));
				int 	amountSpam			= Integer.parseInt(temp.getString("AmountSpam"));
				int	    localUserMarkStatus = Integer.parseInt(temp.getString("Evaluation"));
				double  latitudeCreate 		= Double.parseDouble(temp.getString("LatitudeCreate"));
				double  longitudeCreate 	= Double.parseDouble(temp.getString("LongitudeCreate"));
				double  latitudeExist		= Double.parseDouble(temp.getString("LatitudeExist"));
				double  longitudeExist		= Double.parseDouble(temp.getString("LongitudeExist"));
				double  distance 			= -1;
				boolean favorited	    	= false;
				String  favorited_return 	= temp.getString("Favorited");
				if(favorited_return == "true")
					favorited = true;
				
				message = new DetailMessage(id, idAuthor, authorUserName, idCategory, dateCreate, content, amountComment, amountHelpful, amountSpam, localUserMarkStatus, latitudeCreate, longitudeCreate, latitudeExist, longitudeExist, distance, favorited);
				
			}
			else{
				String  authorUserName 		= temp.getString("UserName");
				String  idCategory			= temp.getString("IdCategory");
				String  dateCreate			= temp.getString("DateCreate");
				String  content 		  	= temp.getString("Content");
				int     amountComment 	  	= 0;
				int     amountHelpful		= 0;
				int     amountSpam			= 0;
				int     localUserMarkStatus = 0;
				double  latitudeCreate 		= 0;
				double  longitudeCreate 	= 0;
				double  latitudeExist		= 0;
				double  longitudeExist		= 0;
				double  distance 			= -1;
				boolean favorited 			= false;

				message = new DetailMessage(id, idAuthor, authorUserName, idCategory, dateCreate, content, amountComment, amountHelpful, amountSpam, localUserMarkStatus, latitudeCreate, longitudeCreate, latitudeExist, longitudeExist, distance, favorited);
			}
		}
		catch(JSONException e){
			Log.d("json error",e.getMessage().toString());
		}
		return message;
	}
	
	//创建纸条
	private static String id;
	
	public static String getId(){
		return MessageInfoImp.id;
	}
	
	public static String createMessage(DetailMessage newmessage){
		String rec = "";
		String hasPicture = "0";
		String hasVoice   = "0";
		FileImp.sendFileStatus  = "0";
		FileImp.sendImageStatus = "0";
		FileImp.sendVoiceStatus = "0";
		
		//send image
		if(newmessage.getPictureFile() != null && newmessage.getPictureFile().size() != 0){
			FileImp.sendImage(newmessage.getPictureFile());
			hasPicture = "1";
			Log.d("sendimage", "pass");
		}

		//send voice
		if(newmessage.getVoiceFile() != null){
			FileImp.sendVoice(newmessage.getVoiceFile());
			hasVoice = "1";
			Log.d("sendvoice", "pass");
		}

		//send message
		if(FileImp.getSendImageStatus() == "0" && FileImp.getSendVoiceStatus() == "0"){
			String url = GlobalParameter.POST_NEWMESSAGE;
			rec		   = MessageInfoImp.message(newmessage, hasPicture, hasVoice);
		}
		return rec;
	}
	
	private static String message(DetailMessage message, String hasPicture, String hasVoice){
		String 			  rec 		= "";
		ArrayList<String> keysName  = new ArrayList<String>();
		ArrayList<String> keysValue = new ArrayList<String>();
		keysName.add("uId");
		keysName.add("cId");
		keysName.add("LatitudeCreate");
		keysName.add("LongitudeCreate");
		keysName.add("LatitudeExist");
		keysName.add("LongitudeExist");
		keysName.add("Content");
		keysName.add("PrivacyLevel");
		keysName.add("HasPicture");
		keysName.add("HasVoice");
		keysValue.add(message.getIdAuthor());
		keysValue.add(message.getIdCategory());
		keysValue.add(String.valueOf(message.getLatitudeCreate()));
		keysValue.add(String.valueOf(message.getLongitudeCreate()));
		keysValue.add(String.valueOf(message.getLatitudeExist()));
		keysValue.add(String.valueOf(message.getLongitudeExist()));
		keysValue.add(message.getContent());
		keysValue.add(String.valueOf(message.getPrivacyLevel()));
		keysValue.add(hasPicture);
		keysValue.add(hasVoice);
		
		String url 	  = GlobalParameter.POST_NEWMESSAGE;
		String result = BaseFunctions.httpConnGBK(keysName, keysValue, url);
		
		try{
			JSONTokener jsonParser = new JSONTokener(result);  
			JSONObject temp 	   = (JSONObject) jsonParser.nextValue();  
			MessageInfoImp.id      = temp.getString("mId");
			rec					   = temp.getString("ErrCode");
			Log.d("status", rec);
		}
		catch(JSONException e){
			Log.d("json error",e.getMessage().toString());
		}
		return rec;
	}
}
