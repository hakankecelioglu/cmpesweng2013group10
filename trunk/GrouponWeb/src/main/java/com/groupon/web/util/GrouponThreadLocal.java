package com.groupon.web.util;

import com.groupon.web.dao.model.User;

public class GrouponThreadLocal {
	public static final ThreadLocal<GrouponThreadLocal> threadLocal = new ThreadLocal<GrouponThreadLocal>();

	private User user = null;

	public static void set(User user) {
		GrouponThreadLocal grouponThreadLocal = threadLocal.get();

		if (grouponThreadLocal == null) {
			grouponThreadLocal = new GrouponThreadLocal();
		}
		grouponThreadLocal.user = user;

		threadLocal.set(grouponThreadLocal);
	}

	public static void unset() {
		threadLocal.remove();
	}

	public static User get() {
		GrouponThreadLocal grouponThreadLocal = threadLocal.get();

		if (grouponThreadLocal == null) {
			return null;
		}

		return grouponThreadLocal.user;
	}

}
