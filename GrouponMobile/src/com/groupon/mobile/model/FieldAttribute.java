package com.groupon.mobile.model;

public class FieldAttribute {

	private String name;
	private String value;

	private TaskTypeField taskTypeField;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public TaskTypeField getTaskTypeField() {
		return taskTypeField;
	}

	public void setTaskTypeField(TaskTypeField taskTypeField) {
		this.taskTypeField = taskTypeField;
	}

}
