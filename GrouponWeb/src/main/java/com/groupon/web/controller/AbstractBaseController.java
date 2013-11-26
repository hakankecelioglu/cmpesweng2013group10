package com.groupon.web.controller;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;

import com.groupon.web.dao.model.RoleName;
import com.groupon.web.dao.model.User;
import com.groupon.web.dao.model.UserStatus;
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

	public void setGlobalAttributesToModel(Model model) {
		model.addAttribute("user", getUser());
	}

	public Long getLongParameter(HttpServletRequest request, String parameterName) {
		String value = request.getParameter(parameterName);
		return Long.parseLong(value);
	}

	public Integer getIntegerParameter(HttpServletRequest request, String parameterName) {
		String value = request.getParameter(parameterName);
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

}