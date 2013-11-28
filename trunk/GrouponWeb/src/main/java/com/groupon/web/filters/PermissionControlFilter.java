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
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Component;

import com.groupon.web.dao.model.User;
import com.groupon.web.dao.model.UserStatus;
import com.groupon.web.util.ControllerConstants;
import com.groupon.web.util.GrouponLogger;

@Component("permissionControlFilter")
public class PermissionControlFilter implements Filter {

	final Pattern excludePatternUsers = Pattern.compile("((^/res/)|(^/user/deactivated)|(^/user/deleted)|(^/user/banned)|(^/logout)|(^/emailApproval))");

	private GrouponLogger logger = GrouponLogger.getLogger(getClass());

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		HttpServletRequest req = (HttpServletRequest) request;
		HttpServletResponse resp = (HttpServletResponse) response;

		HttpSession session = req.getSession(true);
		User user = (User) session.getAttribute(ControllerConstants.SESSION_ATTR_USER);
		if (user != null) {
			checkUserStatusAndRedirectOrChain(user, req, resp, chain);
		} else {
			chain.doFilter(request, response);
		}
	}

	@Override
	public void destroy() {

	}

	private void checkUserStatusAndRedirectOrChain(User user, HttpServletRequest req, HttpServletResponse resp, FilterChain chain) throws IOException, ServletException {
		String requestURI = req.getRequestURI();
		if (req.getContextPath().length() > 0) {
			requestURI = requestURI.substring(req.getContextPath().length());
		}
		Matcher matcher = excludePatternUsers.matcher(requestURI);

		if (!matcher.find()) {
			logger.debug("checkUserStatusAndRedirectOrChain::not matched requestURI::{0}", requestURI);

			if (user.getStatus() == UserStatus.DEACTIVE) {
				resp.sendRedirect(req.getContextPath() + "/user/deactivated");
				return;
			} else if (user.getStatus() == UserStatus.BANNED) {
				resp.sendRedirect(req.getContextPath() + "/user/deleted");
				return;
			}
		}
		chain.doFilter(req, resp);
	}
}
