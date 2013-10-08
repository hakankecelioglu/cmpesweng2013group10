package com.groupon.web.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.groupon.web.dao.TaskDAO;
import com.groupon.web.dao.model.Task;

@Component
public class TaskService {

	@Autowired
	private TaskDAO taskDao;

	public List<Task> getAllTasks() {
		return taskDao.findAll();
	}
}
