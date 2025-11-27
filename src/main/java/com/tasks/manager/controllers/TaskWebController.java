package com.tasks.manager.controllers;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import com.tasks.manager.dto.TaskDTO;
import com.tasks.manager.model.Task;
import com.tasks.manager.services.TaskService;

@Controller
public class TaskWebController {

	private TaskService taskService;

	private static final String TASK_ATTRIBUTE = "task";
	private static final String TASKS_ATTRIBUTE = "tasks";
	private static final String MESSAGE_ATTRIBUTE = "message";
	private static final String ISSORTED_ATTRIBUTE = "isSorted";

	public TaskWebController(TaskService taskService) {
		this.taskService = taskService;
	}

	@GetMapping("/")
	public String index(Model model) {
		List<Task> allTasks = taskService.getAllTasks();

		model.addAttribute(TASKS_ATTRIBUTE, allTasks);
		model.addAttribute(MESSAGE_ATTRIBUTE, allTasks.isEmpty() ? "No task to show" : "");
		model.addAttribute(ISSORTED_ATTRIBUTE, false);

		return "index";
	}

	@GetMapping("/ordered")
	public String indexOrdered(Model model) {
		List<Task> orderedTasks = taskService.getAllTasksByDescendentPriority();

		model.addAttribute(TASKS_ATTRIBUTE, orderedTasks);
		model.addAttribute(MESSAGE_ATTRIBUTE, orderedTasks.isEmpty() ? "No task to show" : "");
		model.addAttribute(ISSORTED_ATTRIBUTE, true);

		return "index";
	}

	@GetMapping("/edit/{id}")
	public String editTask(@PathVariable String id, Model model) {
		Task taskById = taskService.getTaskById(id);

		model.addAttribute(TASK_ATTRIBUTE, taskById);
		model.addAttribute(MESSAGE_ATTRIBUTE, taskById == null ? "No task found with id: " + id : "");

		return "edit";
	}

	@GetMapping("/new")
	public String newTask(Model model) {
		model.addAttribute(TASK_ATTRIBUTE, new Task());
		model.addAttribute(MESSAGE_ATTRIBUTE, "");

		return "edit";
	}

	@PostMapping("/save")
	public String saveUser(TaskDTO taskDTO) {
		taskService.insertNewTask(taskDTO);

		return "redirect:/";
	}
}
