package com.groupon.web.dao;

import com.groupon.web.dao.model.TaskType;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;

@Repository
public class TaskTypeDao extends BaseDaoImpl{
	public TaskType getTaskTypeById(Long id) {
		Query query = this.getSession().createQuery("from TaskType where id = :id");
		query.setParameter("id", id);
		return (TaskType) query.uniqueResult();
	}
}
