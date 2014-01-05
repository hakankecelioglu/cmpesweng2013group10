package com.groupon.mobile.model;

import java.util.List;

public class TaskType {

	private long id;
	private String name;
	private String description;
	private List<TaskTypeField> fields;
	private NeedType needType;
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

	public List<TaskTypeField> getFields() {
		return fields;
	}

	public void setFields(List<TaskTypeField> fields) {
		this.fields = fields;
	}

	public NeedType getNeedType() {
		return needType;
	}

	public void setNeedType(NeedType needType) {
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

}
