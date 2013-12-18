package com.groupon.web.exception;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.json.MappingJacksonJsonView;

import com.groupon.web.util.GrouponLogger;

/**
 * This class handles the exceptions.
 * 
 * @author sedrik
 */
public class GrouponExceptionResolver implements HandlerExceptionResolver {
	private transient GrouponLogger logger = GrouponLogger.getLogger(getClass());

	public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
		MappingJacksonJsonView jsonView = new MappingJacksonJsonView();
		Map<String, Object> responseBody = new HashMap<String, Object>();

		if (ex instanceof GrouponException) {
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			logger.info("resolveException::Exception::{0}", ex.getMessage());
		} else {
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			logger.error(ex, "Resolved Exception::{0}", ex.getMessage());
		}

		responseBody.put("error", ex.getMessage());
		return new ModelAndView(jsonView, responseBody);
	}
}