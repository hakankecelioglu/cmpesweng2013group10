package com.groupon.web.filters;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.groupon.web.dao.model.User;
import com.groupon.web.service.UserService;
import com.groupon.web.util.ControllerConstants;
import com.groupon.web.util.GrouponThreadLocal;
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

	final Pattern excludePatternRes = Pattern.compile("((^/res/))");

	@Autowired
	private UserService userService;

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		HttpServletRequest req = (HttpServletRequest) request;
		HttpServletResponse resp = (HttpServletResponse) response;
		Matcher matcher = excludePatternRes.matcher(req.getRequestURI());
		if (matcher.find()) {
			chain.doFilter(request, response);
			return;
		}
		
		User user = null;
		
		String authToken = req.getHeader(ControllerConstants.REQUEST_HEADER_AUTH_KEY);
		if (authToken == null) {
			HttpSession session = req.getSession(true);
			user = (User) session.getAttribute(ControllerConstants.SESSION_ATTR_USER);

			if (user == null) {
				user = checkCookieAndSetSession(req, resp, chain, session);
			}
		} else {
			user = checkAuthTokenAndGetUser(authToken);
		}
		
		if (user != null) {
			GrouponThreadLocal.set(user);
		} else {
			GrouponThreadLocal.unset();
		}
		chain.doFilter(req, resp);
	}

	private User checkCookieAndSetSession(HttpServletRequest req, HttpServletResponse resp, FilterChain chain, HttpSession session) throws IOException, ServletException {
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
								return user;
							}
						}
						break;
					}
				}
			}
		}
		return null;
	}
	
	private User checkAuthTokenAndGetUser(String authToken) {
		Long userId = GrouponWebUtils.extractUserIdFromCookieValue(authToken);
		if (userId != null) {
			User user = userService.getUserById(userId);
			if (user != null) {
				String userHash = GrouponWebUtils.generateUserHash(user);
				String userHashInCookie = GrouponWebUtils.extractUserHashFromCookieValue(authToken);
				if (userHash.equals(userHashInCookie)) {
					return user;
				}
			}
		}
		return null;
	}

	@Override
	public void destroy() {

	}

}
