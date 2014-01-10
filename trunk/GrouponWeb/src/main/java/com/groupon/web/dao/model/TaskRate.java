package com.groupon.web.dao.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

@Entity
@Table(name = "task_rate", uniqueConstraints = { @UniqueConstraint(columnNames = { "user_id", "task_id" }) })
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class TaskRate extends BaseModel {
	private static final long serialVersionUID = 1L;

	@ManyToOne
	@JoinColumn(name = "user_id", nullable = false)
	private User user;

	@ManyToOne
	@JoinColumn(name = "task_id", nullable = false)
	private Task task;

	@Enumerated(EnumType.STRING)
	@Column(name = "direction", nullable = false, length = 4)
	private RateDirection direction = RateDirection.UP;

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Task getTask() {
		return task;
	}

	public void setTask(Task task) {
		this.task = task;
	}

	public RateDirection getDirection() {
		return direction;
	}

	public void setDirection(RateDirection direction) {
		this.direction = direction;
	}
}
