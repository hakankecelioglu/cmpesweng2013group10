package com.groupon.mobile;

import android.app.Activity;
import android.app.Application;
import android.content.SharedPreferences;

import com.groupon.mobile.model.User;
import com.groupon.mobile.utils.Constants;

public class GrouponApplication extends Application {
	private User user;
	private String authToken;

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

	public String getAuthToken() {
		if (authToken == null) {
			SharedPreferences prefs = getSharedPreferences(Constants.PREFS_NAME_SECURITY, Activity.MODE_PRIVATE);
			authToken = prefs.getString(Constants.PREFS_AUTH_TOKEN_KEY, null);
		}
		return authToken;
	}

	public void setAuthToken(String token) {
		authToken = token;
		SharedPreferences preferences = getSharedPreferences(Constants.PREFS_NAME_SECURITY, Activity.MODE_PRIVATE);
		SharedPreferences.Editor editor = preferences.edit();
		if (token == null) {
			editor.remove(Constants.PREFS_AUTH_TOKEN_KEY);
		} else {
			editor.putString(Constants.PREFS_AUTH_TOKEN_KEY, authToken);
		}
		editor.commit();
	}

	private User readLoggedUser() {
		SharedPreferences preferences = getSharedPreferences("user", Activity.MODE_PRIVATE);
		String username = preferences.getString("username", null);
		Long id = preferences.getLong("id", -1);
		if (username == null || id == -1) {
			return null;
		}
		String email = preferences.getString("email", null);
		String name = preferences.getString("name", null);
		String surname = preferences.getString("surname", null);

		User user = new User();
		user.setId(id);
		user.setUsername(username);
		user.setEmail(email);
		user.setName(name);
		user.setSurname(surname);
		return user;
	}

	private void writeLoggedUser(User user) {
		SharedPreferences preferences = getSharedPreferences("user", Activity.MODE_PRIVATE);
		SharedPreferences.Editor editor = preferences.edit();
		editor.putString("username", user.getUsername());
		editor.putLong("id", user.getId());
		editor.putString("name", user.getName());
		editor.putString("surname", user.getSurname());
		editor.putString("email", user.getEmail());
		editor.commit();
	}
}
