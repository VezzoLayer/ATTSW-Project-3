package com.tasks.manager.controllers;

import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.htmlunit.WebClient;
import org.htmlunit.html.HtmlForm;
import org.htmlunit.html.HtmlPage;
import org.htmlunit.html.HtmlTable;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.junit4.SpringRunner;

import com.tasks.manager.dto.TaskDTO;
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
		assertThat(page.getAnchorByText("Sort by Priority").getHrefAttribute()).isEqualTo("/ordered");

		HtmlTable table = page.getHtmlElementById("tasks_table");

		String expectedTableContent = """
				Tasks
				ID Title Description Priority Done Action
				t1 title1 descr1 8 true Delete
				t2 title2 descr2 10 true Delete""";

		// replace /t con spazi bianchi, rimuove /r, più spazi bianchi diventano 1
		assertThat(table.asNormalizedText().replace("\t", " ").replace("\r", "").replaceAll(" +", " ").trim())
				.isEqualTo(expectedTableContent);

		page.getHtmlElementById("btn_delete_t1");
		page.getHtmlElementById("btn_delete_t2");
	}

	public void testHomePageShowsTasksInDescendingPriorityOrder() throws Exception {
		when(taskService.getAllTasksByDescendentPriority()).thenReturn(
				asList(new Task("t2", "title2", "descr2", 10, true), new Task("t1", "title1", "descr1", 5, false)));

		HtmlPage page = this.webClient.getPage("/ordered");

		assertThat(page.getBody().getTextContent()).doesNotContain("No Tasks");
		assertThat(page.getAnchorByText("Sort by Priority").getHrefAttribute()).isEqualTo("/");

		HtmlTable table = page.getHtmlElementById("tasks_table");

		String expectedTableContent = """
				Tasks
				ID Title Description Priority Done Action
				t2 title2 descr2 10 true Delete
				t1 title1 descr1 5 false Delete""";

		// replace /t con spazi bianchi, rimuove /r, più spazi bianchi diventano 1
		assertThat(table.asNormalizedText().replace("\t", " ").replace("\r", "").replaceAll(" +", " ").trim())
				.isEqualTo(expectedTableContent);

		page.getHtmlElementById("btn_delete_t1");
		page.getHtmlElementById("btn_delete_t2");
	}

	@Test
	public void testEditTaskPageTitle() throws Exception {
		HtmlPage page = webClient.getPage("/edit/t1");

		assertThat(page.getTitleText()).isEqualTo("Edit Task");
	}

	@Test
	public void testEditNonExistingTask() throws Exception {
		when(taskService.getTaskById("t1")).thenReturn(null);

		HtmlPage page = this.webClient.getPage("/edit/t1");

		assertThat(page.getBody().getTextContent()).contains("No task found with id: t1");
	}

	@Test
	public void testEditExistingTask() throws Exception {
		when(taskService.getTaskById("t1")).thenReturn(new Task("t1", "original title", "original descr", 0, false));

		HtmlPage page = this.webClient.getPage("/edit/t1");
		final HtmlForm form = page.getFormByName("task_form");

		form.getInputByValue("original title").setValueAttribute("mod title");
		form.getInputByValue("original descr").setValueAttribute("mod descr");
		form.getInputByValue("0").setValueAttribute("10");
		form.getInputByValue("false").setValueAttribute("true");

		form.getButtonByName("btn_submit").click();

		verify(taskService).updateTaskById("t1", new TaskDTO("t1", "mod title", "mod descr", 10, true));
	}

	@Test
	public void testEditNewTask() throws Exception {
		HtmlPage page = this.webClient.getPage("/new");

		final HtmlForm form = page.getFormByName("task_form");

		form.getInputByName("title").setValueAttribute("new title");
		form.getInputByName("description").setValueAttribute("new description");
		form.getInputByName("priority").setValueAttribute("10");
		form.getInputByName("done").setValueAttribute("true");

		form.getButtonByName("btn_submit").click();

		verify(taskService).insertNewTask(new TaskDTO(null, "new title", "new description", 10, true));
	}

	@Test
	public void testDeleteTask() throws Exception {
		when(taskService.getAllTasks()).thenReturn(asList(new Task("t1", "Title", "Desc", 10, false)));

		HtmlPage page = this.webClient.getPage("/");
		page.getHtmlElementById("btn_delete_t1").click();

		verify(taskService).deleteTaskById("t1");
	}
}
