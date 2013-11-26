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
import com.groupon.mobile.model.Community;
import com.groupon.mobile.utils.Constants;

public class CommunityService {
	private GrouponApplication app;

	public CommunityService(GrouponApplication app) {
		this.app = app;
	}

	public void createCommunity(final Community community, final GrouponCallback<Community> callback) {
		GrouponTask<Community> communityTask = new GrouponTask<Community>(callback) {
			public Community run() throws GrouponException {
				String url = Constants.SERVER + "createCommunityAndroid";

				Map<String, String> communityMap = new HashMap<String, String>();
				communityMap.put("name", community.getName());
				communityMap.put("description", community.getDescription());

				JSONObject json = ConnectionUtils.makePostRequest(url, communityMap, app.getAuthToken());

				if (json.has("communityId")) {
					try {
						Long id = json.getLong("communityId");
						community.setId(id);
					} catch (JSONException e) {
						throw new GrouponException("An error occured while parsing json returned from the server!");
					}
				} else {
					throw new GrouponException("An unknown error occured while creating the community :(");
				}

				return community;
			}
		};

		GrouponTask.execute(communityTask);
	}
}
