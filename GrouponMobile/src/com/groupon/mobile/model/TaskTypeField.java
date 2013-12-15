package com.groupon.mobile.model;

import java.util.List;

public class TaskTypeField {
	private String name;

	private FieldType fieldType;

	private TaskType taskType;

	private List<FieldAttribute> attributes;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public FieldType getFieldType() {
		return fieldType;
	}

	public void setFieldType(FieldType fieldType) {
		this.fieldType = fieldType;
	}

	public TaskType getTaskType() {
		return taskType;
	}

	public void setTaskType(TaskType taskType) {
		this.taskType = taskType;
	}

	public List<FieldAttribute> getAttributes() {
		return attributes;
	}

	public void setAttributes(List<FieldAttribute> attributes) {
		this.attributes = attributes;
	}
}
