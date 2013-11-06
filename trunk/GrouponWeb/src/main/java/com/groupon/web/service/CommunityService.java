package com.groupon.web.service;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.groupon.web.dao.CommunityDao;
import com.groupon.web.dao.model.Community;
import com.groupon.web.dao.model.User;

@Component
public class CommunityService {
	@Autowired
	private CommunityDao communityDao;

	public void createCommunity(Community community) {
		community.setCreateDate(new Date());
		communityDao.saveCommunity(community);
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
		community.getMembers().add(user);
		communityDao.update(community);
	}
}
