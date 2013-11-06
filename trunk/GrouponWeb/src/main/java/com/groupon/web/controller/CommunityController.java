package com.groupon.web.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.groupon.web.dao.model.Community;
import com.groupon.web.dao.model.Task;
import com.groupon.web.dao.model.User;
import com.groupon.web.service.CommunityService;
import com.groupon.web.service.TaskService;

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

	@Value("${tasks.per.community.page}")
	private int numberOfTasksPerPage;

	@Value("${PHOTO_SRC}")
	private String photoDirectory;

	@RequestMapping(value = "createCommunity", method = RequestMethod.GET)
	public Object createCommunity(HttpServletRequest request, Model model) {
		User user = getUser(request);
		if (user == null) {
			return "redirect:/login";
		}

		setGlobalAttributesToModel(model, request);
		model.addAttribute("page", "createCommunity");

		return "createCommunity.view";
	}

	@RequestMapping(value = "createCommunity", method = RequestMethod.POST)
	public ResponseEntity<Map<String, Object>> createCommunity(HttpServletRequest request, @RequestParam(value = "file", required = false) MultipartFile file,
			@RequestParam String name, @RequestParam String description) {
		Map<String, Object> response = new HashMap<String, Object>();
		User user = getUser(request);

		try {
			if (user == null) {
				throw new GrouponException("You must be logged in before creating a community!");
			}

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
			
			// Every user is a member of his own communities
			communityService.addMemberToCommunity(community, user);

			response.put("message", "OK");
			response.put("communityId", community.getId());
			return prepareSuccessResponse(response);
		} catch (GrouponException e) {
			response.put("error", e.getMessage());
			return prepareErrorResponse(response);
		}
	}

	@RequestMapping(value = "community/{id}")
	public Object communityPage(HttpServletRequest request, Model model, @PathVariable Long id) {
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

		return "community.view";
	}

	@RequestMapping(value = "community/picture/{pictureName:.+}", method = RequestMethod.GET)
	public void getCommunityPicture(@PathVariable String pictureName, HttpServletResponse response) {
		try {
			InputStream in = new FileInputStream(new File(photoDirectory + pictureName));

			IOUtils.copy(in, response.getOutputStream());
			response.flushBuffer();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private String saveFile(MultipartFile multipartFile) throws IllegalStateException, IOException {
		File destinationDir = new File(photoDirectory);
		if (!destinationDir.exists()) {
			destinationDir.mkdirs();
		}

		String fileName = generateUniqueFileName(multipartFile);
		File file = new File(photoDirectory + fileName);

		multipartFile.transferTo(file);

		return fileName;
	}

	private String generateUniqueFileName(MultipartFile multipartFile) {
		Random random = new Random();
		int randomNumber = 1000000 + random.nextInt(1000000);

		StringBuilder fileNameBuilder = new StringBuilder();
		fileNameBuilder.append("com-pic-");
		fileNameBuilder.append(randomNumber);
		fileNameBuilder.append("-");
		fileNameBuilder.append(System.currentTimeMillis());

		String extension = getFileExtension(multipartFile);
		if (extension != null) {
			fileNameBuilder.append(".");
			fileNameBuilder.append(extension);
		}

		return fileNameBuilder.toString();
	}

	private String getFileExtension(MultipartFile file) {
		String filename = file.getOriginalFilename();
		if (filename != null) {
			int lastIndexOfDot = filename.lastIndexOf('.');
			if (lastIndexOfDot > 0 && lastIndexOfDot < filename.length() - 1) {
				return filename.substring(lastIndexOfDot + 1);
			}
		}
		return null;
	}
}
