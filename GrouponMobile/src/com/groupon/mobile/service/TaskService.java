package com.groupon.mobile.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.groupon.mobile.GrouponApplication;
import com.groupon.mobile.conn.ConnectionUtils;
import com.groupon.mobile.conn.GrouponCallback;
import com.groupon.mobile.conn.GrouponTask;
import com.groupon.mobile.exception.GrouponException;
import com.groupon.mobile.model.ReplyField;
import com.groupon.mobile.model.ReplyFieldAttribute;
import com.groupon.mobile.model.Task;
import com.groupon.mobile.model.TaskAttribute;
import com.groupon.mobile.utils.Constants;
import com.groupon.mobile.utils.DateUtils;

public class TaskService {
	private GrouponApplication app;

	public TaskService(GrouponApplication app) {
		this.app = app;
	}

	public void getReplyFields(final long taskId, GrouponCallback<List<ReplyField>> callback) {
		GrouponTask<List<ReplyField>> taskTask = new GrouponTask<List<ReplyField>>(callback) {

			@Override
			public List<ReplyField> run() throws GrouponException {
				String url = Constants.SERVER + "task/getReplyForm";
				Map<String, String> idMap = new HashMap<String, String>();
				idMap.put("taskId", "" + taskId);
				List<ReplyField> replyFields = new ArrayList<ReplyField>();
				JSONObject json = ConnectionUtils.makeGetRequest(url, idMap, app.getAuthToken());
				try {
					JSONArray replyFieldsJson = json.getJSONArray("fields");

					for (int i = 0; i < replyFieldsJson.length(); i++) {
						replyFields.add(ConvertJSONObjectToReplyField(replyFieldsJson.getJSONObject(i)));
					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				return replyFields;
			}

		};
		GrouponTask.execute(taskTask);
	}

	private ReplyField ConvertJSONObjectToReplyField(JSONObject json) throws JSONException {
		ReplyField replyField = new ReplyField();
		if (json.has("name")) {
			replyField.setName(json.getString("name"));
		}
		if (json.has("fieldType")) {
			replyField.setFieldType("fieldType");
		}
		if (json.has("attributes")) {
			JSONArray attributesJsonArray = json.getJSONArray("attributes");
			List<ReplyFieldAttribute> replyFieldAttributes = new ArrayList<ReplyFieldAttribute>();
			for (int i = 0; i < attributesJsonArray.length(); i++) {
				ReplyFieldAttribute replyFieldAttribute = ConvertJsonToReplyFieldAttribute(attributesJsonArray.getJSONObject(i));
				replyFieldAttributes.add(replyFieldAttribute);
			}
		}

		return replyField;

	}

	private ReplyFieldAttribute ConvertJsonToReplyFieldAttribute(JSONObject json) throws JSONException {
		ReplyFieldAttribute replyFieldAttribute = new ReplyFieldAttribute();
		if (json.has("name")) {
			replyFieldAttribute.setName(json.getString("name"));
		}
		if (json.has("value")) {
			replyFieldAttribute.setValue(json.getString("value"));
		}
		return replyFieldAttribute;
	}

	public void getCommunityTasks(final long communityId, GrouponCallback<ArrayList<Task>> callback) {
		GrouponTask<ArrayList<Task>> taskTask = new GrouponTask<ArrayList<Task>>(callback) {
			public ArrayList<Task> run() throws GrouponException {
				String url = Constants.SERVER + "task/getCommunityTasks";
				Map<String, String> idMap = new HashMap<String, String>();
				idMap.put("communityId", "" + communityId);
				JSONObject json = ConnectionUtils.makeGetRequest(url, idMap, app.getAuthToken());
				ArrayList<Task> tasks = new ArrayList<Task>();
				if (json.has("tasks")) {
					try {
						JSONArray tasksJson = json.getJSONArray("tasks");

						for (int i = 0; i < tasksJson.length(); i++) {
							tasks.add(convertJSONObjectToTask(tasksJson.getJSONObject(i)));
						}
					} catch (JSONException e) {
						throw new GrouponException("An error occured while parsing json returned from the server!");
					}
				} else {
					throw new GrouponException("An unknown error occured while creating the community :(");
				}

				return tasks;
			}
		};

		GrouponTask.execute(taskTask);

	}

	public void getFollowedTasks(GrouponCallback<ArrayList<Task>> callback) {
		GrouponTask<ArrayList<Task>> taskTask = new GrouponTask<ArrayList<Task>>(callback) {
			public ArrayList<Task> run() throws GrouponException {
				String url = Constants.SERVER + "task/getFollowedTasks";

				JSONObject json = ConnectionUtils.makeGetRequest(url, null, app.getAuthToken());
				ArrayList<Task> tasks = new ArrayList<Task>();
				if (json.has("tasks")) {
					try {
						JSONArray tasksJson = json.getJSONArray("tasks");

						for (int i = 0; i < tasksJson.length(); i++) {
							tasks.add(convertJSONObjectToTask(tasksJson.getJSONObject(i)));
						}
					} catch (JSONException e) {
						throw new GrouponException("An error occured while parsing json returned from the server!");
					}
				} else {
					throw new GrouponException("An unknown error occured while creating the community :(");
				}

				return tasks;
			}
		};

		GrouponTask.execute(taskTask);

	}

	public void unFollowTask(final long taskId, final GrouponCallback<Task> callback) {
		GrouponTask<Task> taskTask = new GrouponTask<Task>(callback) {

			@Override
			public Task run() throws GrouponException {
				String url = Constants.SERVER + "task/unfollowTask";
				Map<String, String> idMap = new HashMap<String, String>();
				idMap.put("taskId", "" + taskId);
				JSONObject json = ConnectionUtils.makePostRequest(url, idMap, app.getAuthToken());
				Task task = new Task();
				try {
					task.setFollowerCount(json.getLong("followerCount"));
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				return task;
			}

		};
		GrouponTask.execute(taskTask);
	}

	public void followTask(final long taskId, final GrouponCallback<Task> callback) {
		GrouponTask<Task> taskTask = new GrouponTask<Task>(callback) {

			@Override
			public Task run() throws GrouponException {
				String url = Constants.SERVER + "task/followTask";
				Map<String, String> idMap = new HashMap<String, String>();
				idMap.put("taskId", "" + taskId);
				JSONObject json = ConnectionUtils.makePostRequest(url, idMap, app.getAuthToken());
				Task task = new Task();
				try {
					task.setFollowerCount(json.getLong("followerCount"));
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				return task;
			}

		};
		GrouponTask.execute(taskTask);
	}

	public void createTask(final Task task, final long communityId, final GrouponCallback<Task> callback) {
		GrouponTask<Task> taskTask = new GrouponTask<Task>(callback) {
			public Task run() throws GrouponException {
				String url = Constants.SERVER + "task/create";

				JSONObject json = new JSONObject();
				try {
					json.put("name", task.getName());
					json.put("description", task.getDescription());
					json.put("deadline", task.getDeadline());

					json.put("communityId", communityId);

					json.put("attributes", taskAttributestoJSONArray(task.getAttributes()));
					json.put("type", task.getNeedType());
					json.put("requirementName", task.getRequirementName());
					json.put("requirementQuantity", task.getRequirementQuantity());
					json.put("tags", new JSONArray());
				} catch (JSONException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}

				JSONObject jsonRes = ConnectionUtils.makePostRequest(url, null, json, app.getAuthToken());

				if (jsonRes.has("taskId")) {
					try {
						Long id = jsonRes.getLong("taskId");
						task.setId(id);
					} catch (JSONException e) {
						throw new GrouponException("An error occured while parsing json returned from the server!");
					}
				} else {
					throw new GrouponException("An unknown error occured while creating the task :(");
				}

				return task;
			}
		};

		GrouponTask.execute(taskTask);
	}

	public void getTask(final long id, final GrouponCallback<Task> callback) {
		GrouponTask<Task> taskTask = new GrouponTask<Task>(callback) {
			public Task run() throws GrouponException {
				String url = Constants.SERVER + "task/mobileShow/" + id;

				JSONObject json = ConnectionUtils.makePostRequest(url, null, app.getAuthToken());

				Task task = null;
				try {

					task = convertJSONObjectToTask(json.getJSONObject("task"));

					if (json.has("taskAttributes")) {
						JSONObject j = json.getJSONObject("taskAttributes");
						Map<String, List<String>> m = convertJSONObjectToTaskAttribute(j);
						task.setAttributeMap(m);
					}
					if (json.has("isFollower"))
						task.setFollower(json.getBoolean("isFollower"));

				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				return task;
			}
		};

		GrouponTask.execute(taskTask);

	}

	private Map<String, List<String>> convertJSONObjectToTaskAttribute(JSONObject json) throws JSONException {

		Map<String, List<String>> m = new HashMap<String, List<String>>();
		Iterator<?> iter = json.keys();
		while (iter.hasNext()) {
			String key = (String) iter.next();
			Object o = json.get(key);
			List<String> l = new ArrayList<String>();

			if (o instanceof String) {
				l.add((String) o);
				m.put(key, l);
			} else if (o instanceof JSONArray) {
				JSONArray values = (JSONArray) o;
				for (int i = 0; i < values.length(); i++) {
					String value = values.getString(i);

					l.add(value);
				}
				m.put(key, l);
			}
		}
		return m;
	}

	protected Task convertJSONObjectToTask(JSONObject json) throws JSONException {

		Task task = new Task();
		if (json.has("auth")) {
			String auth = json.getString("auth");
			app.setAuthToken(auth);
		}

		if (json.has("user")) {
			json = json.getJSONObject("user");
		}

		if (json.has("title")) {
			task.setName(json.getString("title"));
		}

		if (json.has("description")) {
			task.setDescription(json.getString("description"));
		}

		if (json.has("id")) {
			task.setId(json.getLong("id"));
		}

		if (json.has("ownerUsername")) {
			task.setOwnerUsername(json.getString("ownerUsername"));
		}

		if (json.has("ownerId")) {
			task.setOwnerId(json.getLong("ownerId"));
		}

		if (json.has("communityName")) {
			task.setCommunityName(json.getString("communityName"));
		}

		if (json.has("needType")) {
			task.setNeedType(json.getString("needType"));
		}

		if (json.has("status")) {
			task.setStatus(json.getString("status"));
		}

		if (json.has("deadline")) {
			Long l = json.getLong("deadline");
			Date d = new Date(l);
			DateUtils.calculateDayDifference(d);
			task.setDeadlineCount((long) DateUtils.calculateDayDifference(d));
		}

		if (json.has("requirementName"))
			task.setRequirementName(json.getString("requirementName"));

		if (json.has("requirementQuantity"))
			task.setRequirementQuantity(json.getInt("requirementQuantity"));

		if (json.has("followerCount")) {
			task.setFollowerCount(json.getLong("followerCount"));
		}

		if (json.has("communityId")) {
			task.setCommunityId(json.getLong("communityId"));
		}

		if (json.has("createDate")) {
			task.setCreateDate(json.getLong("createDate"));
		}

		return task;
	}

	private JSONArray taskAttributestoJSONArray(List<TaskAttribute> taskAttributes) {
		JSONArray array = new JSONArray();

		for (TaskAttribute taskAttribute : taskAttributes) {
			JSONObject json = new JSONObject();
			try {
				json.put("name", taskAttribute.getName());
				json.put("value", taskAttribute.getValue());
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			array.put(json);
		}
		return array;
	}

}
