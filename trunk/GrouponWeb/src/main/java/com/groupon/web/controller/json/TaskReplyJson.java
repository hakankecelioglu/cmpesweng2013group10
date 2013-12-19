package com.groupon.web.controller.json;

import java.util.ArrayList;
import java.util.List;

import com.groupon.web.dao.model.TaskReply;

public class TaskReplyJson {
	private UserJson replier;
	private long date;
	private List<ReplyAttributeJson> attributes;

	public UserJson getReplier() {
		return replier;
	}

	public void setReplier(UserJson replier) {
		this.replier = replier;
	}

	public List<ReplyAttributeJson> getAttributes() {
		return attributes;
	}

	public void setAttributes(List<ReplyAttributeJson> attributes) {
		this.attributes = attributes;
	}

	public long getDate() {
		return date;
	}

	public void setDate(long date) {
		this.date = date;
	}

	public static TaskReplyJson convert(TaskReply taskReply) {
		TaskReplyJson json = new TaskReplyJson();
		json.setReplier(UserJson.convert(taskReply.getReplier()));
		json.setAttributes(ReplyAttributeJson.convert(taskReply.getAttributes()));
		json.setDate(taskReply.getCreateDate().getTime());
		return json;
	}

	public static List<TaskReplyJson> convert(List<TaskReply> taskReplies) {
		List<TaskReplyJson> json = new ArrayList<TaskReplyJson>();
		if (taskReplies != null && !taskReplies.isEmpty()) {
			for (TaskReply taskReply : taskReplies) {
				json.add(convert(taskReply));
			}
		}
		return json;
	}
}
