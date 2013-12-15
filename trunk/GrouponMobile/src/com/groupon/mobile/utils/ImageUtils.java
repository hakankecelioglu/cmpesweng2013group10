package com.groupon.mobile.utils;

import android.widget.ImageView;

import com.groupon.mobile.conn.DownloadImageTask;

public class ImageUtils {

	public static void loadBitmap(ImageView picture, String pictureUrl) {
		String url;
		if (StringUtils.isNotBlank(pictureUrl) && !pictureUrl.equals("null")) {
			url = Constants.COMMUNITY_ICON + pictureUrl;
		} else
			url = Constants.COMMUNITY_DEFAULT_ICON;
		new DownloadImageTask(picture).execute(url);
	}
}
