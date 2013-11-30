package com.groupon.web.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.task.TaskExecutor;
import org.springframework.stereotype.Component;

import com.groupon.web.dao.NotificationDao;
import com.groupon.web.dao.model.Community;
import com.groupon.web.dao.model.Notification;
import com.groupon.web.dao.model.NotificationType;
import com.groupon.web.dao.model.Task;
import com.groupon.web.dao.model.User;
import com.groupon.web.util.AsyncWebTask;

@Component
public class NotificationService {

	@Autowired
	private NotificationDao notificationDao;

	@Autowired
	@Qualifier("notificationExecutor")
	private TaskExecutor taskExecutor;

	private Object lockNotificationCount = new Object();

	private Map<Long, Integer> unreadNotifications = new HashMap<Long, Integer>();

	public List<Notification> getNotifications(Long userId) {
		return notificationDao.findNotificationsOfUser(userId);
	}

	public Integer getNotificationCount(Long userId) {
		if (unreadNotifications.containsKey(userId)) {
			return unreadNotifications.get(userId);
		} else {
			Integer count = notificationDao.countUnreadNotifications(userId);
			unreadNotifications.put(userId, count);
			return count;
		}
	}

	public void markTaskNotificationsRead(final Long userId, final Long taskId) {
		taskExecutor.execute(new AsyncWebTask(notificationDao.getSessionFactory()) {
			public void runInBackground() {
				Integer updatedRows = notificationDao.markNotificationsAsReadByTask(getSession(), userId, taskId);
				if (unreadNotifications.containsKey(userId)) {
					synchronized (lockNotificationCount) {
						Integer count = unreadNotifications.get(userId);
						unreadNotifications.put(userId, count - updatedRows);
					}
				}
			}
		});
	}

	public void sendTaskCreatedInFollowedCommunityNotification(final Long communityId, final Long taskId) {
		taskExecutor.execute(new AsyncWebTask(notificationDao.getSessionFactory()) {
			public void runInBackground() {
				Community community = (Community) getSession().get(Community.class, communityId);
				Task task = (Task) getSession().get(Task.class, taskId);
				User source = task.getOwner();

				for (User user : community.getMembers()) {
					if (!user.equals(source)) {
						Notification notification = new Notification();
						notification.setTask(task);
						notification.setCommunity(community);
						notification.setReceiver(user);
						notification.setSource(source);
						notification.setType(NotificationType.TASK_CREATED_IN_FOLLOWED_COMMUNITY);
						notificationDao.saveNotification(getSession(), notification);

						if (unreadNotifications.containsKey(user.getId())) {
							synchronized (lockNotificationCount) {
								Integer count = unreadNotifications.get(user.getId());
								unreadNotifications.put(user.getId(), count + 1);
							}
						}
					}
				}
			}
		});
	}
}
