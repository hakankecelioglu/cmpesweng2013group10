package com.groupon.web.dao.model;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

@Entity
@Table(name = "task_reply")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class TaskReply extends BaseModel {
	private static final long serialVersionUID = -8193747037372160672L;

	@ManyToOne
	@JoinColumn(name = "task_id", nullable = true)
	private Task task;

	@ManyToOne
	@JoinColumn(name = "user_id", nullable = true)
	private User replier;

	@OneToMany(mappedBy = "taskReply", cascade = CascadeType.ALL)
	private List<ReplyAttribute> attributes;

	public Task getTask() {
		return task;
	}

	public void setTask(Task task) {
		this.task = task;
	}

	public User getReplier() {
		return replier;
	}

	public void setReplier(User replier) {
		this.replier = replier;
	}

	public List<ReplyAttribute> getAttributes() {
		return attributes;
	}

	public void setAttributes(List<ReplyAttribute> attributes) {
		this.attributes = attributes;
	}
}
