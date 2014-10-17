/**
 * @author mzalive
 * 基础类用全局常量类
 * 不单独使用
 * 构造方法声明私有，禁止实例化
 */

package com.WhiteBoard.BaseClasses;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.baidu.platform.comapi.basestruct.GeoPoint;

public class DefaultSettings {
	
	public final static int zoomLevel = 13;
	
	public final static String REMOTE_STORE_URL_PREFIX_PICTURE = "http://whiteboard-imagefile.stor.sinaapp.com/";
	public final static String REMOTE_STORE_URL_PREFIX_VOICE = "http://whiteboard-voicefile.stor.sinaapp.com/";	
	public final static String REMOTE_STORE_URL_PREFIX_AVATAT = "http://whiteboard-avatarfile.stor.sinaapp.com/";
	public final static String REMOTE_STORE_URL_PREFIX_BACKGROUND = "http://whiteboard-backgroundfile.stor.sinaapp.com/";
	
	public final static String CATEGORY_ID_COMMERCIAL = "1,";
	public final static String CATEGORY_ID_PERSONAL_STATUS = "2,";
	public final static String CATEGORY_ID_FRIEND_MAKING = "3,";
	public final static String CATEGORY_ID_MUTUAL_HELP = "4,";
	
	public final static String HAS_VOICE = "1,";
	public final static String HAS_PICTURE = "2,";
	
	public final static int USER_MARK_NONE = 0;
	public final static int USER_MARK_HELPFUL = 1;
	public final static int USER_MARK_SPAM = -1;
	
	public final static int PRIVACY_PUBLIC = 0;
	public final static int PRIVACY_FOLLOWING = 1;
	public final static int PRIVACY_PRIVATE = 2;
	
	public final static String DEFAULT_PUBLISH_CATEGORY = CATEGORY_ID_PERSONAL_STATUS;
	public final static int DEFAULT_PRIVACY_LEVEL = PRIVACY_PUBLIC;
	
	public static boolean firstUse = true;
	public static boolean loggedIn = false;

	public static boolean prefsLoaded = false;
	
	public static String lastUid;
	public static String lastCredential;
	
	private static String localUid = "0";
	private static String localUserName = "null";
	private static String email = "null";
	private static String phone = "null";
	private static String defaultCategory = ",1,2,3,4,";
	private static boolean followable = true;
	private static long updateStamp = 0;
	
	private static boolean categoryCommercialChecked = false;
	private static boolean categoryPersonalChecked = false;
	private static boolean categoryFriendChecked = false;
	private static boolean categoryHelpChecked = false;
	
	private static double longitudeCurrent = 0;
	private static double latitudeCurrent = 0;
	
	private static String addressCurrent = "未获得";
	
	public static float currentZoomLevel = 13;
	public static GeoPoint currentGeoPoint = null;
	
	private DefaultSettings() {
	}
	
	/*设置登录状态
	 * 登录成功后LocalUser数据拉取可能因网络状况而出现延迟，
	 * 建议此时单独设置一次登录状态以保证程序中所有组件为 登陆后-可用 状态
	 */
	public static void setLoggedIn(String localUid) {
		DefaultSettings.localUid = localUid;
		loggedIn = true;
	}
	
	/*设置注销状态*/
	public static void setLoggedOut() {
		loggedIn = false;
		resetAll();	
	}
	
	/*接受一个本地用户的User实例来更新数据*/
	public static void setLocalUser(User localUser) {
		//loggedIn = true;
		localUid = localUser.getId();
		localUserName = localUser.getUserName();
		email = localUser.getEmail();
		phone = localUser.getPhone();
		defaultCategory = localUser.getIdDefaultCategory();
		followable = localUser.isFollowable();
		updateStamp = localUser.getUpdateStamp();
		decodeCategoryState();
			
	}
	
	/*销毁全部数据*/
	public static void resetAll() {
		
		//firstUse = true;
		
		loggedIn = false;
		lastUid = localUid;
		localUid = "0";
		localUserName = "null";
		email = "null";
		phone = "null";
		defaultCategory = ",1,2,3,4,";
		followable = true;
		updateStamp = 0;
		decodeCategoryState();
	}
	
	/* 获取默认单个分类状态前，务必调用此方法恢复分类选取状态
	 * 允许调用相关setter临时改变单个分类状态，以用于列表筛选等
	 */
	public static void decodeCategoryState() {
		setCategoryCommercialChecked(false);
		setCategoryPersonalChecked(false);
		setCategoryFriendChecked(false);
		setCategoryHelpChecked(false);
		if (defaultCategory.indexOf("," + CATEGORY_ID_COMMERCIAL) != -1)
			setCategoryCommercialChecked(true);
		if (defaultCategory.indexOf("," + CATEGORY_ID_PERSONAL_STATUS) != -1)
			setCategoryPersonalChecked(true);
		if (defaultCategory.indexOf("," + CATEGORY_ID_FRIEND_MAKING) != -1)
			setCategoryFriendChecked(true);
		if (defaultCategory.indexOf("," + CATEGORY_ID_MUTUAL_HELP) != -1)
			setCategoryHelpChecked(true);
	}
	
	/* 调用此方法将当前分类状态编码为大串以供网络传输使用*/
	public static String encodeCategoryState() {
		String category = ",";
		if (isCategoryCommercialChecked())
			category += CATEGORY_ID_COMMERCIAL;
		if (isCategoryPersonalChecked()) 
			category += CATEGORY_ID_PERSONAL_STATUS;
		if (isCategoryFriendChecked())
			category += CATEGORY_ID_FRIEND_MAKING;
		if (isCategoryHelpChecked())
			category += CATEGORY_ID_MUTUAL_HELP;
		return category;
	}
	
	public static void solveUpdateSettings() {
		//调用工工提供的方法发送localUid，defaultCategory，followable，updateStamp
	}
	
	/*为下次登陆记录本次登陆用户信息*/
	public static void setLastUser(String lastUid, String lastCredential) {
		DefaultSettings.lastUid = lastUid;
		DefaultSettings.lastCredential = lastCredential;
	}
	
	public static String getLastUid() {
		return lastUid;
	}

	public static String getLastCredential() {
		return lastCredential;
	}

	public static void setUsed() {
		DefaultSettings.firstUse = false;
	}

	public static boolean isLoggedIn() {
		return loggedIn;
	}

	public static String getLocalUid() {
		return localUid;
	}

	public static String getLocalUserName() {
		return localUserName;
	}

	public static void setLocalUserName(String localUserName) {
		DefaultSettings.localUserName = localUserName;
	}

	public static String getEmail() {
		return email;
	}

	public static String getPhone() {
		return phone;
	}

	public static String getDefaultCategory() {
		return defaultCategory;
	}

	public static void setDefaultCategory(String defaultCategory) {
		DefaultSettings.defaultCategory = defaultCategory;
	}
	
	public static boolean hasCategory(String categoryId) {
		if (defaultCategory.indexOf(","+categoryId) == -1)
			return false;
		else 
			return true;
	}

	public static boolean isFollowable() {
		return followable;
	}

	public static void setFollowable(boolean followable) {
		DefaultSettings.followable = followable;
	}

	public static long getUpdateStamp() {
		return updateStamp;
	}

	/*仅在用户更改数据时调用此方法*/
	public static long newUpdateStamp() {
		DefaultSettings.updateStamp = System.currentTimeMillis();
		return updateStamp;
	}

	public static boolean isCategoryCommercialChecked() {
		return categoryCommercialChecked;
	}

	public static boolean isCategoryPersonalChecked() {
		return categoryPersonalChecked;
	}

	public static boolean isCategoryFriendChecked() {
		return categoryFriendChecked;
	}

	public static boolean isCategoryHelpChecked() {
		return categoryHelpChecked;
	}

	public static void setCategoryCommercialChecked(boolean categoryCommercialChecked) {
		DefaultSettings.categoryCommercialChecked = categoryCommercialChecked;
	}

	public static void setCategoryPersonalChecked(boolean categoryPersonalChecked) {
		DefaultSettings.categoryPersonalChecked = categoryPersonalChecked;
	}

	public static void setCategoryFriendChecked(boolean categoryFriendChecked) {
		DefaultSettings.categoryFriendChecked = categoryFriendChecked;
	}

	public static void setCategoryHelpChecked(boolean categoryHelpChecked) {
		DefaultSettings.categoryHelpChecked = categoryHelpChecked;
	}
	
	public static void setLocalPosition(double latitude, double longitude){
		DefaultSettings.latitudeCurrent = latitude;
		DefaultSettings.longitudeCurrent = longitude;
	}
	
	public static double getLongitudeCurrent(){
		return longitudeCurrent;
	}
	
	public static double getLatitudeCurrent() {
		return latitudeCurrent;
	}	

	public static void setAddressCurrent(String address){
		addressCurrent = address;
	}
	
	public static String getAddressCurrent(){
		return addressCurrent;
	}
	
	
	public static void loadPrefs(Context context) {
		
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
		
		String uid, userName, idDefaultCategory;
		boolean followable;
		long updateStamp;
		uid = prefs.getString("uid", "0");
		userName = prefs.getString("username", "null");
		idDefaultCategory = prefs.getString("default_category", ",1,2,3,4,");
		updateStamp = prefs.getLong("update_stamp", 0);
		followable = prefs.getBoolean("followable", true);
		User user = new User(uid, userName, "null", "null", followable, idDefaultCategory, updateStamp);
		
		if (prefs.getBoolean("logged_in", false)) {
			DefaultSettings.setLoggedIn(uid);
			DefaultSettings.setLocalUser(user);
		}
		
		DefaultSettings.lastUid = uid;
		DefaultSettings.lastCredential = prefs.getString("identifier", "");
		DefaultSettings.firstUse = prefs.getBoolean("first_use", true);
	}
	
	public static void savePrefs(Context context) {
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
		SharedPreferences.Editor editor = prefs.edit();
		editor.putBoolean("first_use", DefaultSettings.firstUse);
		editor.putBoolean("logged_in", DefaultSettings.isLoggedIn());
		editor.putString("identifier", DefaultSettings.lastCredential);
		editor.putString("uid", DefaultSettings.getLocalUid());
		editor.putString("username", DefaultSettings.getLocalUserName());
		editor.putString("default_category", DefaultSettings.getDefaultCategory());
		editor.putBoolean("followable", DefaultSettings.isFollowable());
		editor.putLong("update_stamp", DefaultSettings.getUpdateStamp());
		
		editor.commit();
		
	}
}
