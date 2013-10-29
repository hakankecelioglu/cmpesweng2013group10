package com.groupon.web.controller;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;

import com.groupon.web.dao.model.User;
import com.groupon.web.util.ControllerConstants;

public abstract class AbstractBaseController {
	public User getUser(HttpServletRequest request) {
		HttpSession session = request.getSession(true);
		Object userObject = session.getAttribute(ControllerConstants.SESSION_ATTR_USER);
		if (userObject != null && userObject instanceof User) {
			return (User) userObject;
		}
		return null;
	}

	public ResponseEntity<Map<String, Object>> prepareSuccessResponse(Map<String, Object> response) {
		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.OK);
	}

	public ResponseEntity<Map<String, Object>> prepareErrorResponse(Map<String, Object> response) {
		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.BAD_REQUEST);
	}

	public void setGlobalAttributesToModel(Model model, HttpServletRequest request) {
		model.addAttribute("user", getUser(request));
	}

	public Long getLongParameter(HttpServletRequest request, String parameterName) {
		String value = request.getParameter(parameterName);
		return Long.parseLong(value);
	}

	public Integer getIntegerParameter(HttpServletRequest request, String parameterName) {
		String value = request.getParameter(parameterName);
		return Integer.parseInt(value);
	}
}