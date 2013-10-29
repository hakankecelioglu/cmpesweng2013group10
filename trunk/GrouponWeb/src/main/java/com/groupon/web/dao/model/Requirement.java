package com.groupon.web.dao.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "requirement")
public class Requirement extends BaseModel implements Serializable {
	private static final long serialVersionUID = 7284531901424699279L;

	@Column(name = "name", nullable = false, unique = true, length = 100)
	private String name;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
