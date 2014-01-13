package com.groupon.web.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;

import com.groupon.web.dao.model.NeedType;
import com.groupon.web.dao.model.RoleName;
import com.groupon.web.dao.model.SortBy;
import com.groupon.web.dao.model.Task;
import com.groupon.web.dao.model.User;
import com.groupon.web.dao.model.UserStatus;
import com.groupon.web.util.GrouponConstants;
import com.groupon.web.util.GrouponLogger;
import com.groupon.web.util.GrouponThreadLocal;

/**
 * Abstract controller class which is extended by every controller. Includes basic request/response and user authorization operations.
 * @author sedrik
 */
public abstract class AbstractBaseController {
	protected GrouponLogger logger = GrouponLogger.getLogger(getClass());

	/**
	 * Returns the logged user, if any.
	 * @return the logged user, if any. null otherwise.
	 */
	public User getUser() {
		return GrouponThreadLocal.get();
	}

	/**
	 * Prepares a Http 200 json response
	 * @param map map which will be converted to json object.
	 * @return json Htpp 200 response for a request 
	 */
	public ResponseEntity<Map<String, Object>> prepareSuccessResponse(Map<String, Object> response) {
		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.OK);
	}

	/**
	 * Prepares a Http 400 json response
	 * @param response map which will be converted to json object.
	 * @return json Http 400 response for a request
	 */
	public ResponseEntity<Map<String, Object>> prepareErrorResponse(Map<String, Object> response) {
		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.BAD_REQUEST);
	}

	/**
	 * Returns the logged user's sortby preference for homepage.
	 * @param request the request object that the user made
	 * @return the logged user's sortby preference for homepage.
	 */
	public SortBy getCurrentSortBy(HttpServletRequest request) {
		HttpSession session = request.getSession(true);
		SortBy sortby = (SortBy) session.getAttribute(GrouponConstants.SESSION_ATTR_SORTBY);
		return sortby == null ? SortBy.DEADLINE : sortby;
	}

	/**
	 * Sets global attributes such as logged user, user's sortby preference and context path of the servlet to the model object.
	 * @param model model object for view
	 * @param request request object that the user made
	 */
	public void setGlobalAttributesToModel(Model model, HttpServletRequest request) {
		model.addAttribute("user", getUser());
		model.addAttribute("sortby", getCurrentSortBy(request));
		String cp = request.getContextPath() + "/";
		model.addAttribute("contextPath", cp);
	}

	/**
	 * Returns a long parameter from a HttpServletRequest
	 * @param request
	 * @param parameterName name of the parameter
	 * @return value of the request parameter casted to long
	 */
	public Long getLongParameter(HttpServletRequest request, String parameterName) {
		String value = request.getParameter(parameterName);
		if (value == null)
			return null;
		return Long.parseLong(value);
	}

	/**
	 * Returns an integer parameter from a HttpServletRequest
	 * @param request
	 * @param parameterName name of the parameter
	 * @return value of the request parameter casted to int
	 */
	public Integer getIntegerParameter(HttpServletRequest request, String parameterName) {
		String value = request.getParameter(parameterName);
		if (value == null)
			return null;
		return Integer.parseInt(value);
	}

	/**
	 * Returns true if the logged user has given roles
	 * @param roleNames rolenames to check whether the logged user has or not
	 * @return true if the logged user has given roles, false otherwise
	 */
	public boolean hasRoleAccessGranted(RoleName... roleNames) {
		User user = getUser();
		if (user == null || user.getRole() == null || user.getRole().getRole() == null || user.getRole().getRole().getName() == null) {
			return false;
		}

		if (roleNames == null || roleNames.length == 0) {
			return true;
		}

		for (RoleName roleName : roleNames) {
			if (roleName == user.getRole().getRole().getName()) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Returns true if the logged user has any of the given statuses
	 * @param statuses status names to check whether the logged user has or not
	 * @return true if the logged user has any of the given statuses, false otherwise
	 */
	public boolean hasStatusAccessGranted(UserStatus... statuses) {
		User user = getUser();
		if (user == null || user.getStatus() == null) {
			return false;
		}

		if (statuses == null || statuses.length == 0) {
			return true;
		}

		for (UserStatus status : statuses) {
			if (status == user.getStatus()) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Puts reply percentages to model object. Calculates how much of the tasks are completed.
	 * @param tasks tasks whose percentages will be calculated.
	 * @param replyCounts counts of replies which made to tasks
	 * @param model model object to fill
	 */
	public void putReplyPercentagesToModel(List<Task> tasks, Map<Long, Integer> replyCounts, Model model) {
		Map<Long, Integer> percentCompleted = new HashMap<Long, Integer>();
		for (Task task : tasks) {
			if (task.getNeedType() == NeedType.GOODS) {
				Integer taskNeed = task.getRequirementQuantity();
				if (replyCounts.containsKey(task.getId()) && taskNeed != null && taskNeed > 0) {
					Integer completed = replyCounts.get(task.getId());
					Integer percent = (completed * 100) / taskNeed;
					percentCompleted.put(task.getId(), percent);
				}
			}
		}
		model.addAttribute("percentCompleted", percentCompleted);
		model.addAttribute("quantityCompleted", replyCounts);
	}

}