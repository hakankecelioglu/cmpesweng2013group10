package com.groupon.web.dao.model;

import java.io.Serializable;
import java.util.Collection;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

@Entity
@Table(name = "tag")
public class Tag extends BaseModel implements Serializable {
	private static final long serialVersionUID = -5059825584462987228L;

	@Column(name = "name", unique = true)
	private String name;

	@ManyToMany(fetch = FetchType.LAZY, cascade = { CascadeType.ALL })
	@JoinTable(name = "tag_relation", joinColumns = @JoinColumn(name = "tag_id"), inverseJoinColumns = @JoinColumn(name = "related_tag_id"))
	private Collection<Tag> relatedTags;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Collection<Tag> getRelatedTags() {
		return relatedTags;
	}

	public void setRelatedTags(Collection<Tag> relatedTags) {
		this.relatedTags = relatedTags;
	}

}
