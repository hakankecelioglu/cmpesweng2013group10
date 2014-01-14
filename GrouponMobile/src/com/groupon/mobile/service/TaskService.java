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

import android.util.Log;

import com.groupon.mobile.GrouponApplication;
import com.groupon.mobile.conn.ConnectionUtils;
import com.groupon.mobile.conn.GrouponCallback;
import com.groupon.mobile.conn.GrouponTask;
import com.groupon.mobile.exception.GrouponException;
import com.groupon.mobile.model.FieldAttribute;
import com.groupon.mobile.model.Task;
import com.groupon.mobile.model.TaskAttribute;
import com.groupon.mobile.model.TaskReply;
import com.groupon.mobile.model.TaskTypeField;
import com.groupon.mobile.model.User;
import com.groupon.mobile.utils.Constants;
import com.groupon.mobile.utils.DateUtils;

/**
 * Provides service functions related to tasks such as creating task, returning
 * task, following and unfollowing tasks.
 * 
 * @author serkan
 * 
 */
public class TaskService {
	private GrouponApplication app;

	public TaskService(GrouponApplication app) {
		this.app = app;
	}

	public void getReplyFields(final long taskId, GrouponCallback<List<TaskTypeField>> callback) {
		GrouponTask<List<TaskTypeField>> taskTask = new GrouponTask<List<TaskTypeField>>(callback) {

			@Override
			public List<TaskTypeField> run() throws GrouponException {
				String url = Constants.SERVER + "task/getReplyForm";
				Map<String, String> idMap = new HashMap<String, String>();
				idMap.put("taskId", "" + taskId);
				TaskTypeService taskTypeService = new TaskTypeService(app);

				List<TaskTypeField> replyFields = new ArrayList<TaskTypeField>();
				JSONObject json = ConnectionUtils.makeGetRequest(url, idMap, app.getAuthToken());
				try {
					JSONArray replyFieldsJson = json.getJSONArray("fields");

					for (int i = 0; i < replyFieldsJson.length(); i++) {
						replyFields.add(taskTypeService.convertJSONObjectToTaskTypeField(replyFieldsJson.getJSONObject(i)));
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

	/**
	 * Make a get request and returns tasks of a community
	 * 
	 * @param communityId
	 *            id of community whose tasks are returned
	 * @param callback
	 *            callback passed as parameter to GrouponTask
	 */
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

	/**
	 * Makes a get request to return followed tasks of user
	 * 
	 * @param callback
	 *            callback passed as parameter to GrouponTask
	 */
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

	/**
	 * make a get request get home feed tasks of user
	 * 
	 * @param callback
	 *            callback passed as parameter to GrouponTask
	 */
	public void getHomeFeedTasks(GrouponCallback<ArrayList<Task>> callback) {
		GrouponTask<ArrayList<Task>> taskTask = new GrouponTask<ArrayList<Task>>(callback) {
			public ArrayList<Task> run() throws GrouponException {
				String url = Constants.SERVER + "task/getHomeFeedTasks";

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

	/**
	 * makes a post request to unfollow a task
	 * 
	 * @param taskId
	 *            id of task unfollowed
	 * @param callback
	 *            callback passed as parameter to GrouponTask
	 */
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

	/**
	 * makes a post request to follow a task
	 * 
	 * @param taskId
	 *            id of task followed
	 * @param callback
	 *            callback passed as parameter to GrouponTask
	 */
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

	/**
	 * makes a post request to reply a task
	 * 
	 * @param taskId
	 *            id of task replied
	 * @param replyFields
	 *            list of TaskAttributes which constitues reply data
	 * @param callback
	 *            callback passed as parameter to GrouponTask
	 */
	public void replyTask(final long taskId, final List<TaskAttribute> replyFields, final GrouponCallback<Integer> callback) {
		GrouponTask<Integer> taskTask = new GrouponTask<Integer>(callback) {
			public Integer run() throws GrouponException {
				String url = Constants.SERVER + "task/reply";

				JSONObject json = new JSONObject();
				try {
					json.put("taskId", taskId);
					json.put("fields", taskAttributestoJSONArray(replyFields));
					JSONObject response = ConnectionUtils.makePostRequest(url, null, json, app.getAuthToken());

					if (response.has("requirementQuantity")) {
						return response.getInt("requirementQuantity");
					} else {
						return null;
					}
				} catch (JSONException e) {
					throw new GrouponException("An error occured!");
				}
			}
		};

		GrouponTask.execute(taskTask);
	}

	/**
	 * makes a post request to create task
	 * 
	 * @param task
	 *            Task instance which is posted
	 * @param communityId
	 *            id of community which task belongs to
	 * @param callback
	 *            callback passed as parameter to GrouponTask
	 */
	public void createTask(final Task task, final long communityId, final GrouponCallback<Task> callback) {
		GrouponTask<Task> taskTask = new GrouponTask<Task>(callback) {
			public Task run() throws GrouponException {
				String url = Constants.SERVER + "task/create";

				JSONObject json = new JSONObject();
				try {
					json.put("name", task.getName());
					json.put("description", task.getDescription());
					json.put("deadline", task.getDeadline());
					json.put("taskType", task.getTaskTypeId());
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

	/**
	 * Makes a post request and returns a task
	 * 
	 * @param id
	 *            id of returned task
	 * @param callback
	 *            callback passed as parameter to GrouponTask
	 */
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

	/**
	 * Makes a get request to get the list of task replies
	 * 
	 * @param taskId
	 *            id of the task whose replies will be here soon
	 * @param callback
	 *            callback function which will be called when success or error
	 */
	public void getTaskReplies(final Long taskId, final GrouponCallback<List<TaskReply>> callback) {
		GrouponTask<List<TaskReply>> task = new GrouponTask<List<TaskReply>>(callback) {
			public List<TaskReply> run() throws GrouponException {
				String url = Constants.SERVER + "task/replies";

				Map<String, String> params = new HashMap<String, String>();
				params.put("taskId", String.valueOf(taskId));

				List<TaskReply> list = new ArrayList<TaskReply>();

				JSONObject response = ConnectionUtils.makeGetRequest(url, params, app.getAuthToken());
				if (response.has("replies")) {
					try {
						JSONArray arr = response.getJSONArray("replies");
						for (int i = 0; i < arr.length(); i++) {
							JSONObject replyObj = arr.getJSONObject(i);
							list.add(convertJSONObjectToTaskReply(replyObj));
						}
					} catch (Exception e) {
						Log.e("groupon", e.getMessage());
					}
				}

				return list;
			}
		};

		GrouponTask.execute(task);
	}

	private TaskReply convertJSONObjectToTaskReply(JSONObject obj) throws JSONException {
		TaskReply taskReply = new TaskReply();

		if (obj.has("replier")) {
			JSONObject replier = obj.getJSONObject("replier");
			taskReply.setUser(convertJsonToUser(replier));
		}

		if (obj.has("date")) {
			Long millis = obj.getLong("date");
			taskReply.setDate(new Date(millis));
		}

		List<FieldAttribute> attributes = new ArrayList<FieldAttribute>();
		if (obj.has("attributes")) {
			JSONArray arr = obj.getJSONArray("attributes");
			for (int i = 0; i < arr.length(); i++) {
				JSONObject attr = arr.getJSONObject(i);
				String name = attr.getString("name");
				String value = attr.getString("value");

				FieldAttribute attribute = new FieldAttribute();
				attribute.setName(name);
				attribute.setValue(value);
				attributes.add(attribute);
			}
		}
		taskReply.setAttributes(attributes);

		return taskReply;
	}

	private User convertJsonToUser(JSONObject json) throws JSONException {
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

	/**
	 * Converts JSONObject to Task
	 * 
	 * @param json
	 *            JSON data converted
	 * @return Task created
	 * @throws JSONException
	 */
	private Task convertJSONObjectToTask(JSONObject json) throws JSONException {

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

		if (json.has("follower")) {
			task.setFollower(json.getBoolean("follower"));
		}

		return task;
	}

	/**
	 * convert TaskAttribute list to JSONArray
	 * 
	 * @param taskAttributes
	 *            TaskAttribute list converted
	 * @return JSONArray o task attributes
	 */
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
