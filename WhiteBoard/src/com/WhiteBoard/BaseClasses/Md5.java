/**
 * @author mzalive
 * MD5�㷨ʵ����
 * ����һ����̬����getMd5���ڽ���Ϊ�������ܵ��ַ���ת��ΪСдMD5������
 * ���췽������˽�У���ֹʵ������
 */

package com.WhiteBoard.BaseClasses;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Md5 {
	private Md5() {
	}
	
	public static String getMd5(String str) {
		try {
		//32λ����md 
		MessageDigest md = MessageDigest.getInstance("MD5");
		md.update(str.getBytes()); //����
		byte[] bt=md.digest();     //ժҪ
		//����������ַ���
		StringBuilder sb=new StringBuilder();
		int p=0;
		for(int i=0;i<bt.length;i++){
		p=bt[i];                        
		   if(p<0) p +=256;        //��ֵʱ�Ĵ��� 
		if(p<16) sb.append("0");
		   sb.append(Integer.toHexString(p));//ת����16����
		}
		return sb.toString();
		} catch (NoSuchAlgorithmException e) {
		e.printStackTrace();
		return str;
		}
	}
}
