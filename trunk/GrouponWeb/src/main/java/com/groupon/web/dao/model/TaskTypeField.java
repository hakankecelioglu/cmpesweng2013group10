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
@Table(name = "task_type_field")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class TaskTypeField extends BaseModel {
	private static final long serialVersionUID = 5935373269930933875L;

	@Column(name = "name", nullable = false)
	private String name;

	@Enumerated(EnumType.STRING)
	@Column(name = "type", nullable = false)
	private FieldType fieldType;

	@ManyToOne
	@JoinColumn(name = "task_type_id", nullable = false)
	private TaskType taskType;

	@OneToMany(mappedBy = "taskTypeField", cascade = CascadeType.ALL)
	private List<FieldAttribute> attributes;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public FieldType getFieldType() {
		return fieldType;
	}

	public void setFieldType(FieldType fieldType) {
		this.fieldType = fieldType;
	}

	public TaskType getTaskType() {
		return taskType;
	}

	public void setTaskType(TaskType taskType) {
		this.taskType = taskType;
	}

	public List<FieldAttribute> getAttributes() {
		return attributes;
	}

	public void setAttributes(List<FieldAttribute> attributes) {
		this.attributes = attributes;
	}
}
