/**
 * @author solarcreeper;
 * 基础通信实现
 */
package com.WhiteBoard.Send;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.HttpConnectionParams;

import com.WhiteBoard.BaseClasses.GlobalParameter;

import android.R.integer;
import android.util.Log;

public class BaseFunctions {
	public static String httpConnGBK(ArrayList<String> keysName, ArrayList<String> keysValue, String url, int errReturn){
		String result = "";
		boolean flag  = true;
		InputStream is = null;
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		
		for(int i = 0; i < keysName.size(); i++)
			params.add(new BasicNameValuePair(keysName.get(i), keysValue.get(i)));
		
		try{
			HttpClient httpClient = new DefaultHttpClient();
			httpClient.getParams().setParameter(HttpConnectionParams.SO_TIMEOUT, 3000);
			httpClient.getParams().setIntParameter(HttpConnectionParams.CONNECTION_TIMEOUT, 3000);
			HttpPost httpPost = new HttpPost(url);
			httpPost.setEntity(new UrlEncodedFormEntity(params, "GBK"));
			HttpResponse resposet = httpClient.execute(httpPost);
			HttpEntity entity = resposet.getEntity();
			is = entity.getContent();
		}
		catch(Exception e){
			flag = false;
			Log.d("Error in http connection",e.getMessage().toString());
		}
		
		if(flag){
		try{
			BufferedReader br = new BufferedReader(new InputStreamReader(is,"GBK"));
			StringBuilder sb = new StringBuilder();
			String line = null;
			while((line = br.readLine()) != null){
				sb.append(line + "\n");
			}
			is.close();
			result = sb.toString();
			Log.d("result", result);
		}
		catch(Exception e){
			result = BaseFunctions.checkReturn(errReturn);
			Log.e("Error read from inputstream", e.getMessage().toString());  
		}
		}
		else{
			result = BaseFunctions.checkReturn(errReturn);
		}
		return result;
	}
	public static String httpConnUTF8(ArrayList<String> keysName, ArrayList<String> keysValue, String url,int errReturn){
		String 				result = "";
		boolean 			flag = true;
		InputStream 		is	   = null;
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		
		for(int i = 0; i < keysName.size(); i++)
			params.add(new BasicNameValuePair(keysName.get(i), keysValue.get(i)));
			
		try{
			HttpClient   httpClient = new DefaultHttpClient();
			httpClient.getParams().setParameter(HttpConnectionParams.SO_TIMEOUT, 2000);
			httpClient.getParams().setIntParameter(HttpConnectionParams.CONNECTION_TIMEOUT, 2000);
			HttpPost     httpPost   = new HttpPost(url);
			httpPost.setEntity(new UrlEncodedFormEntity(params, "GBK"));
			HttpResponse resposet   = httpClient.execute(httpPost);
			HttpEntity   entity     = resposet.getEntity();
			is = entity.getContent();
		}
		catch(Exception e){
			flag = false;
			Log.d("Error in http connection",e.getMessage().toString());
		}
		if(flag){
		try{
			BufferedReader br   = new BufferedReader(new InputStreamReader(is,"UTF_8"));
			StringBuilder  sb   = new StringBuilder();
			String         line = null;
			while((line = br.readLine()) != null){
				sb.append(line + "\n");
			}
			is.close();
			result = BaseFunctions.utf8ToStr(sb.toString());
			Log.d("result", "result"+result.toString());
		}
		catch(Exception e){
			result = BaseFunctions.checkReturn(errReturn);
			Log.e("Error read from inputstream", e.getMessage().toString());  
		}
		}
		else{
			result = BaseFunctions.checkReturn(errReturn);
		}
		return result;
	}
	public static String utf8ToStr(String s) {
        String ret = "null";
        try {
            ret = java.net.URLDecoder.decode(s, "utf-8");
        } catch (UnsupportedEncodingException ex) {
        }
        return ret;
    }
	private static String checkReturn(int x){
		String rec = "";
		switch(x){
		case 1:
			rec = GlobalParameter.loginReturn;
			break;
		case 2:
			break;
		case 3:
			break;
		default:
			break;
		}
		return rec;
	}
	
}
