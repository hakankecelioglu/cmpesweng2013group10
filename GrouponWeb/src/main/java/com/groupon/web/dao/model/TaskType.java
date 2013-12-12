package com.groupon.web.dao.model;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

@Entity
@Table(name = "task_type")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class TaskType extends BaseModel {
	private static final long serialVersionUID = 4963667324696625134L;

	@Column(name = "name", nullable = false)
	private String name;

	@Column(name = "description", nullable = false)
	private String description;
	
	@Enumerated(EnumType.STRING)
	@Column(name = "need_type", nullable = false)
	private NeedType needType;

	@OneToMany(mappedBy = "taskType", cascade = CascadeType.ALL)
	private List<TaskTypeField> fields;

	@ManyToOne
	@JoinColumn(name = "community_id", nullable = false)
	private Community community;

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

	public NeedType getNeedType() {
		return needType;
	}

	public void setNeedType(NeedType needType) {
		this.needType = needType;
	}

	public List<TaskTypeField> getFields() {
		return fields;
	}

	public void setFields(List<TaskTypeField> fields) {
		this.fields = fields;
	}

	public Community getCommunity() {
		return community;
	}

	public void setCommunity(Community community) {
		this.community = community;
	}

}
