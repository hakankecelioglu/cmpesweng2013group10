package com.groupon.web.dao;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import com.groupon.web.dao.model.Tag;

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
}
