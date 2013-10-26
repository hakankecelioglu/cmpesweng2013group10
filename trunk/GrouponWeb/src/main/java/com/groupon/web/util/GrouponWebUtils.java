package com.groupon.web.util;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;

import com.groupon.web.controller.GrouponException;
import com.groupon.web.dao.model.User;

public class GrouponWebUtils {
	public static String generateCookieForUser(User user) {
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append(user.getId());
		stringBuilder.append("#");
		stringBuilder.append(generateUserHash(user));
		return stringBuilder.toString();
	}

	public static Long extractUserIdFromCookieValue(String cookieValue) {
		int index = cookieValue.indexOf('#');
		if (index == -1)
			return null;
		String lhs = cookieValue.substring(0, index);
		return Long.parseLong(lhs);
	}

	public static String extractUserHashFromCookieValue(String cookieValue) {
		int index = cookieValue.indexOf('#');
		if (index == -1)
			return "";
		String rhs = cookieValue.substring(index + 1);
		return rhs;
	}

	public static String generateUserHash(User user) {
		StringBuilder toBeHashed = new StringBuilder();
		toBeHashed.append("abcdef123456789jfuejdhs!'?^='?+)=%&)%&");
		toBeHashed.append(user.getEmail());
		toBeHashed.append(user.getId());
		toBeHashed.append(user.getUsername());
		return md5(toBeHashed.toString());
	}

	public static String hashPasswordForDB(String password) {
		StringBuilder builder = new StringBuilder();
		builder.append(md5(password));
		builder.append("oursecretkey_123456");
		return md5(builder.toString());
	}

	public static String md5(String md5) {
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			byte[] array = md.digest(md5.getBytes("UTF-8"));
			StringBuffer sb = new StringBuffer();
			for (int i = 0; i < array.length; ++i) {
				sb.append(Integer.toHexString((array[i] & 0xFF) | 0x100).substring(1, 3));
			}
			return sb.toString();
		} catch (java.security.NoSuchAlgorithmException e) {
		} catch (UnsupportedEncodingException e) {
		}
		return null;
	}

	public static void rejectIfEmpty(HttpServletRequest request, String... params) throws GrouponException {
		for (String param : params) {
			String value = request.getParameter(param);
			if (StringUtils.isBlank(value)) {
				throw new GrouponException(param + " cannot be null or empty!");
			}
		}
	}
}