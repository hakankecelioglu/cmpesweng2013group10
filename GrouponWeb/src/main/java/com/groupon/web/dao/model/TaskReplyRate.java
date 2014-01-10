package com.groupon.web.dao.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

@Entity
@Table(name = "task_reply_rate")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class TaskReplyRate extends BaseModel {
	private static final long serialVersionUID = 1L;

	@ManyToOne
	@JoinColumn(name = "user_id", nullable = false)
	private User user;

	@ManyToOne
	@JoinColumn(name = "task_reply_id", nullable = false)
	private TaskReply taskReply;

	@Enumerated(EnumType.STRING)
	@Column(name = "direction", nullable = false, length = 4)
	private RateDirection direction = RateDirection.UP;

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public TaskReply getTaskReply() {
		return taskReply;
	}

	public void setTaskReply(TaskReply taskReply) {
		this.taskReply = taskReply;
	}

	public RateDirection getDirection() {
		return direction;
	}

	public void setDirection(RateDirection direction) {
		this.direction = direction;
	}
}
