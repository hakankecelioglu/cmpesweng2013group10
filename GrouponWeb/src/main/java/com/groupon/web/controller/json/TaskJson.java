package com.groupon.web.controller.json;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.groupon.web.dao.model.NeedType;
import com.groupon.web.dao.model.Task;

public class TaskJson implements Serializable {
	private static final long serialVersionUID = -2502516367929258493L;

	private Long id;
	private String title;
	private String description;
	private String needType;
	private String ownerUsername;
	private Long ownerId;
	private String requirementName;
	private int requirementQuantity;
	private String communityName;
	private String communityPicture;
	private Long communityId;
	private String status;
	private Long deadline;
	private Long followerCount = 0L;
	private Long createDate = System.currentTimeMillis();

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

	public String getNeedType() {
		return needType;
	}

	public void setNeedType(String needType) {
		this.needType = needType;
	}

	public String getRequirementName() {
		return requirementName;
	}

	public void setRequirementName(String requirementName) {
		this.requirementName = requirementName;
	}

	public int getRequirementQuantity() {
		return requirementQuantity;
	}

	public void setRequirementQuantity(int requirementQuantity) {
		this.requirementQuantity = requirementQuantity;
	}

	public Long getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Long createDate) {
		this.createDate = createDate;
	}

	public String getCommunityPicture() {
		return communityPicture;
	}

	public void setCommunityPicture(String communityPicture) {
		this.communityPicture = communityPicture;
	}

	public static TaskJson convert(Task task) {
		TaskJson taskJson = new TaskJson();

		taskJson.setId(task.getId());

		if (task.getCommunity() != null) {
			taskJson.setCommunityId(task.getCommunity().getId());
			taskJson.setCommunityName(task.getCommunity().getName());
			taskJson.setCommunityPicture(task.getCommunity().getPicture());
		}

		if (task.getDeadline() != null) {
			taskJson.setDeadline(task.getDeadline().getTime());
		}

		taskJson.setDescription(task.getDescription());
		taskJson.setFollowerCount(task.getFollowerCount());
		NeedType needType = task.getNeedType();
		taskJson.setNeedType(needType.toString());
		if (needType == NeedType.GOODS) {
			taskJson.setRequirementName(task.getRequirementName());
			taskJson.setRequirementQuantity(task.getRequirementQuantity());
		} else if (needType == NeedType.SERVICE) {
			taskJson.setRequirementName(task.getRequirementName());

		}
		if (task.getOwner() != null) {
			taskJson.setOwnerId(task.getOwner().getId());
			taskJson.setOwnerUsername(task.getOwner().getUsername());
		}

		if (task.getStatus() != null) {
			taskJson.setStatus(task.getStatus().name());
		}

		taskJson.setTitle(task.getTitle());
		taskJson.setCreateDate(task.getCreateDate().getTime());
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
