package com.groupon.web.controller;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.groupon.web.dao.model.Community;
import com.groupon.web.dao.model.User;
import com.groupon.web.service.CommunityService;
import com.groupon.web.service.TaskService;

@Controller
public class HomeController extends AbstractBaseController {
	@Autowired
	private TaskService taskService;
	@Autowired
	private CommunityService communityService;

	/**
	 * Opens home page
	 * 
	 * @return the view which will be shown on homepage
	 * @throws JSONException
	 */
	@RequestMapping(value = "/", method = RequestMethod.GET)
	public Object home(HttpServletRequest request, Model model) throws JSONException {
		setGlobalAttributesToModel(model, request);
		model.addAttribute("page", "home");
		User user = getUser(request);
		model.addAttribute("alltasks", taskService.getAllTasks());
		if (user == null) {
			model.addAttribute("allcommunities", communityService.getAllCommunities());
			return "homeguest.view";
		}

		// generateDummyContent(model);
		return "home.view";
	}

	@RequestMapping(value = "/dummy", method = RequestMethod.GET)
	public ResponseEntity<Map<String, Object>> createCommunityAndroid(HttpServletRequest request) {
		Map<String, Object> response = new HashMap<String, Object>();
		response.put("connection", "ok");
		return prepareSuccessResponse(response);

	}
}
