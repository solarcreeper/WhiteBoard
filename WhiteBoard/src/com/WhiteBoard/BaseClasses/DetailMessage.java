/**
 * @author mzalive
 * 纸条详细类，继承自RoughMessage类
 * 不单独使用，需添加相应的Android SDK库支持
 * 使用DefaultSettings类中定义的全局常量
 * ！需要确定媒体数据存储及传输方式
 * 调用构造方法创建新纸条时，两组坐标均被初始化为-1，务必使用相应set方法更新数据。
 *
 * 媒体数据标准确定后将会提供重载的构造方法用以在不同情况下直接创建纸条实例
 */

package com.WhiteBoard.BaseClasses;

import java.util.ArrayList;

public class DetailMessage extends RoughMessage {
	
	protected String authorUserName;
	protected int amountComment;
	protected int localUserMarkStatus;
	protected double latitudeCreate;
	protected double longitudeCreate;
	protected int privacyLevel;
	protected boolean favorited;
	
	/* 以下数据成员仅在创建发布用纸条时使用 */
	/* 多媒体数据默认为null（不是“null”）
	 * 提供setter接受一个对象做参数，
	 * 数据接收方需手动验证 
	 * eg. if （pictureFile == null)
	 */
	protected ArrayList<String> pictureFile;
	protected ArrayList<String> pictureName;
	protected String voiceFile;
	protected String voiceName;
	
	
	/*创建一个用于发布的新纸条,*/
    public DetailMessage() {
    	idAuthor = DefaultSettings.getLocalUid();
    	authorUserName = "NULL";
    	idCategory = DefaultSettings.DEFAULT_PUBLISH_CATEGORY;
    	latitudeCreate = -1;
    	longitudeCreate = -1;
    	latitudeExist = -1;
    	longitudeExist = -1;
    	content = "NULL";
    	pictureFile = null;
    	pictureName = null;
    	voiceFile = null;
    	voiceName = null;
    	privacyLevel = DefaultSettings.DEFAULT_PRIVACY_LEVEL;    	
    }
    
    /*创建完全信息的纸条*/
    public DetailMessage(String id, String idAuthor, String authorUserName, String idCategory, String dateCreate, String content, int amountComment, int amountHelpful, int amountSpam, int localUserMarkStatus, double latitudeCreate, double longituteCreate, double latitudeExist, double longitudeExist, double distance, boolean favorited) {
    	this.id = id;
    	this.idAuthor = idAuthor;
    	this.authorUserName = authorUserName;
    	this.idCategory = idCategory;
    	this.dateCreate = dateCreate;
    	this.content = content;
    	this.amountComment = amountComment;
    	this.amountHelpful = amountHelpful;
    	this.amountSpam = amountSpam;
    	this.localUserMarkStatus = localUserMarkStatus;
    	this.latitudeCreate = latitudeCreate;
    	this.longitudeCreate = longituteCreate;
    	this.latitudeExist = latitudeExist;
    	this.longitudeExist = longitudeExist;
    	this.distance = distance;
    	this.favorited = favorited;
    }
    
    /*从id请求一个DetailMessage*/
	/*public DetailMessage(String id) {
    	this.id = id;
    	//待定
    }*/
	
	public void setPictureFile(ArrayList<String> pictureFile) {
		this.pictureFile = pictureFile;
	}

	public void setPictureName(ArrayList<String> pictureName) {
		this.pictureName = pictureName;
	}

	public void setVoiceFile(String voiceFile) {
		this.voiceFile = voiceFile;
	}

	public void setVoiceName(String voiceName) {
		this.voiceName = voiceName;
	}

	public ArrayList<String> getPictureFile() {
		return pictureFile;
	}

	public ArrayList<String> getPictureName() {
		return pictureName;
	}

	public String getVoiceFile() {
		return voiceFile;
	}

	public String getVoiceName() {
		return voiceName;
	}

	public void setCategory(String idCategory) {
		this.idCategory = idCategory;
	}
    
	public String getAuthorUserName() {
		return authorUserName;
	}
	
    public void setContent(String content) {
    	this.content = content;
    }   

	public int getAmountComment() {
		return amountComment;
	}

	public int getLocalUserMarkStatus() {
		return localUserMarkStatus;
	}

	public boolean isThrowedAway() {
		return (latitudeCreate == latitudeExist && longitudeCreate == longitudeExist);
	}

	public double getLatitudeCreate() {
		return latitudeCreate;
	}

	public double getLongitudeCreate() {
		return longitudeCreate;
	}
	
	public void setLocationCreate(double latitudeCreate, double longitudeCreate) {
		this.latitudeCreate = latitudeCreate;
		this.longitudeCreate = longitudeCreate;
	}

	public void setLocationExist(double latitudeExist, double longitudeExist) {
		this.latitudeExist = latitudeExist;
		this.longitudeExist = longitudeExist;
	}
	
	public void setCreateLocally() {
		setLocationExist(latitudeCreate, longitudeCreate);
	}

	public int getPrivacyLevel() {
		return privacyLevel;
	}

	public void setPrivacyLevel(int privacyLevel) {
		this.privacyLevel = privacyLevel;
	}
	
	public boolean isFavotited() {
		return this.favorited;
	}

	public boolean expandMessage(RoughMessage original) {
		if (this.id == original.id) {
			this.idAuthor = original.idAuthor;
			this.idCategory = original.idCategory;
			this.dateCreate = original.dateCreate;
			this.content = original.content;
			this.hasPicture = original.hasPicture;
			this.hasVoice = original.hasVoice;
			this.amountHelpful = original.amountHelpful;
			this.amountSpam = original.amountSpam;
			this.latitudeExist = original.latitudeExist;
			this.longitudeExist = original.longitudeExist;
			this.distance = original.distance;
			return true;
		}
		else 
			return false;
	}
	
	public void setLocalUserMarkStatusHelpful() {
		if (localUserMarkStatus != DefaultSettings.USER_MARK_HELPFUL) {
			localUserMarkStatus = DefaultSettings.USER_MARK_HELPFUL;
			++amountHelpful;
			/* 更新服务器MarkStatus Only
			 * 服务器需注意*完善*维护相对文章的amountHelpful
			 */
			
		}
	}
	
	public void setLocalUserMarkStatusSpam() {
		if (localUserMarkStatus != DefaultSettings.USER_MARK_SPAM) {
			localUserMarkStatus = DefaultSettings.USER_MARK_SPAM;
			++amountSpam;
			/* 更新服务器MarkStatus Only
			 * 服务器需注意*完善*维护相对文章的amountHelpful
			 */
		}
	}
	
}