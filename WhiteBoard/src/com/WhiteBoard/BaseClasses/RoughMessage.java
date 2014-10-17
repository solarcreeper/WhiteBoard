/**
 * @author mzalive
 * 纸条简略类
 * 不单独使用，需添加相应的Android SDK库支持
 * 使用DefaultSettings类中定义的全局常量
 * 
 * 默认构造方法RoughMessage()在任何情况下都不应使用
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
	
	/*创建一个id为null的RougnMessage，作列表末尾标志用*/
	public RoughMessage() {
		this(null, null, null, null, null, false, false, 0, 0, 0, 0, 0);
	}
	
	/*用于创建钉子列表中超简RoughMessage的构造函数*/
	public RoughMessage(String id, String idCategory, double latitudeExist, double longitudeExist) {
		this(id, "", idCategory, "", "", false, false, 0, 0, latitudeExist, longitudeExist, 0);
	}
	
	/*标准RoughMessage构造函数*/
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
