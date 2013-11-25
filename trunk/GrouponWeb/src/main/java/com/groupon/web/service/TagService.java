package com.groupon.web.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.groupon.web.dao.TagDao;
import com.groupon.web.dao.model.Tag;
import com.groupon.web.dao.model.TagUser;
import com.groupon.web.dao.model.User;

@Component
public class TagService {
	@Autowired
	private TagDao tagDao;

	public Tag getTagByName(String tagName) {
		return tagDao.getTagByName(tagName);
	}

	public Tag createTag(Tag tag) {
		return tagDao.saveTag(tag);
	}

	public void createTagUserRelation(Tag tag, User user, long amount) {
		TagUser tagUser = tagDao.findTagUserRelation(tag.getId(), user.getId());
		if (tagUser == null) {
			tagUser = new TagUser();
			tagUser.setTag(tag);
			tagUser.setUser(user);
			tagUser.setRelatedness(amount);
			tagDao.save(tagUser);
		} else {
			tagUser.setRelatedness(tagUser.getRelatedness() + amount);
			tagDao.update(tagUser);
		}
	}

	public void createTagUserRelations(final List<Tag> tags, final User user, final long amount) {
		for (Tag tag : tags) {
			createTagUserRelation(tag, user, amount);
		}
	}

	public List<Tag> searchTags(String query, int page, int maxResults) {
		return tagDao.searchTags(query, page, maxResults);
	}

}
