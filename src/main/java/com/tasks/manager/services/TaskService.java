package com.tasks.manager.services;

import java.util.List;

import com.tasks.manager.model.Task;
import com.tasks.manager.repositories.TaskRepository;

public class TaskService {

	private TaskRepository taskRepository;

	public TaskService(TaskRepository taskRepository) {
		this.taskRepository = taskRepository;
	}

	public List<Task> getAllTasks() {
		return taskRepository.findAll();
	}

	public Task getTaskById(String id) {
		return taskRepository.findById(id).orElse(null);
	}
}
