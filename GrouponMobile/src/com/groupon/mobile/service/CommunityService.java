package com.groupon.mobile.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.groupon.mobile.GrouponApplication;
import com.groupon.mobile.conn.ConnectionUtils;
import com.groupon.mobile.conn.GrouponCallback;
import com.groupon.mobile.conn.GrouponTask;
import com.groupon.mobile.exception.GrouponException;
import com.groupon.mobile.model.Community;
import com.groupon.mobile.utils.Constants;
/**
 * Provides service functions related to community types such as creating community, returning communities, leaving and joining communities.
 * @author serkan
 *
 */
public class CommunityService {
	private GrouponApplication app;

	public CommunityService(GrouponApplication app) {
		this.app = app;
	}
	/**
	 * Makes a post request to create community
	 * @param community	Community instance to create post data
	 * @param callback	callback passed as parameter to GrouponTask
	 */
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
	/**
	 * Makes a post request and returns list of comunities
	 * @param isAll returns all communities if true , communities of user if false
	 * @param callback callback passed as parameter to GrouponTask
	 */
	public void getCommunities(final boolean isAll, final GrouponCallback<ArrayList<Community>> callback) {
		GrouponTask<ArrayList<Community>> communityTask = new GrouponTask<ArrayList<Community>>(callback) {
			public ArrayList<Community> run() throws GrouponException {
				String url = Constants.SERVER + "getCommunitiesOfUser";

				if (isAll)
					url = Constants.SERVER + "getAllCommunities";
				JSONObject json = ConnectionUtils.makePostRequest(url, null, app.getAuthToken());
				ArrayList<Community> communities = new ArrayList<Community>();
				if (json.has("communities")) {
					try {
						JSONArray communitiesJson = json.getJSONArray("communities");

						for (int i = 0; i < communitiesJson.length(); i++) {
							communities.add(convertJsonToCommunity(communitiesJson.getJSONObject(i)));
						}
					} catch (JSONException e) {
						throw new GrouponException("An error occured while parsing json returned from the server!");
					}
				} else {
					throw new GrouponException("An unknown error occured while creating the community :(");
				}

				return communities;
			}
		};

		GrouponTask.execute(communityTask);
	}
	/**
	 * Make a post request and returns a community
	 * @param communityId	Id of a community requested
	 * @param callback  callback passed as parameter to GrouponTask
	 */
	public void getCommunity(final Long communityId, final GrouponCallback<Community> callback) {
		GrouponTask<Community> communityTask = new GrouponTask<Community>(callback) {
			public Community run() throws GrouponException {
				String url = Constants.SERVER + "communityMobile/" + communityId;
				JSONObject obj = ConnectionUtils.makePostRequest(url, null, app.getAuthToken());
				try {
					JSONObject community = obj.getJSONObject("community");
					Community c = convertJsonToCommunity(community);
					return c;
				} catch (JSONException e) {
					throw new GrouponException("An error occured while parsing json returned from the server!");
				}
			}
		};
		GrouponTask.execute(communityTask);
	}
	/**
	 * Convert JSONObject to Community
	 * @param json json converted
	 * @return Community created
	 * @throws JSONException
	 */
	private Community convertJsonToCommunity(JSONObject json) throws JSONException {
		Community community = new Community();

		if (json.has("name")) {
			community.setName(json.getString("name"));
		}

		if (json.has("description")) {
			community.setDescription(json.getString("description"));
		}

		if (json.has("id")) {
			community.setId(json.getLong("id"));
		}

		if (json.has("picture")) {
			community.setPicture(json.getString("picture"));
		}

		
		if (json.has("ownerUsername")) {
			community.setOwnerUsername(json.getString("ownerUsername"));
		}
		
		if (json.has("ownerId")) {
			community.setOwnerId(json.getLong("ownerId"));
		}

		return community;
	}
	/**
	 * Makes a post request to leave community
	 * @param communityId id of community left
	 * @param callback callback passed as parameter to GrouponTask
	 */
	public void leaveCommunity(final long communityId, GrouponCallback<Community> callback) {
		GrouponTask<Community> taskCommunity = new GrouponTask<Community>(callback) {

			@Override
			public Community run() throws GrouponException {
				String url = Constants.SERVER + "community/mobileleave";
				Map<String, String> idMap = new HashMap<String, String>();
				idMap.put("communityId", "" + communityId);
				ConnectionUtils.makePostRequest(url, idMap, app.getAuthToken());
				Community community = new Community();

				return community;
			}

		};
		GrouponTask.execute(taskCommunity);
	}
	/**
	 * Makes a post request to join community
	 * @param communityId id of community joined
	 * @param callback callback passed as parameter to GrouponTask
	 */
	public void joinCommunity(final long communityId, GrouponCallback<Community> callback) {
		GrouponTask<Community> taskCommunity = new GrouponTask<Community>(callback) {

			@Override
			public Community run() throws GrouponException {
				String url = Constants.SERVER + "community/mobilejoin";
				Map<String, String> idMap = new HashMap<String, String>();
				idMap.put("communityId", "" + communityId);
				ConnectionUtils.makePostRequest(url, idMap, app.getAuthToken());
				Community community = new Community();

				return community;
			}

		};
		GrouponTask.execute(taskCommunity);

	}
}
