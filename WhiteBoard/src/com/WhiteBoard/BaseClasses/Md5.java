/**
 * @author mzalive
 * MD5算法实现类
 * 包含一个静态方法getMd5用于将作为参数接受的字符串转换为小写MD5串返回
 * 构造方法声明私有，禁止实例化。
 */

package com.WhiteBoard.BaseClasses;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Md5 {
	private Md5() {
	}
	
	public static String getMd5(String str) {
		try {
		//32位加密md 
		MessageDigest md = MessageDigest.getInstance("MD5");
		md.update(str.getBytes()); //更新
		byte[] bt=md.digest();     //摘要
		//保留结果的字符串
		StringBuilder sb=new StringBuilder();
		int p=0;
		for(int i=0;i<bt.length;i++){
		p=bt[i];                        
		   if(p<0) p +=256;        //负值时的处理 
		if(p<16) sb.append("0");
		   sb.append(Integer.toHexString(p));//转换成16进制
		}
		return sb.toString();
		} catch (NoSuchAlgorithmException e) {
		e.printStackTrace();
		return str;
		}
	}
}
