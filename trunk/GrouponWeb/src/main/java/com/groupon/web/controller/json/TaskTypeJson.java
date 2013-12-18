package com.groupon.web.controller.json;

import java.util.ArrayList;
import java.util.List;

import com.groupon.web.dao.model.TaskType;
import com.groupon.web.dao.model.TaskTypeField;


public class TaskTypeJson {
	
	private long id;
	private String name;
	private String description;
	private List<TaskTypeFieldJson> fields;
	private String needType;
	private long communityId;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}


	public String getNeedType() {
		return needType;
	}

	public void setNeedType(String needType) {
		this.needType = needType;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getCommunityId() {
		return communityId;
	}

	public void setCommunityId(long communityId) {
		this.communityId = communityId;
	}
	
	public List<TaskTypeFieldJson> getFields() {
		return fields;
	}

	public void setFields(List<TaskTypeFieldJson> fields) {
		this.fields = fields;
	}
	public static TaskTypeJson convert(TaskType taskType){
		TaskTypeJson taskTypeJson = new TaskTypeJson();
		taskTypeJson.setName(taskType.getName());
		taskTypeJson.setDescription(taskType.getDescription());
		taskTypeJson.setNeedType(taskType.getNeedType().toString());
		taskTypeJson.setCommunityId(taskType.getCommunity().getId());
		List<TaskTypeFieldJson> taskTypeFieldJsons = new ArrayList<TaskTypeFieldJson>();
		List<TaskTypeField> TaskTypeFields = taskType.getFields();
			for (TaskTypeField taskTypeField: TaskTypeFields){
				taskTypeFieldJsons.add(TaskTypeFieldJson.convert(taskTypeField));			
			}
		taskTypeJson.setFields(taskTypeFieldJsons);	
			
	
		
		return taskTypeJson;
		
	}



}
