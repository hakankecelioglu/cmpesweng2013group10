package com.groupon.web.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.groupon.web.dao.model.Task;
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
	 */
	@RequestMapping(value = "/", method = RequestMethod.GET)
	public Object home(HttpServletRequest request, Model model) {
		setGlobalAttributesToModel(model);
		model.addAttribute("page", "home");

		User user = getUser();
		if (user == null) {
			model.addAttribute("allcommunities", communityService.getAllCommunities());
			return "homeguest.view";
		}

		List<Task> homeFeedTasks = taskService.getHomeFeedTasks(user);
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

		setGlobalAttributesToModel(model);
		model.addAttribute("page", "home");

		model.addAttribute("followedTasks", taskService.getFollowedTasks(user));
		model.addAttribute("communityTasks", taskService.getCommunityTasks(user));
		model.addAttribute("recommendedTasks", taskService.getRecommendedTasks(user));

		return "home.view";
	}

}
