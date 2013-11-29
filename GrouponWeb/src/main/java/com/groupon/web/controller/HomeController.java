package com.groupon.web.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.groupon.web.dao.model.SortBy;
import com.groupon.web.dao.model.Task;
import com.groupon.web.dao.model.User;
import com.groupon.web.service.CommunityService;
import com.groupon.web.service.TaskService;
import com.groupon.web.util.ControllerConstants;

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
	 */
	@RequestMapping(value = "/", method = RequestMethod.GET)
	public Object home(HttpServletRequest request, Model model) {
		setGlobalAttributesToModel(model, request);
		model.addAttribute("page", "home");

		User user = getUser();
		if (user == null) {
			model.addAttribute("allcommunities", communityService.getAllCommunities());
			return "homeguest.view";
		}

		List<Task> homeFeedTasks = taskService.getHomeFeedTasks(user, getCurrentSortBy(request));
		if (homeFeedTasks == null || homeFeedTasks.size() == 0) {
			model.addAttribute("emptyHomeFeed", Boolean.TRUE);
			homeFeedTasks = taskService.getAllTasks(0, 5);
		}

		model.addAttribute("homeFeedTasks", homeFeedTasks);
		model.addAttribute("recommendedTasks", taskService.getRecommendedTasks(user));

		return "home.view";
	}

	/**
	 * Opens community feed
	 * 
	 * @return the view which will be shown on homepage
	 */
	@RequestMapping(value = "/feed/community", method = RequestMethod.GET)
	public Object communityFeed(HttpServletRequest request, Model model) {
		User user = getUser();
		if (user == null) {
			return "redirect:/";
		}

		setGlobalAttributesToModel(model, request);
		model.addAttribute("page", "home");

		model.addAttribute("followedTasks", taskService.getFollowedTasks(user));
		model.addAttribute("communityTasks", taskService.getCommunityTasks(user));
		model.addAttribute("recommendedTasks", taskService.getRecommendedTasks(user));

		return "home.view";
	}

	@RequestMapping(value = "/setSorting", method = RequestMethod.POST)
	public ResponseEntity<Map<String, Object>> setSorting(@RequestParam String sortBy, HttpServletRequest request) {
		Map<String, Object> response = new HashMap<String, Object>();

		HttpSession session = request.getSession(true);
		SortBy newSortBy = SortBy.valueOf(sortBy);
		session.setAttribute(ControllerConstants.SESSION_ATTR_SORTBY, newSortBy);

		response.put("sortBy", newSortBy);

		return prepareSuccessResponse(response);
	}

}
