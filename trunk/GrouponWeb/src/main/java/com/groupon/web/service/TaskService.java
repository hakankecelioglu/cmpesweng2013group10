package com.groupon.web.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.groupon.web.dao.TaskDAO;
import com.groupon.web.dao.model.Community;
import com.groupon.web.dao.model.Tag;
import com.groupon.web.dao.model.Task;
import com.groupon.web.dao.model.TaskStatus;
import com.groupon.web.dao.model.User;

@Component
public class TaskService {

	@Autowired
	private TaskDAO taskDao;

	@Autowired
	private TagService tagService;

	public List<Task> getAllTasks() {
		return taskDao.findAll();
	}

	public Task createTask(Task task, User owner) {
		task.setCreateDate(new Date());
		task.setOwner(owner);
		task.setStatus(TaskStatus.OPEN);

		arrangeTagsOfTask(task);

		return taskDao.saveTask(task);
	}

	public List<Task> getTasks(long communityId, int page, int numberOfTasksPerPage) {
		return taskDao.getTasks(communityId, page, numberOfTasksPerPage);
	}
	
	public synchronized Long followTask(Long taskId, User user) {
		Task task = taskDao.getTaskById(taskId);
		task.getFollowers().add(user);
		task.setFollowerCount(task.getFollowerCount() + 1);
		taskDao.updateTask(task);
		return task.getFollowerCount();
	}
	
	public synchronized Long unfollowTask(Long taskId, User user) {
		Task task = taskDao.getTaskById(taskId);
		task.getFollowers().remove(user);
		task.setFollowerCount(task.getFollowerCount() - 1);
		taskDao.updateTask(task);
		return task.getFollowerCount();
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
	public Task getTaskById(Long id) {
		return taskDao.getTaskById2(id);
	}
}
