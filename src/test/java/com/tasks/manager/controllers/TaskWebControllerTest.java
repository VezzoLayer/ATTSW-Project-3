package com.tasks.manager.controllers;

import static java.util.Arrays.asList;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import java.util.Collections;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.ModelAndViewAssert;
import org.springframework.test.web.servlet.MockMvc;

import com.tasks.manager.model.Task;
import com.tasks.manager.services.TaskService;

@RunWith(SpringRunner.class)
@WebMvcTest(controllers = TaskWebController.class)
public class TaskWebControllerTest {

	@Autowired
	private MockMvc mvc;

	@MockitoBean
	private TaskService taskService;

	@Test
	public void testStatus200() throws Exception {
		mvc.perform(get("/")).andExpect(status().is2xxSuccessful());
	}

	@Test
	public void testReturnHomeView() throws Exception {
		ModelAndViewAssert.assertViewName(mvc.perform(get("/")).andReturn().getModelAndView(), "index");
	}

	@Test
	public void testHomeViewShowsTasksWhenThereAreSome() throws Exception {
		List<Task> tasks = asList(new Task("t1", "test", "test", 10, true));

		when(taskService.getAllTasks()).thenReturn(tasks);

		mvc.perform(get("/")).andExpect(view().name("index")).andExpect(model().attribute("tasks", tasks))
				.andExpect(model().attribute("message", ""));
	}

	@Test
	public void testHomeViewShowsMessageWhenThereAreNoTasks() throws Exception {
		when(taskService.getAllTasks()).thenReturn(Collections.emptyList());

		mvc.perform(get("/")).andExpect(view().name("index"))
				.andExpect(model().attribute("tasks", Collections.emptyList()))
				.andExpect(model().attribute("message", "No task to show"));
	}
}
