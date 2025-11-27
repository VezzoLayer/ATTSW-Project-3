package com.tasks.manager.services;

import java.util.List;

import org.springframework.stereotype.Service;

import com.tasks.manager.dto.TaskDTO;
import com.tasks.manager.model.Task;
import com.tasks.manager.repositories.TaskRepository;

@Service
public class TaskService {

	private TaskRepository taskRepository;

	public TaskService(TaskRepository taskRepository) {
		this.taskRepository = taskRepository;
	}

	public List<Task> getAllTasks() {
		return taskRepository.findAll();
	}

	public List<Task> getAllTasksByDescendentPriority() {
		return taskRepository.findAllByOrderByPriorityDesc();
	}

	public Task getTaskById(String id) {
		return taskRepository.findById(id).orElse(null);
	}

	public Task insertNewTask(TaskDTO taskDTO) {
		Task persistentTask = new Task();

		persistentTask.setId(null);
		persistentTask.setTitle(taskDTO.getTitle());
		persistentTask.setDescription(taskDTO.getDescription());
		persistentTask.setPriority(taskDTO.getPriority());
		persistentTask.setDone(taskDTO.isDone());

		return taskRepository.save(persistentTask);
	}

	public Task updateTaskById(String id, TaskDTO replacementDTO) {
		Task replacementePersistent = new Task();

		replacementePersistent.setId(id);
		replacementePersistent.setTitle(replacementDTO.getTitle());
		replacementePersistent.setDescription(replacementDTO.getDescription());
		replacementePersistent.setPriority(replacementDTO.getPriority());
		replacementePersistent.setDone(replacementDTO.isDone());

		return taskRepository.save(replacementePersistent);
	}

	public void deleteTaskById(String id) {
		taskRepository.deleteById(id);
	}
}
