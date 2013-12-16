package com.groupon.mobile.utils;

import android.widget.ImageView;

import com.nostra13.universalimageloader.core.ImageLoader;

public class ImageUtils {

	public static void loadBitmap(ImageView picture, String pictureUrl) {
		String url;
		if (StringUtils.isNotBlank(pictureUrl) && !pictureUrl.equals("null")) {
			url = Constants.COMMUNITY_ICON + pictureUrl;
		} else
			url = Constants.COMMUNITY_DEFAULT_ICON;

		ImageLoader.getInstance().displayImage(url, picture);
	}
}
