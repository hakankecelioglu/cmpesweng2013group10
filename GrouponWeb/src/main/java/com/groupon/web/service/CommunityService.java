package com.groupon.web.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.groupon.web.dao.CommunityDao;
import com.groupon.web.dao.model.Community;
import com.groupon.web.dao.model.Tag;
import com.groupon.web.dao.model.Task;
import com.groupon.web.dao.model.TaskType;
import com.groupon.web.dao.model.User;
import com.groupon.web.util.GrouponConstants;

@Component
public class CommunityService {
	@Autowired
	private CommunityDao communityDao;

	@Autowired
	private TagService tagService;
	
	@Autowired
	private TaskService taskService;

	@Autowired
	private NotificationService notificationService;

	public void createCommunity(Community community) {
		community.setCreateDate(new Date());

		arrangeTagsOfCommunity(community);
		communityDao.saveCommunity(community);

		tagService.createTagUserRelationsOfCommunity(community.getId(), community.getOwner().getId(), GrouponConstants.TAG_USER_CREATE_COMMUNITY);

		// Every user is a member of his own communities
		addMemberToCommunity(community, community.getOwner());
	}

	public Community getCommunityById(Long id) {
		return communityDao.getCommunityById(id);
	}

	public List<Community> getCommunitiesByFollowerId(Long userId, int page, int max) {
		return communityDao.getCommunitiesByFollowerId(userId, page, max);
	}

	public List<Community> getAllCommunities() {
		return communityDao.findAll();
	}

	public List<Community> getNewestCommunities(int page, int max) {
		return communityDao.getNewestCommunities(page, max);
	}

	public Long getCommunityCount() {
		return communityDao.countCommunities();
	}

	public void addMemberToCommunity(Community community, User user) {
		if (community.getMembers().contains(user)) {
			return;
		}
		community.getMembers().add(user);
		communityDao.update(community);

		if (!community.getOwner().equals(user)) {
			tagService.createTagUserRelationsOfCommunity(community.getId(), user.getId(), GrouponConstants.TAG_USER_JOIN_COMMUNITY);
			System.out.println("async!!!");
		}
	}

	public void removeMemberFromCommunity(Community community, User user) {
		if (!community.getMembers().contains(user)) {
			return;
		}
		community.getMembers().remove(user);
		communityDao.update(community);
	}

	public List<Community> getSimiliarCommunities(Long communityId, int page, int max) {
		return communityDao.findSimiliarCommunities(communityId, page, max);
	}

	public void createTaskType(TaskType taskType) {
		communityDao.save(taskType);
	}

	public List<Map<String, Object>> getTaskTypeNames(Long communityId) {
		List<Object[]> taskTypesList = communityDao.getTaskTypeNames(communityId);

		List<Map<String, Object>> taskTypes = new ArrayList<Map<String, Object>>();
		if (taskTypesList != null && taskTypesList.size() > 0) {
			for (Object[] arr : taskTypesList) {
				Map<String, Object> taskType = new HashMap<String, Object>();
				taskType.put("id", arr[0]);
				taskType.put("name", arr[1]);
				taskTypes.add(taskType);
			}
		}
		return taskTypes;
	}

	public TaskType getTaskType(Long taskTypeId) {
		return communityDao.getTaskType(taskTypeId);
	}

	public List<Community> searchCommunities(String queryText) {
		return communityDao.searchCommunities(queryText);
	}

	public void removeCommunity(Community community) {
		notificationService.removeCommunityNotifications(community);

		community.getTags().clear();
		
		Set<Task> tasks = new HashSet<Task>(community.getTasks());
		for (Task task : tasks) {
			community.getTasks().remove(task);
			taskService.removeTask(task);
		}
		
		community.getTaskTypes().clear();
		community.getMembers().clear();

		communityDao.update(community);
		communityDao.delete(community);
	}

	private void arrangeTagsOfCommunity(Community community) {
		List<Tag> tags = community.getTags();
		if (tags != null) {
			List<Tag> tags2 = new ArrayList<Tag>(tags.size());
			for (Tag tag : tags) {
				Tag tagInDb = tagService.getTagByName(tag.getName());
				if (tagInDb == null) {
					tagInDb = tagService.createTag(tag);
				}
				tags2.add(tagInDb);
			}
			community.setTags(tags2);
		}
	}
}
