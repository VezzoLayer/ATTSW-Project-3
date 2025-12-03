package com.tasks.manager;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import com.tasks.manager.model.Task;
import com.tasks.manager.repositories.TaskRepository;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@ActiveProfiles("mongodb")
public class TaskWebControllerIT {

	@Autowired
	private TaskRepository taskRepository;

	@LocalServerPort
	private int port;

	private WebDriver driver;

	private String baseUrl;

	@Before
	public void setup() {
		baseUrl = "http://localhost:" + port;
		driver = new HtmlUnitDriver();

		taskRepository.deleteAll();
	}

	@After
	public void teardown() {
		driver.quit();
	}

	@Test
	public void testHomePageWhenTasksArePresent() {
		Task task = taskRepository.save(new Task(null, "title", "descr", 10, true));
		driver.get(baseUrl);

		assertThat(driver.findElement(By.id("tasks_table")).getText()).contains("title", "descr", "10", "true", "Edit",
				"Delete");

		driver.findElement(By.cssSelector("a[href*='/edit/" + task.getId() + "']"));
		driver.findElement(By.cssSelector("form[action*='/delete/" + task.getId() + "']"));
		driver.findElement(By.cssSelector("a[href*='/ordered']"));
	}

	@Test
	public void testEditPageNewTask() {
		driver.get(baseUrl + "/new");

		driver.findElement(By.name("title")).sendKeys("new title");
		driver.findElement(By.name("description")).sendKeys("new descr");
		driver.findElement(By.name("priority")).sendKeys("10");

		WebElement doneField = driver.findElement(By.name("done"));
		doneField.clear();
		doneField.sendKeys("true");

		driver.findElement(By.name("btn_submit")).click();

		assertThat(taskRepository.findByTitle("new title")).usingRecursiveComparison().ignoringFields("id")
				.isEqualTo(new Task(null, "new title", "new descr", 10, true));
	}
}
