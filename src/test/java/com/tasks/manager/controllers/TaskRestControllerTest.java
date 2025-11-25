package com.tasks.manager.controllers;

import static java.util.Arrays.asList;
import static org.hamcrest.CoreMatchers.is;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import com.tasks.manager.model.Task;
import com.tasks.manager.services.TaskService;

@RunWith(SpringRunner.class)
@WebMvcTest(controllers = TaskRestController.class)
public class TaskRestControllerTest {

	@Autowired
	private MockMvc mvc;

	@MockitoBean
	private TaskService taskService;

	@Test
	public void testAllTasksWhenEmpty() throws Exception {
		this.mvc.perform(get("/api/tasks").accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
				.andExpect(content().json("[]"));
	}

	@Test
	public void testAllTasksWhenNotEmpty() throws Exception {
		when(taskService.getAllTasks()).thenReturn(
				asList(new Task("t1", "title1", "descr1", 5, true), new Task("t2", "title2", "descr2", 10, true)));

		this.mvc.perform(get("/api/tasks").accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
				.andExpect(jsonPath("$[0].id", is("t1"))).andExpect(jsonPath("$[0].title", is("title1")))
				.andExpect(jsonPath("$[0].description", is("descr1"))).andExpect(jsonPath("$[0].priority", is(5)))
				.andExpect(jsonPath("$[0].done", is(true))).andExpect(jsonPath("$[1].id", is("t2")))
				.andExpect(jsonPath("$[1].title", is("title2"))).andExpect(jsonPath("$[1].description", is("descr2")))
				.andExpect(jsonPath("$[1].priority", is(10))).andExpect(jsonPath("$[1].done", is(true)));
	}

	@Test
	public void testAllTasksOrderedByDescendentPriorityWhenEmpty() throws Exception {
		this.mvc.perform(get("/api/tasks/ordered").accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
				.andExpect(content().json("[]"));
	}

	@Test
	public void testAllTasksOrderedByDescendentPriorityWhenNotEmpty() throws Exception {
		when(taskService.getAllTasksByDescendentPriority()).thenReturn(
				asList(new Task("t1", "High", "High Desc", 10, true), new Task("t2", "Low", "Low Desc", 5, true)));

		this.mvc.perform(get("/api/tasks/ordered").accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
				.andExpect(jsonPath("$[0].id", is("t1"))).andExpect(jsonPath("$[0].title", is("High")))
				.andExpect(jsonPath("$[0].description", is("High Desc"))).andExpect(jsonPath("$[0].priority", is(10)))
				.andExpect(jsonPath("$[0].done", is(true))).andExpect(jsonPath("$[1].id", is("t2")))
				.andExpect(jsonPath("$[1].title", is("Low"))).andExpect(jsonPath("$[1].description", is("Low Desc")))
				.andExpect(jsonPath("$[1].priority", is(5))).andExpect(jsonPath("$[1].done", is(true)));
	}

	@Test
	public void testOneTaskByIdWithExistingTask() throws Exception {
		when(taskService.getTaskById(anyString())).thenReturn(new Task("t1", "title", "descr", 10, true));

		this.mvc.perform(get("/api/tasks/t1").accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
				.andExpect(jsonPath("$.id", is("t1"))).andExpect(jsonPath("$.title", is("title")))
				.andExpect(jsonPath("$.description", is("descr"))).andExpect(jsonPath("$.priority", is(10)))
				.andExpect(jsonPath("$.done", is(true)));
	}

	@Test
	public void testOneTaskByIdWithNotFoundTask() throws Exception {
		when(taskService.getTaskById(anyString())).thenReturn(null);

		this.mvc.perform(get("/api/tasks/t1").accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
				.andExpect(content().string(""));
	}

	@Test
	public void testPostTask() throws Exception {
		Task requestBodyTask = new Task(null, "title", "descr", 10, true);

		when(taskService.insertNewTask(requestBodyTask)).thenReturn(new Task("t1", "title", "descr", 10, true));

		this.mvc.perform(post("/api/tasks/new").contentType(MediaType.APPLICATION_JSON)
				.content("{\"title\":\"title\", \"description\":\"descr\", \"priority\":10, \"done\":\"true\"}")
				.accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk()).andExpect(jsonPath("$.id", is("t1")))
				.andExpect(jsonPath("$.title", is("title"))).andExpect(jsonPath("$.description", is("descr")))
				.andExpect(jsonPath("$.priority", is(10))).andExpect(jsonPath("$.done", is(true)));
	}
}
