/**
 * @author mzalive
 * 评论类
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
	
	/*创建id为null，作列尾信号用的Comment实例*/
	public Comment() {
		this(null, null, null, null, null, null);
		
	}
	
	/*创建完整的Comment实例*/
	public Comment(String id, String idMessage, String idAuthor, String authorUserName, String dateCreate, String content) {	
		this.id = id;
		this.idMessage = idMessage;
		this.idAuthor = idAuthor;
		this.authorUserName = authorUserName;
		this.dateCreate = dateCreate;
		this.content = content;
	}
	
	/*创建用以发送至服务器的Comment实例*/
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

