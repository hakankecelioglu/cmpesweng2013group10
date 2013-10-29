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

	/**
	 * Creates a new user with a given role
	 * 
	 * @param user
	 *            user object to be created
	 * @param roleName
	 *            roleName which user will have
	 * @throws GrouponException
	 *             when the username or email exist in DB or when the given role
	 *             cannot be found in DB.
	 */
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

	/**
	 * Finds the user from DB object by its email address and password fields
	 * 
	 * @param email
	 *            email address of the user
	 * @param password
	 *            password of the user
	 * @return User found in DB
	 * @throws GrouponException
	 *             when the email exists in DB but the password is wrong
	 */
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

	/**
	 * Finds the user from DB object by its username and password fields
	 * 
	 * @param username
	 *            username of the user
	 * @param password
	 *            password of the user
	 * @return User found in DB
	 * @throws GrouponException
	 *             when the username exists in DB but the password is wrong
	 */
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

	/**
	 * Find the user from DB object by its id (primary key)
	 */
	public User getUserById(Long id) {
		User user = userDao.findUserById(id);
		return user;
	}
}
