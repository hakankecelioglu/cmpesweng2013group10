package com.groupon.mobile.model;

import java.util.Date;
import java.util.List;

public class TaskReply {
	private User user;
	private List<FieldAttribute> attributes;
	private Date date;

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public List<FieldAttribute> getAttributes() {
		return attributes;
	}

	public void setAttributes(List<FieldAttribute> attributes) {
		this.attributes = attributes;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

}
