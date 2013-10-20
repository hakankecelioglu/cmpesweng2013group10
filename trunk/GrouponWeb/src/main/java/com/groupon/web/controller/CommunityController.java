package com.groupon.web.controller;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
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

import com.groupon.web.dao.model.Community;
import com.groupon.web.dao.model.User;
import com.groupon.web.service.CommunityService;

@Controller
/**
 * Controller for community pages and actions.
 * 
 * @author sedrik
 */
public class CommunityController extends AbstractBaseController {
	
	@Autowired
	private CommunityService communityService;
	
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
	public ResponseEntity<Map<String, Object>> createCommunity(@RequestBody String data, HttpServletRequest request) {
		Map<String, Object> response = new HashMap<String, Object>();
		User user = getUser(request);
		
		try {
			if (user == null) {
				throw new GrouponException("You must be logged in before creating a community!");
			}
			
			JSONObject communityJson = new JSONObject(data);
			Community community = convertJSONToCommunity(communityJson);
			
			community.setOwner(user);
			communityService.createCommunity(community);
			
			response.put("message", "OK");
			response.put("communityId", community.getId());
			return prepareSuccessResponse(response);
		} catch (JSONException e) {
			response.put("error", "error occured while parsing json!");
			return prepareErrorResponse(response);
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
		
		return "community.view";
	}
	
	private Community convertJSONToCommunity(JSONObject json) throws JSONException, GrouponException {
		Community community = new Community();
		String name = json.getString("name");
		String description = json.getString("description");
		
		if (StringUtils.isBlank(name)) {
			throw new GrouponException("community name cannot be empty!");
		}
		
		if (StringUtils.isBlank(description)) {
			throw new GrouponException("community description cannot be empty!");
		}
		
		community.setName(name);
		community.setDescription(description);
		return community;
	}
}
