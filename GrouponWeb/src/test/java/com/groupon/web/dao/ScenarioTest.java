package com.groupon.web.dao;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import junit.framework.Assert;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.groupon.web.AbstractGrouponTest;
import com.groupon.web.controller.GrouponException;
import com.groupon.web.dao.model.Community;
import com.groupon.web.dao.model.NeedType;
import com.groupon.web.dao.model.RoleName;
import com.groupon.web.dao.model.Tag;
import com.groupon.web.dao.model.Task;
import com.groupon.web.dao.model.TaskStatus;
import com.groupon.web.dao.model.User;
import com.groupon.web.service.CommunityService;
import com.groupon.web.service.TagService;
import com.groupon.web.service.TaskService;
import com.groupon.web.service.UserService;

public class ScenarioTest extends AbstractGrouponTest {
	@Autowired
	private TaskService taskService;

	@Autowired
	private TagService tagService;

	@Autowired
	private CommunityService communityService;

	@Autowired
	private UserService userService;

	@Test
	public void createFullTaskScenario() throws GrouponException {
		User user = createUser();
		Community community = createCommunity(user);
		Task task = createTask(community, user);
		Assert.assertNotNull(task.getId());
	}

	private Task createTask(Community community, User owner) {
		Task task = new Task();
		task.setCommunity(community);
		task.setDeadline(new Date(System.currentTimeMillis() + 10 * 24 * 60 * 60 * 1000));
		task.setDescription("Unit Test Task");
		task.setFollowerCount(0L);
		task.setLocation("Van");
		task.setNeedType(NeedType.GOODS);
		task.setRequirementName("Çadır");
		task.setRequirementQuantity(124);
		task.setStatus(TaskStatus.OPEN);
		task.setTitle("Unit Test Task");

		List<Tag> tags = new ArrayList<Tag>();
		Tag tag = new Tag();
		tag.setName("Unit Test Tag1");
		tags.add(tag);

		tag = new Tag();
		tag.setName("Unit Test Tag2");
		tags.add(tag);

		tag = new Tag();
		tag.setName("Unit Test Tag3");
		tags.add(tag);

		task.setTags(tags);

		taskService.createTask(task, owner);

		return task;
	}

	private Community createCommunity(User owner) {
		Community community = new Community();

		community.setName("Unit Test Community");
		community.setDescription("Created for Unit Testing");
		community.setOwner(owner);

		communityService.createCommunity(community);

		return community;
	}

	private User createUser() throws GrouponException {
		User user = new User();
		user.setUsername("testUser_" + System.currentTimeMillis());
		user.setName("name");
		user.setSurname("surname");
		user.setPassword("123456");
		user.setEmail("test" + System.currentTimeMillis() + "@testemail.com");

		userService.registerUser(user, RoleName.USER);
		return user;
	}

}
