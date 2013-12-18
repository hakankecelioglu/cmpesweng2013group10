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
@Table(name = "task_reply_field")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class ReplyField extends BaseModel {
	private static final long serialVersionUID = 4963667324696625134L;

	@Column(name = "name", nullable = false)
	private String name;

	@Enumerated(EnumType.STRING)
	@Column(name = "type", nullable = false)
	private FieldType fieldType;

	@ManyToOne
	@JoinColumn(name = "task_type_id", nullable = false)
	private TaskType taskType;

	@OneToMany(mappedBy = "replyField", cascade = CascadeType.ALL)
	private List<ReplyFieldAttribute> attributes;

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

	public List<ReplyFieldAttribute> getAttributes() {
		return attributes;
	}

	public void setAttributes(List<ReplyFieldAttribute> attributes) {
		this.attributes = attributes;
	}
}
