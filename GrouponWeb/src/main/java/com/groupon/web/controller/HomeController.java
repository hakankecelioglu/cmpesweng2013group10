package com.groupon.web.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

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

		model.addAttribute("followedTasks", taskService.getFollowedTasks(user));
		model.addAttribute("communityTasks", taskService.getCommunityTasks(user));
		model.addAttribute("recommendedTasks", taskService.getRecommendedTasks(user));

		return "home.view";
	}

}
