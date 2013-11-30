package com.groupon.web.service;

import java.util.List;

import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.task.TaskExecutor;
import org.springframework.stereotype.Component;

import com.groupon.web.dao.TagDao;
import com.groupon.web.dao.model.Community;
import com.groupon.web.dao.model.Tag;
import com.groupon.web.dao.model.TagUser;
import com.groupon.web.dao.model.Task;
import com.groupon.web.dao.model.User;
import com.groupon.web.util.AsyncWebTask;
import com.groupon.web.util.GrouponLogger;

@Component
public class TagService {
	private GrouponLogger logger = GrouponLogger.getLogger(getClass());

	@Autowired
	private TagDao tagDao;

	@Autowired
	@Qualifier("tagRelationExecutor")
	private TaskExecutor tagRelationExecutor;

	public Tag getTagByName(String tagName) {
		return tagDao.getTagByName(tagName);
	}

	public Tag createTag(Tag tag) {
		return tagDao.saveTag(tag);
	}

	public void createTagUserRelation(final Long tagId, final Long userId, final long amount) {
		tagRelationExecutor.execute(new AsyncWebTask(tagDao.getSessionFactory()) {
			public void runInBackground() {
				Session session = getSession();
				Tag tag = (Tag) session.get(Tag.class, tagId);
				User user = (User) session.get(User.class, userId);
				createTagUserRelation(session, tag, user, amount);
			}
		});
	}

	public void createTagUserRelationsOfCommunity(final Long communityId, final Long userId, final long amount) {
		tagRelationExecutor.execute(new AsyncWebTask(tagDao.getSessionFactory()) {
			public void runInBackground() {
				Session session = getSession();
				Community community = (Community) session.get(Community.class, communityId);
				User user = (User) session.get(User.class, userId);
				for (Tag tag : community.getTags()) {
					createTagUserRelation(session, tag, user, amount);
				}
			}
		});
	}

	public void createTagUserRelationsOfTask(final Long taskId, final Long userId, final long amount) {
		tagRelationExecutor.execute(new AsyncWebTask(tagDao.getSessionFactory()) {
			public void runInBackground() {
				Session session = getSession();
				Task task = (Task) session.get(Task.class, taskId);
				User user = (User) session.get(User.class, userId);
				for (Tag tag : task.getTags()) {
					createTagUserRelation(session, tag, user, amount);
				}
			}
		});
	}

	public List<Tag> searchTags(String query, int page, int maxResults) {
		return tagDao.searchTags(query, page, maxResults);
	}

	private void createTagUserRelation(Session session, Tag tag, User user, long amount) {
		TagUser tagUser = tagDao.findTagUserRelation(session, tag.getId(), user.getId());
		if (tagUser == null) {
			tagUser = new TagUser();
			tagUser.setTag(tag);
			tagUser.setUser(user);
			tagUser.setRelatedness(amount);
			tagDao.saveWithSession(session, tagUser);
		} else {
			tagUser.setRelatedness(tagUser.getRelatedness() + amount);
			tagDao.updateWithSession(session, tagUser);
		}
	}

}
