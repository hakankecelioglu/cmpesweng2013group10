package com.groupon.web.controller.json;

import java.util.ArrayList;
import java.util.List;

import com.groupon.web.dao.model.ReplyField;
import com.groupon.web.dao.model.ReplyFieldAttribute;

public class ReplyFieldJson {
	private String name;
	private String fieldType;
	private List<ReplyFieldAttributeJson> attributes;

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

	public List<ReplyFieldAttributeJson> getAttributes() {
		return attributes;
	}

	public void setFields(List<ReplyFieldAttributeJson> attributes) {
		this.attributes = attributes;
	}

	public static ReplyFieldJson convert(ReplyField taskTypeField) {
		ReplyFieldJson taskTypeFieldJson = new ReplyFieldJson();
		taskTypeFieldJson.setFieldType(taskTypeField.getFieldType().toString());
		taskTypeFieldJson.setName(taskTypeField.getName());
		List<ReplyFieldAttributeJson> fieldAttributeJsons = new ArrayList<ReplyFieldAttributeJson>();
		List<ReplyFieldAttribute> fieldAttributes = taskTypeField.getAttributes();
		if (fieldAttributes != null) {
			for (ReplyFieldAttribute fieldAttribute : fieldAttributes)
				fieldAttributeJsons.add(ReplyFieldAttributeJson.convert(fieldAttribute));

		}
		taskTypeFieldJson.setFields(fieldAttributeJsons);
		return taskTypeFieldJson;
	}

	public static List<ReplyFieldJson> convert(List<ReplyField> replyFields) {
		List<ReplyFieldJson> list = new ArrayList<ReplyFieldJson>();
		if (replyFields != null) {
			for (ReplyField field : replyFields) {
				list.add(convert(field));
			}
		}
		return list;
	}
}
