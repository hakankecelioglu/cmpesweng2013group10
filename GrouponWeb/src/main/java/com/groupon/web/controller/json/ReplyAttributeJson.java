package com.groupon.web.controller.json;

import java.util.ArrayList;
import java.util.List;

import com.groupon.web.dao.model.ReplyAttribute;

public class ReplyAttributeJson {
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

	public static ReplyAttributeJson convert(ReplyAttribute attribute) {
		ReplyAttributeJson json = new ReplyAttributeJson();
		json.setName(attribute.getName());
		json.setValue(attribute.getValue());
		return json;
	}

	public static List<ReplyAttributeJson> convert(List<ReplyAttribute> attributes) {
		List<ReplyAttributeJson> json = new ArrayList<ReplyAttributeJson>();
		if (attributes != null && !attributes.isEmpty()) {
			for (ReplyAttribute attribute : attributes) {
				json.add(convert(attribute));
			}
		}
		return json;
	}
}
