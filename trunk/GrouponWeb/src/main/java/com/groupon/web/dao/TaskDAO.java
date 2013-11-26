package com.groupon.web.dao;

import java.util.List;

import org.hibernate.Criteria;
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

	@SuppressWarnings("unchecked")
	public List<Task> getRecommendedTasks(long userId) {
		String sql = "select task.*, community.name as cname, tag_user.rel as tagscore " //
				+ "from task, community, task_tags, tag_user, tag " //
				+ "where task.id = task_tags.task_id " //
				+ "and task_tags.tag_id = tag.id " //
				+ "and tag_user.tag_id = tag.id " //
				+ "and community.id = task.community_id " //
				+ "and tag_user.user_id = :tagUserUserId " //
				+ "and (task.deadline IS NULL or task.deadline > NOW()) " //
				+ "and task.owner_id != :taskOwnerId " //
				+ "and not exists ( " //
				+ "select * from task_follower where task_follower.task_id = task.id and task_follower.user_id = :taskFollowerUserId " //
				+ ") " //
				+ "group by community.id " //
				+ "order by tagscore DESC " //
				+ "limit 0, 10;";
		Query query = this.getSession().createSQLQuery(sql).addEntity("task", Task.class).setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		query.setParameter("tagUserUserId", userId);
		query.setParameter("taskOwnerId", userId);
		query.setParameter("taskFollowerUserId", userId);
		return (List<Task>) query.list();
	}
}
