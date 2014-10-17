/**
 * @author solarcerrper;
 * 通信调用接口；
 */
package com.WhiteBoard.Send;

import java.util.ArrayList;

import android.util.Log;

import com.WhiteBoard.BaseClasses.Comment;
import com.WhiteBoard.BaseClasses.DetailMessage;
import com.WhiteBoard.BaseClasses.RoughMessage;
import com.WhiteBoard.BaseClasses.User;

public class CommunityFactory {
	
    //发送注册消息
	public static String sendRegister(String nickname, String password, String email, String phone, String updateStamp){
		Register.register(nickname, password, email, phone, updateStamp);
		ArrayList<String> registerResult = Register.getRegisterResult();
		Log.d("regiserResult", registerResult.get(1));
		return registerResult.get(1); 
	}
	
	//获得注册返回userID
	public static String getRegisterId(){
		ArrayList<String> registerResult = Register.getRegisterResult();
		Log.d("regiserResult", registerResult.get(0));
		return registerResult.get(0); 
	}
	
	//发送登陆消息
	public static String sendLogin(String Identifier, String passwordMD5){
		 LoginImp.login(Identifier, passwordMD5);
		ArrayList<String> loginReturn = LoginImp.getSendLogin();
		Log.d("Login Result", loginReturn.get(1));
		return loginReturn.get(1);
	}
	//获得登陆返回的userID;
	public static String getLoginId(){
		ArrayList<String> loginReturn = LoginImp.getSendLogin();
		Log.d("Login uId", loginReturn.get(0));
		return loginReturn.get(0);
	}
	
	//发送修改密码消息
	public static String sendResetPassword(String uid, String oldPassword, String newPassword){
		return ResetPasswordImp.resetPasswordImp(uid, oldPassword, newPassword);
	}
	
	//获取localuser数据
	public static User getLocalUserInfo(String uid, String updatestamp){
		return UserInfoImp.localUserInfo(uid, updatestamp);
	}
	
	//获取目标用户是否允许被关注
	public static String getTargetUserFollowable(){
		Log.d("TargetUserFollowable", UserInfoImp.targetUserFollowable());
		return UserInfoImp.targetUserFollowable();
	}
	
	//获取用户关注状态
	public static String getIsUserFollowed(String targetuid, String localuid){
		return UserInfoImp.isUserFollowed(targetuid, localuid);
	}
	
	//获取某用户关注的人列表
	public static ArrayList<User> getFollowingList(String uid, String page){
		return UserInfoImp.followingList(uid, page);
	}
	
	//获取关注某用户的人列表
	public static ArrayList<User> getFollowerList(String uid, String page){
		return UserInfoImp.followerList(uid, page);
	}
	
	//获取某用户的收藏纸条列表
	public static ArrayList<RoughMessage> getFavoriteList(String targetuId, String localuId, String latitudeCurrent, String longitudeCurrent, String categorySet, String sortMethod, String messageFilter, String page ){
		return UserInfoImp.favoriteList(targetuId, localuId, latitudeCurrent, longitudeCurrent, categorySet, sortMethod, messageFilter, page);
	}
	//获取某用户发布过的纸条列表
	public static ArrayList<RoughMessage> getPublishedList(String uId, String localuId, String latitudeCurrent, String longitudeCurrent, String categorySet, String sortMethod, String messageFilter, String page ){
		return UserInfoImp.publishedList(uId, localuId, latitudeCurrent, longitudeCurrent, categorySet, sortMethod, messageFilter, page);
	}
	
	//获取某用户的评论过的纸条列表
	public static ArrayList<RoughMessage> getCommentedList(String uId, String localuId, String latitudeCurrent, String longitudeCurrent, String categorySet, String sortMethod, String messageFilter, String page){
		return UserInfoImp.commentedList(uId, localuId, latitudeCurrent, longitudeCurrent, categorySet, sortMethod, messageFilter, page);
	}
	
	//修改关注或取消关注
	//参数值：status 1&0
	public static String sendFollowStatus(String uid, String tid, String status){
		return SettingsImp.followStatus(uid, tid, status);
	}
	
	//更改自己是否可被关注
	//参数值：followable: 1&0
	public static String sendFollowable(String uid, String followable, String updateStamp){
		return SettingsImp.followable(uid, followable, updateStamp);
	}
	
	//获得followable
	public static String getFollowable(){
		return SettingsImp.followable();
	}	
	
	//修改privacylevel消息
	public static String sendPrivcyLevel(String uid, String privacylevel, String updateStamp){
		return SettingsImp.privacyLevel(uid, privacylevel, updateStamp);
	}
	
	public static String getPrivcyLevel(){
		return SettingsImp.privacyLevel();
	}
	
	//发送修改默认显示字条类型消息
	public static String sendDefaultMsgType(String uid, String defaultmsgtype, String updateStamp){
		return SettingsImp.defaultMsgType(uid, defaultmsgtype, updateStamp);
	}
	
	//添加/取消收藏纸条
	public static String setFavorite(String uid, String mid, boolean favorited){
		return SettingsImp.favorite(uid, mid, favorited);
	}
	
	//支持&反对
	public static String sendEvaluation(String uid, String mid, String evaluation){
		return SettingsImp.evaluation(uid, mid, evaluation);
	}
	
	//地图视野内的钉子
	public static ArrayList<RoughMessage> getPinnedList(String uId, String  latitudeTarget, String longitudeTarget,String latitudeShift, String longitudeShift, String defaultCategory ){
		return MessageInfoImp.pinnedList(uId, latitudeTarget, longitudeTarget, latitudeShift, longitudeShift, defaultCategory);
	}
	
	//纸条列表数据
	public static ArrayList<RoughMessage> getMessageList(String uId, String latitudeCurrent, String longitudeCurrent,String latitudeTarget, String longitudeTarget,String geoShift, String categorySet, String index, String sortMethod, String messageFilter, String page){
		return MessageInfoImp.messageList(uId, latitudeCurrent, longitudeCurrent, latitudeTarget, longitudeTarget, geoShift, categorySet, index, sortMethod, messageFilter, page);
	}
	
	//纸条详细信息
	public  static DetailMessage getDetailMessage(String uid, String mid){
		return MessageInfoImp.detailMessage(uid, mid);
	}
	
	//创建纸条
	public static String createMessage(DetailMessage newmessage){
		return MessageInfoImp.createMessage(newmessage);
	}
	
	//获取创建纸条的Id
	public static String getCreatedMsgId(){
		return MessageInfoImp.getId();
	}
	
	//发送头像
	public static String sendAvatar(String srcPath){
		FileImp.sendAvatar(srcPath);
		return FileImp.getSendFileStatus();
	}
	
	//发送背景图片
	public static String sendBackgroundFile(String srcPath){
		FileImp.sendBackgroundFile(srcPath);
		return FileImp.getSendFileStatus();
	}
	
	//创建评论
	public static String createComment(String localUid, String mId, String content){
		return CommentImp.comment(localUid, mId, content);
	}
	
	//评论列表
	public static ArrayList<Comment> getCommentList(String mId, String page){
		return CommentImp.commentList(mId, page);
	}
		
	//删除评论
	public static String delComment(String commentId, String messageId){
		return DeleteImp.deleteComment(commentId, messageId);
	}
	
	//删除消息
	public static String delMessage(String messageId, String uid){
		return DeleteImp.deleteMessage(messageId, uid);
	}
}
