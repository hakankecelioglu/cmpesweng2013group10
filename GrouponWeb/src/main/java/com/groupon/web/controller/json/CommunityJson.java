package com.groupon.web.controller.json;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.groupon.web.dao.model.Community;

public class CommunityJson implements Serializable {
	private static final long serialVersionUID = 1L;

	private String name;
	private String description;
	private Long id;
	private String picture;

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

	public String getPicture() {
		return picture;
	}

	public void setPicture(String picture) {
		this.picture = picture;
	}

	public static CommunityJson convert(Community community) {
		CommunityJson communityJson = new CommunityJson();
		communityJson.setDescription(community.getDescription());
		communityJson.setId(community.getId());
		communityJson.setName(community.getName());
		communityJson.setPicture(community.getPicture());
		return communityJson;
	}

	public static List<CommunityJson> convert(List<Community> communities) {
		List<CommunityJson> jsons = new ArrayList<CommunityJson>();
		if (communities != null) {
			for (Community community : communities) {
				jsons.add(convert(community));
			}
		}
		return jsons;
	}
}
