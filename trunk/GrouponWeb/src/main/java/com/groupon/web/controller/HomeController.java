package com.groupon.web.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.json.JSONException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.groupon.web.dao.model.Requirement;
import com.groupon.web.dao.model.Task;
import com.groupon.web.dao.model.TaskRequirement;

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
		generateDummyContent(model);
		return "home.view";
	}

	private static void generateDummyContent(Model model) {
		List<Task> urgentTasks = new ArrayList<Task>();
		urgentTasks.add(generateDummyTask());
		urgentTasks.add(generateDummyTask());
		model.addAttribute("urgentTasks", urgentTasks);

		List<Task> latestTasks = new ArrayList<Task>();
		latestTasks.add(generateDummyTask());
		latestTasks.add(generateDummyTask());
		latestTasks.add(generateDummyTask());
		latestTasks.add(generateDummyTask());
		latestTasks.add(generateDummyTask());
		model.addAttribute("latestTasks", latestTasks);

		List<Task> randomTasks = new ArrayList<Task>();
		randomTasks.add(generateDummyTask());
		randomTasks.add(generateDummyTask());
		model.addAttribute("randomTasks", randomTasks);
	}

	private static Task generateDummyTask() {
		Task task = new Task();
		task.setCreateDate(new Date());
		task.setDescription("We are waiting for doctors inside of the Taksim Burger which is placed Imam Adnan Sokak. We have only gas masks! We are waiting for doctors inside of the Taksim Burger which is placed Imam Adnan Sokak. We have only gas masks!We are waiting for doctors inside of the Taksim Burger which is placed Imam Adnan Sokak. We have only gas masks!We are waiting for doctors inside of the Taksim Burger which is placed Imam Adnan Sokak. We have only gas masks!");
		task.setTitle("We need a doctor near Taksim.");

		List<TaskRequirement> requirements = new ArrayList<TaskRequirement>();

		Requirement requirement = new Requirement();
		requirement.setName("Doctor");
		TaskRequirement taskRequirement = new TaskRequirement();
		taskRequirement.setAmount(3f);
		taskRequirement.setRequirement(requirement);
		requirements.add(taskRequirement);

		requirement = new Requirement();
		requirement.setName("First aid outfit");
		taskRequirement = new TaskRequirement();
		taskRequirement.setAmount(null);
		taskRequirement.setRequirement(requirement);
		requirements.add(taskRequirement);

		task.setRequirements(requirements);
		return task;
	}

}
