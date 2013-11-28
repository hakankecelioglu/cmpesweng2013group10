package com.groupon.web.controller.json;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.groupon.web.dao.model.Task;

public class TaskJson implements Serializable {
	private static final long serialVersionUID = -2502516367929258493L;

	private Long id;
	private String title;
	private String description;
	private String ownerUsername;
	private Long ownerId;
	private String communityName;
	private Long communityId;
	private String status;
	private Long deadline;
	private Long followerCount = 0L;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getOwnerUsername() {
		return ownerUsername;
	}

	public void setOwnerUsername(String ownerUsername) {
		this.ownerUsername = ownerUsername;
	}

	public Long getOwnerId() {
		return ownerId;
	}

	public void setOwnerId(Long ownerId) {
		this.ownerId = ownerId;
	}

	public String getCommunityName() {
		return communityName;
	}

	public void setCommunityName(String communityName) {
		this.communityName = communityName;
	}

	public Long getCommunityId() {
		return communityId;
	}

	public void setCommunityId(Long communityId) {
		this.communityId = communityId;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Long getDeadline() {
		return deadline;
	}

	public void setDeadline(Long deadline) {
		this.deadline = deadline;
	}

	public Long getFollowerCount() {
		return followerCount;
	}

	public void setFollowerCount(Long followerCount) {
		this.followerCount = followerCount;
	}

	public static TaskJson convert(Task task) {
		TaskJson taskJson = new TaskJson();

		taskJson.setId(task.getId());

		if (task.getCommunity() != null) {
			taskJson.setCommunityId(task.getCommunity().getId());
			taskJson.setCommunityName(task.getCommunity().getName());
		}

		if (task.getDeadline() != null) {
			taskJson.setDeadline(task.getDeadline().getTime());
		}

		taskJson.setDescription(task.getDescription());
		taskJson.setFollowerCount(task.getFollowerCount());

		if (task.getOwner() != null) {
			taskJson.setOwnerId(task.getOwner().getId());
			taskJson.setOwnerUsername(task.getOwner().getUsername());
		}

		if (task.getStatus() != null) {
			taskJson.setStatus(task.getStatus().name());
		}

		taskJson.setTitle(task.getTitle());
		return taskJson;
	}

	public static List<TaskJson> convert(Collection<Task> tasks) {
		List<TaskJson> jsons = new ArrayList<TaskJson>();
		if (tasks != null) {
			for (Task task : tasks) {
				jsons.add(convert(task));
			}
		}
		return jsons;
	}
}
