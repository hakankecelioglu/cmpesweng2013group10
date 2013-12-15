package com.groupon.mobile.model;

import java.io.Serializable;

public class Community implements Serializable {
	private static final long serialVersionUID = 9143384393814027138L;

	private Long id;

	private String name;

	private String description;
	
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
	

}
