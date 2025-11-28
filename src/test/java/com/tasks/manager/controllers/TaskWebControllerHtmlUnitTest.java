package com.tasks.manager.controllers;

import static org.assertj.core.api.Assertions.assertThat;

import org.htmlunit.WebClient;
import org.htmlunit.html.HtmlPage;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.junit4.SpringRunner;

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
}
