package com.groupon.web.controller.adm;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.groupon.web.controller.AbstractBaseController;
import com.groupon.web.controller.json.UserJson;
import com.groupon.web.dao.model.RoleName;
import com.groupon.web.dao.model.User;
import com.groupon.web.exception.GrouponException;
import com.groupon.web.service.UserService;

@Controller
@RequestMapping(value = "admin")
public class AdminController extends AbstractBaseController {

	@Autowired
	private UserService userService;

	@RequestMapping(value = "users", method = RequestMethod.GET)
	public String getManageUsers(HttpServletRequest request, Model model) {
		if (!hasRoleAccessGranted(RoleName.ADMIN)) {
			return "redirect:/";
		}

		setGlobalAttributesToModel(model, request);

		return "admManageUsers.view";
	}

	@RequestMapping(value = "getUserById", method = RequestMethod.GET)
	public ResponseEntity<Map<String, Object>> getUserById(HttpServletRequest request, Model model, @RequestParam Long userId) {
		if (!hasRoleAccessGranted(RoleName.ADMIN)) {
			throw new GrouponException("Not authorized!");
		}

		if (userId == null) {
			throw new GrouponException("Enter a userId!!");
		}

		User user = userService.getUserById(userId);
		if (user == null) {
			throw new GrouponException("User not found");
		}

		Map<String, Object> response = new HashMap<String, Object>();
		response.put("user", UserJson.convert(user));
		response.put("status", user.getStatus());
		response.put("role", user.getRole().getRole().getName());

		return prepareSuccessResponse(response);
	}

	@RequestMapping(value = "getUserByUserName", method = RequestMethod.GET)
	public ResponseEntity<Map<String, Object>> getUserByUserName(HttpServletRequest request, Model model, @RequestParam String username) {
		if (!hasRoleAccessGranted(RoleName.ADMIN)) {
			throw new GrouponException("Not authorized!");
		}

		if (StringUtils.isBlank(username)) {
			throw new GrouponException("Enter a username!!");
		}

		User user = userService.getUserByUsername(username);
		if (user == null) {
			throw new GrouponException("User not found");
		}

		Map<String, Object> response = new HashMap<String, Object>();
		response.put("user", UserJson.convert(user));
		response.put("status", user.getStatus());
		response.put("role", user.getRole().getRole().getName());

		return prepareSuccessResponse(response);
	}

	@RequestMapping(value = "makeAdmin", method = RequestMethod.POST)
	public ResponseEntity<Map<String, Object>> makeAdmin(HttpServletRequest request, Model model, @RequestParam Long userId) {
		if (!hasRoleAccessGranted(RoleName.ADMIN)) {
			throw new GrouponException("Not authorized!");
		}

		if (userId == null) {
			throw new GrouponException("Enter a userId!!");
		}

		User user = userService.getUserById(userId);
		if (user == null) {
			throw new GrouponException("User not found");
		}

		userService.updateUserRole(user, RoleName.ADMIN);

		Map<String, Object> response = new HashMap<String, Object>();
		return prepareSuccessResponse(response);
	}
}
