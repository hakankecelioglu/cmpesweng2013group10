package com.groupon.web.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.groupon.web.dao.TaskDAO;
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
