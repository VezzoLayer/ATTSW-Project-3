package com.tasks.manager.repositories;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Collection;

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
}
