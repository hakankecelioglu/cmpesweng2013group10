package com.groupon.web.filters;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import com.groupon.web.dao.model.User;
import com.groupon.web.service.UserService;
import com.groupon.web.util.ControllerConstants;
import com.groupon.web.util.GrouponWebUtils;

/**
 * If there is no user in session, checks cookie and puts logged in user to the
 * session if he exists in cookie.
 * 
 * @author sedrik
 * 
 */
@Component("sessionControlFilter")
public class SessionControlFilter implements Filter {

	@Autowired
	private UserService userService;

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		Assert.notNull(userService);

		HttpServletRequest req = (HttpServletRequest) request;
		HttpSession session = req.getSession(true);
		if (session.getAttribute(ControllerConstants.SESSION_ATTR_USER) == null) {
			Cookie[] cookies = req.getCookies();
			if (cookies != null) {
				for (Cookie cookie : cookies) {
					if (cookie.getName().equals(ControllerConstants.COOKIE_NAME_USER)) {
						String cookieValue = cookie.getValue();
						Long userId = GrouponWebUtils.extractUserIdFromCookieValue(cookieValue);
						if (userId != null) {
							User user = userService.getUserById(userId);
							if (user != null) {
								String userHash = GrouponWebUtils.generateUserHash(user);
								String userHashInCookie = GrouponWebUtils.extractUserHashFromCookieValue(cookieValue);
								if (userHash.equals(userHashInCookie)) {
									session.setAttribute(ControllerConstants.SESSION_ATTR_USER, user);
								}
							}
							break;
						}
					}
				}
			}
		}
		chain.doFilter(request, response);
	}

	@Override
	public void destroy() {

	}

}
