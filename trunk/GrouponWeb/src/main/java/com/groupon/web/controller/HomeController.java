package com.groupon.web.controller;

import java.util.ArrayList;
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
import com.groupon.web.service.UserService;
import com.groupon.web.util.GrouponConstants;
import com.groupon.web.util.GrouponWebUtils;

@Controller
public class HomeController extends AbstractBaseController {
	@Autowired
	private TaskService taskService;
	
	@Autowired
	private UserService userService;

	@Autowired
	private CommunityService communityService;

	/**
	 * Opens home page
	 * 
	 * @param request
	 * @param model
	 * @return the view which will be shown on homepage
	 */
	@RequestMapping(value = "/", method = RequestMethod.GET)
	public Object home(HttpServletRequest request, Model model) {
		setGlobalAttributesToModel(model, request);
		model.addAttribute("page", "home");

		User user = getUser();
		if (user == null) {
			// TODO : limit search
			model.addAttribute("allcommunities", communityService.getAllCommunities());
			return "homeguest.view";
		}

		List<Task> homeFeedTasks = taskService.getHomeFeedTasks(user, getCurrentSortBy(request));
		if (homeFeedTasks == null || homeFeedTasks.size() == 0) {
			model.addAttribute("emptyHomeFeed", Boolean.TRUE);
			homeFeedTasks = taskService.getAllTasks(0, 5, getCurrentSortBy(request));
		}

		model.addAttribute("homeFeedTasks", homeFeedTasks);

		if (homeFeedTasks.size() > 0) {
			List<Long> taskIds = GrouponWebUtils.convertModelListToLongList(homeFeedTasks);

			Map<Long, Boolean> followedTaskMap = taskService.findFollowedTasksIdsByUser(user, taskIds);
			model.addAttribute("followedMap", followedTaskMap);

			Map<Long, Integer> replyCounts = taskService.getTaskHelpCounts(taskIds);
			putReplyPercentagesToModel(homeFeedTasks, replyCounts, model);
		}

		return "home.view";
	}

	/**
	 * Opens advanced search page
	 * @param request
	 * @param model
	 * @return view for advanced search page
	 */
	@RequestMapping(value = "/search", method = RequestMethod.GET)
	public Object search(HttpServletRequest request, Model model) {
		return "advancedSearch.view";
	}

	/**
	 * Opens contact us page
	 * @param request
	 * @param model
	 * @return view for contact us page
	 */
	@RequestMapping(value = "/contactUs", method = RequestMethod.GET)
	public Object contactUs(HttpServletRequest request, Model model) {
		return "contactUs.view";
	}

	/**
	 * Opens android application download page
	 * @param request
	 * @param model
	 * @return view for android app download page
	 */
	@RequestMapping(value = "/androidDown", method = RequestMethod.GET)
	public Object androidDown(HttpServletRequest request, Model model) {
		return "androidDown.view";
	}

	/**
	 * Opens about use page
	 * @param request
	 * @param model
	 * @return view for about us page
	 */
	@RequestMapping(value = "/aboutUs", method = RequestMethod.GET)
	public Object aboutUs(HttpServletRequest request, Model model) {
		return "aboutUs.view";
	}

	/**
	 * Opens community feed
	 * @param request
	 * @param model
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

	/**
	 * Updates logged user's sortby preference
	 * @param sortBy new sortby preference of the user
	 * @param request
	 * @return json response
	 */
	@RequestMapping(value = "/setSorting", method = RequestMethod.POST)
	public ResponseEntity<Map<String, Object>> setSorting(@RequestParam String sortBy, HttpServletRequest request) {
		Map<String, Object> response = new HashMap<String, Object>();

		HttpSession session = request.getSession(true);
		SortBy newSortBy = SortBy.valueOf(sortBy);
		session.setAttribute(GrouponConstants.SESSION_ATTR_SORTBY, newSortBy);

		response.put("sortBy", newSortBy);

		return prepareSuccessResponse(response);
	}

	/**
	 * Opens newest communities page
	 * @param request
	 * @param model
	 * @param page the page to start with
	 * @return the view for newest communities
	 */
	@RequestMapping(value = "/communities/newest", method = RequestMethod.GET)
	public Object allCommunities(HttpServletRequest request, Model model, @RequestParam(required = false) Integer page) {
		setGlobalAttributesToModel(model, request);
		model.addAttribute("page", "allCommunities");

		if (page == null || page < 0) {
			page = 0;
		}

		model.addAttribute("allcommunities", communityService.getNewestCommunities(page, 5));

		Long count = communityService.getCommunityCount();
		generatePagination(model, page, count, 5);

		return "allCommunities.view";
	}

	/**
	 * Opens newest tasks page
	 * @param request
	 * @param model
	 * @param page the page to start with
	 * @return view for newest tasks page
	 */
	@RequestMapping(value = "/tasks/newest", method = RequestMethod.GET)
	public Object newestTasks(HttpServletRequest request, Model model, @RequestParam(required = false) Integer page) {
		setGlobalAttributesToModel(model, request);
		model.addAttribute("page", "newestTasks");

		User user = getUser();
		if (user == null) {
			return "redirect:/";
		}
		
		int currentPage = (page != null) ? page.intValue() : 0;

		List<Task> homeFeedTasks = taskService.getAllTasks(currentPage, 5, SortBy.LATEST);
		model.addAttribute("homeFeedTasks", homeFeedTasks);

		if (homeFeedTasks.size() > 0) {
			List<Long> taskIds = GrouponWebUtils.convertModelListToLongList(homeFeedTasks);

			Map<Long, Boolean> followedTaskMap = taskService.findFollowedTasksIdsByUser(user, taskIds);
			model.addAttribute("followedMap", followedTaskMap);

			Map<Long, Integer> replyCounts = taskService.getTaskHelpCounts(taskIds);
			putReplyPercentagesToModel(homeFeedTasks, replyCounts, model);
		}
		
		long count = taskService.countOpenTasks();
		generatePagination(model, currentPage, count, 5);

		return "newestTasks.view";
	}
	
	/**
	 * Opens top helpful users page
	 * @param request
	 * @param model
	 * @param page page to start with
	 * @return the view for top helpful users
	 */
	@RequestMapping(value = "/user/helpful", method = RequestMethod.GET)
	public Object topHelpfulUsers(HttpServletRequest request, Model model, @RequestParam(required = false) Integer page) {
		setGlobalAttributesToModel(model, request);
		model.addAttribute("page", "topHelpfulUsers");

		User user = getUser();
		if (user == null) {
			return "redirect:/";
		}
		
		int currentPage = (page != null) ? page.intValue() : 0;

		List<User> users = userService.getTopHelpfulUsers(currentPage, 10);
		model.addAttribute("users", users);

		long count = userService.countActiveUsers();
		generatePagination(model, currentPage, count, 5);

		return "topHelpfulUsers.view";
	}

	private void generatePagination(Model model, int currentPage, long numberOfItems, int itemsPerPage) {
		ArrayList<Integer> pageNumbers = new ArrayList<Integer>(5);

		int allPages = (int) Math.ceil((double) numberOfItems / itemsPerPage);

		int startPage = Math.max(0, currentPage - 2);
		int endPage = Math.min(allPages - 1, currentPage + 2);
		int numPages = endPage - startPage + 1;

		while (allPages > numPages && numPages < 5) {
			if (startPage > 0) {
				startPage--;
			} else if (endPage < allPages - 1) {
				endPage++;
			}

			numPages++;
		}

		for (int i = startPage; i < endPage + 1; i++) {
			pageNumbers.add(i);
		}

		model.addAttribute("pages", pageNumbers);
		model.addAttribute("currentPage", currentPage);
		model.addAttribute("hasNext", currentPage < allPages - 1);
		model.addAttribute("hasPrev", currentPage > 0);
	}
}
