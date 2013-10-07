package com.groupon.web.listeners;

import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

public class GrouponSessionListener implements HttpSessionListener {

    @Override
    public void sessionCreated(HttpSessionEvent se) {
	se.getSession().setMaxInactiveInterval(-1);
    }

    @Override
    public void sessionDestroyed(HttpSessionEvent se) {

    }

}
