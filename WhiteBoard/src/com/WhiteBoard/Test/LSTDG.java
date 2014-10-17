package com.WhiteBoard.Test;

import java.util.ArrayList;

import android.util.Log;

import com.WhiteBoard.BaseClasses.RoughMessage;
import com.WhiteBoard.BaseClasses.User;

public class LSTDG {

	private static final int MAX_ITEM = 50;
	private static int loadedItem = 0;
	public static ArrayList<User> followingList;
	public static ArrayList<User> followerList;
	public static ArrayList<RoughMessage> rmList;

	private static final String TAG = "LSTDG";

	public static void getFollowing(String uid, int page) {
		Log.d(TAG, "getFollowing done(LSTDG)");
		followingList = new ArrayList<User>();
		try {
			Thread.sleep(3000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		Log.d(TAG, "clear done(LSTDG)");
		if (loadedItem + 20 < MAX_ITEM) {
			for (int i = 1; i <= 20; ++i) {
				++loadedItem;
				followingList.add(new User("UID" + loadedItem, "USER"
						+ loadedItem));
			}
		} else {
			while (loadedItem < MAX_ITEM) {
				++loadedItem;
				followingList.add(new User("UID" + loadedItem, "USER"
						+ loadedItem));
			}
			followingList.add(new User("NULL", "NULL"));
			reset();
		}
	}

	public static void reset() {
		loadedItem = 0;
	}

	public static ArrayList<RoughMessage> getRMList() {
		try {
			Thread.sleep(3000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		rmList = new ArrayList<RoughMessage>();
		for (int i = 1; i <= 20; ++i) {
			RoughMessage item = new RoughMessage("MID " + i, "UID " + i, ""
					+ (1 + i % 4) + ",", "2013-7-23", "content", true, true, i,
					i, i, i, i * 100);
			rmList.add(item);
		}
		Log.d(TAG, "Data Generate Done");
		return rmList;
	}

	/* simulate sendLogin class */
	public static class SendLogin {
		private static final String uid = "000000";
		private static final String username = "Master Liu";
		private static final String pwdMD5 = "5f4dcc3b5aa765d61d8327deb882cf99";
		private static boolean accepted = false;

		public static String getuId() {
			return uid;
		}

		public static String getLoginStatus() {
			if (accepted)
				return "TRUE";
			else
				return "FALSE";
		}

		public static void sendLogin(String Identifier, String passwordMD5) {
			if (Identifier.equals(username) && passwordMD5.equals(pwdMD5))
				accepted = true;
			else
				accepted = false;
		}
	}

	/* simulate SendUserInfo class */
	public static class SendUserInfo {
		//
		private static User user;

		private static void setUser(String uid, String userName, String email,
				String phone, boolean followable, String idDefaultCategory,
				long updateStamp) {
			user = new User(uid, userName, email, phone, followable,
					idDefaultCategory, updateStamp);
		}

		public static User getUser() {
			return user;
		}

		public static void getLocalUserInfo(String uid, String updatestamp) {
			SendUserInfo.setUser(uid, "Master Liu", "test@example.com",
					"13000000000", true, ",1,2,3,4,", 0);
		}

		//
		private static String followed;
		private static String targetUserFollowable;

		public static String getFollowed() {
			return followed;
		}

		private static void setFollowed(String followed) {
			SendUserInfo.followed = followed;
		}

		public static String getTargetUserFollowable() {
			return targetUserFollowable;
		}

		private static void setTargetUserFollowable(String targetUserFollowable) {
			SendUserInfo.targetUserFollowable = targetUserFollowable;
		}

		public static void getIsUserFollowed(String targetuid, String localuid) {
			SendUserInfo.setFollowed("false");
			SendUserInfo.setTargetUserFollowable("true");
		}

		//
		private static ArrayList<String> followingIdList = new ArrayList<String>();
		private static ArrayList<String> followingUserNameList = new ArrayList<String>();

		public static ArrayList<String> getFollowingIdList() {
			return followingIdList;
		}

		public static ArrayList<String> getFollowingUserNameList() {
			return followingUserNameList;
		}

		// func
		public static void getFollowingList(String uid, String page) {
			followingIdList.clear();
			followingUserNameList.clear();
			for (int j = 0; j < 20; j++) {
				followingIdList.add("Id" + j);
				followingUserNameList.add("UserName" + j);
			}
		}

		//
		private static ArrayList<String> followerIdList = new ArrayList<String>();
		private static ArrayList<String> followerUserNameList = new ArrayList<String>();

		public static ArrayList<String> getFollowerIdList() {
			return followerIdList;
		}

		public static ArrayList<String> getFollowerUserNameList() {
			return followerUserNameList;
		}

		public static void getFollowerList(String uid, String page) {
			followerIdList.clear();
			followerUserNameList.clear();
			for (int j = 0; j < 20; j++) {
				followerIdList.add("Id" + j);
				followerUserNameList.add("UserName" + j);
			}
		}

		//
		private static ArrayList<RoughMessage> favoriteList = new ArrayList<RoughMessage>();

		public static ArrayList<RoughMessage> getFavoriteList(String targetuId,
				String localuId, String latitudeCurrent,
				String longitudeCurrent, String categorySet, String sortMethod,
				String messageFilter, String page) {
			try {
				Thread.sleep(3000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			for (int j = 0; j < 20; j++) {
				RoughMessage roughmessage = new RoughMessage(
						"MID" + j,
						"AuthorUID" + j,
						"" + (1 + j % 4) + ",",
						"2013-7-23",
						"我是内容我是内容我是内容我是内容我是内容我是内容我是内容我是内容我是内容我是内容我是内容我是内容我是内容我是内容我是内容我是内容我是内容我是内容我是内容",
						true, true, j, 0, 0, 0, 100 * j);
				favoriteList.add(roughmessage);
			}
			return favoriteList;
		}

		private static ArrayList<RoughMessage> publishedList = new ArrayList<RoughMessage>();

		public static ArrayList<RoughMessage> getPublishedList(String uId,
				String localuId, String latitudeCurrent,
				String longitudeCurrent, String categorySet, String sortMethod,
				String messageFilter, String page) {
			try {
				Thread.sleep(3000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			for (int j = 0; j < 20; j++) {
				RoughMessage roughmessage = new RoughMessage(
						"MID" + j,
						"UID",
						"" + (1 + j % 4) + ",",
						"2013-7-23",
						"我是内容我是内容我是内容我是内容我是内容我是内容我是内容我是内容我是内容我是内容我是内容我是内容我是内容我是内容我是内容我是内容我是内容我是内容我是内容",
						true, true, j, 0, 0, 0, 100 * j);
				publishedList.add(roughmessage);
			}
			return publishedList;
		}

		private static ArrayList<RoughMessage> commentedList = new ArrayList<RoughMessage>();

		public static ArrayList<RoughMessage> getCommentedList(String uId,
				String localuId, String latitudeCurrent,
				String longitudeCurrent, String categorySet, String sortMethod,
				String messageFilter, String page) {
			try {
				Thread.sleep(3000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			for (int j = 0; j < 20; j++) {
				RoughMessage roughmessage = new RoughMessage(
						"MID" + j,
						"UID",
						"" + (1 + j % 4) + ",",
						"2013-7-23",
						"我是内容我是内容我是内容我是内容我是内容我是内容我是内容我是内容我是内容我是内容我是内容我是内容我是内容我是内容我是内容我是内容我是内容我是内容我是内容",
						true, true, j, 0, 0, 0, 100 * j);
				commentedList.add(roughmessage);
			}
			return commentedList;
		}
	}

	/* simulate SendModifyPassword class */
	public static class SendModifyPassword {
		private static String modifyPdStatus;

		public static String getModifyPdStatus() {
			return SendModifyPassword.modifyPdStatus;
		}

		public static void sendModifyPassword(String uid, String oldpassword,
				String newpassword) {
			SendModifyPassword.modifyPdStatus = "TRUE";
		}
	}

	/* simulate SendSettings class */
	public static class SendSettings {

		private static String operateStatus;

		public static String getOperateStatus() {
			return SendSettings.operateStatus;
		}

		public static void sendFollowStatus(String uid, String tid,
				String status) {
			SendSettings.operateStatus = "TURE";
		}

		private static String followable;
		private static String followableStatus;

		private static void setFollowable(String followable) {
			SendSettings.followable = followable;
		}

		public static String getFollowable() {
			return SendSettings.followable;
		}

		private static void setFollowableStatus(String status) {
			SendSettings.followableStatus = status;
		}

		public static String getFollowableStatus() {
			return SendSettings.followableStatus;
		}

		// func
		public static void sendFollowable(String uid, String followable,
				String updateStamp) {
			// SendSettings.setFollowableStatus(status_return);
			// SendSettings.setFollowable(followable_return);
			// TODO 弄清参数意义，统一updateStamp类型和字符串常量大小写
		}

		private static String privacyLevel;
		private static String privacyLevelStatus;

		private static void setPrivacyLevel(String privacylevel) {
			SendSettings.privacyLevel = privacylevel;
		}

		private static void setPrivacyLevelStatus(String status) {
			SendSettings.privacyLevelStatus = status;
		}

		public static String getPrivacyLevel() {
			return SendSettings.privacyLevel;
		}

		public static String getPrivacyLevelStatus() {
			return SendSettings.privacyLevelStatus;
		}

		// func
		public static void sendPrivacyLevel(String uid, String privacylevel,
				String updateStamp) {
			// SendSettings.setPrivacyLevelStatus(status_return);
			// SendSettings.setPrivacyLevel(privacylevel_return);
			// TODO 统一privacylevel的值
		}

		private static String defaultMsgTypeStatus;

		private static void setDefaultMsgTypeStatus(String status) {
			SendSettings.defaultMsgTypeStatus = status;
		}

		public static String getDefaultMsgStatus() {
			return SendSettings.defaultMsgTypeStatus;
		}

		public static void sendDefaultMessType(String uid,
				String defaultmsgtype, String updateStamp) {
			SendSettings.setDefaultMsgTypeStatus(defaultmsgtype);
			// TODO 明确参数的意义
		}

		private static String collectStatus;

		public static String getCollectStatus() {
			return SendSettings.collectStatus;
		}

		public static void sendCollectMessage(String uid, String mid) {
			SendSettings.collectStatus = "TRUE";
		}

		private static String EvaluationStatus;

		public static String getEvaluation() {
			return SendSettings.EvaluationStatus;
		}

		public static void sendEvaluation(String uid, String mid,
				String evaluation) {
			SendSettings.EvaluationStatus = "TRUE";
		}
	}

	public static class SendMessageInfo {

		public static ArrayList<RoughMessage> getPinnedList(String uId,
				String latitudeCurrent, String longitudeCurrent,
				String latitudeShift, String longitudeShift,
				String defaultCategory) {
			
			ArrayList<RoughMessage> tempMSGS = new ArrayList<RoughMessage>();
			for(int i = 0; i < 500; i++){
				RoughMessage tempRM = new RoughMessage("1", "1", (Math.random()*100 - 30), (Math.random()*100 + 49));
				tempMSGS.add(tempRM);
			}
			
			return tempMSGS;

		}
	}

}
