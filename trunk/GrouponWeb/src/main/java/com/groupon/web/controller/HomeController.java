package com.groupon.web.controller;

import javax.servlet.http.HttpServletRequest;

import org.json.JSONException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class HomeController extends AbstractBaseController {

	/**
	 * Opens home page
	 * 
	 * @return the view which will be shown on homepage
	 * @throws JSONException
	 */
	@RequestMapping(value = "/", method = RequestMethod.GET)
	public Object home(HttpServletRequest request, Model model) throws JSONException {
		setGlobalAttributesToModel(model, request);
		model.addAttribute("page", "home");
		return "home.view";
	}
}
