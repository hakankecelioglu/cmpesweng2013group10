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
/**
 * Provides service functions related to users such as login, signup.
 * @author sedrik
 *
 */
public class UserService {

	private GrouponApplication app;

	public UserService(GrouponApplication app) {
		this.app = app;
	}
	/**
	 * Make a post request to log user in
	 * @param username User name of user
	 * @param password password of user
	 * @param callback callback passed as parameter to GrouponTask
	 */
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
	/**
	 * Make  post request to signup request to sign up 
	 * @param username Requested user name of user
	 * @param password Requested password password of user
	 * @param email Requested email password of user
	 * @param callback passed as parameter to GrouponTask
	 */
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
	/**
	 * Make a get request to return user's information data
	 * @param userId id of User requested
	 * @param callback passed as parameter to GrouponTask
	 */
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
	/**
	 * Convert JSONObject to User
	 * @param json JSONObject converted
	 * @return User created.
	 * @throws JSONException
	 */
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
