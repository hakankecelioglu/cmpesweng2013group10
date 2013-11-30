package com.groupon.web.util;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

public abstract class AsyncWebTask implements Runnable {
	private Session session;
	private SessionFactory factory;
	
	public AsyncWebTask(SessionFactory sessionFactory) {
		factory = sessionFactory;
	}
	
	public void run() {
		session = factory.openSession();
		Transaction tx = null;
		
		try {
			tx = session.beginTransaction();
			runInBackground();
			tx.commit();
		} catch (HibernateException e) {
			if (tx != null) {
				tx.rollback();
			}
		} finally {
			session.close();
		}
	}
	
	public abstract void runInBackground();
	
	protected Session getSession() {
		return session;
	}
}
