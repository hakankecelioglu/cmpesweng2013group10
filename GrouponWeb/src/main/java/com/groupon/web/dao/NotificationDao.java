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
		Query query = session.createQuery("update Notification n set n.isRead = true where n.id = :nid");
		query.setParameter("nid", notificationId);
		return query.executeUpdate();
	}

	public Integer markNotificationsAsReadByTask(Session session, Long userId, Long taskId) {
		Query query = session.createQuery("update Notification n set n.isRead = true where n.receiver.id = :rid and n.task.id = :tid");
		query.setParameter("rid", userId);
		query.setParameter("tid", taskId);
		return query.executeUpdate();
	}
	
	public Integer markNotificationsAsReadByTaskAndNotificationType(Session session, Long userId, Long taskId, NotificationType notificationType) {
		Query query = session.createQuery("update Notification n set n.isRead = true where n.receiver.id = :rid and n.task.id = tid and n.type = :nt");
		query.setParameter("rid", userId);
		query.setParameter("tid", taskId);
		query.setParameter("nt", notificationType);
		return query.executeUpdate();
	}
}
