package com.groupon.web.service;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Component;

import com.groupon.web.dao.TaskTypeDao;
import com.groupon.web.dao.model.TaskType;

@Component
public class TaskTypeService {

	@Autowired
	private TaskTypeDao taskTypeDao;
	/**
	 * return a task type with specified id
	 * @param id target task id
	 * @return
	 */
	public TaskType getTaskTypeById(Long id) {
		return taskTypeDao.getTaskTypeById(id);
	}
}
