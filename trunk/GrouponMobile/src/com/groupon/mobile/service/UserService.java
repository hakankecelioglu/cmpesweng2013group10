package com.groupon.mobile.service;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import com.groupon.mobile.GrouponApplication;
import com.groupon.mobile.conn.ConnectionUtils;
import com.groupon.mobile.conn.GrouponCallback;
import com.groupon.mobile.conn.GrouponTask;
import com.groupon.mobile.exception.GrouponException;
import com.groupon.mobile.model.User;
import com.groupon.mobile.utils.Constants;

public class UserService {

	private GrouponApplication app;

	public UserService(GrouponApplication app) {
		this.app = app;
	}

	public void login(final String username, final String password, GrouponCallback<User> callback) {
		GrouponTask<User> userTask = new GrouponTask<User>(callback) {
			@Override
			public User run() throws GrouponException {
				String url = Constants.SERVER + "mobile/login";
				Map<String, String> map = new HashMap<String, String>();
				map.put("username", username);
				map.put("password", password);

				JSONObject jsonObject = ConnectionUtils.makePostRequest(url, map, app.getAuthToken());
				try {
					return convertJsonToUser(jsonObject);
				} catch (JSONException e) {
					throw new GrouponException("JSON returned from server is invalid!");
				}
			}
		};
		GrouponTask.execute(userTask);
	}

	public void signup(final String username, final String password, final String email, GrouponCallback<User> callback) {
		GrouponTask<User> userTask = new GrouponTask<User>(callback) {
			@Override
			public User run() throws GrouponException {
				String url = Constants.SERVER + "mobile/signup";

				JSONObject json = new JSONObject();
				try {
					json.put("email", email);
					json.put("username", username);
					json.put("password", password);
					json.put("name", "");
					json.put("surname", "");
				} catch (JSONException e) {
					throw new GrouponException("An unknown error occured!");
				}

				Map<String, String> map = new HashMap<String, String>();
				JSONObject jsonObject = ConnectionUtils.makePostRequest(url, map, json, app.getAuthToken());

				try {
					return convertJsonToUser(jsonObject);
				} catch (JSONException e) {
					throw new GrouponException("JSON returned from server is invalid!");
				}
			}
		};
		GrouponTask.execute(userTask);
	}

	public void getUserInfo(final Long userId, GrouponCallback<User> callback) {
		GrouponTask<User> userTask = new GrouponTask<User>(callback) {
			public User run() throws GrouponException {
				String url = Constants.SERVER + "mobile/profile/" + userId;
				JSONObject jsonObject = ConnectionUtils.makeGetRequest(url, null, app.getAuthToken());
				try {
					return convertJsonToUser(jsonObject);
				} catch (JSONException e) {
					throw new GrouponException("JSON returned from server is invalid!");
				}
			}
		};
		GrouponTask.execute(userTask);
	}

	private User convertJsonToUser(JSONObject json) throws JSONException {
		if (json.has("auth")) {
			String auth = json.getString("auth");
			app.setAuthToken(auth);
		}

		if (json.has("user")) {
			json = json.getJSONObject("user");
		}

		User user = new User();

		if (json.has("email")) {
			user.setEmail(json.getString("email"));
		}

		if (json.has("id")) {
			user.setId(json.getLong("id"));
		}

		if (json.has("name") && !json.isNull("name")) {
			user.setName(json.getString("name"));
		}

		if (json.has("surname") && !json.isNull("surname")) {
			user.setSurname(json.getString("surname"));
		}

		if (json.has("username")) {
			user.setUsername(json.getString("username"));
		}
		return user;
	}
}
