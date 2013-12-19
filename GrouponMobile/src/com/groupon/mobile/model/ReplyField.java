package com.groupon.mobile.model;

import java.util.List;


public class ReplyField {
	private String name;
	private String fieldType;
	private List<ReplyFieldAttribute> attributes;
	private TaskType taskType;
	public String getFieldType() {
		return fieldType;
	}

	public void setFieldType(String fieldType) {
		this.fieldType = fieldType;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<ReplyFieldAttribute> getAttributes() {
		return attributes;
	}

	public void setAttributes(List<ReplyFieldAttribute> attributes) {
		this.attributes = attributes;
	}

	public TaskType getTaskType() {
		return taskType;
	}

	public void setTaskType(TaskType taskType) {
		this.taskType = taskType;
	}
}