/**
 * @author yehongjiang
 * 全局变量
 * 
 */

package com.WhiteBoard.BaseClasses;


public class GlobalParameter {
	private final static String IP                  	 = "http://whiteboard.sinaapp.com/server/";
	//private final static String IP  					 = "http://192.168.51.61/server/";
	//全局变量
	public final static String LOGIN                   	 =  IP+"login.php";
	public final static String MAPINFO              	 =  IP+"MapInfo.php";
	public final static String GET_LIST          	 	 =  IP+"GetList.php";  
	public final static String REGISTER              	 =  IP+"register.php";
	public final static String ADDFAVOURITE				 =  IP+"AddFavourite.php";  
	public final static String ALTER_AVATAR				 =  IP+"AlterAvatar.php";  
	public final static String UPLOADMEDIA               =  IP+"UploadMedia.php";
	public final static String USEREVALUATION			 =  IP+"UserEvaluation.php";  
	public final static String ALTER_PASSWORD      		 =  IP+"AlterPassword.php";
	public final static String POST_NEWCOMMENT			 =  IP+"PostNewComment.php";  
	public final static String POST_NEWMESSAGE			 =  IP+"PostNewMessage.php";  
	public final static String GET_COMMENTLIST           =  IP+"GetCommentList.php";  
	public final static String ALTER_BACKGROUND			 =  IP+"AlterBackground.php";  
	public final static String GET_FOLLOWSTATUS          =  IP+"GetFollowStatus.php";
	public final static String GET_LOCALPROFILE			 =  IP+"GetLocalProfile.php";	
	public final static String GET_FOLLOWERLIST          =  IP+"GetFollowerList.php";
	public final static String GET_FAVORITELIST          =  IP+"GetFavouriteList.php";  
	public final static String GET_COMMENTEDLIST    	 =  IP+"GetCommentedList.php";
	public final static String GET_FOLLOWINGLIST         =  IP+"GetFollowingList.php";
	public final static String GET_PUBLISHEDLIST         =  IP+"GetPublishedList.php";  
	public final static String ALTER_PRIVACYLEVEL 		 =  IP+"AlterPrivacyLevel.php";
	public final static String ALTER_FOLLOWSTATUS 	 	 =  IP+"AlterFollowStatus.php";
	public final static String GET_DETAILEDMESSAGE       =  IP+"GetDetailedMessage.php"; 
	public final static String SET_DEFAULTCATEGORY    	 =  IP+"SetDefaultCategory.php";
	public final static String ALTER_FOLLOWATTRIBUTE     =  IP+"AlterFollowAttribute.php";
	public final static String DeleteMessage			 = IP+"DeleteMessage.php";
	public final static String DeleteComment			 = IP + "DeleteComment.php";
	
	//异常处理
	//LoginImp.java
	public final static String loginReturn       		 = "{\"ErrCode\":\"678\",\"uId\":\"0\"}";
	
	//Register.java
	public final static String registerReturn 			 = "";
	
	//ResetPasswordImp.java
	public final static String resetPasswordReturn 		 = "";
	
	//SettingsImp.java
	public final static String followStatusReturn		 = "";
	public final static String followableReturn			 = "";
	public final static String privacyLevelReturn        = "";
	public final static String defaultMsgTypeReturn 	 = "";
	public final static String favoriteReturn			 = "";
	public final static String evaluationReturn 		 = "";
	
	//UserInfoImp.java
	public final static String localUserInfoReturn  	 = "";
	public final static String isUserFollowedReturn      = "";
	public final static String followingListReturn		 = "";
	public final static String followerListReturn        = "";
	public final static String favoriteListReturn		 = "";
	public final static String publishedListReturn   	 = "";
	public final static String commentedListReturn  	 = "";
	
	//MessageInfoImp.java
	public final static String pinnedListReturn			 = "";
	public final static String messageListReturn 		 = "";
	public final static String detailMessageReturn	 	 = "";
	public final static String createMessageReturn		 = "";
	
	//CommentImp.java
	public final static String commentReturn			 = "";
	public final static String commentListReturn		 = "";
	
	//DeleteImp.java
	public final static String deleteCommentReturn		 = ""; 
	public final static String deleteMessageReturn	 	 = "";
}
