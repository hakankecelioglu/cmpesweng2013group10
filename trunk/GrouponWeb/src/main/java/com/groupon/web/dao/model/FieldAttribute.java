package com.groupon.web.dao.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

@Entity
@Table(name = "field_attribute")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class FieldAttribute extends BaseModel {
	private static final long serialVersionUID = -1946921842036731331L;

	@Column(name = "name", nullable = false, length = 50)
	private String name;

	@Column(name = "value", nullable = false, length = 255)
	private String value;

	@JoinColumn(name = "task_type_field_id", nullable = false)
	private TaskTypeField taskTypeField;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public TaskTypeField getTaskTypeField() {
		return taskTypeField;
	}

	public void setTaskTypeField(TaskTypeField taskTypeField) {
		this.taskTypeField = taskTypeField;
	}

}
