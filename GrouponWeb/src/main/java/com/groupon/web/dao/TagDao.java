package com.groupon.web.dao;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.groupon.web.dao.model.Tag;

@Repository
public class TagDao {
	@Autowired
	private SessionFactory sessionFactory;
	
	@Transactional
	public Tag getTagByName(String tagName) {
		Session session = sessionFactory.getCurrentSession();
		Query query = session.createQuery("from Tag where name = :name");
		query.setParameter("name", tagName);
		return (Tag) query.uniqueResult();
	}
	
	@Transactional
	public Tag saveTag(Tag tag) {
		Session session = sessionFactory.getCurrentSession();
		session.save(tag);
		return tag;
	}
}
