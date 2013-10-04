package com.groupon.web.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.groupon.web.dao.model.PCTask;
import com.groupon.web.service.TaskService;

@Controller
public class HomeController {

	@Autowired
	private TaskService taskService;

	@RequestMapping(value = "/", method = RequestMethod.GET)
	public Object home() throws JSONException {
		List<PCTask> tasks = taskService.getAllTasks();
		System.out.println(tasks.size());
		JSONObject obj = new JSONObject();
		obj.put("test", 2);
		// return new ResponseEntity<JSONObject>(obj, HttpStatus.OK);
		return "home";
	}

	@RequestMapping(value = "testjson", method = RequestMethod.GET)
	public @ResponseBody
	ResponseEntity<Map<String, Object>> testjson() throws JSONException {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("a", 21);
		return new ResponseEntity<Map<String, Object>>(map, HttpStatus.OK);
	}
}
