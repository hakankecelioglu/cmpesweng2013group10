package com.groupon.mobile;

import android.app.Activity;
import android.app.Application;
import android.content.SharedPreferences;

import com.groupon.mobile.model.User;
import com.groupon.mobile.utils.Constants;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

/**
 * Application class which represents the Task Together application as singleton on runtime.
 * 
 * @author sedrik
 */
public class GrouponApplication extends Application {
	private User user;
	private String authToken;

	@Override
	public void onCreate() {
		super.onCreate();
		user = readLoggedUser();

		DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder().cacheInMemory(true).cacheOnDisc(true).showImageOnFail(R.drawable.logonew).build();
		ImageLoaderConfiguration.Builder builder = new ImageLoaderConfiguration.Builder(getApplicationContext());
		builder.defaultDisplayImageOptions(defaultOptions);
		ImageLoaderConfiguration config = builder.build();
		ImageLoader.getInstance().init(config);
	}

	/**
	 * Returns the logged user, if any
	 * @return the logged user, if any
	 */
	public User getLoggedUser() {
		return user;
	}

	/**
	 * Sets the logged user. This user will be logged until he logs out. Stored in shared preferences.
	 * @param user user being logged in
	 */
	public void setLoggedUser(User user) {
		this.user = user;
		writeLoggedUser(user);
	}

	/**
	 * Returns the authorization token of the logged user, if any
	 * @return the authorization token of the logged user, if any
	 */
	public String getAuthToken() {
		if (authToken == null) {
			SharedPreferences prefs = getSharedPreferences(Constants.PREFS_NAME_SECURITY, Activity.MODE_PRIVATE);
			authToken = prefs.getString(Constants.PREFS_AUTH_TOKEN_KEY, null);
		}
		return authToken;
	}

	/**
	 * Sets the authorization token of the logged user. Stores it in shared preferences.
	 * @param token the authorization token to be set.
	 */
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
		if (user == null) {
			editor.remove("username");
			editor.remove("id");
			editor.remove("name");
			editor.remove("surname");
			editor.remove("email");
		} else {
			editor.putString("username", user.getUsername());
			editor.putLong("id", user.getId());
			editor.putString("name", user.getName());
			editor.putString("surname", user.getSurname());
			editor.putString("email", user.getEmail());
		}
		editor.commit();
	}
}
