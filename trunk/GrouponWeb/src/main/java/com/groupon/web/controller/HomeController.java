package com.groupon.web.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.groupon.web.dao.model.PCTask;
import com.groupon.web.service.TaskService;

@Controller
public class HomeController {
	
	@Autowired
	private TaskService taskService;
	
	@RequestMapping(value = "/")
	public String home() {
		List<PCTask> tasks = taskService.getAllTasks();
		System.out.println(tasks.size());
		return "home";
	}
}
