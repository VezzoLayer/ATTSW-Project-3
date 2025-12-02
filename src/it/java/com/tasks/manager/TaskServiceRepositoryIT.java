package com.tasks.manager;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import com.tasks.manager.dto.TaskDTO;
import com.tasks.manager.model.Task;
import com.tasks.manager.repositories.TaskRepository;
import com.tasks.manager.services.TaskService;

@RunWith(SpringRunner.class)
@DataMongoTest
@Import(TaskService.class)
@ActiveProfiles("mongodb")
public class TaskServiceRepositoryIT {

	@Autowired
	private TaskService taskService;

	@Autowired
	private TaskRepository taskRepository;

	@Before
	public void setup() {
		// DataMongoTest non sono transactional, db pulito prima di ogni test
		taskRepository.deleteAll();
	}

	@Test
	public void testServiceCanInsertIntoTaskRepository() {
		Task savedTask = taskService.insertNewTask(new TaskDTO(null, "title", "descr", 10, true));

		assertThat(taskRepository.findById(savedTask.getId())).isPresent();
	}

	@Test
	public void testServiceCanUpdateTaskRepository() {
		Task savedTask = taskRepository.save(new Task(null, "title", "descr", 10, false));

		Task modifiedTask = taskService.updateTaskById(savedTask.getId(),
				new TaskDTO(savedTask.getId(), "mod", "mod", 5, true));

		assertThat(taskRepository.findById(savedTask.getId())).contains(modifiedTask);
	}

	@Test
	public void testServiceRetrieveAllTasksByPriority() {
		Task low = taskRepository.save(new Task("t1", "Low Prio", "desc", 1, false));
		Task high = taskRepository.save(new Task("t2", "High Prio", "desc", 10, false));
		Task medium = taskRepository.save(new Task("t3", "Medium Prio", "desc", 5, false));

		List<Task> tasks = taskService.getAllTasksByDescendentPriority();

		assertThat(tasks).containsExactly(high, medium, low);
	}
}
