package com.groupon.web.controller;

import org.json.JSONException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class HomeController extends BaseController {

	/**
	 * Opens home page
	 * 
	 * @return the view which will be shown on homepage
	 * @throws JSONException
	 */
	@RequestMapping(value = "/", method = RequestMethod.GET)
	public Object home() throws JSONException {
		// TODO to be implemented
		return "home.view";
	}

}
