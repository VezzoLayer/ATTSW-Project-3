package com.tasks.manager.mongo;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.test.context.junit4.SpringRunner;

import com.tasks.manager.model.Task;

@DataMongoTest
@RunWith(SpringRunner.class)
public class TaskMongoTest {

	@Autowired
	private MongoTemplate mongoTemplate;

	@Before
	public void setup() {
		// DataMongoTest non sono transactional, db pulito prima di ogni test
		mongoTemplate.remove(new Query(), Task.class);
	}

	@Test
	public void testTaskMongoMapping() {
		Task savedTask = mongoTemplate.save(new Task(null, "title", "description", 10, true));

		assertThat(savedTask.getTitle()).isEqualTo("title");
		assertThat(savedTask.getDescription()).isEqualTo("description");
		assertThat(savedTask.getPriority()).isEqualTo(10);
		assertThat(savedTask.isDone()).isTrue();

		assertThat(savedTask.getId()).isNotNull();
		assertThat(savedTask.getId()).isNotEmpty();

		// Per vedere identifier generato
		LoggerFactory.getLogger(TaskMongoTest.class).info("Saved: {}", savedTask);
	}
}
