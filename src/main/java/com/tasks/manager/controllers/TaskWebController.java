package com.tasks.manager.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.tasks.manager.services.TaskService;

@Controller
public class TaskWebController {

	private TaskService taskService;

	private static final String TASKS_ATTRIBUTE = "tasks";

	public TaskWebController(TaskService taskService) {
		this.taskService = taskService;
	}

	@GetMapping("/")
	public String index(Model model) {
		model.addAttribute(TASKS_ATTRIBUTE, taskService.getAllTasks());

		return "index";
	}

}
