package com.groupon.mobile.model;

import java.util.Date;

public class Task {
	
		private Long id;
		
		private String title;

		private String description;

		private User owner;

		private Community community;

		private Date deadline;
		
		private String location;
		


		public String getTitle() {
			return title;
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



		public Long getId() {
			return id;
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
		
		public void setId(Long id) {
			this.id = id;
		}




}
