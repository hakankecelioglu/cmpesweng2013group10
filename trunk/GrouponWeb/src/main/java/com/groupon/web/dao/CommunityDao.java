package com.groupon.web.dao;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.groupon.web.dao.model.Community;

@Repository
public class CommunityDao {

	@Autowired
	private SessionFactory sessionFactory;

	@Transactional
	public void saveCommunity(Community community) {
		Session session = sessionFactory.getCurrentSession();
		session.save(community);
	}

	@Transactional
	public Community getCommunityById(Long id) {
		Session session = sessionFactory.getCurrentSession();
		Query query = session.createQuery("from Community where id = :id");
		query.setParameter("id", id);
		return (Community) query.uniqueResult();
	}
}
