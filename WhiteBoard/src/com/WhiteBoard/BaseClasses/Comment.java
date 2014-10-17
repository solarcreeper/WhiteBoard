/**
 * @author mzalive
 * ������
 * 
 */

package com.WhiteBoard.BaseClasses;

public class Comment {
	protected String id;
	protected String idMessage;
	protected String idAuthor;
	protected String authorUserName;
	protected String dateCreate;
	protected String content;
	
	/*����idΪnull������β�ź��õ�Commentʵ��*/
	public Comment() {
		this(null, null, null, null, null, null);
		
	}
	
	/*����������Commentʵ��*/
	public Comment(String id, String idMessage, String idAuthor, String authorUserName, String dateCreate, String content) {	
		this.id = id;
		this.idMessage = idMessage;
		this.idAuthor = idAuthor;
		this.authorUserName = authorUserName;
		this.dateCreate = dateCreate;
		this.content = content;
	}
	
	/*�������Է�������������Commentʵ��*/
	public Comment(String idMessage, String idAuthor, String content ) {
		this("NULL", idMessage, idAuthor, "NULL", "NULL", content);
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getId() {
		return id;
	}

	public String getIdMessage() {
		return idMessage;
	}

	public String getIdAuthor() {
		return idAuthor;
	}

	public String getAuthorUserName() {
		return authorUserName;
	}

	public String getDateCreate() {
		return dateCreate;
	}
	
	
}

