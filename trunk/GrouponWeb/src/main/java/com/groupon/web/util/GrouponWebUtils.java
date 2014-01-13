package com.groupon.web.util;

import java.awt.Dimension;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONException;
import org.json.JSONObject;

import com.groupon.web.dao.model.BaseModel;
import com.groupon.web.dao.model.User;
import com.groupon.web.exception.GrouponException;

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

	public static void rejectIfEmpty(JSONObject json, String... params) throws JSONException {
		for (String param : params) {
			if (json.has(param)) {
				String value = json.getString(param);
				if (StringUtils.isBlank(value)) {
					throw new GrouponException(param + " cannot be null or empty!");
				}
			} else {
				throw new GrouponException(param + " cannot be null or empty!");
			}
		}
	}

	public static List<Long> convertModelListToLongList(Collection<? extends BaseModel> objects) {
		List<Long> longs = new ArrayList<Long>();
		if (objects != null) {
			for (BaseModel obj : objects) {
				longs.add(obj.getId());
			}
		}
		return longs;
	}

	public static boolean isBlank(JSONObject json, String key) throws JSONException {
		if (json.has(key)) {
			String value = json.getString(key);
			if (StringUtils.isNotBlank(value)) {
				return false;
			}
		}
		return true;
	}

	public static Dimension getDimensionFitBounds(int width, int height, int boundWidth, int boundHeight) {
		int original_width = width;
		int original_height = height;
		int new_width = original_width;
		int new_height = original_height;

		// first check if we need to scale width
		if (original_width > boundWidth) {
			// scale width to fit
			new_width = boundWidth;
			// scale height to maintain aspect ratio
			new_height = (new_width * original_height) / original_width;
		}

		// then check if we need to scale even with the new height
		if (new_height > boundHeight) {
			// scale height to fit instead
			new_height = boundHeight;
			// scale width to maintain aspect ratio
			new_width = (new_height * original_width) / original_height;
		}

		return new Dimension(new_width, new_height);
	}

	public static Dimension getDimensionExtendsBounds(int width, int height, int boundWidth, int boundHeight) {
		int original_width = width;
		int original_height = height;
		int new_width = original_width;
		int new_height = original_height;

		double widthRate = (double) width / boundWidth;
		double heightRate = (double) height / boundHeight;

		if (widthRate < heightRate) {
			// scale width to fit
			new_width = boundWidth;
			// scale height to maintain aspect ratio
			new_height = (new_width * original_height) / original_width;
		} else {
			// scale height to fit instead
			new_height = boundHeight;
			// scale width to maintain aspect ratio
			new_width = (new_height * original_width) / original_height;
		}

		return new Dimension(new_width, new_height);
	}
}