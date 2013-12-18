package com.groupon.web.controller.json;

import com.groupon.web.dao.model.ReplyFieldAttribute;

public class ReplyFieldAttributeJson {
	private String name;
	private String value;

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

	public static ReplyFieldAttributeJson convert(ReplyFieldAttribute fieldAttribute) {
		ReplyFieldAttributeJson fieldAttributeJson = new ReplyFieldAttributeJson();
		fieldAttributeJson.setName(fieldAttribute.getName());
		fieldAttributeJson.setValue(fieldAttribute.getValue());
		return fieldAttributeJson;
	}
}
