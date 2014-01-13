package com.groupon.web.dao.model;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
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

	@OneToMany(mappedBy = "taskReply", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	private List<ReplyAttribute> attributes;

	@Column(name = "accepted", columnDefinition = "tinyint(1) default false")
	private boolean isAccepted = false;

	@Column(name = "votes", nullable = false, columnDefinition = "int default 0")
	private int votes = 0;

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

	public boolean isAccepted() {
		return isAccepted;
	}

	public void setAccepted(boolean isAccepted) {
		this.isAccepted = isAccepted;
	}

	public int getVotes() {
		return votes;
	}

	public void setVotes(int votes) {
		this.votes = votes;
	}
}
