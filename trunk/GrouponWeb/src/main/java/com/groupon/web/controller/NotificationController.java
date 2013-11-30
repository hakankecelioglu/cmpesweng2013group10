package com.groupon.web.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.groupon.web.dao.model.Community;
import com.groupon.web.dao.model.Notification;
import com.groupon.web.dao.model.Task;
import com.groupon.web.dao.model.User;
import com.groupon.web.exception.GrouponException;
import com.groupon.web.service.NotificationService;

@Controller
@RequestMapping(value = "notification")
public class NotificationController extends AbstractBaseController {

	@Autowired
	private NotificationService notificationService;

	@RequestMapping(value = "get", method = RequestMethod.POST)
	public ResponseEntity<Map<String, Object>> getNotifications() {
		Map<String, Object> response = new HashMap<String, Object>();
		User user = getUser();

		if (user == null) {
			throw new GrouponException("You must be logged in to receive notifications!");
		}

		List<Notification> notifications = notificationService.getNotifications(user.getId());
		response.put("notifications", convertNotificationsToList(notifications));

		return prepareSuccessResponse(response);
	}

	@RequestMapping(value = "count", method = RequestMethod.POST)
	public ResponseEntity<Map<String, Object>> getNotificationCount() {
		Map<String, Object> response = new HashMap<String, Object>();
		User user = getUser();

		if (user == null) {
			throw new GrouponException("You must be logged in to receive notifications!");
		}

		Integer count = notificationService.getNotificationCount(user.getId());
		response.put("count", count);

		return prepareSuccessResponse(response);
	}
	
	private List<Map<String, Object>> convertNotificationsToList(List<Notification> notifs) {
		List<Map<String, Object>> list = new ArrayList<Map<String,Object>>();
		for (Notification notif : notifs) {
			list.add(convertNotificationToMap(notif));
		}
		return list;
	}

	private Map<String, Object> convertNotificationToMap(Notification notif) {
		Map<String, Object> map = new HashMap<String, Object>();
		
		switch (notif.getType()) {
		case TASK_CREATED_IN_FOLLOWED_COMMUNITY:
			convertTaskInCommunityNotificationToMap(notif, map);
		}
		
		map.put("date", notif.getCreateDate().getTime());
		map.put("isRead", notif.getIsRead());
		map.put("type", notif.getType());
		return map;
	}
	
	private Map<String, Object> convertTaskInCommunityNotificationToMap(Notification notif, Map<String, Object> map) {
		map.put("source", convertSourceToMap(notif.getSource()));
		map.put("task", convertTaskToMap(notif.getTask()));
		map.put("community", convertCommunityToMap(notif.getCommunity()));
		return map;
	}
	
	private Map<String, Object> convertSourceToMap(User user) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("id", user.getId());
		map.put("uname", user.getUsername());
		return map;
	}
	
	private Map<String, Object> convertTaskToMap(Task task) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("id", task.getId());
		map.put("name", task.getTitle());
		return map;
	}
	
	private Map<String, Object> convertCommunityToMap(Community community) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("id", community.getId());
		map.put("name", community.getName());
		return map;
	}
}
