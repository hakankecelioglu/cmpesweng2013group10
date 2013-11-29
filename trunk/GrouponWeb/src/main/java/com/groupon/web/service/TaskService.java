package com.groupon.web.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.groupon.web.dao.TaskDAO;
import com.groupon.web.dao.model.SortBy;
import com.groupon.web.dao.model.Tag;
import com.groupon.web.dao.model.Task;
import com.groupon.web.dao.model.TaskStatus;
import com.groupon.web.dao.model.User;
import com.groupon.web.util.ControllerConstants;

@Component
public class TaskService {

	@Autowired
	private TaskDAO taskDao;

	@Autowired
	private TagService tagService;

	public Task getTaskById(Long id) {
		return taskDao.getTaskById(id);
	}

	public List<Task> getFollowedTasks(User user) {
		return taskDao.getFollowedTasks(user.getId());
	}

	public List<Task> getCommunityTasks(User user) {
		return taskDao.getCommunityTasks(user.getId());
	}

	public List<Task> getHomeFeedTasks(User user, SortBy sortBy) {
		return taskDao.getHomeFeedTasks(user.getId(), sortBy);
	}

	public List<Task> getAllTasks(int page, int max) {
		return taskDao.findAll(page, max);
	}

	public Task createTask(Task task, User owner) {
		task.setCreateDate(new Date());
		task.setOwner(owner);
		task.setStatus(TaskStatus.OPEN);

		arrangeTagsOfTask(task);
		Task taskCreated = taskDao.saveTask(task);

		followTask(taskCreated.getId(), owner);

		tagService.createTagUserRelationsOfTask(task.getId(), owner.getId(), ControllerConstants.TAG_USER_CREATE_TASK);

		return taskCreated;
	}

	public List<Task> getTasks(long communityId, int page, int numberOfTasksPerPage) {
		return taskDao.getTasks(communityId, page, numberOfTasksPerPage);
	}

	public synchronized Long followTask(Long taskId, User user) {
		Task task = taskDao.getTaskById(taskId);
		task.getFollowers().add(user);
		task.setFollowerCount(task.getFollowerCount() + 1);
		taskDao.updateTask(task);

		tagService.createTagUserRelationsOfTask(task.getId(), user.getId(), ControllerConstants.TAG_USER_FOLLOW_TASK);
		return task.getFollowerCount();
	}

	public synchronized Long unfollowTask(Long taskId, User user) {
		Task task = taskDao.getTaskById(taskId);
		task.getFollowers().remove(user);
		task.setFollowerCount(task.getFollowerCount() - 1);
		taskDao.updateTask(task);
		return task.getFollowerCount();
	}

	public List<Task> getRecommendedTasks(User user) {
		return taskDao.getRecommendedTasks(user.getId());
	}

	private void arrangeTagsOfTask(Task task) {
		List<Tag> tags = task.getTags();
		List<Tag> tags2 = new ArrayList<Tag>(tags.size());
		for (Tag tag : tags) {
			Tag tagInDb = tagService.getTagByName(tag.getName());
			if (tagInDb == null) {
				tagInDb = tagService.createTag(tag);
			}
			tags2.add(tagInDb);
		}
		task.setTags(tags2);
	}
}
