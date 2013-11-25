package com.groupon.web.controller.json;

import java.io.Serializable;

import com.groupon.web.dao.model.Community;

public class CommunityJson implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private String name;
	private String description;
	private Long id;
	
	public String getName() {
		return name;
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

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public static CommunityJson convert(Community community) {
		CommunityJson communityJson = new CommunityJson();
		communityJson.setDescription(community.getDescription());
		communityJson.setId(community.getId());
		communityJson.setName(community.getName());
		return communityJson;
	}
}
