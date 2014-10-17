package com.WhiteBoard.Send;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import android.util.Log;

import com.WhiteBoard.BaseClasses.Comment;

import com.WhiteBoard.BaseClasses.GlobalParameter;

public class CommentImp {

	//创建评论
	public static String comment(String localUid, String mId, String content){
		String 			  rec 		= "";
		ArrayList<String> keysName  = new ArrayList<String>();
		ArrayList<String> keysValue = new ArrayList<String>();
		keysName.add("LocalUid");
		keysName.add("mId");
		keysName.add("Content");
		keysValue.add(localUid);
		keysValue.add(mId);
		keysValue.add(content);
		
		String url = GlobalParameter.POST_NEWCOMMENT;
		String result = BaseFunctions.httpConnGBK(keysName, keysValue, url);
		try{
			JSONTokener jsonParser 			  = new JSONTokener(result);
			JSONObject temp 				  = (JSONObject)jsonParser.nextValue();
			rec 				  			  = temp.getString("CommentId");
			//test
			Log.d("commentId", rec);
		}
		catch(JSONException e){
			Log.d("json error",e.getMessage().toString());
		}
		return rec;
	}
	
	//评论列表
	public static ArrayList<Comment> commentList(String mId, String page){
		 ArrayList<Comment> commentList = new ArrayList<Comment>();
		 ArrayList<String>  keysName    = new ArrayList<String>();
		 ArrayList<String>  keysValue   = new ArrayList<String>();
		 keysName.add("mId");
		 keysName.add("Page");
		 keysValue.add(mId);
		 keysValue.add(page);
		 
		 String url    = GlobalParameter.GET_COMMENTLIST;
		 String result = BaseFunctions.httpConnUTF8(keysName, keysValue, url);
		 
		 try{
				JSONArray array = new JSONArray(result);
				for(int j = 0; j < array.length(); j++){
					JSONObject temp = (JSONObject)array.get(j);
					Log.d("temp", temp.toString());
					String  id            	 = temp.getString("CommentId");
					String  idMessage 	 	 = temp.getString("IdMessage");
					String  idAuthor    	 = temp.getString("IdAuthor");
					String  authorUserName	 = temp.getString("UserName");
					String  dateCreate    	 = temp.getString("DateCreate");
					String  content     	 = temp.getString("Content");

					Comment comment 		 = new Comment(id, idMessage, idAuthor, authorUserName, dateCreate, content);
					Log.d("comment", comment.toString());
					commentList.add(comment);
				}
			}
			catch(JSONException e){
				Log.d("json error",e.getMessage().toString());
			}
		 return commentList;
	}
}
