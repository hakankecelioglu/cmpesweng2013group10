package com.groupon.web.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.groupon.web.dao.CommunityDao;
import com.groupon.web.dao.model.Community;
import com.groupon.web.dao.model.Tag;
import com.groupon.web.dao.model.User;
import com.groupon.web.util.ControllerConstants;

@Component
public class CommunityService {
	@Autowired
	private CommunityDao communityDao;

	@Autowired
	private TagService tagService;

	public void createCommunity(Community community) {
		community.setCreateDate(new Date());

		arrangeTagsOfCommunity(community);
		communityDao.saveCommunity(community);

		tagService.createTagUserRelations(community.getTags(), community.getOwner(), ControllerConstants.TAG_USER_CREATE_COMMUNITY);
		System.out.println("async!!!");
	}

	public Community getCommunityById(Long id) {
		return communityDao.getCommunityById(id);
	}

	public List<Community> getCommunitiesByFollowerId(Long userId) {
		return communityDao.getCommunitiesByFollowerId(userId);
	}

	public List<Community> getAllCommunities() {
		return communityDao.findAll();
	}

	public void addMemberToCommunity(Community community, User user) {
		if (community.getMembers().contains(user)) {
			return;
		}
		community.getMembers().add(user);
		communityDao.update(community);

		if (!community.getOwner().equals(user)) {
			tagService.createTagUserRelations(community.getTags(), user, ControllerConstants.TAG_USER_JOIN_COMMUNITY);
		}
	}

	public void removeMemberFromCommunity(Community community, User user) {
		if (!community.getMembers().contains(user)) {
			return;
		}
		community.getMembers().remove(user);
		communityDao.update(community);
	}

	private void arrangeTagsOfCommunity(Community community) {
		List<Tag> tags = community.getTags();
		List<Tag> tags2 = new ArrayList<Tag>(tags.size());
		for (Tag tag : tags) {
			Tag tagInDb = tagService.getTagByName(tag.getName());
			if (tagInDb == null) {
				tagInDb = tagService.createTag(tag);
			}
			tags2.add(tagInDb);
		}
		community.setTags(tags2);
	}
}
