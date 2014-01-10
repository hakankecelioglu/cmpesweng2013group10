package com.groupon.web.dao;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import com.groupon.web.dao.model.SortBy;
import com.groupon.web.dao.model.Task;
import com.groupon.web.dao.model.TaskRate;
import com.groupon.web.util.GrouponConstants;

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

	@SuppressWarnings("unchecked")
	public List<Task> findAll(int page, int max, SortBy sortBy) {
		Query query;
		if (sortBy == SortBy.DEADLINE) {
			query = this.getSession().createQuery("from Task t where t.deadline > NOW() order by t.deadline ASC");
		} else {
			query = this.getSession().createQuery("from Task t where t.deadline > NOW() order by t.createDate DESC");
		}

		if (page >= 0 && max > 0) {
			query.setFirstResult(page * max);
			query.setMaxResults(max);
		}

		List<Task> tasks = query.list();
		return tasks;
	}

	@SuppressWarnings("unchecked")
	public List<Task> getTasks(long communityId, int page, int numberOfTasksPerPage) {
		Query query = this.getSession().createQuery("SELECT t from Task t where t.community.id=:communityId and t.deadline > NOW() order by t.deadline ASC");
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

	@SuppressWarnings("unchecked")
	public List<Task> getFollowedTasks(long userId) {
		/**
		 * TODO add pagination and sorting
		 */
		Query query = this.getSession().createQuery("SELECT t FROM Task t join fetch t.followers tf where tf.id = :uid");
		query.setParameter("uid", userId);
		return (List<Task>) query.list();
	}

	@SuppressWarnings("unchecked")
	public List<Task> getCommunityTasks(long userId) {
		/**
		 * TODO add pagination and sorting
		 */
		Query query = this.getSession().createQuery("select t from Task t, Community c where c.id = t.community.id and :uid MEMBER OF c.members");
		query.setParameter("uid", userId);
		return (List<Task>) query.list();
	}

	@SuppressWarnings("unchecked")
	public List<Task> getHomeFeedTasks(long userId, SortBy sortBy) {
		Query query;
		if (sortBy == SortBy.LATEST) {
			query = this.getSession().getNamedQuery("homeFeedLatestSorted");
		} else {
			query = this.getSession().getNamedQuery("homeFeedDeadlineSorted");
		}

		query.setParameter("uid", userId);
		return (List<Task>) query.list();
	}

	@SuppressWarnings("unchecked")
	public List<Long> findFollowedTaskIdsByUserId(Long userId, List<Long> inIds) {
		Query query = getSession().createSQLQuery("select task_follower.task_id from task_follower where task_id in (:ids) and user_id = :userid");
		query.setParameterList("ids", inIds);
		query.setParameter("userid", userId);
		return (List<Long>) query.list();
	}

	@SuppressWarnings("unchecked")
	public List<Object[]> findHelpAmount(List<Long> taskIds) {
		Query query = getSession()
				.createQuery(
						"select t.id, sum(ra.value) from Task t, ReplyAttribute ra where ra.taskReply.task.id in (:taskids) and ra.name = :qName and ra.taskReply.task.id = t.id group by t.id");
		query.setParameterList("taskids", taskIds);
		query.setParameter("qName", GrouponConstants.ATTR_NAME_REPLY_QUANTITY);
		Object list = query.list();
		return (List<Object[]>) list;
	}

	@SuppressWarnings("unchecked")
	public List<Task> searchTasks(String queryText) {
		Query query = getSession().createQuery("from Task t where t.title like :queryText or t.description like :queryText");
		String likeText = "%" + queryText + "%";
		query.setParameter("queryText", likeText);
		return query.list();
	}

	public TaskRate findTaskRateByTaskAndUser(Long taskId, Long userId) {
		Query query = getSession().createQuery("from TaskRate t where t.task.id = :taskid and t.user.id = :userid");
		query.setParameter("taskid", taskId);
		query.setParameter("userid", userId);
		return (TaskRate) query.uniqueResult();
	}

	public void incrementTaskVotes(Long taskId, int increment) {
		Query query = getSession().createQuery("update Task set votes = votes + :increment where id = :taskid");
		query.setParameter("increment", increment);
		query.setParameter("taskid", taskId);
		query.executeUpdate();
	}
}
