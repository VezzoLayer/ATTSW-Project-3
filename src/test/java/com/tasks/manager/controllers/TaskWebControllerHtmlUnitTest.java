package com.tasks.manager.controllers;

import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import org.htmlunit.WebClient;
import org.htmlunit.html.HtmlPage;
import org.htmlunit.html.HtmlTable;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.junit4.SpringRunner;

import com.tasks.manager.model.Task;
import com.tasks.manager.services.TaskService;

@RunWith(SpringRunner.class)
@WebMvcTest(controllers = TaskWebController.class)
public class TaskWebControllerHtmlUnitTest {

	@Autowired
	private WebClient webClient;

	@MockitoBean
	private TaskService taskService;

	@Test
	public void testHomePageTitle() throws Exception {
		HtmlPage page = webClient.getPage("/");

		assertThat(page.getTitleText()).isEqualTo("Tasks");
	}

	@Test
	public void testHomePageWithNoTasks() throws Exception {
		when(taskService.getAllTasks()).thenReturn(emptyList());

		HtmlPage page = this.webClient.getPage("/");

		assertThat(page.getBody().getTextContent()).contains("No Tasks");
	}

	@Test
	public void testHomePageWithTasksShouldShowThemInATable() throws Exception {
		when(taskService.getAllTasks()).thenReturn(
				asList(new Task("t1", "title1", "descr1", 8, true), new Task("t2", "title2", "descr2", 10, true)));

		HtmlPage page = this.webClient.getPage("/");

		assertThat(page.getBody().getTextContent()).doesNotContain("No Tasks");

		HtmlTable table = page.getHtmlElementById("tasks_table");

		String expectedTableContent = """
				Tasks
				ID Title Description Priority Done
				t1 title1 descr1 8 true
				t2 title2 descr2 10 true""";

		// replace /t con spazi bianchi e rimuove /r
		assertThat(table.asNormalizedText().replace("\t", " ").replace("\r", "")).isEqualTo(expectedTableContent);
	}
}
