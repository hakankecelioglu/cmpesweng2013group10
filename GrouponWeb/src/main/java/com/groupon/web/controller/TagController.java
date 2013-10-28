package com.groupon.web.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping(value = "/tags")
public class TagController extends AbstractBaseController {
	
	@RequestMapping(value = "/searchTags")
	public ResponseEntity<Map<String, Object>> getTags(@RequestParam String term, @RequestParam Long page) {
		Map<String, Object> response = new HashMap<String, Object>();
		
		List<String> tags = new ArrayList<String>();
		tags.add("Van (city)");
		tags.add("Van Gölü");
		tags.add("Van Depremi");
		tags.add("Çadır Yardımı");
		tags.add("Yardım");
		
		response.put("tags", tags);
		
		return prepareSuccessResponse(response);
	}
}
