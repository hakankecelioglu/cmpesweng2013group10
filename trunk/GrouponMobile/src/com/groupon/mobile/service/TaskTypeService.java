package com.groupon.mobile.service;

import java.util.ArrayList;
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
import com.groupon.mobile.model.FieldAttribute;
import com.groupon.mobile.model.FieldType;
import com.groupon.mobile.model.NeedType;
import com.groupon.mobile.model.TaskType;
import com.groupon.mobile.model.TaskTypeField;
import com.groupon.mobile.utils.Constants;

public class TaskTypeService {

	private GrouponApplication app;

	public TaskTypeService(GrouponApplication app) {
		this.app = app;
	}

	public void getTaskTypes(final long id, final GrouponCallback<ArrayList<TaskType>> callback) {
		GrouponTask<ArrayList<TaskType>> TaskTypeTask = new GrouponTask<ArrayList<TaskType>>(callback) {
			public ArrayList<TaskType> run() throws GrouponException {
				String url = Constants.SERVER + "community/taskTypes";
				Map<String, String> idMap = new HashMap<String, String>();
				idMap.put("communityId", "" + id);
				JSONObject json = ConnectionUtils.makeGetRequest(url, idMap, app.getAuthToken());

				ArrayList<TaskType> TaskTypes = new ArrayList<TaskType>();
				try {
					JSONArray taskTypesJson = json.getJSONArray("taskTypes");
					for (int i = 0; i < taskTypesJson.length(); i++) {
						TaskTypes.add(convertJsonToTaskType(taskTypesJson.getJSONObject(i)));
					}
				} catch (JSONException e) {
					throw new GrouponException("An error occured while parsing json returned from the server!");
				}
				return TaskTypes;
			}
		};

		GrouponTask.execute(TaskTypeTask);

	}

	public void getTaskType(final long id, final GrouponCallback<TaskType> callback) {
		GrouponTask<TaskType> TaskTypeTask = new GrouponTask<TaskType>(callback) {
			public TaskType run() throws GrouponException {
				String url = Constants.SERVER + "taskType";
				Map<String, String> idMap = new HashMap<String, String>();
				idMap.put("taskTypeId", "" + id);
				JSONObject json = ConnectionUtils.makePostRequest(url, idMap, app.getAuthToken());

				TaskType taskType = null;
				try {
					taskType = convertJSONObjectToTaskType(json);
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				return taskType;
			}
		};

		GrouponTask.execute(TaskTypeTask);

	}

	private TaskType convertJsonToTaskType(JSONObject json) throws JSONException {
		if (json.has("auth")) {
			String auth = json.getString("auth");
			app.setAuthToken(auth);
		}
		TaskType tasktype = new TaskType();

		if (json.has("name")) {
			tasktype.setName(json.getString("name"));
		}

		if (json.has("id")) {
			tasktype.setId(json.getLong("id"));
		}
		return tasktype;
	}

	private TaskType convertJSONObjectToTaskType(JSONObject json) throws JSONException {

		json = json.getJSONObject("taskType");
		TaskType taskType = new TaskType();
		if (json.has("name"))
			taskType.setName(json.getString("name"));
		if (json.has("description"))
			taskType.setName(json.getString("description"));
		if (json.has("needType")) {
			String needTypeStr = json.getString("needType");
			NeedType needType = NeedType.valueOf(needTypeStr);
			taskType.setNeedType(needType);
		}
		if (json.has("communityId"))
			taskType.setCommunityId(json.getLong("communityId"));

		List<TaskTypeField> taskTypeFields = new ArrayList<TaskTypeField>();
		if (json.has("fields")) {
			JSONArray fields = json.getJSONArray("fields");
			for (int i = 0; i < fields.length(); i++) {
				JSONObject field = fields.getJSONObject(i);
				TaskTypeField taskTypeField = convertJSONObjectToTaskTypeField(field);
				taskTypeField.setTaskType(taskType);
				taskTypeFields.add(taskTypeField);
			}
		}

		taskType.setFields(taskTypeFields);
		return taskType;
	}

	private TaskTypeField convertJSONObjectToTaskTypeField(JSONObject field) throws JSONException {

		TaskTypeField taskTypeField = new TaskTypeField();

		String name = field.getString("name");
		String type = field.getString("fieldType");
		FieldType fieldType = FieldType.valueOf(type);

		List<FieldAttribute> attributes = new ArrayList<FieldAttribute>();
		if (field.has("attributes")) {
			JSONArray attributesObj = field.getJSONArray("attributes");
			for (int i = 0; i < attributesObj.length(); i++) {
				JSONObject attributeJson = attributesObj.getJSONObject(i);
				FieldAttribute attribute = new FieldAttribute();
				if (attributeJson.has("value"))
					attribute.setValue(attributeJson.getString("value"));

				if (attributeJson.has("name"))
					attribute.setName(attributeJson.getString("name"));

				attribute.setTaskTypeField(taskTypeField);
				attributes.add(attribute);
			}
		}

		taskTypeField.setName(name);
		taskTypeField.setFieldType(fieldType);
		taskTypeField.setAttributes(attributes);
		return taskTypeField;
	}

	public void createTaskType(final TaskType TaskType, final GrouponCallback<TaskType> callback) {
		GrouponTask<TaskType> TaskTypeTask = new GrouponTask<TaskType>(callback) {
			public TaskType run() throws GrouponException {
				String url = Constants.SERVER + "community/createTaskType";
				JSONObject json = convertTaskTypeToJson(TaskType);
				ConnectionUtils.makePostRequest(url, null, json, app.getAuthToken());

				return TaskType;
			}
		};

		GrouponTask.execute(TaskTypeTask);
	}

	private JSONObject convertTaskTypeToJson(TaskType TaskType) {
		JSONObject json = new JSONObject();
		try {
			json.put("name", TaskType.getName());
			json.put("description", TaskType.getDescription());
			json.put("needType", TaskType.getNeedType().toString());
		

			List<TaskTypeField> taskTypeFields = TaskType.getFields();
			if(taskTypeFields!=null){
			JSONArray array = new JSONArray();
				for (TaskTypeField TaskTypeField : taskTypeFields) {
					JSONObject j = convertTaskTypeFieldToJson(TaskTypeField);
					array.put(j);
				}
			json.put("fields", array);
			}
			

			List<TaskTypeField> replyFields = TaskType.getFields();
			if(replyFields!=null){
				JSONArray arrayReplyFields = new JSONArray();
				for (TaskTypeField TaskTypeField : replyFields) {
					JSONObject j = convertTaskTypeFieldToJson(TaskTypeField);
					arrayReplyFields.put(j);
				}
			json.put("replyFields", arrayReplyFields);
			}

			json.put("communityId", TaskType.getCommunityId());

		} catch (JSONException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		return json;

	}

	private JSONObject convertTaskTypeFieldToJson(TaskTypeField TaskTypeField) {
		JSONObject json = new JSONObject();
		try {
			json.put("name", TaskTypeField.getName());
			FieldType ft = TaskTypeField.getFieldType();
			json.put("type", ft.toString());

			JSONArray array = new JSONArray();

			List<FieldAttribute> FieldAttributes = TaskTypeField.getAttributes();
			if (FieldAttributes != null) {
				for (FieldAttribute FieldAttribute : FieldAttributes)
					array.put(convertFieldAttributeToJson(FieldAttribute));
				json.put("attributes", array);
			}
		} catch (JSONException e) {

		}
		return json;
	}

	private JSONObject convertFieldAttributeToJson(FieldAttribute FieldAttribute) {
		JSONObject json = new JSONObject();
		try {
			json.put("name", FieldAttribute.getName());
			json.put("value", FieldAttribute.getValue());

		} catch (JSONException e) {

		}
		return json;
	}
}
