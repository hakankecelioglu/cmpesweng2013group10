package com.groupon.web.service;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.mail.MessagingException;
import javax.mail.internet.AddressException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.groupon.web.dao.UserDao;
import com.groupon.web.dao.model.Role;
import com.groupon.web.dao.model.RoleName;
import com.groupon.web.dao.model.User;
import com.groupon.web.dao.model.UserRole;
import com.groupon.web.dao.model.UserStatus;
import com.groupon.web.exception.GrouponException;
import com.groupon.web.util.GrouponLogger;
import com.groupon.web.util.GrouponWebUtils;

@Component
public class UserService {

	private static GrouponLogger logger = GrouponLogger.getLogger(UserService.class);

	@Autowired
	private UserDao userDao;

	@Autowired
	private MailService mailService;

	@Value("${SITE_URL}")
	private String SITE_URL;

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
	public void registerUser(final User user, RoleName roleName) throws GrouponException {
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

		if (user.getStatus() == UserStatus.DEACTIVE) {
			sendEmailConfirmationMail(user);
		}
	}
	/**
	 * updates a role of user to admin normal user
	 * @param user target user
	 * @param roleName rolename of user
	 */
	public void updateUserRole(User user, RoleName roleName) {
		UserRole userRole = user.getRole();

		if (userRole.getRole().getName() == roleName) {
			throw new GrouponException("User is already [" + roleName + "] !!!");
		}

		Role role = userDao.findRoleByRoleName(roleName);
		userRole.setRole(role);

		userDao.update(userRole);
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
	 * get user with specified user name
	 * @param username
	 * @return
	 */
	public User getUserByUsername(String username) {
		User user = userDao.findUserByUsername(username);
		return user;
	}

	/**
	 * Find the user from DB object by its id (primary key)
	 */
	public User getUserById(Long id) {
		User user = userDao.findUserById(id);
		return user;
	}
	/**
	 * activate a specified user
	 * @param user
	 */
	public void activateUser(User user) {
		user.setStatus(UserStatus.ACTIVE);
		userDao.update(user);
	}
	/**
	 * incremet reputation of user by given increment amount
	 * @param userId
	 * @param increment
	 */
	public void incrementUserReputation(Long userId, int increment) {
		userDao.incrementUserReputation(userId, increment);
	}



	/**
	 * return list of top helpful users
	 */
	public List<User> getTopHelpfulUsers(int page, int max) {
		return userDao.getUsersSortedByReputation(page, max);
	}
	/**
	 * return number of active users
	 */
	public long countActiveUsers() {
		return userDao.countActiveUsers();
	}

	/**
	 * send email confirmation mail to a user
	 * @param user
	 */
	private void sendEmailConfirmationMail(final User user) {
		new Thread(new Runnable() {
			@Override
			public void run() {
				String userCode = GrouponWebUtils.generateUserHash(user);

				StringBuilder linkBuilder = new StringBuilder(SITE_URL);
				linkBuilder.append("emailApproval?code=");
				linkBuilder.append(userCode);
				linkBuilder.append("&userId=");
				linkBuilder.append(user.getId());

				Map<String, Object> params = new HashMap<String, Object>();
				params.put("signup_username", user.getUsername());
				params.put("signup_approval_link", linkBuilder.toString());

				try {
					mailService.sendEmail("mails/signupMail.vm", user.getEmail(), "Task Together Registration", params, null);
				} catch (AddressException e) {
					logger.error(e, "Error occured while sending email");
				} catch (MessagingException e) {
					logger.error(e, "Error occured while sending email");
				} catch (UnsupportedEncodingException e) {
					logger.error(e, "Error occured while sending email");
				}
			}
		}).start();
	}
}
