package com.groupon.mobile;

import android.app.Activity;
import android.app.Application;
import android.content.SharedPreferences;

import com.groupon.mobile.model.User;

public class GrouponApplication extends Application {
	private User user;

	@Override
	public void onCreate() {
		super.onCreate();
		user = readLoggedUser();
		DummyController.init();
	}

	public User getLoggedUser() {
		return user;
	}

	public void setLoggedUser(User user) {
		this.user = user;
		writeLoggedUser(user);
	}

	private User readLoggedUser() {
		SharedPreferences preferences = getSharedPreferences("user", Activity.MODE_PRIVATE);
		String username = preferences.getString("username", null);
		if (username == null) {
			return null;
		}

		User user = new User();
		user.setUsername(username);
		return user;
	}

	private void writeLoggedUser(User user) {
		SharedPreferences preferences = getSharedPreferences("user", Activity.MODE_PRIVATE);
		SharedPreferences.Editor editor = preferences.edit();
		editor.putString("username", user.getUsername());
		editor.commit();
	}
}
