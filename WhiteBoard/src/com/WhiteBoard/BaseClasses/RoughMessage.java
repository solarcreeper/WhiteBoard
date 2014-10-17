/**
 * @author mzalive
 * ֽ��������
 * ������ʹ�ã��������Ӧ��Android SDK��֧��
 * ʹ��DefaultSettings���ж����ȫ�ֳ���
 * 
 * Ĭ�Ϲ��췽��RoughMessage()���κ�����¶���Ӧʹ��
 */

package com.WhiteBoard.BaseClasses;


public class RoughMessage {

	protected String id;
	protected String idAuthor;
	protected String idCategory;
	protected String dateCreate;
	protected String content;
	protected boolean hasPicture;
	protected boolean hasVoice;
	protected int amountHelpful;
	protected int amountSpam;
	protected double latitudeExist;
	protected double longitudeExist;
	protected double distance;
	
	/*����һ��idΪnull��RougnMessage�����б�ĩβ��־��*/
	public RoughMessage() {
		this(null, null, null, null, null, false, false, 0, 0, 0, 0, 0);
	}
	
	/*���ڴ��������б��г���RoughMessage�Ĺ��캯��*/
	public RoughMessage(String id, String idCategory, double latitudeExist, double longitudeExist) {
		this(id, "", idCategory, "", "", false, false, 0, 0, latitudeExist, longitudeExist, 0);
	}
	
	/*��׼RoughMessage���캯��*/
	public RoughMessage(String id, String idAuthor, String idCategory, String dateCreate, String content, boolean hasPicture, boolean hasVoice, int amountHelpful, int amountSpam, double latitudeExist, double longitudeExist, double distance) {
		this.id = id;
		this.idAuthor = idAuthor;
		this.idCategory = idCategory;
		this.dateCreate = dateCreate;
		this.content = content;
		this.hasPicture = hasPicture;
		this.hasVoice = hasVoice;
		this.amountHelpful = amountHelpful;
		this.amountSpam = amountSpam;
		this.latitudeExist = latitudeExist;
		this.longitudeExist = longitudeExist;
		this.distance = distance;
	}

	public String getId() {
		return id;
	}

	public String getIdAuthor() {
		return idAuthor;
	}

	public String getIdCategory() {
		return idCategory;
	}

	public String getDateCreate() {
		return dateCreate;
	}

	public String getContent() {
		return content;
	}

	public boolean hasPicture() {
		return hasPicture;
	}

	public boolean hasVoice() {
		return hasVoice;
	}

	public int getAmountHelpful() {
		return amountHelpful;
	}

	public int getAmountSpam() {
		return amountSpam;
	}

	public double getLatitudeExist() {
		return latitudeExist;
	}

	public double getLongitudeExist() {
		return longitudeExist;
	}

	public double getDitance() {
		return distance;
	}

	public void requestRoughMessage(String id) {
		
	}

}
