package com.groupon.web.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.groupon.web.dao.TaskDAO;
import com.groupon.web.dao.model.RateDirection;
import com.groupon.web.dao.model.SortBy;
import com.groupon.web.dao.model.Tag;
import com.groupon.web.dao.model.Task;
import com.groupon.web.dao.model.TaskRate;
import com.groupon.web.dao.model.TaskReply;
import com.groupon.web.dao.model.TaskStatus;
import com.groupon.web.dao.model.User;
import com.groupon.web.exception.GrouponException;
import com.groupon.web.util.GrouponConstants;

@Component
public class TaskService {

	@Autowired
	private TaskDAO taskDao;

	@Autowired
	private TagService tagService;

	@Autowired
	private NotificationService notificationService;

	@Autowired
	private UserService userService;
	/**
	 * get a task with specified id
	 * @param id
	 * @return
	 */
	public Task getTaskById(Long id) {
		return taskDao.getTaskById(id);
	}
	/**
	 * get followed tasks of user
	 * @param user target user
	 * @return
	 */
	public List<Task> getFollowedTasks(User user) {
		return taskDao.getFollowedTasks(user.getId());
	}
	/**
	 * get tasks of a community
	 * @param user target user
	 * @return
	 */
	public List<Task> getCommunityTasks(User user) {
		return taskDao.getCommunityTasks(user.getId());
	}
	/**
	 * get home feed of tasks
	 * @param user
	 * @param sortBy
	 * @return
	 */
	public List<Task> getHomeFeedTasks(User user, SortBy sortBy) {
		return taskDao.getHomeFeedTasks(user.getId(), sortBy);
	}
	/**
	 * return all tasks
	 * @param page
	 * @param max
	 * @param sortBy
	 * @return
	 */
	public List<Task> getAllTasks(int page, int max, SortBy sortBy) {
		return taskDao.findAll(page, max, sortBy);
	}
	/**
	 * count all open tasks
	 * @return
	 */
	public long countOpenTasks() {
		return taskDao.countOpenTasks();
	}
	/**
	 * creates a task with specified data
	 * @param task
	 * @param owner
	 * @return
	 */
	public Task createTask(Task task, User owner) {
		task.setCreateDate(new Date());
		task.setOwner(owner);
		task.setStatus(TaskStatus.OPEN);

		arrangeTagsOfTask(task);
		Task taskCreated = taskDao.saveTask(task);

		followTask(taskCreated.getId(), owner);

		tagService.createTagUserRelationsOfTask(task.getId(), owner.getId(), GrouponConstants.TAG_USER_CREATE_TASK);
		notificationService.sendTaskCreatedInFollowedCommunityNotification(task.getCommunity().getId(), taskCreated.getId());

		return taskCreated;
	}
	/**
	 * get tasks of a community
	 * @param communityId id of target comumnity
	 * @param page
	 * @param numberOfTasksPerPage
	 * @return
	 */
	public List<Task> getTasks(long communityId, int page, int numberOfTasksPerPage) {
		return taskDao.getTasks(communityId, page, numberOfTasksPerPage);
	}
	/**
	 * Makes necessary dao calls for following task operation
	 * @param taskId id of task followed
	 * @param user follower
	 * @return
	 */
	public synchronized Long followTask(Long taskId, User user) {
		Task task = taskDao.getTaskById(taskId);
		if (task.getFollowers().contains(user)) {
			throw new GrouponException("You are already a follower of this task!");
		}

		task.getFollowers().add(user);
		task.setFollowerCount(task.getFollowerCount() + 1);
		taskDao.updateTask(task);
		if (!task.getOwner().equals(user)) {
			tagService.createTagUserRelationsOfTask(task.getId(), user.getId(), GrouponConstants.TAG_USER_FOLLOW_TASK);
		}
		return task.getFollowerCount();
	}
	/**
	 * Makes necessary dao calls for following task operation
	 * @param taskId id of task unfollowed
	 * @param user unfollower
	 * @return
	 */
	public synchronized Long unfollowTask(Long taskId, User user) {
		Task task = taskDao.getTaskById(taskId);
		if (!task.getFollowers().contains(user)) {
			throw new GrouponException("You are not a follower of that task!");
		}

		if (task.getOwner().equals(user)) {
			throw new GrouponException("You cannot unfollow a task that you created!");
		}

		task.getFollowers().remove(user);
		task.setFollowerCount(task.getFollowerCount() - 1);
		taskDao.updateTask(task);
		return task.getFollowerCount();
	}
	/**
	 * find followed task ids of a user
	 * @param user target user
	 * @param inIds
	 * @return
	 */
	public Map<Long, Boolean> findFollowedTasksIdsByUser(User user, List<Long> inIds) {
		List<Long> followedIds;
		if (inIds != null && inIds.size() > 0) {
			followedIds = taskDao.findFollowedTaskIdsByUserId(user.getId(), inIds);
		} else {
			followedIds = new ArrayList<Long>(0);
		}

		Map<Long, Boolean> response = new HashMap<Long, Boolean>();
		for (Long id : inIds) {
			boolean contains = false;
			for (Number fid : followedIds) {
				if (fid.longValue() == id.longValue()) {
					contains = true;
					break;
				}
			}
			response.put(id, contains);
		}
		return response;
	}
	/**
	 * Return tasks recommended to user
	 * @param user target user
	 * @return
	 */
	public List<Task> getRecommendedTasks(User user) {
		return taskDao.getRecommendedTasks(user.getId());
	}
	/**
	 * Save a task reply 
	 * @param taskReply TaskReply instance representing task reply data
	 */
	public void saveTaskReply(TaskReply taskReply) {
		taskDao.save(taskReply);
		notificationService.sendTaskRepliedNotification(taskReply.getTask().getId(), taskReply.getReplier().getId());
	}
	/**
	 * return a map <count,task id> where count is sum of replied requirement quantities
	 * @param taskIds
	 * @return
	 */
	public Map<Long, Integer> getTaskHelpCounts(List<Long> taskIds) {
		List<Object[]> taskHelpCounts = taskDao.findHelpAmount(taskIds);
		Map<Long, Integer> helpMap = new HashMap<Long, Integer>();
		for (Object[] result : taskHelpCounts) {
			helpMap.put((Long) result[0], Integer.parseInt((String) result[1]));
		}
		return helpMap;
	}
	/**
	 * search tasks for query text
	 * @param queryText 
	 * @return
	 */
	public List<Task> searchTasks(String queryText) {
		return taskDao.searchTasks(queryText);
	}
	/**
	 * makes necessary dao calls for voting a task
	 * @param user voter user
	 * @param task task voted
	 * @param direction
	 * @return
	 */
	public TaskRate voteTask(User user, Task task, RateDirection direction) {
		TaskRate taskRate = taskDao.findTaskRateByTaskAndUser(task.getId(), user.getId());

		if (task.getOwner().equals(user)) {
			throw new GrouponException("You can't vote your own task!");
		}

		if (taskRate == null) {
			// New rate
			taskRate = new TaskRate();
			taskRate.setTask(task);
			taskRate.setUser(user);

			taskDao.incrementTaskVotes(task.getId(), direction.getIncrementValue());

			if (direction == RateDirection.UP) {
				userService.incrementUserReputation(task.getOwner().getId(), GrouponConstants.REPUT_BY_TASK_UP);
			} else {
				userService.incrementUserReputation(task.getOwner().getId(), GrouponConstants.REPUT_BY_TASK_DOWN);
			}

			notificationService.sendTaskVoteNotification(task.getId(), user.getId(), direction);
		} else if (taskRate.getDirection() == direction) {
			throw new GrouponException("Already voted!");
		} else {
			// Old rate is changing
			int incrementValue = direction.getIncrementValue() * 2;
			taskDao.incrementTaskVotes(task.getId(), incrementValue);

			if (direction == RateDirection.UP) {
				userService.incrementUserReputation(task.getOwner().getId(), GrouponConstants.REPUT_BY_TASK_UP - GrouponConstants.REPUT_BY_TASK_DOWN);
			} else {
				userService.incrementUserReputation(task.getOwner().getId(), GrouponConstants.REPUT_BY_TASK_DOWN - GrouponConstants.REPUT_BY_TASK_UP);
			}

			notificationService.updateTaskVoteNotification(task.getId(), user.getId(), direction);
		}

		taskRate.setDirection(direction);
		taskDao.saveOrUpdate(taskRate);

		return taskRate;
	}
	/**
	 * makes necessary dao calls for unvoting a task
	 * @param user unvoter user
	 * @param task task voted
	 * @param direction
	 * @return
	 */
	public void unvoteTask(Task task, Long userId) {
		TaskRate taskRate = taskDao.findTaskRateByTaskAndUser(task.getId(), userId);

		if (taskRate != null) {
			RateDirection direction = taskRate.getDirection();
			taskDao.incrementTaskVotes(task.getId(), -1 * direction.getIncrementValue());

			User taskOwner = task.getOwner();

			if (direction == RateDirection.UP) {
				userService.incrementUserReputation(taskOwner.getId(), -GrouponConstants.REPUT_BY_TASK_UP);
			} else {
				userService.incrementUserReputation(taskOwner.getId(), -GrouponConstants.REPUT_BY_TASK_DOWN);
			}

			notificationService.deleteTaskVoteNotification(task.getId(), taskOwner.getId(), userId);
			taskDao.delete(taskRate);
		}
	}
	/**
	 * return rate which user give to a task
	 * @param taskId target task
	 * @param userId target user
	 * @return
	 */
	public TaskRate findTaskRate(Long taskId, Long userId) {
		return taskDao.findTaskRateByTaskAndUser(taskId, userId);
	}
	/**
	 * remove a task
	 * @param task target task
	 */
	public void removeTask(Task task) {
		notificationService.removeTaskNotifications(task);
		task.getTags().clear();
		task.getFollowers().clear();
		task.getAttributes().clear();
		task.getTaskReplies().clear();

		taskDao.update(task);
		taskDao.delete(task);
	}

	private void arrangeTagsOfTask(Task task) {
		List<Tag> tags = task.getTags();
		List<Tag> tags2 = new ArrayList<Tag>(tags.size());
		for (Tag tag : tags) {
			Tag tagInDb = tagService.getTagByName(tag.getName());
			if (tagInDb == null) {
				tagInDb = tagService.createTag(tag);
			}
			tags2.add(tagInDb);
		}
		task.setTags(tags2);
	}

}
