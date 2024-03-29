package com.groupon.web.util;

public class GrouponConstants {
	public static final String SESSION_ATTR_USER = "user";
	public static final String SESSION_ATTR_SORTBY = "sortby";

	public static final String REQUEST_HEADER_AUTH_KEY = "X-auth-token";
	public static final String COOKIE_NAME_USER = "_g_u";
	public static final int COOKIE_USER_MAX_AGE = 60 * 60 * 24 * 30; // 30 days

	public static final int TAG_USER_COMMENT_TASK = 1;
	public static final int TAG_USER_FOLLOW_TASK = 3;
	public static final int TAG_USER_CONTRIBUTE_TASK = 4;
	public static final int TAG_USER_JOIN_COMMUNITY = 5;
	public static final int TAG_USER_CREATE_TASK = 8;
	public static final int TAG_USER_CREATE_COMMUNITY = 10;

	public static final String ATTR_NAME_REPLY_QUANTITY = "TT_RES_QUANTITY";

	public static final int REPUT_BY_TASK_UP = 5;
	public static final int REPUT_BY_TASK_DOWN = -1;
	public static final int REPUT_BY_REPLY_UP = 10;
	public static final int REPUT_BY_REPLY_DOWN = -1;
	public static final int REPUT_BY_REPLY_ACCEPT = 15;
}
