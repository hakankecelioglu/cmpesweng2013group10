package com.groupon.web.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.groupon.web.controller.json.UserJson;
import com.groupon.web.dao.model.Community;
import com.groupon.web.dao.model.RoleName;
import com.groupon.web.dao.model.User;
import com.groupon.web.dao.model.UserStatus;
import com.groupon.web.exception.GrouponException;
import com.groupon.web.service.CommunityService;
import com.groupon.web.service.UserService;
import com.groupon.web.util.GrouponConstants;
import com.groupon.web.util.EmailValidator;
import com.groupon.web.util.GrouponWebUtils;

@Controller
public class UserController extends AbstractBaseController {

	@Autowired
	private UserService userService;

	@Autowired
	private CommunityService communityService;

	@Value("${COOKIE_PATH}")
	private String cookiePath;
	/**
	 * 
	 * @return login view
	 */
	@RequestMapping(value = "login", method = RequestMethod.GET)
	public Object login() {
		// TODO to be implemented
		return "login.view";
	}
	/**
	 * Logs user and set sesssion variables
	 * @param request
	 * @param resp
	 * @return
	 */
	@RequestMapping(value = "login", method = RequestMethod.POST)
	public ResponseEntity<Map<String, Object>> login(HttpServletRequest request, HttpServletResponse resp) {
		Map<String, Object> response = new HashMap<String, Object>();

		GrouponWebUtils.rejectIfEmpty(request, "username", "password");

		String username = request.getParameter("username");
		String password = request.getParameter("password");

		String passwordHash = GrouponWebUtils.hashPasswordForDB(password);

		User user = userService.getUserByEmailAndPassword(username, passwordHash);
		if (user == null) {
			user = userService.getUserByUsernameAndPassword(username, passwordHash);
			if (user == null) {
				throw new GrouponException("User not found with this username or email address!");
			}
		}

		setUserSession(request, resp, user);

		response.put("message", "OK");
		return prepareSuccessResponse(response);
	}
	/**
	 * login controller for mobile
	 * @param request
	 * @param resp
	 * @return
	 */
	@RequestMapping(value = "mobile/login", method = RequestMethod.POST)
	public ResponseEntity<Map<String, Object>> loginMobile(HttpServletRequest request, HttpServletResponse resp) {
		Map<String, Object> response = new HashMap<String, Object>();

		GrouponWebUtils.rejectIfEmpty(request, "username", "password");

		String username = request.getParameter("username");
		String password = request.getParameter("password");

		String passwordHash = GrouponWebUtils.hashPasswordForDB(password);

		User user = userService.getUserByEmailAndPassword(username, passwordHash);
		if (user == null) {
			user = userService.getUserByUsernameAndPassword(username, passwordHash);
			if (user == null) {
				throw new GrouponException("User not found with this username or email address!");
			}
		}

		response.put("user", UserJson.convert(user));
		response.put("auth", GrouponWebUtils.generateCookieForUser(user));
		response.put("message", "OK");
		return prepareSuccessResponse(response);
	}
	/**
	 * Log out user and redirects to root page
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "logout", method = { RequestMethod.GET, RequestMethod.POST })
	public String logout(HttpServletRequest request, HttpServletResponse response) {
		setUserSession(request, response, null);
		return "redirect:/";
	}
	/**
	 * 
	 * @return sign up view
	 */
	@RequestMapping(value = "signup", method = RequestMethod.GET)
	public Object signup() {
		// TODO to be implemented
		return "signup.view";
	}
	/**
	 * Sign ups user
	 * @param request
	 * @param servletResponse
	 * @return
	 */
	@RequestMapping(value = "signup", method = RequestMethod.POST)
	public ResponseEntity<Map<String, Object>> signup(HttpServletRequest request, HttpServletResponse servletResponse) {
		Map<String, Object> response = new HashMap<String, Object>();

		GrouponWebUtils.rejectIfEmpty(request, "email", "password", "username");

		String email = request.getParameter("email");
		String username = request.getParameter("username");
		String password = request.getParameter("password");
		String name = request.getParameter("name");
		String surname = request.getParameter("surname");

		String passwordHash = GrouponWebUtils.hashPasswordForDB(password);

		EmailValidator validator = new EmailValidator();
		if (!validator.validate(email)) {
			throw new GrouponException("Please enter a valid email address!");
		}

		User user = new User();
		user.setEmail(email);
		user.setUsername(username);
		user.setPassword(passwordHash);
		user.setName(name);
		user.setSurname(surname);
		user.setStatus(UserStatus.ACTIVE);

		userService.registerUser(user, RoleName.USER);
		setUserSession(request, servletResponse, user);

		response.put("message", "OK");
		return prepareSuccessResponse(response);
	}
	/**
	 * Mobile controller for signing up user
	 * @param request
	 * @param body
	 * @return
	 * @throws JSONException
	 */
	@RequestMapping(value = "mobile/signup", method = RequestMethod.POST)
	public ResponseEntity<Map<String, Object>> signupMobile(HttpServletRequest request, @RequestBody String body) throws JSONException {
		Map<String, Object> response = new HashMap<String, Object>();

		JSONObject json = new JSONObject(body);
		GrouponWebUtils.rejectIfEmpty(json, "email", "password", "username");

		String email = json.getString("email");
		String username = json.getString("username");
		String password = json.getString("password");
		String passwordHash = GrouponWebUtils.hashPasswordForDB(password);

		EmailValidator validator = new EmailValidator();
		if (!validator.validate(email)) {
			throw new GrouponException("Please enter a valid email address!");
		}

		User user = new User();
		user.setEmail(email);
		user.setUsername(username);
		user.setPassword(passwordHash);
		user.setStatus(UserStatus.ACTIVE);

		if (json.has("name")) {
			String name = json.getString("name");
			user.setName(name);
		}

		if (json.has("surname")) {
			String surname = json.getString("surname");
			user.setSurname(surname);
		}

		userService.registerUser(user, RoleName.USER);

		response.put("user", UserJson.convert(user));
		response.put("auth", GrouponWebUtils.generateCookieForUser(user));
		response.put("message", "OK");

		return prepareSuccessResponse(response);
	}
	/**
	 * return profile data of logged user
	 * @param request
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "profile", method = RequestMethod.GET)
	public Object getMyProfile(HttpServletRequest request, Model model) {
		User user = getUser();
		if (user == null) {
			return "redirect:/";
		}

		List<Community> usersCommunities = communityService.getCommunitiesByFollowerId(user.getId(), 0, 0);
		model.addAttribute("myCommunities", usersCommunities);

		model.addAttribute("page", "myprofile");
		model.addAttribute("profile", user);
		setGlobalAttributesToModel(model, request);
		return "profile.view";
	}
	/**
	 * return profile data of a user
	 * @param request
	 * @param model
	 * @param username
	 * @return
	 */
	@RequestMapping(value = "profile/{username}", method = RequestMethod.GET)
	public Object getMyProfile(HttpServletRequest request, Model model, @PathVariable("username") String username) {
		User user = getUser();
		if (user != null && username.equals(user.getUsername())) {
			return "redirect:/profile";
		}

		User profile = userService.getUserByUsername(username);
		if (profile == null) {
			return "redirect:/";
		}

		List<Community> usersCommunities = communityService.getCommunitiesByFollowerId(profile.getId(), 0, 0);
		model.addAttribute("myCommunities", usersCommunities);

		model.addAttribute("page", "myprofile");
		model.addAttribute("profile", profile);
		setGlobalAttributesToModel(model, request);
		return "profile.view";
	}
	/**
	 * return profile data of a user (mobile)
	 * @param request
	 * @param userId
	 * @return
	 */
	@RequestMapping(value = "mobile/profile/{userid}", method = RequestMethod.GET)
	public ResponseEntity<Map<String, Object>> getUserProfile(HttpServletRequest request, @PathVariable("userid") Long userId) {
		Map<String, Object> response = new HashMap<String, Object>();
		User user = getUser();
		if (user == null) {
			throw new GrouponException("Login before doing this action!");
		}

		User profile = userService.getUserById(userId);
		if (profile == null) {
			throw new GrouponException("Username cannot be found!");
		}

		response.put("user", UserJson.convert(profile));
		return prepareSuccessResponse(response);
	}
	/**
	 * controller that returns view  upon email activation.
	 * @param code
	 * @param userId
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "emailApproval", method = RequestMethod.GET)
	public Object emailApproval(@RequestParam String code, @RequestParam Long userId, HttpServletRequest request, HttpServletResponse response) {
		if (userId != null && code != null) {
			User user = userService.getUserById(userId);
			String hash = GrouponWebUtils.generateUserHash(user);
			if (hash.equals(code)) {
				userService.activateUser(user);

				setUserSession(request, response, user);
				return "redirect:/?approvalSuccessful";
			}
		}
		return "redirect:/user/deactivated?failed=true";
	}
	/**
	 * return view for deactivated user
	 * @return
	 */
	@RequestMapping(value = "user/deactivated", method = RequestMethod.GET)
	public Object userDeactivated() {
		return "userDeactivated.view";
	}
	/**
	 * return view for banned user
	 * @return
	 */
	@RequestMapping(value = "user/banned", method = RequestMethod.GET)
	public Object userBanned() {
		return "userBanned.view";
	}
	/**
	 * return view for deleted user
	 * @return
	 */
	@RequestMapping(value = "user/deleted", method = RequestMethod.GET)
	public Object userDeleted() {
		return "userDeleted.view";
	}
	/**
	 * set session variables for user
	 * @param request
	 * @param response
	 * @param user
	 */
	private void setUserSession(HttpServletRequest request, HttpServletResponse response, User user) {
		HttpSession session = request.getSession(true);
		if (user != null) {
			session.setAttribute(GrouponConstants.SESSION_ATTR_USER, user);
			Cookie cookie = new Cookie(GrouponConstants.COOKIE_NAME_USER, GrouponWebUtils.generateCookieForUser(user));
			cookie.setPath(cookiePath);
			cookie.setMaxAge(GrouponConstants.COOKIE_USER_MAX_AGE);
			response.addCookie(cookie);
		} else {
			session.removeAttribute(GrouponConstants.SESSION_ATTR_USER);
			Cookie[] cookies = request.getCookies();
			for (Cookie cookie : cookies) {
				if (cookie.getName().equals(GrouponConstants.COOKIE_NAME_USER)) {
					cookie.setMaxAge(0);
					cookie.setValue("");
					cookie.setPath(cookiePath);
					response.addCookie(cookie);
				}
			}
		}
	}
}