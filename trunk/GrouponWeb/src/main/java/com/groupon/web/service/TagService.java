package com.groupon.web.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.groupon.web.dao.TagDao;
import com.groupon.web.dao.model.Tag;

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

}
