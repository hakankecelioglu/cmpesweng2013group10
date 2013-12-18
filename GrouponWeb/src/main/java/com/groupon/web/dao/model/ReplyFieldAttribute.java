package com.groupon.web.dao.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

@Entity
@Table(name = "reply_field_attribute")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class ReplyFieldAttribute extends BaseModel {
	private static final long serialVersionUID = -1946921842036731331L;

	@Column(name = "name", nullable = false, length = 50)
	private String name;

	@Column(name = "value", nullable = false, length = 255)
	private String value;

	@ManyToOne
	@JoinColumn(name = "reply_field_id", nullable = false)
	private ReplyField replyField;

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

	public ReplyField getReplyField() {
		return replyField;
	}

	public void setReplyField(ReplyField replyField) {
		this.replyField = replyField;
	}

}
