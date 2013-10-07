package com.groupon.web.dao;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.groupon.web.dao.model.PCTask;

@Repository
public class TaskDAO {

	@Autowired
	private SessionFactory sessionFactory;

	@Transactional
	public List<PCTask> findAll() {
		Session session = sessionFactory.getCurrentSession();
		// test commit hanil
		@SuppressWarnings("unchecked")
		List<PCTask> tasks = session.createQuery("from PCTask").list();
		return tasks;
	}
}
