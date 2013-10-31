package com.groupon.mobile;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.groupon.mobile.model.User;

public class DummyController {
	public static List<Map<String, Object>> communities = new LinkedList<Map<String, Object>>();

	public static List<Map<String, Object>> tasks = new LinkedList<Map<String, Object>>();
	
	public static User admin = new User();
	
	public static void init() {
		admin.setUsername("admin");
		admin.setPassword("password");
	}

	public static int createCommunity(String name, String description) {
		Map<String, Object> community = new HashMap<String, Object>();
		community.put("name", name);
		community.put("description", description);
		community.put("id", communities.size());

		communities.add(community);
		return communities.size() - 1;
	}

	public static Map<String, Object> getCommunity(int id) {
		return communities.get(id);
	}
	public static Map<String, Object> getTask(int id) {
		return tasks.get(id);
	}
	@SuppressWarnings("unchecked")
	public static int createTask(Map<String, Object> task, int communityId) {
		task.put("communityId", communityId);
		task.put("id", tasks.size());

		Map<String, Object> community = communities.get(communityId);
		List<Map<String, Object>> taskList;
		if (!community.containsKey("tasks")) {
			taskList = new ArrayList<Map<String, Object>>();
			community.put("tasks", taskList);
		} else {
			taskList = (ArrayList<Map<String, Object>>) community.get("tasks");
		}
		taskList.add(task);

		tasks.add(task);
		return tasks.size() - 1;
	}
}