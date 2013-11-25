package com.groupon.web.dao.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

@Entity
@Table(name = "tag_user", uniqueConstraints = @UniqueConstraint(columnNames = { "tag_id", "user_id" }))
public class TagUser extends BaseModel {
	private static final long serialVersionUID = 9220472004168965400L;

	@ManyToOne
	@JoinColumn(name = "tag_id", nullable = false)
	private Tag tag;

	@ManyToOne
	@JoinColumn(name = "user_id", nullable = false)
	private User user;

	@Column(name = "rel", nullable = false)
	private Long relatedness = 0L;

	public Tag getTag() {
		return tag;
	}

	public void setTag(Tag tag) {
		this.tag = tag;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Long getRelatedness() {
		return relatedness;
	}

	public void setRelatedness(Long relatedness) {
		this.relatedness = relatedness;
	}

}
