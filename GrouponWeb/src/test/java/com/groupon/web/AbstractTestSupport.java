package com.groupon.web;

import junit.framework.Assert;

import org.junit.runner.RunWith;
import org.springframework.mock.web.MockServletConfig;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.GenericWebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;

/**
 * @author turankonan
 * 
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(loader = MockWebContextLoader.class, locations = { "classpath:app-config-test.xml" })
public abstract class AbstractTestSupport {

	private static DispatcherServlet dispatcherServlet;

	@SuppressWarnings("serial")
	public static DispatcherServlet getServletInstance() {
		try {
			if (dispatcherServlet == null) {
				dispatcherServlet = new DispatcherServlet() {
					@Override
					protected WebApplicationContext createWebApplicationContext(WebApplicationContext parent) {
						GenericWebApplicationContext wac = new GenericWebApplicationContext();
						wac.setParent(MockWebContextLoader.getInstance());
						wac.refresh();
						return wac;
					}
				};

				dispatcherServlet.init(new MockServletConfig());
			}
		} catch (Throwable t) {
			Assert.fail("Unable to create a dispatcher servlet: " + t.getMessage());
		}
		return dispatcherServlet;
	}

	@SuppressWarnings("unchecked")
	public static <T> T getBean(String bean) {
		WebApplicationContext instance = MockWebContextLoader.getInstance();

		return (T) instance.getBean(bean);
	}
}