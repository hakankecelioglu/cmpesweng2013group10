package com.groupon.web.dao.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "role")
public class Role implements Serializable {
	private static final long serialVersionUID = -22152945907749137L;

	@Id
	@GeneratedValue
	@Column(name = "id")
	private long id;

	@Enumerated(EnumType.STRING)
	@Column(name = "name", nullable = false, length = 50, unique = true)
	private RoleName name;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public RoleName getName() {
		return name;
	}

	public void setName(RoleName name) {
		this.name = name;
	}
}
