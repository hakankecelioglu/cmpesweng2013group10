package com.groupon.web.controller.json;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.groupon.web.dao.model.User;

/**
 * @author sedrik
 */
public class UserJson implements Serializable {
	private static final long serialVersionUID = -7906377743831329171L;

	private Long id;
	private String username;
	private String name;
	private String surname;
	private String email;
	private String picture;

	public Long getId() {
		return id;
	}

	public void setId(Long userId) {
		this.id = userId;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSurname() {
		return surname;
	}

	public void setSurname(String surname) {
		this.surname = surname;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPicture() {
		return picture;
	}

	public void setPicture(String picture) {
		this.picture = picture;
	}

	public static UserJson convert(User user) {
		UserJson userJson = new UserJson();

		userJson.setEmail(user.getEmail());
		userJson.setName(user.getName());
		userJson.setSurname(user.getSurname());
		userJson.setId(user.getId());
		userJson.setUsername(user.getUsername());
		userJson.setPicture(user.getPicture());

		return userJson;
	}

	public static List<UserJson> convert(Collection<User> users) {
		List<UserJson> userJsons = new ArrayList<UserJson>(users.size());
		if (users != null) {
			for (User user : users) {
				userJsons.add(convert(user));
			}
		}
		return userJsons;
	}

}
