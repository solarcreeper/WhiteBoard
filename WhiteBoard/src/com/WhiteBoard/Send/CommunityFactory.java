/**
 * @author solarcerrper;
 * ͨ�ŵ��ýӿڣ�
 */
package com.WhiteBoard.Send;

import java.util.ArrayList;

import android.util.Log;

import com.WhiteBoard.BaseClasses.Comment;
import com.WhiteBoard.BaseClasses.DetailMessage;
import com.WhiteBoard.BaseClasses.RoughMessage;
import com.WhiteBoard.BaseClasses.User;

public class CommunityFactory {
	
    //����ע����Ϣ
	public static String sendRegister(String nickname, String password, String email, String phone, String updateStamp){
		Register.register(nickname, password, email, phone, updateStamp);
		ArrayList<String> registerResult = Register.getRegisterResult();
		Log.d("regiserResult", registerResult.get(1));
		return registerResult.get(1); 
	}
	
	//���ע�᷵��userID
	public static String getRegisterId(){
		ArrayList<String> registerResult = Register.getRegisterResult();
		Log.d("regiserResult", registerResult.get(0));
		return registerResult.get(0); 
	}
	
	//���͵�½��Ϣ
	public static String sendLogin(String Identifier, String passwordMD5){
		 LoginImp.login(Identifier, passwordMD5);
		ArrayList<String> loginReturn = LoginImp.getSendLogin();
		Log.d("Login Result", loginReturn.get(1));
		return loginReturn.get(1);
	}
	//��õ�½���ص�userID;
	public static String getLoginId(){
		ArrayList<String> loginReturn = LoginImp.getSendLogin();
		Log.d("Login uId", loginReturn.get(0));
		return loginReturn.get(0);
	}
	
	//�����޸�������Ϣ
	public static String sendResetPassword(String uid, String oldPassword, String newPassword){
		return ResetPasswordImp.resetPasswordImp(uid, oldPassword, newPassword);
	}
	
	//��ȡlocaluser����
	public static User getLocalUserInfo(String uid, String updatestamp){
		return UserInfoImp.localUserInfo(uid, updatestamp);
	}
	
	//��ȡĿ���û��Ƿ�������ע
	public static String getTargetUserFollowable(){
		Log.d("TargetUserFollowable", UserInfoImp.targetUserFollowable());
		return UserInfoImp.targetUserFollowable();
	}
	
	//��ȡ�û���ע״̬
	public static String getIsUserFollowed(String targetuid, String localuid){
		return UserInfoImp.isUserFollowed(targetuid, localuid);
	}
	
	//��ȡĳ�û���ע�����б�
	public static ArrayList<User> getFollowingList(String uid, String page){
		return UserInfoImp.followingList(uid, page);
	}
	
	//��ȡ��עĳ�û������б�
	public static ArrayList<User> getFollowerList(String uid, String page){
		return UserInfoImp.followerList(uid, page);
	}
	
	//��ȡĳ�û����ղ�ֽ���б�
	public static ArrayList<RoughMessage> getFavoriteList(String targetuId, String localuId, String latitudeCurrent, String longitudeCurrent, String categorySet, String sortMethod, String messageFilter, String page ){
		return UserInfoImp.favoriteList(targetuId, localuId, latitudeCurrent, longitudeCurrent, categorySet, sortMethod, messageFilter, page);
	}
	//��ȡĳ�û���������ֽ���б�
	public static ArrayList<RoughMessage> getPublishedList(String uId, String localuId, String latitudeCurrent, String longitudeCurrent, String categorySet, String sortMethod, String messageFilter, String page ){
		return UserInfoImp.publishedList(uId, localuId, latitudeCurrent, longitudeCurrent, categorySet, sortMethod, messageFilter, page);
	}
	
	//��ȡĳ�û������۹���ֽ���б�
	public static ArrayList<RoughMessage> getCommentedList(String uId, String localuId, String latitudeCurrent, String longitudeCurrent, String categorySet, String sortMethod, String messageFilter, String page){
		return UserInfoImp.commentedList(uId, localuId, latitudeCurrent, longitudeCurrent, categorySet, sortMethod, messageFilter, page);
	}
	
	//�޸Ĺ�ע��ȡ����ע
	//����ֵ��status 1&0
	public static String sendFollowStatus(String uid, String tid, String status){
		return SettingsImp.followStatus(uid, tid, status);
	}
	
	//�����Լ��Ƿ�ɱ���ע
	//����ֵ��followable: 1&0
	public static String sendFollowable(String uid, String followable, String updateStamp){
		return SettingsImp.followable(uid, followable, updateStamp);
	}
	
	//���followable
	public static String getFollowable(){
		return SettingsImp.followable();
	}	
	
	//�޸�privacylevel��Ϣ
	public static String sendPrivcyLevel(String uid, String privacylevel, String updateStamp){
		return SettingsImp.privacyLevel(uid, privacylevel, updateStamp);
	}
	
	public static String getPrivcyLevel(){
		return SettingsImp.privacyLevel();
	}
	
	//�����޸�Ĭ����ʾ����������Ϣ
	public static String sendDefaultMsgType(String uid, String defaultmsgtype, String updateStamp){
		return SettingsImp.defaultMsgType(uid, defaultmsgtype, updateStamp);
	}
	
	//���/ȡ���ղ�ֽ��
	public static String setFavorite(String uid, String mid, boolean favorited){
		return SettingsImp.favorite(uid, mid, favorited);
	}
	
	//֧��&����
	public static String sendEvaluation(String uid, String mid, String evaluation){
		return SettingsImp.evaluation(uid, mid, evaluation);
	}
	
	//��ͼ��Ұ�ڵĶ���
	public static ArrayList<RoughMessage> getPinnedList(String uId, String  latitudeTarget, String longitudeTarget,String latitudeShift, String longitudeShift, String defaultCategory ){
		return MessageInfoImp.pinnedList(uId, latitudeTarget, longitudeTarget, latitudeShift, longitudeShift, defaultCategory);
	}
	
	//ֽ���б�����
	public static ArrayList<RoughMessage> getMessageList(String uId, String latitudeCurrent, String longitudeCurrent,String latitudeTarget, String longitudeTarget,String geoShift, String categorySet, String index, String sortMethod, String messageFilter, String page){
		return MessageInfoImp.messageList(uId, latitudeCurrent, longitudeCurrent, latitudeTarget, longitudeTarget, geoShift, categorySet, index, sortMethod, messageFilter, page);
	}
	
	//ֽ����ϸ��Ϣ
	public  static DetailMessage getDetailMessage(String uid, String mid){
		return MessageInfoImp.detailMessage(uid, mid);
	}
	
	//����ֽ��
	public static String createMessage(DetailMessage newmessage){
		return MessageInfoImp.createMessage(newmessage);
	}
	
	//��ȡ����ֽ����Id
	public static String getCreatedMsgId(){
		return MessageInfoImp.getId();
	}
	
	//����ͷ��
	public static String sendAvatar(String srcPath){
		FileImp.sendAvatar(srcPath);
		return FileImp.getSendFileStatus();
	}
	
	//���ͱ���ͼƬ
	public static String sendBackgroundFile(String srcPath){
		FileImp.sendBackgroundFile(srcPath);
		return FileImp.getSendFileStatus();
	}
	
	//��������
	public static String createComment(String localUid, String mId, String content){
		return CommentImp.comment(localUid, mId, content);
	}
	
	//�����б�
	public static ArrayList<Comment> getCommentList(String mId, String page){
		return CommentImp.commentList(mId, page);
	}
		
	//ɾ������
	public static String delComment(String commentId, String messageId){
		return DeleteImp.deleteComment(commentId, messageId);
	}
	
	//ɾ����Ϣ
	public static String delMessage(String messageId, String uid){
		return DeleteImp.deleteMessage(messageId, uid);
	}
}
