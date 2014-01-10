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

public abstract class AbstractBaseController {
	protected GrouponLogger logger = GrouponLogger.getLogger(getClass());

	public User getUser() {
		return GrouponThreadLocal.get();
	}

	public ResponseEntity<Map<String, Object>> prepareSuccessResponse(Map<String, Object> response) {
		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.OK);
	}

	public ResponseEntity<Map<String, Object>> prepareErrorResponse(Map<String, Object> response) {
		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.BAD_REQUEST);
	}

	public SortBy getCurrentSortBy(HttpServletRequest request) {
		HttpSession session = request.getSession(true);
		SortBy sortby = (SortBy) session.getAttribute(GrouponConstants.SESSION_ATTR_SORTBY);
		return sortby == null ? SortBy.DEADLINE : sortby;
	}

	public void setGlobalAttributesToModel(Model model, HttpServletRequest request) {
		model.addAttribute("user", getUser());
		model.addAttribute("sortby", getCurrentSortBy(request));
		String cp = request.getContextPath() + "/";
		model.addAttribute("contextPath", cp);
	}

	public Long getLongParameter(HttpServletRequest request, String parameterName) {
		String value = request.getParameter(parameterName);
		if (value == null)
			return null;
		return Long.parseLong(value);
	}

	public Integer getIntegerParameter(HttpServletRequest request, String parameterName) {
		String value = request.getParameter(parameterName);
		if (value == null)
			return null;
		return Integer.parseInt(value);
	}

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