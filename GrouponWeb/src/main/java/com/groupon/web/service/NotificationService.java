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
import com.groupon.web.dao.model.RateDirection;
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
				updateNotificationMapIfUserExists(userId, -updatedRows);
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

						updateNotificationMapIfUserExists(user.getId(), 1);
					}
				}
			}
		});
	}

	public void sendTaskRepliedNotification(final Long taskId, final Long replier) {
		taskExecutor.execute(new AsyncWebTask(notificationDao.getSessionFactory()) {
			public void runInBackground() {
				Task task = (Task) getSession().get(Task.class, taskId);
				User source = (User) getSession().get(User.class, replier);

				List<User> followers = task.getFollowers();
				for (User follower : followers) {
					if (!follower.equals(source)) {
						Notification notification = new Notification();
						notification.setTask(task);
						notification.setReceiver(follower);
						notification.setSource(source);
						notification.setType(NotificationType.TASK_REPLY);
						notificationDao.saveNotification(getSession(), notification);

						updateNotificationMapIfUserExists(follower.getId(), 1);
					}
				}
			}
		});
	}

	public void sendTaskVoteNotification(final Long taskId, final Long voterUserId, final RateDirection direction) {
		taskExecutor.execute(new AsyncWebTask(notificationDao.getSessionFactory()) {
			public void runInBackground() {
				Task task = (Task) getSession().get(Task.class, taskId);
				User source = (User) getSession().get(User.class, voterUserId);
				User receiver = task.getOwner();

				Notification notification = new Notification();
				notification.setTask(task);
				notification.setReceiver(receiver);
				notification.setSource(source);
				if (direction == RateDirection.UP) {
					notification.setType(NotificationType.TASK_UPVOTE);
				} else {
					notification.setType(NotificationType.TASK_DOWNVOTE);
				}
				notificationDao.saveNotification(getSession(), notification);

				updateNotificationMapIfUserExists(receiver.getId(), 1);
			}
		});
	}

	public void updateTaskVoteNotification(final Long taskId, final Long voterUserId, final RateDirection direction) {
		taskExecutor.execute(new AsyncWebTask(notificationDao.getSessionFactory()) {
			public void runInBackground() {
				NotificationType previousNotificationType;
				NotificationType newNotificationType;

				Task task = (Task) getSession().get(Task.class, taskId);
				User source = (User) getSession().get(User.class, voterUserId);
				User receiver = task.getOwner();

				if (direction == RateDirection.UP) {
					previousNotificationType = NotificationType.TASK_DOWNVOTE;
					newNotificationType = NotificationType.TASK_UPVOTE;
				} else {
					previousNotificationType = NotificationType.TASK_UPVOTE;
					newNotificationType = NotificationType.TASK_DOWNVOTE;
				}

				Notification notification = notificationDao.findNotification(getSession(), receiver.getId(), source.getId(), task.getId(), previousNotificationType);

				if (notification != null) {
					notification.setType(newNotificationType);
					notificationDao.updateNotification(getSession(), notification);
				}
			}
		});
	}

	public void deleteTaskVoteNotification(final Long taskId, final Long receiverId, final Long voterUserId) {
		taskExecutor.execute(new AsyncWebTask(notificationDao.getSessionFactory()) {
			public void runInBackground() {
				User source = (User) getSession().get(User.class, voterUserId);
				User receiver = (User) getSession().get(User.class, receiverId);

				Notification notification = notificationDao.findTaskVoteNotification(getSession(), receiver.getId(), source.getId(), taskId);

				if (notification != null) {
					if (notification.getIsRead() == Boolean.FALSE) {
						updateNotificationMapIfUserExists(receiver.getId(), -1);
					}

					notificationDao.deleteWithSession(getSession(), notification);
				}
			}
		});
	}
	
	public void removeTaskNotifications(Task task) {
		notificationDao.deleteTaskNotifications(task);
	}
	
	public void removeCommunityNotifications(Community community) {
		notificationDao.deleteCommunityNotifications(community);
	}

	private void updateNotificationMapIfUserExists(Long userId, int increment) {
		synchronized (lockNotificationCount) {
			if (unreadNotifications.containsKey(userId)) {
				Integer count = unreadNotifications.get(userId);
				unreadNotifications.put(userId, count + increment);
			}
		}
	}
}
