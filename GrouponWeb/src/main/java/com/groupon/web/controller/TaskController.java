package com.groupon.web.controller;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
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

import com.groupon.web.controller.json.ReplyFieldJson;
import com.groupon.web.controller.json.TaskJson;
import com.groupon.web.controller.json.TaskReplyJson;
import com.groupon.web.dao.model.Community;
import com.groupon.web.dao.model.NeedType;
import com.groupon.web.dao.model.RateDirection;
import com.groupon.web.dao.model.ReplyAttribute;
import com.groupon.web.dao.model.ReplyField;
import com.groupon.web.dao.model.RoleName;
import com.groupon.web.dao.model.Tag;
import com.groupon.web.dao.model.Task;
import com.groupon.web.dao.model.TaskAttribute;
import com.groupon.web.dao.model.TaskRate;
import com.groupon.web.dao.model.TaskReply;
import com.groupon.web.dao.model.TaskType;
import com.groupon.web.dao.model.User;
import com.groupon.web.exception.GrouponException;
import com.groupon.web.service.CommunityService;
import com.groupon.web.service.NotificationService;
import com.groupon.web.service.TaskService;
import com.groupon.web.service.TaskTypeService;
import com.groupon.web.util.GrouponWebUtils;

@Controller
@RequestMapping("/task")
public class TaskController extends AbstractBaseController {
	@Autowired
	private TaskService taskService;

	@Autowired
	private CommunityService communityService;

	@Autowired
	private NotificationService notificationService;

	@Autowired
	private TaskTypeService taskTypeService;

	/**
	 * return view of a task page
	 * 
	 * @param request
	 * @param model
	 * @param id
	 * @return
	 */
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
		if (task.getNeedType() == NeedType.GOODS) {
			Map<Long, Integer> quantityCounts = taskService.getTaskHelpCounts(Arrays.asList(id));
			if (quantityCounts.size() == 1) {
				Integer count = quantityCounts.get(id);
				Integer requirementQuantity = task.getRequirementQuantity();
				Integer percent = (count * 100) / requirementQuantity;
				model.addAttribute("needPercent", percent);
				model.addAttribute("completedQuantity", count);
			}
		}

		model.addAttribute("isFollower", isAFollower);

		Map<String, Object> attributes = getTaskAttributeMapForModel(task);
		model.addAttribute("taskAttributes", attributes);

		return "task.view";

	}

	/**
	 * return task data for mobile request
	 * 
	 * @param request
	 * @param id
	 * @return
	 */
	@RequestMapping(value = "/mobileShow/{id}")
	public ResponseEntity<Map<String, Object>> taskMobile(HttpServletRequest request, @PathVariable Long id) {
		User user = getUser();
		Task task = taskService.getTaskById(id);
		boolean isAFollower = task.getFollowers().contains(user);
		Map<String, Object> response = new HashMap<String, Object>();

		TaskJson taskJson = TaskJson.convert(task);

		response.put("task", taskJson);
		response.put("isFollower", isAFollower);
		response.put("taskAttributes", getTaskAttributeMapForModel(task));

		if (task.getNeedType() == NeedType.GOODS) {
			Map<Long, Integer> quantityCounts = taskService.getTaskHelpCounts(Arrays.asList(id));
			if (quantityCounts.containsKey(id)) {
				Integer count = quantityCounts.get(id);
				taskJson.setRequirementQuantity(taskJson.getRequirementQuantity() - count);
			}
		}

		return prepareSuccessResponse(response);
	}

	/**
	 * return view upon creating tasks
	 * 
	 * @param request
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/create", method = RequestMethod.GET)
	public Object createTask(HttpServletRequest request, Model model) {
		User user = getUser();
		if (user == null) {
			return "redirect:/login";
		}

		Long taskTypeId = getLongParameter(request, "taskType");
		TaskType taskType = null;
		if (taskTypeId != null) {
			taskType = communityService.getTaskType(taskTypeId);
		}

		Long communityId = getLongParameter(request, "communityId");
		Community community = communityService.getCommunityById(communityId);

		if (community == null) {
			return "redirect:/?error=1001";
		}

		model.addAttribute("community", community);
		model.addAttribute("taskType", taskType);
		model.addAttribute("page", "createTask");

		setGlobalAttributesToModel(model, request);
		return "createTask.view";
	}

	/**
	 * Creates a task with requested data
	 * 
	 * @param request
	 * @param body
	 *            data of the task
	 * @return
	 */
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

	/**
	 * Returns a suggested tasks response
	 * 
	 * @param page
	 *            page to start with
	 * @param max
	 *            maximum number of results
	 * @return
	 */
	@RequestMapping(value = "/suggest", method = RequestMethod.GET)
	public ResponseEntity<Map<String, Object>> getSuggestedTasks(@RequestParam(required = false) Integer page, @RequestParam(required = false) Integer max) {
		Map<String, Object> response = new HashMap<String, Object>();

		User user = getUser();
		List<Task> suggestedTasks = taskService.getRecommendedTasks(user);

		response.put("tasks", TaskJson.convert(suggestedTasks));
		return prepareSuccessResponse(response);
	}

	/**
	 * Response task follow requests of user
	 * 
	 * @param request
	 * @param taskId
	 *            id of task followed
	 * @return
	 */
	@RequestMapping(value = "/followTask", method = RequestMethod.POST)
	public ResponseEntity<Map<String, Object>> followTask(HttpServletRequest request, @RequestParam Long taskId) {
		Map<String, Object> response = new HashMap<String, Object>();

		User user = getUser();
		Long followerCount = taskService.followTask(taskId, user);

		response.put("followerCount", followerCount);

		return prepareSuccessResponse(response);
	}

	/**
	 * Response task unfollow requests of user
	 * 
	 * @param request
	 * @param taskId
	 *            id of task followed
	 * @return
	 */
	@RequestMapping(value = "/unfollowTask", method = RequestMethod.POST)
	public ResponseEntity<Map<String, Object>> unfollowTask(HttpServletRequest request, @RequestParam Long taskId) {
		Map<String, Object> response = new HashMap<String, Object>();

		User user = getUser();
		Long followerCount = taskService.unfollowTask(taskId, user);

		response.put("followerCount", followerCount);

		return prepareSuccessResponse(response);
	}

	/**
	 * returns reply form of a task
	 * 
	 * @param request
	 * @param taskId
	 *            id of task
	 * @return
	 */
	@RequestMapping(value = "/getReplyForm", method = RequestMethod.GET)
	public ResponseEntity<Map<String, Object>> getReplyForm(HttpServletRequest request, @RequestParam Long taskId) {
		Map<String, Object> response = new HashMap<String, Object>();

		User user = getUser();
		if (user == null) {
			logger.info("Try to access reply form without login!");
			throw new GrouponException("You cannot reply a task without login!");
		}

		if (taskId == null) {
			logger.info("taskId cannot be null!");
			throw new GrouponException("taskId cannot be null!");
		}

		Task task = taskService.getTaskById(taskId);

		if (task == null) {
			logger.info("Task with this id cannot be found::{0}", taskId);
			throw new GrouponException("Task with this id cannot be found!");
		}

		List<ReplyField> replyFields = null;
		if (task.getTaskType() != null && task.getTaskType().getReplyFields() != null) {
			replyFields = task.getTaskType().getReplyFields();
		}

		response.put("fields", ReplyFieldJson.convert(replyFields));
		return prepareSuccessResponse(response);
	}

	/**
	 * returns followed tasks of a user
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/getFollowedTasks", method = RequestMethod.GET)
	public ResponseEntity<Map<String, Object>> getFollowedTasks(HttpServletRequest request) {
		Map<String, Object> response = new HashMap<String, Object>();

		User user = getUser();
		if (user == null) {
			throw new GrouponException("Login before doing this action!");
		}

		List<Task> followedTasks = taskService.getFollowedTasks(user);
		putTaskListToMobileResponseModel(followedTasks, response);

		return prepareSuccessResponse(response);
	}

	/**
	 * returns home feed tasks
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/getHomeFeedTasks", method = RequestMethod.GET)
	public ResponseEntity<Map<String, Object>> getHomeFeedTasks(HttpServletRequest request) {
		Map<String, Object> response = new HashMap<String, Object>();

		User user = getUser();
		if (user == null) {
			throw new GrouponException("Login before doing this action!");
		}

		List<Task> homeFeedTasks = taskService.getHomeFeedTasks(user, getCurrentSortBy(request));

		if (homeFeedTasks == null || homeFeedTasks.size() == 0) {
			response.put("emptyHomeFeed", Boolean.TRUE);
			homeFeedTasks = taskService.getAllTasks(0, 10, getCurrentSortBy(request));
		}

		putTaskListToMobileResponseModel(homeFeedTasks, response);

		return prepareSuccessResponse(response);
	}

	/**
	 * returns tasks of a community
	 * 
	 * @param request
	 * @param communityId
	 *            id of community
	 * @return
	 */
	@RequestMapping(value = "/getCommunityTasks", method = RequestMethod.GET)
	public ResponseEntity<Map<String, Object>> getCommunityTasks(HttpServletRequest request, @RequestParam Long communityId) {
		Map<String, Object> response = new HashMap<String, Object>();

		List<Task> followedTasks = taskService.getTasks(communityId, 0, 5);
		putTaskListToMobileResponseModel(followedTasks, response);

		return prepareSuccessResponse(response);
	}

	/**
	 * Response reply request of a task
	 * 
	 * @param request
	 * @param body
	 *            reply data
	 * @return
	 * @throws JSONException
	 */
	@RequestMapping(value = "/reply", method = RequestMethod.POST)
	public ResponseEntity<Map<String, Object>> replyTask(HttpServletRequest request, @RequestBody String body) throws JSONException {
		Map<String, Object> response = new HashMap<String, Object>();

		User user = getUser();
		if (user == null) {
			throw new GrouponException("Login before replying a task!");
		}

		JSONObject json = new JSONObject(body);
		TaskReply taskReply = generateTaskReplyFromJson(json);
		taskReply.setReplier(user);

		taskService.saveTaskReply(taskReply);
		
		Task task = taskReply.getTask();
		Long taskId = task.getId();
		
		if (task.getNeedType() == NeedType.GOODS) {
			Map<Long, Integer> quantityCounts = taskService.getTaskHelpCounts(Arrays.asList(taskId));
			if (quantityCounts.containsKey(taskId)) {
				Integer count = quantityCounts.get(taskId);
				response.put("requirementQuantity", task.getRequirementQuantity() - count);
			}
		}

		return prepareSuccessResponse(response);
	}

	/**
	 * returns current replies of a task
	 * 
	 * @param taskId
	 *            task id
	 * @return
	 */
	@RequestMapping(value = "/replies", method = RequestMethod.GET)
	public ResponseEntity<Map<String, Object>> getTaskReplies(@RequestParam Long taskId) {
		Map<String, Object> response = new HashMap<String, Object>();

		if (taskId == null) {
			throw new GrouponException("What task are you looking for???");
		}

		Task task = taskService.getTaskById(taskId);
		if (task == null) {
			throw new GrouponException("What task are you looking for???");
		}

		List<TaskReply> taskReplies = task.getTaskReplies();
		response.put("replies", TaskReplyJson.convert(taskReplies));

		return prepareSuccessResponse(response);
	}

	/**
	 * search tasks and return results
	 * 
	 * @param q
	 *            search text
	 * @param model
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/search", method = RequestMethod.GET)
	public Object searchTasks(@RequestParam String q, Model model, HttpServletRequest request) {
		User user = getUser();
		if (user == null) {
			return "redirect:/";
		}

		if (StringUtils.isBlank(q)) {
			return "redirect:/search";
		}

		setGlobalAttributesToModel(model, request);

		List<Task> tasks = taskService.searchTasks(q);
		model.addAttribute("tasks", tasks);
		model.addAttribute("keywords", q);

		if (tasks.size() > 0) {
			List<Long> taskIds = GrouponWebUtils.convertModelListToLongList(tasks);

			Map<Long, Boolean> followedTaskMap = taskService.findFollowedTasksIdsByUser(user, taskIds);
			model.addAttribute("followedMap", followedTaskMap);

			Map<Long, Integer> replyCounts = taskService.getTaskHelpCounts(taskIds);
			putReplyPercentagesToModel(tasks, replyCounts, model);
		}

		return "searchResult.view";
	}

	/**
	 * response vote request of an user
	 * 
	 * @param request
	 * @param taskId
	 *            task id voted
	 * @param directionStr
	 *            shows whether vote is upvote or downvote
	 * @return
	 */
	@RequestMapping(value = "voteTask", method = RequestMethod.POST)
	public ResponseEntity<Map<String, Object>> voteTask(HttpServletRequest request, @RequestParam Long taskId, @RequestParam(value = "direction") String directionStr) {
		Map<String, Object> response = new HashMap<String, Object>();

		User user = getUser();
		if (user == null) {
			throw new GrouponException("Please login before vote!");
		}

		if (taskId == null) {
			throw new GrouponException("What are you trying to do, man!?");
		}

		Task task = taskService.getTaskById(taskId);
		if (task == null) {
			throw new GrouponException("What are you trying to do, man!?");
		}

		RateDirection direction = RateDirection.valueOf(directionStr);
		TaskRate taskRate = taskService.voteTask(user, task, direction);

		response.put("direction", taskRate.getDirection());

		return prepareSuccessResponse(response);
	}

	/**
	 * remove a vote previously given to a specific task
	 * 
	 * @param request
	 * @param taskId
	 *            id of task
	 * @return
	 */
	@RequestMapping(value = "unvoteTask", method = RequestMethod.POST)
	public ResponseEntity<Map<String, Object>> unvoteTask(HttpServletRequest request, @RequestParam Long taskId) {
		Map<String, Object> response = new HashMap<String, Object>();

		User user = getUser();
		if (user == null) {
			throw new GrouponException("Please login before vote!");
		}

		if (taskId == null) {
			throw new GrouponException("What are you trying to do, man!?");
		}

		Task task = taskService.getTaskById(taskId);
		if (task == null) {
			throw new GrouponException("What are you trying to do, man!?");
		}

		taskService.unvoteTask(task, user.getId());

		return prepareSuccessResponse(response);
	}

	/**
	 * returns direction of vote of user previously given. Initial vote
	 * direction is determined with this.
	 * 
	 * @param request
	 * @param taskId
	 *            id of task
	 * @return
	 */
	@RequestMapping(value = "getUserVotes", method = RequestMethod.GET)
	public ResponseEntity<Map<String, Object>> getUserVotes(HttpServletRequest request, @RequestParam Long taskId) {
		Map<String, Object> response = new HashMap<String, Object>();

		User user = getUser();
		if (user == null) {
			throw new GrouponException("Please login before vote!");
		}

		if (taskId == null) {
			throw new GrouponException("What are you trying to do, man!?");
		}

		Task task = taskService.getTaskById(taskId);
		if (task == null) {
			throw new GrouponException("What are you trying to do, man!?");
		}

		TaskRate taskRate = taskService.findTaskRate(taskId, user.getId());
		if (taskRate != null) {
			response.put("task", taskRate.getDirection());
		}

		return prepareSuccessResponse(response);
	}

	/**
	 * deletes a task
	 * 
	 * @param taskId
	 *            id of task
	 * @return
	 */
	@RequestMapping(value = "delete", method = RequestMethod.GET)
	public ResponseEntity<Map<String, Object>> deleteTask(@RequestParam(required = false) Long taskId) {
		if (!hasRoleAccessGranted(RoleName.ADMIN)) {
			throw new GrouponException("You can't delete tasks!");
		}

		if (taskId == null) {
			throw new GrouponException("Enter taskId!");
		}

		Task task = taskService.getTaskById(taskId);
		if (task == null) {
			throw new GrouponException("Task not found!");
		}

		taskService.removeTask(task);

		Map<String, Object> response = new HashMap<String, Object>();
		return prepareSuccessResponse(response);
	}

	/**
	 * parse task reply from json object
	 * 
	 * @param json
	 *            data parsed
	 * @return
	 * @throws JSONException
	 */
	private TaskReply generateTaskReplyFromJson(JSONObject json) throws JSONException {
		TaskReply taskReply = new TaskReply();

		if (!json.has("taskId")) {
			throw new GrouponException("Task reply without a task cannot be allowed!");
		}

		if (!json.has("fields")) {
			throw new GrouponException("A reply must contain at least one field!");
		}

		Long taskId = json.getLong("taskId");
		Task task = taskService.getTaskById(taskId);
		if (task == null) {
			throw new GrouponException("Task reply without a task cannot be allowed!");
		}

		taskReply.setTask(task);

		JSONArray fields = json.getJSONArray("fields");
		List<ReplyAttribute> replyAttributes = new ArrayList<ReplyAttribute>();
		for (int i = 0; i < fields.length(); i++) {
			JSONObject field = fields.getJSONObject(i);
			String name = field.getString("name");
			String value = field.getString("value");

			ReplyAttribute attr = new ReplyAttribute();
			attr.setName(name);
			attr.setValue(value);
			attr.setTaskReply(taskReply);

			replyAttributes.add(attr);
		}
		taskReply.setAttributes(replyAttributes);

		return taskReply;
	}

	/**
	 * parse task reply from json object
	 * 
	 * @param json
	 *            data parsed
	 * @return
	 * @throws JSONException
	 */
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

		List<TaskAttribute> taskAttributes = new ArrayList<TaskAttribute>();

		if (json.has("attributes")) {
			JSONArray attributeArray = json.getJSONArray("attributes");
			for (int i = 0; i < attributeArray.length(); i++) {
				JSONObject attributeObj = attributeArray.getJSONObject(i);
				String attrName = attributeObj.getString("name");
				String attrValue = attributeObj.getString("value");

				TaskAttribute taskAttribute = new TaskAttribute();
				taskAttribute.setName(attrName);
				taskAttribute.setValue(attrValue);
				taskAttribute.setTask(task);
				taskAttributes.add(taskAttribute);
			}
		}

		task.setAttributes(taskAttributes);

		String type = json.getString("type");
		NeedType needType = NeedType.valueOf(type);
		task.setNeedType(needType);
		if (needType == NeedType.GOODS) {
			String requirementName = json.getString("requirementName");
			int requirementQuantity = json.getInt("requirementQuantity");
			task.setRequirementName(requirementName);
			task.setRequirementQuantity(requirementQuantity);
		} else if (needType == NeedType.SERVICE) {
			String requirementName = json.getString("requirementName");
			task.setRequirementName(requirementName);
		}
		if (json.has("tags")) {
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
		}

		Long communityId = json.getLong("communityId");
		Community community = communityService.getCommunityById(communityId);
		task.setCommunity(community);

		if (json.has("taskType")) {
			long taskTypeId = json.getLong("taskType");
			TaskType taskType = taskTypeService.getTaskTypeById(taskTypeId);
			task.setTaskType(taskType);
		}

		return task;
	}

	/**
	 * get task attributes of a task
	 * 
	 * @param task
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private Map<String, Object> getTaskAttributeMapForModel(Task task) {
		Map<String, Object> attributes = new HashMap<String, Object>();
		List<TaskAttribute> taskAttributes = task.getAttributes();
		if (taskAttributes == null) {
			return attributes;
		}

		for (TaskAttribute attr : taskAttributes) {
			if (attributes.containsKey(attr.getName())) {
				Object value = attributes.get(attr.getName());
				if (value instanceof List<?>) {
					((List<String>) value).add(attr.getValue());
				} else {
					List<String> list = new ArrayList<String>();
					list.add((String) value);
					list.add(attr.getValue());
					attributes.put(attr.getName(), list);
				}
			} else {
				attributes.put(attr.getName(), attr.getValue());
			}
		}
		return attributes;
	}

	/**
	 * puts task mobile to response model
	 * 
	 * @param tasks
	 * @param response
	 */
	private void putTaskListToMobileResponseModel(List<Task> tasks, Map<String, Object> response) {
		List<TaskJson> taskJsons = TaskJson.convert(tasks);

		if (tasks.size() > 0) {
			List<Long> taskIds = GrouponWebUtils.convertModelListToLongList(tasks);

			Map<Long, Boolean> followedTaskMap = taskService.findFollowedTasksIdsByUser(getUser(), taskIds);

			for (TaskJson taskJson : taskJsons) {
				taskJson.setFollower(followedTaskMap.get(taskJson.getId()));
			}
		}
		response.put("tasks", taskJsons);
	}
}
