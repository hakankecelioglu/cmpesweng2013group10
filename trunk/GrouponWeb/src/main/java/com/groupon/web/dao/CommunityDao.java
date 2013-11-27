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

	@SuppressWarnings("unchecked")
	public List<Community> getCommunitiesByFollowerId(Long userId, int page, int max) {
		Query query = getSession().createQuery("select c from Community c join fetch c.members m where m.id = :userId");
		query.setParameter("userId", userId);
		if (page >= 0 && max > 0) {
			query.setFirstResult(page * max);
			query.setMaxResults(max);
		}
		return query.list();
	}

}
