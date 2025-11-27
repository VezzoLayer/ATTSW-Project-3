package com.tasks.manager.controllers;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.tasks.manager.model.Task;
import com.tasks.manager.services.TaskService;

@Controller
public class TaskWebController {

	private TaskService taskService;

	private static final String TASKS_ATTRIBUTE = "tasks";
	private static final String MESSAGE_ATTRIBUTE = "message";

	public TaskWebController(TaskService taskService) {
		this.taskService = taskService;
	}

	@GetMapping("/")
	public String index(Model model) {
		List<Task> allTasks = taskService.getAllTasks();

		model.addAttribute(TASKS_ATTRIBUTE, allTasks);
		model.addAttribute(MESSAGE_ATTRIBUTE, allTasks.isEmpty() ? "No task to show" : "");

		return "index";
	}

}
