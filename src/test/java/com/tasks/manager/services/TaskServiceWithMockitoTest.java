package com.tasks.manager.services;

import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import com.tasks.manager.dto.TaskDTO;
import com.tasks.manager.model.Task;
import com.tasks.manager.repositories.TaskRepository;

@RunWith(MockitoJUnitRunner.class)
public class TaskServiceWithMockitoTest {

	@Mock
	private TaskRepository taskRepository;

	@InjectMocks
	private TaskService taskService;

	@Captor
	private ArgumentCaptor<Task> taskCaptor;

	@Test
	public void testGetAllTasks() {
		Task task1 = new Task("t1", "test", "test", 10, true);
		Task task2 = new Task("t2", "test", "test", 10, true);

		when(taskRepository.findAll()).thenReturn(asList(task1, task2));

		assertThat(taskService.getAllTasks()).containsExactly(task1, task2);
	}

	@Test
	public void testGetAllTasksByPriorityDesc() {
		Task task1 = new Task("t1", "test", "test", 10, true);
		Task task2 = new Task("t2", "test", "test", 1, true);

		when(taskRepository.findAllByOrderByPriorityDesc()).thenReturn(asList(task1, task2));

		assertThat(taskService.getAllTasksByDescendentPriority()).containsExactly(task1, task2);
	}

	@Test
	public void testGetTaskByIdWhenFound() {
		Task task = new Task("t", "test", "test", 10, true);

		when(taskRepository.findById("t")).thenReturn(Optional.of(task));

		assertThat(taskService.getTaskById("t")).isSameAs(task);
	}

	@Test
	public void testGetTaskByIdWhenNotFound() {
		when(taskRepository.findById(anyString())).thenReturn(Optional.empty());

		assertThat(taskService.getTaskById("t")).isNull();
	}

	@Test
	public void testInsertNewTaskShouldConvertDTOWithNullIdAndReturnsSavedTask() {
		TaskDTO taskToSave = new TaskDTO("t99", "to save", "to save", 0, false);
		Task savedTask = new Task("t1", "saved", "saved", 10, true);

		when(taskRepository.save(any(Task.class))).thenReturn(savedTask);

		Task result = taskService.insertNewTask(taskToSave);
		assertThat(result).isSameAs(savedTask);

		verify(taskRepository).save(taskCaptor.capture());
		Task capturedTask = taskCaptor.getValue();

		// Controllo i valori salvati con capture
		assertThat(capturedTask.getId()).isNull();
		assertThat(capturedTask.getTitle()).isEqualTo("to save");
		assertThat(capturedTask.getDescription()).isEqualTo("to save");
		assertThat(capturedTask.getPriority()).isZero();
		assertThat(capturedTask.isDone()).isFalse();
	}

	@Test
	public void testUpdateTaskByIdShouldConvertDTOAndSetsIdToArgumentAndReturnsSavedTask() {
		TaskDTO replacement = new TaskDTO(null, "repl", "repl", 0, false);
		Task replaced = new Task("t1", "saved", "saved", 10, true);

		when(taskRepository.save(any(Task.class))).thenReturn(replaced);

		Task result = taskService.updateTaskById("t1", replacement);
		assertThat(result).isSameAs(replaced);

		verify(taskRepository).save(taskCaptor.capture());
		Task capturedTask = taskCaptor.getValue();

		// Controllo i valori salvati con capture
		assertThat(capturedTask.getId()).isEqualTo("t1");
		assertThat(capturedTask.getTitle()).isEqualTo("repl");
		assertThat(capturedTask.getDescription()).isEqualTo("repl");
		assertThat(capturedTask.getPriority()).isZero();
		assertThat(capturedTask.isDone()).isFalse();
	}

	@Test
	public void testDeleteTaskById() {
		taskService.deleteTaskById("t1");

		verify(taskRepository).deleteById("t1");
	}
}
