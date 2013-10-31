package com.groupon.web.dao;

import java.util.List;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import com.groupon.web.dao.model.Community;

@Repository
public class CommunityDao extends BaseDaoImpl {

	public void saveCommunity(Community community) {
		this.save(community);
	}

	public Community getCommunityById(Long id) {
		Query query = this.getSession().createQuery("from Community where id = :id");
		query.setParameter("id", id);
		return (Community) query.uniqueResult();
	}

	public List<Community> findAll() {
		@SuppressWarnings("unchecked")
		List<Community> communities = this.getSession().createQuery("from Community").list();
		return communities;
	}
}
