package com.tasks.manager.controllers;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tasks.manager.model.Task;
import com.tasks.manager.services.TaskService;

@RestController
@RequestMapping("/api/tasks")
public class TaskRestController {

	private TaskService taskService;

	public TaskRestController(TaskService taskService) {
		this.taskService = taskService;
	}

	@GetMapping
	public List<Task> allTasks() {
		return taskService.getAllTasks();
	}
}
