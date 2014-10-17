package com.WhiteBoard.Send;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import android.util.Log;


import com.WhiteBoard.BaseClasses.GlobalParameter;

public class FileImp {
	public static String sendFileStatus;
	public static String sendImageStatus;
	public static String sendVoiceStatus;
	
	public static String getSendImageStatus(){
		return FileImp.sendImageStatus;
	}
	
	public static String getSendVoiceStatus(){
		return FileImp.sendVoiceStatus;
	}
	
	public static String getSendFileStatus(){
		return FileImp.sendFileStatus;
	}
	
	private static  void send(String srcPath, String type, String urlPath){
		
		String end 		  = "\r\n";
		String twoHyphens = "--";
		String boundary   = "******";
		try{
			URL url = new URL(urlPath);
			HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();

			httpURLConnection.setDoInput(true);
			httpURLConnection.setDoOutput(true);
			httpURLConnection.setUseCaches(false);

			httpURLConnection.setRequestMethod("POST");
			httpURLConnection.setRequestProperty("Connection", "Keep-Alive");
			httpURLConnection.setRequestProperty("Charset", "UTF-8");
			httpURLConnection.setRequestProperty("Content-Type","multipart/form-data;boundary=" + boundary);

			DataOutputStream dos = new DataOutputStream(httpURLConnection.getOutputStream());
			dos.writeBytes(twoHyphens + boundary + end);
			dos.writeBytes("Content-Disposition: form-data; name=\"" +type+"\"; filename=\"" + srcPath.substring(srcPath.lastIndexOf("/") + 1) + "\"" + end);
			dos.writeBytes(end);

			FileInputStream fis    = new FileInputStream(srcPath);
			byte[] 			buffer = new byte[8192]; // 8k
			int 			count  = 0;

			// ¶ÁÈ¡ÎÄ¼þ
			while ((count = fis.read(buffer)) != -1){
				dos.write(buffer, 0, count);
			}
			fis.close();

			dos.writeBytes(end);
			dos.writeBytes(twoHyphens + boundary + twoHyphens + end);
			dos.flush();

			int responseCode = httpURLConnection.getResponseCode();
			Log.d("Uploading", ""+responseCode);
			InputStream is = httpURLConnection.getInputStream();
			try{
				BufferedReader br   = new BufferedReader(new InputStreamReader(is,"GBK"));
				StringBuilder  sb   = new StringBuilder();
				String         line = null;
				while((line = br.readLine()) != null){
					sb.append(line + "\n");
				}
				is.close();
				String result = sb.toString();
				Log.d("result", result.toString());
				
				try{
					JSONTokener jsonParser = new JSONTokener(result);  
				    JSONObject  temp 	   = (JSONObject) jsonParser.nextValue(); 
				    String     status	   = temp.getString("ErrCode");
				    FileImp.setStatusByType(type, status);
				}
				catch(JSONException e){
					Log.d("json error",e.getMessage().toString());
				}
			}
			catch(Exception e){
				Log.e("log_tag", e.getMessage().toString());  
			}
		}
		catch (Exception e){
			e.printStackTrace();
		}
	}
	
	public static void sendImage(ArrayList<String> srcPathList){
		for(int i = 0; i < srcPathList.size(); i++){
			String srcPath = srcPathList.get(i);
			FileImp.sendImageByPath(srcPath);
		}
	}
	
	private static void sendImageByPath(String srcPath){
		String type = "ImageFile";
		String url = GlobalParameter.UPLOADMEDIA;
		FileImp.send(srcPath, type, url);
	}
	
	public static void sendVoice(String srcPath){
		String type = "VoiceFile";
		String url = GlobalParameter.UPLOADMEDIA;
		FileImp.send(srcPath, type, url);
	}
	
	public static void sendAvatar(String srcPath){
		String type = "AvatarFile";
		String url = GlobalParameter.ALTER_AVATAR;
		FileImp.send(srcPath, type, url);
	}
	
	public static void sendBackgroundFile(String srcPath){
		String type = "BackgroundFile";
		String url = GlobalParameter.ALTER_BACKGROUND;
		FileImp.send(srcPath, type, url);
	}
	
	private static void setStatusByType(String type, String errcode){
		if(type == "VoiceFile")
			FileImp.sendVoiceStatus = errcode;
		if(type == "ImageFile")
			FileImp.sendImageStatus = errcode;
		if(type == "AvatarFile")
			FileImp.sendFileStatus  = errcode;
		if(type == "BackgroundFile")
			FileImp.sendFileStatus  = errcode;
	} 
}
