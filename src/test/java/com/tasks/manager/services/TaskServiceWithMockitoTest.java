package com.tasks.manager.services;

import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InOrder;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import com.tasks.manager.model.Task;
import com.tasks.manager.repositories.TaskRepository;

@RunWith(MockitoJUnitRunner.class)
public class TaskServiceWithMockitoTest {

	@Mock
	private TaskRepository taskRepository;

	@InjectMocks
	private TaskService taskService;

	@Test
	public void testGetAllTasks() {
		Task task1 = new Task("t1", "test", "test", 10, true);
		Task task2 = new Task("t2", "test", "test", 10, true);

		when(taskRepository.findAll()).thenReturn(asList(task1, task2));

		assertThat(taskService.getAllTasks()).containsExactly(task1, task2);
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
	public void testInsertNewTaskShouldSetIdToNullAndReturnsSavedTask() {
		Task taskToSave = spy(new Task("t99", "", "", 0, false));
		Task savedTask = new Task("t1", "saved", "saved", 10, true);

		when(taskRepository.save(any(Task.class))).thenReturn(savedTask);

		Task result = taskService.insertNewTask(taskToSave);

		assertThat(result).isSameAs(savedTask);

		InOrder inOrder = inOrder(taskToSave, taskRepository);
		inOrder.verify(taskToSave).setId(null);
		inOrder.verify(taskRepository).save(taskToSave);
	}

	@Test
	public void testUpdateTaskByIdSetsIdToArgumentAndReturnsSavedTask() {
		Task replacement = spy(new Task(null, "repl", "repl", 0, false));
		Task replaced = new Task("t1", "saved", "saved", 10, true);

		when(taskRepository.save(any(Task.class))).thenReturn(replaced);

		Task result = taskService.updateTaskById("t1", replacement);

		assertThat(result).isSameAs(replaced);

		InOrder inOrder = inOrder(replacement, taskRepository);
		inOrder.verify(replacement).setId("t1");
		inOrder.verify(taskRepository).save(replacement);
	}

	@Test
	public void testDeleteTaskById() {
		taskService.deleteTaskById("t1");

		verify(taskRepository).deleteById("t1");
	}
}
