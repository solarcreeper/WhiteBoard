/**
 * @author mzalive
 * �û���
 * ������ʹ�ã��������Ӧ��Android SDK��֧��
 * 
 * Ĭ�Ϲ��췽��User()���κ�����¶���Ӧʹ��
 * ���췽��User(String id)���ڴ��������û�ʵ��
 * 
 */

package com.WhiteBoard.BaseClasses;

public class User {
	
	protected String id;
	protected String userName;
	protected String email; 
	protected String phone;
	//protected Bitmap avatar;
	//protected Bitmap userBackgound
	
	/** �������ݼ���ط����ݲ����� 
	 * protected int amountFollowing;
	 * protected int amountFollower;
	 * protected int amountMessage;
	 * protected int amountComment;
	 * protected int amountFavorite;	
	 */
	
	protected String idDefaultCategory;
	protected boolean followable;
	protected boolean followed;
	protected long updateStamp;
	
	public User() {
		this(null, null);
	}
	
	public User(String uid, String userName) {
		this.id = uid;
		this.userName = userName;
	}
	
	/*��׼User���캯��*/
	public User(String uid, String userName, String email, String phone, boolean followable, String idDefaultCategory, long updateStamp) {
		this.id = uid;
		this.userName = userName;
		this.email = email;
		this.phone = phone;
		this.followable = followable;
		this.idDefaultCategory = idDefaultCategory;
		this.updateStamp = updateStamp;
	}
	
	public User(String uid) {
		this.id = uid;
		//���������ݣ�����userName, email, phone, followable, idDefaultCategory, updateStamp
	}
	
	public String getId() {
		return id;
	}
	
	public String getUserName() {
		return userName;
	}
	
	public String getEmail() {
		return email;
	}
	
	public String getPhone() {
		return phone;
	}
	
/*	public Bitmap getAvatar() {
		return avatar;
	}
*/	
	/*public int getAmountFollowing() {
		return amountFollowing;
	}
	
	public int getAmountFollower() {
		return amountFollower;
	}
	
	public int getAmountMessage() {
		return amountMessage;
	}
	
	public int getAmountComment() {
		return amountComment;
	}
	
	public int getAmountFavorite() {
		return amountFavorite;
	}*/
	
	
		
	public long getUpdateStamp() {
		return updateStamp;
	}

	public String getIdDefaultCategory() {
		return idDefaultCategory;
	}

	public boolean isFollowable() {
		return followable;
	}

	public boolean isFollowed() {
		return followed;
	}
	
	
}
