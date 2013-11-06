package com.groupon.web.dao;

import java.util.List;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import com.groupon.web.dao.model.Task;

@Repository
public class TaskDAO extends BaseDaoImpl {

	public Task saveTask(Task task) {
		this.save(task);
		return task;
	}

	public Task updateTask(Task task) {
		this.update(task);
		return task;
	}

	public List<Task> findAll() {
		@SuppressWarnings("unchecked")
		List<Task> tasks = this.getSession().createQuery("from Task").list();
		return tasks;
	}

	@SuppressWarnings("unchecked")
	public List<Task> getTasks(long communityId, int page, int numberOfTasksPerPage) {
		Query query = this.getSession().createQuery("SELECT t from Task t where t.community.id=:communityId");
		query.setParameter("communityId", communityId);
		query.setMaxResults(numberOfTasksPerPage);
		query.setFirstResult(page * numberOfTasksPerPage);
		query.setCacheable(true);
		return (List<Task>) query.list();
	}

	public Task getTaskById(Long taskId) {
		return findById(Task.class, taskId);
	}

	public Task getTaskById2(Long id) {
		Query query = this.getSession().createQuery("from Task where id = :id");
		query.setParameter("id", id);
		return (Task) query.uniqueResult();
	}
}
