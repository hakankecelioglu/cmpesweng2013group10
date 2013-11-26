package com.groupon.web.dao;

import java.util.List;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import com.groupon.web.dao.model.Tag;
import com.groupon.web.dao.model.TagUser;

@Repository
public class TagDao extends BaseDaoImpl {

	public Tag getTagByName(String tagName) {
		Query query = this.getSession().createQuery("from Tag where name = :name");
		query.setParameter("name", tagName);
		return (Tag) query.uniqueResult();
	}

	public Tag saveTag(Tag tag) {
		this.save(tag);
		return tag;
	}

	public TagUser findTagUserRelation(long tagId, long userId) {
		Query query = this.getSession().createQuery("from TagUser tu where tu.user.id = :userId and tu.tag.id = :tagId");
		query.setParameter("userId", userId);
		query.setParameter("tagId", tagId);
		return (TagUser) query.uniqueResult();
	}

	@SuppressWarnings("unchecked")
	public List<Tag> searchTags(String queryStr, int page, int maxResults) {
		Query query = this.getSession().createQuery("from Tag tag where tag.name like :queryStr");
		query.setParameter("queryStr", "%" + queryStr + "%");
		if (page >= 0 && maxResults > 0) {
			query.setMaxResults(maxResults);
			query.setFirstResult(page * maxResults);
		}
		return (List<Tag>) query.list();
	}

}
