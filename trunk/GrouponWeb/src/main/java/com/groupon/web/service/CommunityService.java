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
	/**
	 * creates a community.Set create data from system data . Makes owner member of community.
	 * 
	 * @param community
	 */
	public void createCommunity(Community community) {
		community.setCreateDate(new Date());

		arrangeTagsOfCommunity(community);
		communityDao.saveCommunity(community);

		tagService.createTagUserRelationsOfCommunity(community.getId(), community.getOwner().getId(), GrouponConstants.TAG_USER_CREATE_COMMUNITY);

		// Every user is a member of his own communities
		addMemberToCommunity(community, community.getOwner());
	}
	/**
	 * returns community with specified id
	 * @param id
	 * @return
	 */
	public Community getCommunityById(Long id) {
		return communityDao.getCommunityById(id);
	}
	/**
	 * get communities of a member
	 * @param userId member id
	 * @param page
	 * @param max
	 * @return
	 */
	public List<Community> getCommunitiesByFollowerId(Long userId, int page, int max) {
		return communityDao.getCommunitiesByFollowerId(userId, page, max);
	}
	/**
	 * return all communities in the system
	 * @return
	 */
	public List<Community> getAllCommunities() {
		return communityDao.findAll();
	}
	/**
	 * get newest communities
	 * @param page
	 * @param max
	 * @return
	 */
	public List<Community> getNewestCommunities(int page, int max) {
		return communityDao.getNewestCommunities(page, max);
	}
	/**
	 * returns number of communities in the system.
	 * @return
	 */
	public Long getCommunityCount() {
		return communityDao.countCommunities();
	}
	/**
	 * add a member to community.
	 * @param community community input
	 * @param user user who become member
	 */
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
	/**
	 * remove a member from community
	 * @param community target community
	 * @param user	removed member
	 */
	public void removeMemberFromCommunity(Community community, User user) {
		if (!community.getMembers().contains(user)) {
			return;
		}
		community.getMembers().remove(user);
		communityDao.update(community);
	}
	/**
	 * get similar communities to a community
	 * @param communityId id of target community
	 * @param page
	 * @param max
	 * @return
	 */
	public List<Community> getSimiliarCommunities(Long communityId, int page, int max) {
		return communityDao.findSimiliarCommunities(communityId, page, max);
	}
	/**
	 * creates a task type
	 * @param taskType task type input
	 */
	public void createTaskType(TaskType taskType) {
		communityDao.save(taskType);
	}
	/**
	 * get task type names of community
	 * @param communityId id of target community
	 * @return
	 */
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
	/**
	 * get task type with specified id
	 * @param taskTypeId id of target task type
	 * @return
	 */
	public TaskType getTaskType(Long taskTypeId) {
		return communityDao.getTaskType(taskTypeId);
	}
	/**
	 * returns result of community search
	 * @param queryText query text
	 * @return
	 */
	public List<Community> searchCommunities(String queryText) {
		return communityDao.searchCommunities(queryText);
	}
	/**
	 * remove a community
	 * @param community target community
	 */
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
