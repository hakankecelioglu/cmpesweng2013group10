package com.groupon.web.dao;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.groupon.web.dao.model.Task;

@Repository
public class TaskDAO {

	@Autowired
	private SessionFactory sessionFactory;

	@Transactional
	public List<Task> findAll() {
		Session session = sessionFactory.getCurrentSession();
		@SuppressWarnings("unchecked")
		List<Task> tasks = session.createQuery("from Task").list();
		return tasks;
	}
}
