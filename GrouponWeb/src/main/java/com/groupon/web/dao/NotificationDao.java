package com.groupon.web.dao;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;

import com.groupon.web.dao.model.Notification;
import com.groupon.web.dao.model.NotificationType;

@Repository
public class NotificationDao extends BaseDaoImpl {
	public void saveNotification(Session session, Notification notification) {
		saveWithSession(session, notification);
	}

	public void updateNotification(Session session, Notification notification) {
		updateWithSession(session, notification);
	}

	public Integer countUnreadNotifications(Long userId) {
		Query query = getSession().createQuery("select count(*) from Notification n where n.receiver.id = :receiverId and n.isRead = false");
		query.setParameter("receiverId", userId);
		return ((Number) query.uniqueResult()).intValue();
	}

	@SuppressWarnings("unchecked")
	public List<Notification> findUnreadNotifications(Long userId) {
		Query query = getSession().createQuery("from Notification n where n.receiver.id = :receiverId and n.isRead = false");
		query.setParameter("receiverId", userId);
		return (List<Notification>) query.list();
	}

	@SuppressWarnings("unchecked")
	public List<Notification> findNotificationsOfUser(Long userId) {
		Query query = getSession().createQuery("from Notification n where n.receiver.id = :receiverId order by n.createDate DESC");
		query.setParameter("receiverId", userId);
		return (List<Notification>) query.list();
	}

	public Integer markNotificationAsRead(Session session, Long notificationId) {
		Query query = session.createQuery("update Notification n set n.isRead = true where n.id = :nid and n.isRead = false");
		query.setParameter("nid", notificationId);
		return query.executeUpdate();
	}

	public Integer markNotificationsAsReadByTask(Session session, Long userId, Long taskId) {
		Query query = session.createQuery("update Notification n set n.isRead = true where n.receiver.id = :rid and n.task.id = :tid and n.isRead = false");
		query.setParameter("rid", userId);
		query.setParameter("tid", taskId);
		return query.executeUpdate();
	}

	public Integer markNotificationsAsReadByTaskAndNotificationType(Session session, Long userId, Long taskId, NotificationType notificationType) {
		Query query = session.createQuery("update Notification n set n.isRead = true where n.receiver.id = :rid and n.task.id = tid and n.type = :nt and n.isRead = false");
		query.setParameter("rid", userId);
		query.setParameter("tid", taskId);
		query.setParameter("nt", notificationType);
		return query.executeUpdate();
	}

	public Notification findNotification(Session session, Long receiver, Long source, Long task, NotificationType notificationType) {
		Query query = session.createQuery("from Notification n where n.receiver.id = :recid and n.task.id = :taskid and n.type = :type and n.source.id = :srcid");
		query.setParameter("recid", receiver);
		query.setParameter("taskid", task);
		query.setParameter("type", notificationType);
		query.setParameter("srcid", source);
		query.setMaxResults(1);
		return (Notification) query.uniqueResult();
	}
	
	public Notification findTaskVoteNotification(Session session, Long receiver, Long source, Long task) {
		Query query = session.createQuery("from Notification n where n.receiver.id = :recid and n.task.id = :taskid and (n.type = :type1 or n.type = :type2) and n.source.id = :srcid");
		query.setParameter("recid", receiver);
		query.setParameter("taskid", task);
		query.setParameter("srcid", source);
		query.setParameter("type1", NotificationType.TASK_UPVOTE);
		query.setParameter("type2", NotificationType.TASK_DOWNVOTE);
		query.setMaxResults(1);
		return (Notification) query.uniqueResult();
	}
}
