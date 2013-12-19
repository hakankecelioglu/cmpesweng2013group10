package com.groupon.web.dao.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

@Entity
@Table(name = "reply_attribute")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class ReplyAttribute extends BaseModel {
	private static final long serialVersionUID = -8193747037372160672L;

	@Column(name = "name", nullable = false, length = 200)
	private String name;

	@Column(name = "value", nullable = false, length = 200)
	private String value;

	@ManyToOne
	@JoinColumn(name = "reply_id", nullable = false)
	private TaskReply taskReply;

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

	public TaskReply getTaskReply() {
		return taskReply;
	}

	public void setTaskReply(TaskReply taskReply) {
		this.taskReply = taskReply;
	}
}
