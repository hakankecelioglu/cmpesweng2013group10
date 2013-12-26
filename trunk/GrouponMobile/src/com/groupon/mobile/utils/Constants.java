package com.groupon.mobile.utils;

public class Constants {
	// For development
	// public static final String SERVER = " http://titan.cmpe.boun.edu.tr:8089/groupon/ http://192.168.1.5:1717/";
	// For production
	public static final String SERVER = "http://titan.cmpe.boun.edu.tr:8089/groupon/";

	public static final String REQUEST_AUTH_HEADER = "X-auth-token";

	public static final String PREFS_NAME_SECURITY = "_sec";
	public static final String PREFS_AUTH_TOKEN_KEY = "_auth_token";

	public static final String COMMUNITY_ICON = SERVER + "community/thumb/medium/";
	public static final String COMMUNITY_DEFAULT_ICON = SERVER + "res/img/default_com_picture.jpg";
}
