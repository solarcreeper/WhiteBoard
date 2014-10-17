/**
 * @author mzalive
 * ֽ����ϸ�࣬�̳���RoughMessage��
 * ������ʹ�ã��������Ӧ��Android SDK��֧��
 * ʹ��DefaultSettings���ж����ȫ�ֳ���
 * ����Ҫȷ��ý�����ݴ洢�����䷽ʽ
 * ���ù��췽��������ֽ��ʱ���������������ʼ��Ϊ-1�����ʹ����Ӧset�����������ݡ�
 *
 * ý�����ݱ�׼ȷ���󽫻��ṩ���صĹ��췽�������ڲ�ͬ�����ֱ�Ӵ���ֽ��ʵ��
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
	
	/* �������ݳ�Ա���ڴ���������ֽ��ʱʹ�� */
	/* ��ý������Ĭ��Ϊnull�����ǡ�null����
	 * �ṩsetter����һ��������������
	 * ���ݽ��շ����ֶ���֤ 
	 * eg. if ��pictureFile == null)
	 */
	protected ArrayList<String> pictureFile;
	protected ArrayList<String> pictureName;
	protected String voiceFile;
	protected String voiceName;
	
	
	/*����һ�����ڷ�������ֽ��,*/
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
    
    /*������ȫ��Ϣ��ֽ��*/
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
    
    /*��id����һ��DetailMessage*/
	/*public DetailMessage(String id) {
    	this.id = id;
    	//����
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
			/* ���·�����MarkStatus Only
			 * ��������ע��*����*ά��������µ�amountHelpful
			 */
			
		}
	}
	
	public void setLocalUserMarkStatusSpam() {
		if (localUserMarkStatus != DefaultSettings.USER_MARK_SPAM) {
			localUserMarkStatus = DefaultSettings.USER_MARK_SPAM;
			++amountSpam;
			/* ���·�����MarkStatus Only
			 * ��������ע��*����*ά��������µ�amountHelpful
			 */
		}
	}
	
}