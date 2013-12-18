package com.groupon.mobile.service;



import java.util.HashMap;
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
import com.groupon.mobile.model.Community;
import com.groupon.mobile.model.Task;
import com.groupon.mobile.model.TaskAttribute;
import com.groupon.mobile.utils.Constants;

public class TaskService {
	private GrouponApplication app;

	public TaskService(GrouponApplication app) {
		this.app = app;
	}
	public void unFollowTask(final long taskId, final GrouponCallback<Task> callback){
		GrouponTask<Task> taskTask = new GrouponTask<Task>(callback) {

			@Override
			public Task run() throws GrouponException {
				String url = Constants.SERVER+"task/unfollowTask";
				Map<String,String> idMap= new HashMap<String, String>();
				idMap.put("taskId", ""+taskId);
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
	public void followTask(final long taskId, final GrouponCallback<Task> callback){
		GrouponTask<Task> taskTask = new GrouponTask<Task>(callback) {

			@Override
			public Task run() throws GrouponException {
				String url = Constants.SERVER+"task/followTask";
				Map<String,String> idMap= new HashMap<String, String>();
				idMap.put("taskId", ""+taskId);
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
					json.put("name",task.getName());
					json.put("description", task.getDescription());
					json.put("deadline",task.getDeadline());
					
					json.put("communityId",communityId);
					
					json.put("attributes",taskAttributestoJSONArray(task.getAttributes()));
					json.put("type",task.getNeedType());
					json.put("requirementName",task.getRequirementName());
					json.put("requirementQuantity", task.getRequirementQuantity());
					json.put("tags",new JSONArray());
				} catch (JSONException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}

				JSONObject jsonRes = ConnectionUtils.makePostRequest(url, null,json, app.getAuthToken());

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
				String url = Constants.SERVER + "task/mobileShow/"+id;

				JSONObject json = ConnectionUtils.makePostRequest(url, null, app.getAuthToken());
				
				Task task = null;
				try {
					
					task = convertJSONObjectToTask(json.getJSONObject("task"));
					if(json.has("isFollower"))
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
	protected Task convertJSONObjectToTask(JSONObject json) throws JSONException {

		Task task = new Task();
		if (json.has("auth")) {
			String auth = json.getString("auth");
			app.setAuthToken(auth);
		}
		Community community = new Community();
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
		
		if (json.has("ownerUsername")){
			task.setOwnerUsername(json.getString("ownerUsername"));
		}
		if(json.has("ownerId"))
			task.setOwnerId(json.getLong("ownerId"));
		if(json.has("communityName"))
			task.setCommunityName(json.getString("communityName"));
		if(json.has("needType"))
			task.setNeedType(json.getString("needType"));
		if(json.has("status"))
			task.setStatus(json.getString("status"));
		if(json.has("deadline"))
			task.setDeadlineCount(json.getLong("deadline"));
		if(json.has("requirementName"))
			task.setRequirementName(json.getString("requirementName"));
		if(json.has("requirementQuantity"))
			task.setRequirementQuantity(json.getInt("requirementQuantity"));

		if(json.has("followerCount"))
			task.setFollowerCount(json.getLong("followerCount"));

		
		
		return task;
	}

	private JSONArray taskAttributestoJSONArray(List<TaskAttribute> taskAttributes){
		JSONArray array=new JSONArray();
		
		
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
