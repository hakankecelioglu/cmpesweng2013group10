package com.groupon.mobile.service;

import java.util.List;

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
import com.groupon.mobile.model.TaskType;
import com.groupon.mobile.model.TaskTypeField;
import com.groupon.mobile.utils.Constants;

public class TaskTypeService {

	private GrouponApplication app;

	public TaskTypeService(GrouponApplication app) {
		this.app = app;
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
			JSONArray array = new JSONArray();

			List<TaskTypeField> TaskTypeFields = TaskType.getFields();
			for (TaskTypeField TaskTypeField : TaskTypeFields) {
				JSONObject j = convertTaskTypeFieldToJson(TaskTypeField);
				array.put(j);
			}
			json.put("fields", array);

			json.put("communityId", TaskType.getCommunity().getId());

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
