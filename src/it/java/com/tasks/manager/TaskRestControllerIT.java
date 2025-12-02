package com.tasks.manager;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import com.tasks.manager.model.Task;
import com.tasks.manager.repositories.TaskRepository;

import io.restassured.RestAssured;
import io.restassured.response.Response;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@ActiveProfiles("mongodb")
public class TaskRestControllerIT {

	@Autowired
	private TaskRepository taskRepository;

	@LocalServerPort
	private int port;

	@Before
	public void setup() {
		RestAssured.port = port;

		taskRepository.deleteAll();
	}

	@Test
	public void testNewTask() {
		Response response = given().contentType(MediaType.APPLICATION_JSON_VALUE)
				.body(new Task(null, "title", "descr", 10, true)).when().post("/api/tasks/new");

		Task savedTask = response.getBody().as(Task.class);

		assertThat(taskRepository.findById(savedTask.getId())).contains(savedTask);
	}
}
