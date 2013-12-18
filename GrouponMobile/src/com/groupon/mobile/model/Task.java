package com.groupon.mobile.model;

import java.util.Date;
import java.util.List;
import java.util.Map;



public class Task {

	private Long id;

	private String name;

	private String description;

	private User owner;

	private Community community;

	private String deadline;

	private String location;
	private Map<String,List<String>> AttributeMap;
	private String requirementName;
	private boolean isFollower;
	private int requirementQuantity;
	private String ownerUsername;
	private Long ownerId;
	private String communityName;
	private String needType;
	private String status;
	private Long deadlineCount;
	private Long followerCount = 0L;
	private List<TaskAttribute> attributes;
	public String getName() {
		return name;
	}

	public String getDeadline() {
		return deadline;
	}

	public void setDeadline(String deadline) {
		this.deadline = deadline;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public Long getId() {
		return id;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public User getOwner() {
		return owner;
	}

	public void setOwner(User owner) {
		this.owner = owner;
	}

	public Community getCommunity() {
		return community;
	}

	public void setCommunity(Community community) {
		this.community = community;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public List<TaskAttribute> getAttributes() {
		return attributes;
	}

	public void setAttributes(List<TaskAttribute> attributes) {
		this.attributes = attributes;
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

	public String getNeedType() {
		return needType;
	}

	public void setNeedType(String needType) {
		this.needType = needType;
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

	public Long getDeadlineCount() {
		return deadlineCount;
	}

	public void setDeadlineCount(Long deadlineCount) {
		this.deadlineCount = deadlineCount;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Long getFollowerCount() {
		return followerCount;
	}

	public void setFollowerCount(Long followerCount) {
		this.followerCount = followerCount;
	}

	public boolean isFollower() {
		return isFollower;
	}

	public void setFollower(boolean isFollower) {
		this.isFollower = isFollower;
	}

	public Map<String,List<String>> getAttributeMap() {
		return AttributeMap;
	}

	public void setAttributeMap (Map<String,List<String>> AttributeMap ) {
		this.AttributeMap = AttributeMap;
	}



}
