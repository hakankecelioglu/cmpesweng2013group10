package com.groupon.web.controller;

import java.awt.AlphaComposite;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.groupon.web.controller.json.CommunityJson;
import com.groupon.web.controller.json.TaskTypeJson;
import com.groupon.web.controller.json.UserJson;
import com.groupon.web.dao.model.Community;
import com.groupon.web.dao.model.FieldAttribute;
import com.groupon.web.dao.model.FieldType;
import com.groupon.web.dao.model.NeedType;
import com.groupon.web.dao.model.ReplyField;
import com.groupon.web.dao.model.ReplyFieldAttribute;
import com.groupon.web.dao.model.RoleName;
import com.groupon.web.dao.model.Tag;
import com.groupon.web.dao.model.Task;
import com.groupon.web.dao.model.TaskType;
import com.groupon.web.dao.model.TaskTypeField;
import com.groupon.web.dao.model.User;
import com.groupon.web.exception.GrouponException;
import com.groupon.web.service.CommunityService;
import com.groupon.web.service.TaskService;
import com.groupon.web.service.TaskTypeService;
import com.groupon.web.util.GrouponWebUtils;

@Controller
/**
 * Controller for community pages and actions.
 * 
 * @author sedrik
 */
public class CommunityController extends AbstractBaseController {

	@Autowired
	private CommunityService communityService;

	@Autowired
	private TaskService taskService;

	@Autowired
	private TaskTypeService taskTypeService;

	@Value("${tasks.per.community.page}")
	private int numberOfTasksPerPage;

	@Value("${PHOTO_SRC}")
	private String photoDirectory;

	@Value("${COMMUNITY_IMAGE_MAX_WIDTH}")
	private int COMMUNITY_IMAGE_MAX_WIDTH;

	@Value("${COMMUNITY_IMAGE_MAX_HEIGHT}")
	private int COMMUNITY_IMAGE_MAX_HEIGHT;

	@Value("${COMMUNITY_THUMB_MAX_WIDTH}")
	private int COMMUNITY_THUMB_MAX_WIDTH;

	@Value("${COMMUNITY_THUMB_MAX_HEIGHT}")
	private int COMMUNITY_THUMB_MAX_HEIGHT;

	@Value("${COMMUNITY_LARGE_THUMB_MAX_WIDTH}")
	private int COMMUNITY_LARGE_THUMB_MAX_WIDTH;

	@Value("${COMMUNITY_LARGE_THUMB_MAX_HEIGHT}")
	private int COMMUNITY_LARGE_THUMB_MAX_HEIGHT;

	private static long lastImageTime = System.currentTimeMillis();
	private static Integer imageNumber = 0;

	@RequestMapping(value = "getCommunitiesOfUser", method = RequestMethod.POST)
	public ResponseEntity<Map<String, Object>> getCommunitiesAndroid(HttpServletRequest request, @RequestParam(required = false) Integer page,
			@RequestParam(required = false) Integer max) {
		Map<String, Object> response = new HashMap<String, Object>();
		User user = getUser();

		if (user == null) {
			throw new GrouponException("Login before getting the list of your communities!");
		}

		int pagePrimitive = 0;
		int maxPrimitive = 0;
		if (page != null && max != null) {
			pagePrimitive = page;
			maxPrimitive = max;
		}

		List<Community> communities = communityService.getCommunitiesByFollowerId(user.getId(), pagePrimitive, maxPrimitive);
		response.put("communities", CommunityJson.convert(communities));

		return prepareSuccessResponse(response);
	}

	@RequestMapping(value = "getAllCommunities", method = RequestMethod.POST)
	public ResponseEntity<Map<String, Object>> getAllCommunitiesAndroid(HttpServletRequest request, @RequestParam(required = false) Integer page,
			@RequestParam(required = false) Integer max) {
		Map<String, Object> response = new HashMap<String, Object>();

		List<Community> communities = communityService.getAllCommunities();
		response.put("communities", CommunityJson.convert(communities));

		return prepareSuccessResponse(response);
	}

	@RequestMapping(value = "getSimiliarCommunities", method = RequestMethod.POST)
	public ResponseEntity<Map<String, Object>> getSimiliarCommunities(HttpServletRequest request, @RequestParam(required = true) Long communityId) {
		Map<String, Object> response = new HashMap<String, Object>();

		Integer page = getIntegerParameter(request, "page");
		Integer max = getIntegerParameter(request, "max");
		if (page == null || max == null) {
			page = 0;
			max = 0;
		}

		List<Community> communities = communityService.getSimiliarCommunities(communityId, page, max);
		response.put("communities", CommunityJson.convert(communities));

		return prepareSuccessResponse(response);
	}

	@RequestMapping(value = "createCommunity", method = RequestMethod.GET)
	public Object createCommunity(HttpServletRequest request, Model model) {
		User user = getUser();
		if (user == null) {
			return "redirect:/login";
		}

		setGlobalAttributesToModel(model, request);
		model.addAttribute("page", "createCommunity");

		return "createCommunity.view";
	}

	@RequestMapping(value = "createCommunity", method = RequestMethod.POST)
	public ResponseEntity<Map<String, Object>> createCommunity(HttpServletRequest request, @RequestParam(value = "file", required = false) MultipartFile file) throws JSONException {
		Map<String, Object> response = new HashMap<String, Object>();
		User user = getUser();

		if (user == null) {
			throw new GrouponException("You must be logged in before creating a community!");
		}

		Community community = new Community();
		String name = request.getParameter("name");
		if (StringUtils.isBlank(name)) {
			throw new GrouponException("community name cannot be empty!");
		}

		String description = request.getParameter("description");
		if (StringUtils.isBlank(description)) {
			throw new GrouponException("community description cannot be empty!");
		}

		community.setName(name);
		community.setDescription(description);
		community.setOwner(user);

		String tags = request.getParameter("tags");

		if (tags != null) {
			JSONArray tagArray = new JSONArray(tags);
			List<Tag> tagList = new ArrayList<Tag>(tagArray.length());
			for (int i = 0; i < tagArray.length(); i++) {
				Tag tag = new Tag();
				tag.setName(tagArray.getString(i));
				tagList.add(tag);
			}
			community.setTags(tagList);
		}

		if (file != null && file.getSize() > 0) {
			try {
				String fileName = saveFile(file);
				community.setPicture(fileName);
			} catch (IllegalStateException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		communityService.createCommunity(community);
		logger.debug("Community Created::communityId::{0}", community.getId());

		response.put("message", "OK");
		response.put("communityId", community.getId());
		return prepareSuccessResponse(response);
	}

	@RequestMapping(value = "createCommunityAndroid", method = RequestMethod.POST)
	public ResponseEntity<Map<String, Object>> createCommunityAndroid(HttpServletRequest request, @RequestParam String name, @RequestParam String description) {
		Map<String, Object> response = new HashMap<String, Object>();
		User user = getUser();
		Community community = new Community();
		if (StringUtils.isBlank(name)) {
			throw new GrouponException("community name cannot be empty!");
		}

		if (StringUtils.isBlank(description)) {
			throw new GrouponException("community description cannot be empty!");
		}

		community.setName(name);
		community.setDescription(description);
		community.setOwner(user);

		communityService.createCommunity(community);

		response.put("message", "OK");
		response.put("communityId", community.getId());
		return prepareSuccessResponse(response);
	}

	@RequestMapping(value = "community/{id}")
	public Object communityPage(HttpServletRequest request, Model model, @PathVariable Long id) {
		User user = getUser();

		if (id == null) {
			return "redirect:/";
		}
		setGlobalAttributesToModel(model, request);
		Community community = communityService.getCommunityById(id);

		if (community == null) {
			return "redirect:/";
		}
		model.addAttribute("community", community);

		List<Task> tasks = taskService.getTasks(community.getId(), 0, numberOfTasksPerPage);
		model.addAttribute("tasks", tasks);

		if (tasks.size() > 0) {
			List<Long> taskIds = GrouponWebUtils.convertModelListToLongList(tasks);

			Map<Long, Boolean> followedTaskMap = taskService.findFollowedTasksIdsByUser(user, taskIds);
			model.addAttribute("followedMap", followedTaskMap);

			Map<Long, Integer> replyCounts = taskService.getTaskHelpCounts(taskIds);
			putReplyPercentagesToModel(tasks, replyCounts, model);
		}

		Set<User> members = community.getMembers();

		boolean isMember = (user == null) ? false : members.contains(user);
		boolean isOwner = community.getOwner().equals(user);
		model.addAttribute("members", members);
		model.addAttribute("isMember", isMember);
		model.addAttribute("isOwner", isOwner);

		return "community.view";
	}

	@RequestMapping(value = "community/{id}/members", method = RequestMethod.GET)
	public ResponseEntity<Map<String, Object>> getCommunityMembers(HttpServletRequest request, Model model, @PathVariable Long id) {
		Map<String, Object> response = new HashMap<String, Object>();
		if (id == null) {
			throw new GrouponException("Invalid request! community id cannot be null!");
		}

		Community community = communityService.getCommunityById(id);
		if (community == null) {
			throw new GrouponException("community with that id is not found!");
		}

		Set<User> members = community.getMembers();
		response.put("members", UserJson.convert(members));

		return prepareSuccessResponse(response);
	}

	@RequestMapping(value = "communityMobile/{id}")
	public ResponseEntity<Map<String, Object>> communityPageMobile(HttpServletRequest request, Model model, @PathVariable Long id) {
		// User user = userService.getUserById((long) 1);
		Community community = communityService.getCommunityById(id);
		Map<String, Object> response = new HashMap<String, Object>();
		response.put("community", CommunityJson.convert(community));
		return prepareSuccessResponse(response);

	}

	@RequestMapping(value = "community/mobilejoin")
	public ResponseEntity<Map<String, Object>> joinCommunityMobile(HttpServletRequest request, @RequestParam Long communityId) {
		if (communityId == null) {
			return null;
		}

		Community community = communityService.getCommunityById(communityId);

		User user = getUser();

		communityService.addMemberToCommunity(community, user);
		Map<String, Object> response = new HashMap<String, Object>();

		return prepareSuccessResponse(response);
	}

	@RequestMapping(value = "community/join")
	public Object joinCommunity(HttpServletRequest request, @RequestParam Long communityId) {
		if (communityId == null) {
			return "redirect:/";
		}

		Community community = communityService.getCommunityById(communityId);

		if (community == null) {
			return "redirect:/";
		}

		User user = getUser();

		communityService.addMemberToCommunity(community, user);

		return "redirect:/community/" + communityId;
	}

	@RequestMapping(value = "community/mobileleave")
	public ResponseEntity<Map<String, Object>> leaveCommunityMobile(HttpServletRequest request, @RequestParam Long communityId) {
		User user = getUser();
		if (communityId == null) {
			return null;
		}

		Community community = communityService.getCommunityById(communityId);

		Map<String, Object> response = new HashMap<String, Object>();

		if (community.getOwner().equals(user)) {
			response.put("err", "err");

			prepareErrorResponse(response);
		}

		communityService.removeMemberFromCommunity(community, user);

		return prepareSuccessResponse(response);
	}

	@RequestMapping(value = "community/leave")
	public Object leaveCommunity(HttpServletRequest request, @RequestParam Long communityId) {
		User user = getUser();
		if (communityId == null) {
			return "redirect:/";
		}

		Community community = communityService.getCommunityById(communityId);

		if (community == null) {
			return "redirect:/";
		}

		if (community.getOwner().equals(user)) {
			return "redirect:/community/" + communityId + "?err=leaveowncommunity";
		}

		communityService.removeMemberFromCommunity(community, user);

		return "redirect:/community/" + communityId;
	}

	@RequestMapping(value = "community/createTaskType", method = RequestMethod.GET)
	public Object createTaskType(HttpServletRequest request, Model model, @RequestParam Long communityId) {
		setGlobalAttributesToModel(model, request);
		User user = getUser();
		if (user == null) {
			return "redirect:/";
		}

		if (communityId == null) {
			return "redirect:/";
		}

		Community community = communityService.getCommunityById(communityId);

		if (!community.getOwner().equals(user)) {
			return "redirect:/";
		}

		model.addAttribute("community", community);
		model.addAttribute("bodyClass", "createTaskType");

		return "createTaskType.view";
	}

	@RequestMapping(value = "community/createTaskType", method = RequestMethod.POST)
	public ResponseEntity<Map<String, Object>> createTaskType(@RequestBody String body) throws JSONException {
		User user = getUser();

		JSONObject jsonObject = new JSONObject(body);
		TaskType taskType = convertJSONObjectToTaskType(jsonObject);

		if (!taskType.getCommunity().getOwner().equals(user)) {
			throw new GrouponException("You cannot create a task type in a community that you don't manage!");
		}

		communityService.createTaskType(taskType);

		Map<String, Object> response = new HashMap<String, Object>();
		return prepareSuccessResponse(response);
	}

	@RequestMapping(value = "community/taskTypes", method = RequestMethod.GET)
	public ResponseEntity<Map<String, Object>> getTaskTypes(@RequestParam Long communityId) {
		Map<String, Object> response = new HashMap<String, Object>();

		if (communityId == null) {
			throw new GrouponException("communityId cannot be null");
		}

		List<Map<String, Object>> taskTypeNames = communityService.getTaskTypeNames(communityId);
		response.put("taskTypes", taskTypeNames);
		return prepareSuccessResponse(response);
	}

	@RequestMapping(value = "taskType", method = RequestMethod.POST)
	public ResponseEntity<Map<String, Object>> getTaskType(@RequestParam Long taskTypeId) {
		Map<String, Object> response = new HashMap<String, Object>();

		if (taskTypeId == null) {
			throw new GrouponException("communityId cannot be null");
		}
		TaskType taskType = taskTypeService.getTaskTypeById(taskTypeId);
		response.put("taskType", TaskTypeJson.convert(taskType));
		return prepareSuccessResponse(response);
	}

	@RequestMapping(value = "community/picture/{pictureName:.+}", method = RequestMethod.GET)
	public void getCommunityPicture(@PathVariable String pictureName, HttpServletResponse response) {
		try {
			InputStream in = new FileInputStream(new File(photoDirectory + pictureName + ".png"));

			IOUtils.copy(in, response.getOutputStream());
			response.flushBuffer();
		} catch (Exception e) {
			logger.error("Exception occured while reaching image file! {0}", e.getMessage());
		}
	}

	@RequestMapping(value = "community/thumb/small/{pictureName:.+}", method = RequestMethod.GET)
	public void getCommunitySmallThumbnail(@PathVariable String pictureName, HttpServletResponse response) {
		try {
			InputStream in = new FileInputStream(new File(photoDirectory + pictureName + "_small.png"));

			IOUtils.copy(in, response.getOutputStream());
			response.flushBuffer();
		} catch (Exception e) {
			logger.error("Exception occured while reaching image file! {0}", e.getMessage());
		}
	}

	@RequestMapping(value = "community/thumb/medium/{pictureName:.+}", method = RequestMethod.GET)
	public void getCommunityMediumThumbnail(@PathVariable String pictureName, HttpServletResponse response) {
		try {
			InputStream in = new FileInputStream(new File(photoDirectory + pictureName + "_medium.png"));

			IOUtils.copy(in, response.getOutputStream());
			response.flushBuffer();
		} catch (Exception e) {
			logger.error("Exception occured while reaching image file! {0}", e.getMessage());
		}
	}

	@RequestMapping(value = "community/search", method = RequestMethod.GET)
	public Object searchCommunities(@RequestParam String q, Model model, HttpServletRequest request) {
		User user = getUser();
		if (user == null) {
			return "redirect:/";
		}

		if (StringUtils.isBlank(q)) {
			return "redirect:/search";
		}

		setGlobalAttributesToModel(model, request);
		model.addAttribute("keywords", q);

		List<Community> results = communityService.searchCommunities(q);
		model.addAttribute("communities", results);

		return "searchResult.view";
	}

	@RequestMapping(value = "community/delete", method = RequestMethod.GET)
	public Object deleteCommunity(@RequestParam(required = false) Long communityId) {
		if (!hasRoleAccessGranted(RoleName.ADMIN)) {
			throw new GrouponException("You can't delete community!");
		}

		if (communityId == null) {
			throw new GrouponException("Enter communityId!");
		}

		Community community = communityService.getCommunityById(communityId);
		if (community == null) {
			throw new GrouponException("Community not found!");
		}

		communityService.removeCommunity(community);

		return "OK";
	}

	private String saveFile(MultipartFile multipartFile) throws IllegalStateException, IOException {
		File destinationDir = new File(photoDirectory);
		if (!destinationDir.exists()) {
			destinationDir.mkdirs();
		}

		String fileName = generateUniqueFileName(multipartFile);
		String fullPath = photoDirectory + fileName;

		logger.debug("saveFile::file is transfering to::{0}", fileName);

		BufferedImage originalImage = ImageIO.read(multipartFile.getInputStream());
		creataThumbnails(originalImage, fullPath);
		resizeImage(originalImage, fullPath);

		return fileName;
	}

	private void resizeImage(BufferedImage originalImage, String fullPath) throws IOException {
		int type = originalImage.getType() == 0 ? BufferedImage.TYPE_INT_ARGB : originalImage.getType();
		int width = originalImage.getWidth();
		int height = originalImage.getHeight();

		Dimension resizeDimension = GrouponWebUtils.getDimensionFitBounds(width, height, COMMUNITY_IMAGE_MAX_WIDTH, COMMUNITY_IMAGE_MAX_HEIGHT);
		logger.debug("resizeImage::image dimension::[{0}, {1}]::thumbDimension::[{2}, {3}]", width, height, resizeDimension.width, resizeDimension.height);

		BufferedImage resizedImage = new BufferedImage(resizeDimension.width, resizeDimension.height, type);
		Graphics2D g = resizedImage.createGraphics();
		g.drawImage(originalImage, 0, 0, resizeDimension.width, resizeDimension.height, 0, 0, width, height, null);
		g.setComposite(AlphaComposite.Src);
		g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
		g.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g.dispose();

		ImageIO.write(resizedImage, "png", new File(fullPath + ".png"));
	}

	private void creataThumbnails(BufferedImage originalImage, String fullPath) throws IOException {
		BufferedImage smallThumbnail = createSquareImage(originalImage, COMMUNITY_THUMB_MAX_WIDTH, COMMUNITY_THUMB_MAX_HEIGHT);
		ImageIO.write(smallThumbnail, "png", new File(fullPath + "_small.png"));
		BufferedImage mediumThumbnail = createSquareImage(originalImage, COMMUNITY_LARGE_THUMB_MAX_WIDTH, COMMUNITY_LARGE_THUMB_MAX_HEIGHT);
		ImageIO.write(mediumThumbnail, "png", new File(fullPath + "_medium.png"));
	}

	private BufferedImage createSquareImage(BufferedImage originalImage, int maxWidth, int maxHeight) {
		int type = originalImage.getType() == 0 ? BufferedImage.TYPE_INT_ARGB : originalImage.getType();
		int width = originalImage.getWidth();
		int height = originalImage.getHeight();

		Dimension thumbnailDimension = GrouponWebUtils.getDimensionExtendsBounds(width, height, maxWidth, maxHeight);

		int dx1 = (maxWidth - thumbnailDimension.width) / 2;
		int dx2 = dx1 + thumbnailDimension.width;
		int dy1 = (maxHeight - thumbnailDimension.height) / 2;
		int dy2 = dy1 + thumbnailDimension.height;

		BufferedImage resizedImage = new BufferedImage(maxWidth, maxHeight, type);
		Graphics2D g = resizedImage.createGraphics();
		g.drawImage(originalImage, dx1, dy1, dx2, dy2, 0, 0, width, height, null);
		g.setComposite(AlphaComposite.Src);
		g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
		g.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g.dispose();
		return resizedImage;
	}

	private String generateUniqueFileName(MultipartFile multipartFile) {
		int currentImageNumber;
		long time = System.currentTimeMillis();
		synchronized (imageNumber) {
			if (time != lastImageTime) {
				lastImageTime = time;
				imageNumber = 0;
			}
			currentImageNumber = ++imageNumber;
		}

		StringBuilder fileNameBuilder = new StringBuilder();
		fileNameBuilder.append("com-pic-");
		fileNameBuilder.append(currentImageNumber);
		fileNameBuilder.append("-");
		fileNameBuilder.append(time);

		return fileNameBuilder.toString();
	}

	private TaskType convertJSONObjectToTaskType(JSONObject json) throws JSONException {
		if (GrouponWebUtils.isBlank(json, "name")) {
			throw new GrouponException("Task type must have a name!");
		}

		if (GrouponWebUtils.isBlank(json, "description")) {
			throw new GrouponException("Task type must have a description!");
		}

		if (!json.has("communityId")) {
			throw new GrouponException("Community id cannot be leave as empty!");
		}

		if (!json.has("needType")) {
			throw new GrouponException("Task type must specify a need type!");
		}

		TaskType taskType = new TaskType();

		String name = json.getString("name");
		String description = json.getString("description");
		String needTypeStr = json.getString("needType");

		NeedType needType = NeedType.valueOf(needTypeStr);
		taskType.setNeedType(needType);

		Long communityId = json.getLong("communityId");
		Community community = communityService.getCommunityById(communityId);

		List<TaskTypeField> taskTypeFields = new ArrayList<TaskTypeField>();
		if (json.has("fields")) {
			JSONArray fields = json.getJSONArray("fields");
			for (int i = 0; i < fields.length(); i++) {
				JSONObject field = fields.getJSONObject(i);
				TaskTypeField taskTypeField = convertJSONObjectToTaskTypeField(field);
				taskTypeField.setTaskType(taskType);
				taskTypeFields.add(taskTypeField);
			}
		}

		List<ReplyField> replyFields = new ArrayList<ReplyField>();
		if (json.has("replyFields")) {
			JSONArray replyFieldsJson = json.getJSONArray("replyFields");
			for (int i = 0; i < replyFieldsJson.length(); i++) {
				JSONObject field = replyFieldsJson.getJSONObject(i);
				ReplyField replyTypeField = convertJSONObjectToReplyField(field);
				replyTypeField.setTaskType(taskType);
				replyFields.add(replyTypeField);
			}
		}

		taskType.setName(name);
		taskType.setDescription(description);
		taskType.setCommunity(community);
		taskType.setFields(taskTypeFields);
		taskType.setReplyFields(replyFields);

		return taskType;
	}

	private TaskTypeField convertJSONObjectToTaskTypeField(JSONObject field) throws JSONException {
		if (GrouponWebUtils.isBlank(field, "name")) {
			throw new GrouponException("A task type field must have a name!");
		}

		if (GrouponWebUtils.isBlank(field, "type")) {
			throw new GrouponException("A task type field must have a type!");
		}

		TaskTypeField taskTypeField = new TaskTypeField();

		String name = field.getString("name");
		String type = field.getString("type");
		FieldType fieldType = FieldType.valueOf(type);

		List<FieldAttribute> attributes = new ArrayList<FieldAttribute>();
		if (field.has("attributes")) {
			JSONArray attributesObj = field.getJSONArray("attributes");
			for (int i = 0; i < attributesObj.length(); i++) {
				JSONObject attributeJson = attributesObj.getJSONObject(i);
				if (!attributeJson.has("value")) {
					throw new GrouponException("Field attributes must have a value!");
				}

				if (GrouponWebUtils.isBlank(attributeJson, "name")) {
					throw new GrouponException("Field attributes must have a name");
				}

				String attrName = attributeJson.getString("name");
				String attrValue = attributeJson.getString("value");
				FieldAttribute attribute = new FieldAttribute();
				attribute.setName(attrName);
				attribute.setValue(attrValue);
				attribute.setTaskTypeField(taskTypeField);
				attributes.add(attribute);
			}
		}

		taskTypeField.setName(name);
		taskTypeField.setFieldType(fieldType);
		taskTypeField.setAttributes(attributes);

		return taskTypeField;
	}

	private ReplyField convertJSONObjectToReplyField(JSONObject field) throws JSONException {
		if (GrouponWebUtils.isBlank(field, "name")) {
			throw new GrouponException("A task type field must have a name!");
		}

		if (GrouponWebUtils.isBlank(field, "type")) {
			throw new GrouponException("A task type field must have a type!");
		}

		ReplyField replyField = new ReplyField();

		String name = field.getString("name");
		String type = field.getString("type");
		FieldType fieldType = FieldType.valueOf(type);

		List<ReplyFieldAttribute> attributes = new ArrayList<ReplyFieldAttribute>();
		if (field.has("attributes")) {
			JSONArray attributesObj = field.getJSONArray("attributes");
			for (int i = 0; i < attributesObj.length(); i++) {
				JSONObject attributeJson = attributesObj.getJSONObject(i);
				if (!attributeJson.has("value")) {
					throw new GrouponException("Field attributes must have a value!");
				}

				if (GrouponWebUtils.isBlank(attributeJson, "name")) {
					throw new GrouponException("Field attributes must have a name");
				}

				String attrName = attributeJson.getString("name");
				String attrValue = attributeJson.getString("value");

				ReplyFieldAttribute attribute = new ReplyFieldAttribute();
				attribute.setName(attrName);
				attribute.setValue(attrValue);
				attribute.setReplyField(replyField);
				attributes.add(attribute);
			}
		}

		replyField.setName(name);
		replyField.setFieldType(fieldType);
		replyField.setAttributes(attributes);

		return replyField;
	}
}
