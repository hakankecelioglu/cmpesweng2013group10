package com.groupon.web.dao;

import java.util.List;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import com.groupon.web.dao.model.Community;
import com.groupon.web.dao.model.TaskType;

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
		Query query = getSession().createQuery("select c from Community c join fetch c.members m where m.id = :userId order by c.updateDate DESC");
		query.setParameter("userId", userId);
		if (page >= 0 && max > 0) {
			query.setFirstResult(page * max);
			query.setMaxResults(max);
		}
		return query.list();
	}

	@SuppressWarnings("unchecked")
	public List<Community> findSimiliarCommunities(Long communityId, int page, int max) {
		Query query = getSession().createQuery("select c from Community c " //
				+ "where c.id != :communityId and exists " //
				+ "(select t from Tag t, Community c1 " //
				+ "where t MEMBER OF c1.tags and t MEMBER OF c.tags and c1.id = :communityId)");
		query.setParameter("communityId", communityId);
		if (page >= 0 && max > 0) {
			query.setFirstResult(page * max);
			query.setMaxResults(max);
		}
		return query.list();
	}

	@SuppressWarnings("unchecked")
	public List<Object[]> getTaskTypeNames(Long communityId) {
		Query query = getSession().createQuery("select tt.id, tt.name from TaskType tt where tt.community.id = :cid");
		query.setParameter("cid", communityId);
		return (List<Object[]>) query.list();
	}
	
	public TaskType getTaskType(Long taskTypeId) {
		Query query = getSession().createQuery("from TaskType t where t.id = :taskTypeId");
		query.setParameter("taskTypeId", taskTypeId);
		return (TaskType) query.uniqueResult();
	}
}
