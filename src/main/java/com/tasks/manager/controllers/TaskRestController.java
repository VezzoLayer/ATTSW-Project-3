package com.tasks.manager.controllers;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
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

	@GetMapping("/ordered")
	public List<Task> allTasksOrdered() {
		return taskService.getAllTasksByDescendentPriority();
	}

	@GetMapping("/{id}")
	public Task oneTask(@PathVariable String id) {
		return taskService.getTaskById(id);
	}

	@PostMapping("/new")
	public Task newTask(@RequestBody Task task) {
		return taskService.insertNewTask(task);
	}
}
