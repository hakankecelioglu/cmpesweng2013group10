package com.groupon.web.controller;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.groupon.web.service.TaskService;

@Controller
public class HomeController extends BaseController {

    @Autowired
    private TaskService taskService;

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public Object home() throws JSONException {
	return "home.view";
    }

    @RequestMapping(value = "testjson", method = RequestMethod.GET)
    public @ResponseBody
    ResponseEntity<Map<String, Object>> testjson() throws JSONException {
	Map<String, Object> map = new HashMap<String, Object>();
	map.put("a", 21);
	return new ResponseEntity<Map<String, Object>>(map, HttpStatus.OK);
    }
    
    @RequestMapping(value = "login", method = RequestMethod.POST)
    public ResponseEntity<Map<String, Object>> login(@RequestParam String username, @RequestParam String password) {
	Map<String, Object> response = new HashMap<String, Object>();
	if ("test".equals(username) && "12345".equals(password)) {
	    response.put("answer", true);
	    return prepareSuccessResponse(response);
	} else {
	    response.put("error", "Wrong username or password!");
	    return prepareErrorResponse(response);
	}
    }
}
