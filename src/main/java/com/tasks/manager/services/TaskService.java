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
		Task taskPersistent = dtoToEntity(taskDTO);

		return taskRepository.save(taskPersistent);
	}

	public Task updateTaskById(String id, TaskDTO replacementDTO) {
		Task taskPersistent = dtoToEntity(replacementDTO);

		taskPersistent.setId(id);

		return taskRepository.save(taskPersistent);
	}

	public void deleteTaskById(String id) {
		taskRepository.deleteById(id);
	}

	private Task dtoToEntity(TaskDTO taskDTO) {
		Task taskPersistent = new Task();

		taskPersistent.setTitle(taskDTO.getTitle());
		taskPersistent.setDescription(taskDTO.getDescription());
		taskPersistent.setPriority(taskDTO.getPriority());
		taskPersistent.setDone(taskDTO.isDone());

		return taskPersistent;
	}
}
