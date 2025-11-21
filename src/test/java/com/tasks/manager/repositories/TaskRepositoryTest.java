package com.tasks.manager.repositories;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Collection;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import com.tasks.manager.model.Task;

@DataMongoTest
@RunWith(SpringRunner.class)
public class TaskRepositoryTest {

	@Autowired
	private TaskRepository repository;

	@Autowired
	private MongoTemplate mongoTemplate;

	@Before
	public void setup() {
		// DataMongoTest non sono transactional, db pulito prima di ogni test
		repository.deleteAll();
	}

	@Test
	public void testRepositoryContainsExactlyOneTask() {
		Task savedTask = mongoTemplate.save(new Task(null, "title", "description", 10, true));

		Collection<Task> tasks = repository.findAll();
		assertThat(tasks).containsExactly(savedTask);
	}

	@Test
	public void testFindTaskByTitle() {
		Task taskShouldBeFound = mongoTemplate.save(new Task(null, "dentist", "test", 10, true));

		Task taskFound = repository.findByTitle("dentist");
		assertThat(taskFound).isEqualTo(taskShouldBeFound);
	}

	@Test
	public void testFindTaskByDescription() {
		Task taskShouldBeFound = mongoTemplate.save(new Task(null, "test", "monday 10am", 10, true));

		Task taskFound = repository.findByDescription("monday 10am");
		assertThat(taskFound).isEqualTo(taskShouldBeFound);
	}

	@Test
	public void testFindTasksByPriority() {
		Task taskShouldBeFound1 = mongoTemplate.save(new Task(null, "test1", "test1", 10, true));
		Task taskShouldBeFound2 = mongoTemplate.save(new Task(null, "test2", "test2", 10, true));

		mongoTemplate.save(new Task(null, "test", "test", 9, true)); // Should Not Be Found

		List<Task> tasks = repository.findByPriority(10);
		assertThat(tasks).containsExactly(taskShouldBeFound1, taskShouldBeFound2);
	}

	@Test
	public void testFindTasksByDone() {
		Task taskShouldBeFound1 = mongoTemplate.save(new Task(null, "test1", "test1", 10, true));
		Task taskShouldBeFound2 = mongoTemplate.save(new Task(null, "test2", "test2", 10, true));

		mongoTemplate.save(new Task(null, "test", "test", 9, false)); // Should Not Be Found

		List<Task> tasks = repository.findByDone(true);
		assertThat(tasks).containsExactly(taskShouldBeFound1, taskShouldBeFound2);
	}

	@Test
	public void testFindTasksByPriorityAndDone() {
		Task taskShouldBeFound1 = mongoTemplate.save(new Task(null, "test1", "test1", 10, true));
		Task taskShouldBeFound2 = mongoTemplate.save(new Task(null, "test2", "test2", 10, true));

		mongoTemplate.save(new Task(null, "test", "test", 9, true)); // Should Not Be Found
		mongoTemplate.save(new Task(null, "test", "test", 10, false)); // Should Not Be Found

		List<Task> tasks = repository.findByPriorityAndDone(10, true);
		assertThat(tasks).containsExactly(taskShouldBeFound1, taskShouldBeFound2);
	}

	@Test
	public void testFindAllTasksWithHighPriority() {
		Task taskShouldBeFound1 = mongoTemplate.save(new Task(null, "test1", "test1", 10, true));
		Task taskShouldBeFound2 = mongoTemplate.save(new Task(null, "test2", "test2", 8, false));

		mongoTemplate.save(new Task(null, "test", "test", 7, true)); // Should Not Be Found

		List<Task> tasks = repository.findAllTasksWithHighPriority(7);
		assertThat(tasks).containsExactly(taskShouldBeFound1, taskShouldBeFound2);
	}

	@Test
	public void testFindAllTasksWithHighPriorityAndDone() {
		Task taskShouldBeFound1 = mongoTemplate.save(new Task(null, "test1", "test1", 10, false));
		Task taskShouldBeFound2 = mongoTemplate.save(new Task(null, "test2", "test2", 9, false));

		mongoTemplate.save(new Task(null, "test", "test", 8, true)); // Should Not Be Found

		List<Task> tasks = repository.findByPriorityGreaterThanAndDone(7, false);
		assertThat(tasks).containsExactly(taskShouldBeFound1, taskShouldBeFound2);
	}

	@Test
	public void testFindTasksByPriorityDescendingOrder() {
		Task taskMedium = mongoTemplate.save(new Task(null, "Medio", "desc", 5, true));
		Task taskLow = mongoTemplate.save(new Task(null, "Basso", "desc", 1, true));
		Task taskHigh = mongoTemplate.save(new Task(null, "Alto", "desc", 10, true));

		List<Task> tasks = repository.findAllByOrderByPriorityDesc();

		// containsExactly verifica anche l'ordine
		assertThat(tasks).containsExactly(taskHigh, taskMedium, taskLow);
	}
}
