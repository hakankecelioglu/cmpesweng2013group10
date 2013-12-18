package com.groupon.web.controller.json;

import java.util.ArrayList;
import java.util.List;

import com.groupon.web.dao.model.FieldAttribute;
import com.groupon.web.dao.model.TaskTypeField;



public class TaskTypeFieldJson {
	private String name;
	private String fieldType;
	private List<FieldAttributeJson> attributes;
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
	public List<FieldAttributeJson> getAttributes() {
		return attributes;
	}

	public void setFields(List<FieldAttributeJson> attributes) {
		this.attributes = attributes;
	}
	public static TaskTypeFieldJson convert(TaskTypeField taskTypeField) {
		TaskTypeFieldJson taskTypeFieldJson = new TaskTypeFieldJson();
		taskTypeFieldJson.setFieldType(taskTypeField.getFieldType().toString());
		taskTypeFieldJson.setName(taskTypeField.getName());
		List<FieldAttributeJson> fieldAttributeJsons = new ArrayList<FieldAttributeJson>();
		List<FieldAttribute> fieldAttributes = taskTypeField.getAttributes();
		if(fieldAttributes!=null){
			for(FieldAttribute fieldAttribute: fieldAttributes)
				fieldAttributeJsons.add(FieldAttributeJson.convert(fieldAttribute));
			
		}
		taskTypeFieldJson.setFields(fieldAttributeJsons);
		return taskTypeFieldJson;
		
	}


}
