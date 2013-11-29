package com.groupon.web.dao.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.UniqueConstraint;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.NamedQueries;
import org.hibernate.annotations.NamedQuery;

@NamedQueries({
		@NamedQuery(name = "homeFeedDeadlineSorted", query = "select t from Task t, Community c where c.id = t.community.id and (:uid MEMBER OF c.members or :uid MEMBER OF t.followers) and t.deadline > NOW() order by t.deadline ASC"),
		@NamedQuery(name = "homeFeedLatestSorted", query = "select t from Task t, Community c where c.id = t.community.id and (:uid MEMBER OF c.members or :uid MEMBER OF t.followers) and t.deadline > NOW() order by t.createDate DESC") })
@Entity
@Table(name = "task")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Task extends BaseModel implements Serializable {
	private static final long serialVersionUID = 3338898679144800802L;

	@Column(name = "title")
	private String title;

	@Column(name = "description", length = 1000)
	private String description;

	@ManyToOne
	@JoinColumn(name = "owner_id", nullable = false)
	private User owner;

	@ManyToOne
	@JoinColumn(name = "community_id", nullable = false)
	private Community community;

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

	@OneToOne
	@JoinColumn(name = "predecessor_id", nullable = true)
	private Task predecessor;

	@Column(name = "requirement_name", nullable = true)
	private String requirementName;

	@Column(name = "requirement_quantity", nullable = true)
	private Integer requirementQuantity;

	@ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JoinTable(name = "task_tags", joinColumns = @JoinColumn(name = "task_id"), inverseJoinColumns = @JoinColumn(name = "tag_id"))
	private List<Tag> tags;

	@ManyToMany(fetch = FetchType.LAZY, cascade = { CascadeType.PERSIST, CascadeType.MERGE })
	@JoinTable(name = "task_follower", joinColumns = @JoinColumn(name = "task_id"), inverseJoinColumns = @JoinColumn(name = "user_id"), uniqueConstraints = @UniqueConstraint(columnNames = {
			"user_id", "task_id" }))
	private List<User> followers = new ArrayList<User>();

	@Column(name = "followers", nullable = false)
	private Long followerCount = 0L;

	@ManyToOne
	@JoinColumn(name = "task_type_id", nullable = true)
	private TaskType taskType;

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

	public List<User> getFollowers() {
		return followers;
	}

	public void setFollowers(List<User> followers) {
		this.followers = followers;
	}

	public Long getFollowerCount() {
		return followerCount;
	}

	public void setFollowerCount(Long followerCount) {
		this.followerCount = followerCount;
	}

	public Task getPredecessor() {
		return predecessor;
	}

	public void setPredecessor(Task predecessor) {
		this.predecessor = predecessor;
	}

	public String getRequirementName() {
		return requirementName;
	}

	public void setRequirementName(String requirementName) {
		this.requirementName = requirementName;
	}

	public Integer getRequirementQuantity() {
		return requirementQuantity;
	}

	public void setRequirementQuantity(Integer requirementQuantity) {
		this.requirementQuantity = requirementQuantity;
	}

	public TaskType getTaskType() {
		return taskType;
	}

	public void setTaskType(TaskType taskType) {
		this.taskType = taskType;
	}

}
