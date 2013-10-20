package com.groupon.web.controller;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.groupon.web.dao.model.RoleName;
import com.groupon.web.dao.model.User;
import com.groupon.web.service.UserService;
import com.groupon.web.util.ControllerConstants;
import com.groupon.web.util.GrouponWebUtils;

@Controller
public class UserController extends AbstractBaseController {

	@Autowired
	private UserService userService;

	@RequestMapping(value = "login", method = RequestMethod.GET)
	public Object login() {
		// TODO to be implemented
		return "login.view";
	}

	@RequestMapping(value = "login", method = RequestMethod.POST)
	public ResponseEntity<Map<String, Object>> login(HttpServletRequest request) {
		Map<String, Object> response = new HashMap<String, Object>();

		try {
			GrouponWebUtils.rejectIfEmpty(request, "username", "password");

			String username = request.getParameter("username");
			String password = request.getParameter("password");

			String passwordHash = GrouponWebUtils.hashPasswordForDB(password);

			User user = userService.getUserByEmailAndPassword(username,
					passwordHash);
			if (user == null) {
				user = userService.getUserByUsernameAndPassword(username,
						passwordHash);
				if (user == null) {
					throw new GrouponException(
							"User not found with this username or email address!");
				}
			}

			setUserSession(request, user);

			response.put("message", "OK");
			return prepareSuccessResponse(response);
		} catch (GrouponException e) {
			response.put("error", e.getMessage());
			return prepareErrorResponse(response);
		}
	}

	@RequestMapping(value = "logout", method = { RequestMethod.GET,
			RequestMethod.POST })
	public String logout(HttpServletRequest request) {
		setUserSession(request, null);
		return "redirect:/";
	}

	@RequestMapping(value = "signup", method = RequestMethod.GET)
	public Object signup() {
		// TODO to be implemented
		return "signup.view";
	}

	@RequestMapping(value = "signup", method = RequestMethod.POST)
	public ResponseEntity<Map<String, Object>> signup(HttpServletRequest request) {
		Map<String, Object> response = new HashMap<String, Object>();

		try {
			GrouponWebUtils.rejectIfEmpty(request, "email", "password",
					"username");

			String email = request.getParameter("email");
			String username = request.getParameter("username");
			String password = request.getParameter("password");
			String name = request.getParameter("name");
			String surname = request.getParameter("surname");

			String passwordHash = GrouponWebUtils.hashPasswordForDB(password);

			User user = new User();
			user.setEmail(email);
			user.setUsername(username);
			user.setPassword(passwordHash);
			user.setName(name);
			user.setSurname(surname);

			userService.registerUser(user, RoleName.USER);
			setUserSession(request, user);

			response.put("message", "OK");
			return prepareSuccessResponse(response);
		} catch (GrouponException e) {
			response.put("error", e.getMessage());
			return prepareErrorResponse(response);
		}
	}

	private void setUserSession(HttpServletRequest request, User user) {
		HttpSession session = request.getSession(true);
		if (user != null) {
			session.setAttribute(ControllerConstants.SESSION_ATTR_USER, user);
		} else {
			session.removeAttribute(ControllerConstants.SESSION_ATTR_USER);
		}
	}
}