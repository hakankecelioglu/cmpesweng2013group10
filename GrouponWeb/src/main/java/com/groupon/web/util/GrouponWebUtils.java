package com.groupon.web.util;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;

import com.groupon.web.dao.model.User;

public class GrouponWebUtils {
	public static String generateCookieForUser(User user) {
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append(user.getId());
		stringBuilder.append("#");
		stringBuilder.append(generateUserHash(user));
		return stringBuilder.toString();
	}

	public static String generateUserHash(User user) {
		StringBuilder toBeHashed = new StringBuilder();
		toBeHashed.append("abcdef123456789jfuejdhs!'?^='?+)=%&)%&");
		toBeHashed.append(user.getEmail());
		toBeHashed.append(user.getId());
		toBeHashed.append(user.getUsername());
		return md5(toBeHashed.toString());
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
}