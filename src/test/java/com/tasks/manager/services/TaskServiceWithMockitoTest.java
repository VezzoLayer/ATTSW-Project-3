package com.tasks.manager.services;

import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import org.junit.Test;
import org.junit.runner.RunWith;
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
}
