package com.groupon.web.dao.model;

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;


@Entity
@Table(name = "task")
public class Task implements Serializable {
	private static final long serialVersionUID = 3338898679144800802L;

	@Id
	@GeneratedValue
	@Column(name = "id")
	private long id;

	@Column(name = "title")
	private String title;

	@Column(name = "description")
	private String description;

	@ManyToOne
	@JoinColumn(name = "owner_id", nullable = false)
	private User owner;

	@ManyToOne
	@JoinColumn(name = "community_id", nullable = false)
	private Community community;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "create_date", nullable = false)
	private Date createDate;

	@Enumerated(EnumType.STRING)
	@Column(name = "status", nullable = false, length = 50)
	private TaskStatus status;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "deadline", nullable = false)
	private Date deadline;

	@Column(name = "location", length = 500)
	private String location;
	
	@Column(name = "loc_latitude")
	private Float latitude;
	
	@Column(name = "loc_longitude")
	private Float longitude;
	
	@Column(name = "need_type")
	@Enumerated(EnumType.STRING)
	private NeedType needType;

	@OneToMany(fetch = FetchType.LAZY, cascade = { CascadeType.ALL }, mappedBy = "task")
	private Collection<TaskRequirement> requirements;

	@ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JoinTable(name = "task_tags", joinColumns = @JoinColumn(name = "task_id"), inverseJoinColumns = @JoinColumn(name = "tag_id"))
	private List<Tag> tags;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public User getOwner() {
		return owner;
	}

	public void setOwner(User owner) {
		this.owner = owner;
	}

	public Community getCommunity() {
		return community;
	}

	public void setCommunity(Community community) {
		this.community = community;
	}

	public Collection<TaskRequirement> getRequirements() {
		return requirements;
	}

	public void setRequirements(Collection<TaskRequirement> requirements) {
		this.requirements = requirements;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public TaskStatus getStatus() {
		return status;
	}

	public void setStatus(TaskStatus status) {
		this.status = status;
	}

	public Date getDeadline() {
		return deadline;
	}

	public void setDeadline(Date deadline) {
		this.deadline = deadline;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public List<Tag> getTags() {
		return tags;
	}

	public void setTags(List<Tag> tags) {
		this.tags = tags;
	}

	public Float getLatitude() {
		return latitude;
	}

	public void setLatitude(Float latitude) {
		this.latitude = latitude;
	}

	public Float getLongitude() {
		return longitude;
	}

	public void setLongitude(Float longitude) {
		this.longitude = longitude;
	}

	public NeedType getNeedType() {
		return needType;
	}

	public void setNeedType(NeedType needType) {
		this.needType = needType;
	}

}
