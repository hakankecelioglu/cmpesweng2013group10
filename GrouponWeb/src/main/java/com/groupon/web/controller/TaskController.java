package com.groupon.web.controller;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.groupon.web.controller.json.TaskJson;
import com.groupon.web.dao.model.Community;
import com.groupon.web.dao.model.NeedType;
import com.groupon.web.dao.model.Tag;
import com.groupon.web.dao.model.Task;
import com.groupon.web.dao.model.User;
import com.groupon.web.service.CommunityService;
import com.groupon.web.service.NotificationService;
import com.groupon.web.service.TaskService;

@Controller
@RequestMapping("/task")
public class TaskController extends AbstractBaseController {
	@Autowired
	private TaskService taskService;

	@Autowired
	private CommunityService communityService;

	@Autowired
	private NotificationService notificationService;

	@RequestMapping(value = "/show/{id}")
	public Object taskPage(HttpServletRequest request, Model model, @PathVariable Long id) {
		if (id == null) {
			return "redirect:/";
		}
		setGlobalAttributesToModel(model, request);
		Task task = taskService.getTaskById(id);

		if (task == null) {
			return "redirect:/";
		}

		User user = getUser();
		if (user != null) {
			notificationService.markTaskNotificationsRead(user.getId(), id);
		}
		
		boolean isAFollower = task.getFollowers().contains(user);

		model.addAttribute("task", task);
		model.addAttribute("isFollower", isAFollower);
		return "task.view";

	}

	@RequestMapping(value = "/create", method = RequestMethod.GET)
	public Object createTask(HttpServletRequest request, Model model) {
		User user = getUser();
		if (user == null) {
			return "redirect:/login";
		}

		Long communityId = getLongParameter(request, "communityId");
		Community community = communityService.getCommunityById(communityId);

		if (community == null) {
			return "redirect:/?error=1001";
		}

		model.addAttribute("community", community);
		model.addAttribute("page", "createTask");

		setGlobalAttributesToModel(model, request);
		return "createTask.view";
	}

	@RequestMapping(value = "/create", method = RequestMethod.POST)
	public ResponseEntity<Map<String, Object>> createTask(HttpServletRequest request, @RequestBody String body) {
		Map<String, Object> response = new HashMap<String, Object>();
		User user = getUser();

		try {
			Task task = generateTaskFromJSON(body);
			taskService.createTask(task, user);
			response.put("taskId", task.getId());
			return prepareSuccessResponse(response);
		} catch (JSONException e) {
			e.printStackTrace();
			response.put("error", e.getMessage());
		} catch (ParseException e) {
			e.printStackTrace();
			response.put("error", e.getMessage());
		}

		return prepareErrorResponse(response);
	}

	@RequestMapping(value = "/suggest", method = RequestMethod.GET)
	public ResponseEntity<Map<String, Object>> getSuggestedTasks(@RequestParam(required = false) Integer page, @RequestParam(required = false) Integer max) {
		Map<String, Object> response = new HashMap<String, Object>();

		User user = getUser();
		List<Task> suggestedTasks = taskService.getRecommendedTasks(user);

		response.put("tasks", TaskJson.convert(suggestedTasks));
		return prepareSuccessResponse(response);
	}

	@RequestMapping(value = "/followTask", method = RequestMethod.POST)
	public ResponseEntity<Map<String, Object>> followTask(HttpServletRequest request, @RequestParam Long taskId) {
		Map<String, Object> response = new HashMap<String, Object>();

		User user = getUser();
		Long followerCount = taskService.followTask(taskId, user);

		response.put("followerCount", followerCount);

		return prepareSuccessResponse(response);
	}

	@RequestMapping(value = "/unfollowTask", method = RequestMethod.POST)
	public ResponseEntity<Map<String, Object>> unfollowTask(HttpServletRequest request, @RequestParam Long taskId) {
		Map<String, Object> response = new HashMap<String, Object>();

		User user = getUser();
		Long followerCount = taskService.unfollowTask(taskId, user);

		response.put("followerCount", followerCount);

		return prepareSuccessResponse(response);
	}
	
	private Task generateTaskFromJSON(String body) throws JSONException, ParseException {
		JSONObject json = new JSONObject(body);
		String name = json.getString("name");
		String description = json.getString("description");
		String deadline = json.getString("deadline");

		Task task = new Task();
		task.setTitle(name);
		task.setDescription(description);

		SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
		Date deadlineDate = formatter.parse(deadline);
		task.setDeadline(deadlineDate);

		if (json.has("location")) {
			JSONObject location = json.getJSONObject("location");
			Float latitude = (float) location.getDouble("latitude");
			Float longitude = (float) location.getDouble("longitude");
			String locationText = location.getString("text");

			task.setLocation(locationText);
			task.setLatitude(latitude);
			task.setLongitude(longitude);
		}

		String type = json.getString("type");
		NeedType needType = NeedType.valueOf(type);
		task.setNeedType(needType);
		if (needType == NeedType.GOODS) {
			String requirementName = json.getString("requirementName");
			int requirementQuantity = json.getInt("requirementQuantity");
			task.setRequirementName(requirementName);
			task.setRequirementQuantity(requirementQuantity);
		}

		else if (needType == NeedType.SERVICE) {
			String requirementName = json.getString("requirementName");
			task.setRequirementName(requirementName);
		}

		JSONArray tags = json.getJSONArray("tags");
		List<Tag> tagList = new ArrayList<Tag>();
		for (int i = 0; i < tags.length(); i++) {
			String tagName = tags.getString(i);
			if (StringUtils.isNotBlank(tagName)) {
				Tag tag = new Tag();
				tag.setName(tagName);
				tagList.add(tag);
			}
		}
		task.setTags(tagList);

		Long communityId = json.getLong("communityId");
		Community community = communityService.getCommunityById(communityId);
		task.setCommunity(community);

		return task;
	}
}
