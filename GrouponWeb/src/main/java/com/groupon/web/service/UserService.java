package com.groupon.web.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.groupon.web.controller.GrouponException;
import com.groupon.web.dao.UserDao;
import com.groupon.web.dao.model.Role;
import com.groupon.web.dao.model.RoleName;
import com.groupon.web.dao.model.User;
import com.groupon.web.dao.model.UserRole;

@Component
public class UserService {
	
	@Autowired
	private UserDao userDao;
	
	public void registerUser(User user, RoleName roleName) throws GrouponException {
		if (userDao.userExistsWithEmail(user.getEmail())) {
			throw new GrouponException("User with this email address exists!");
		}
		
		if (userDao.userExistsWithUsername(user.getUsername())) {
			throw new GrouponException("User with this username exists!");
		}
		
		userDao.saveUser(user);

		Role role = userDao.findRoleByRoleName(roleName);
		
		if (role == null) {
			throw new GrouponException("Role not found! Server setup error!");
		}
		
		UserRole userRole = new UserRole();
		userRole.setUser(user);
		userRole.setRole(role);
		userDao.saveRole(userRole);
		
		user.setRole(userRole);
	}
	
	public User getUserByEmailAndPassword(String email, String password) throws GrouponException {
		User user = userDao.findUserByEmail(email);
		if (user == null) {
			return null;
		} else if (!user.getPassword().equals(password)) {
			throw new GrouponException("Wrong password!");
		} else {
			return user;
		}
	}
	
	public User getUserByUsernameAndPassword(String username, String password) throws GrouponException {
		User user = userDao.findUserByUsername(username);
		if (user == null) {
			return null;
		} else if (!user.getPassword().equals(password)) {
			throw new GrouponException("Wrong password!");
		} else {
			return user;
		}
	}
}
