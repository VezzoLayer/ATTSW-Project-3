package com.tasks.manager;

import static org.assertj.core.api.Assertions.assertThat;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import io.github.bonigarcia.wdm.WebDriverManager;

public class TaskWebControllerE2E { // NOSONAR not a standard testcase name

	private static final Logger LOGGER = LoggerFactory.getLogger(TaskWebControllerE2E.class);

	private static int port = Integer.parseInt(System.getProperty("server.port", "8080"));

	private static String baseUrl = "http://localhost:" + port;

	private WebDriver driver;

	@BeforeClass
	public static void setupClass() {
		// setup Chrome Driver
		WebDriverManager.chromedriver().setup();
	}

	@Before
	public void setup() {
		baseUrl = "http://localhost:" + port;
		driver = new ChromeDriver();
	}

	@After
	public void teardown() {
		driver.quit();
	}

	@Test
	public void testCreateNewTask() {
		driver.get(baseUrl);

		driver.findElement(By.cssSelector("a[href*='/new")).click();

		driver.findElement(By.name("title")).sendKeys("new task");
		driver.findElement(By.name("description")).sendKeys("new descr");
		driver.findElement(By.name("priority")).sendKeys("10");

		WebElement doneField = driver.findElement(By.name("done"));
		doneField.clear();
		doneField.sendKeys("false");

		driver.findElement(By.name("btn_submit")).click();

		assertThat(driver.findElement(By.id("tasks_table")).getText()).contains("new task", "new descr", "10", "false");
	}

	@Test
	public void testEditTask() throws JSONException {
		String id = postTask("title to edit", "descr to edit", 8, false);

		driver.get(baseUrl);
		driver.findElement(By.cssSelector("a[href*='/edit/" + id + "']")).click();

		final WebElement titleField = driver.findElement(By.name("title"));
		titleField.clear();
		titleField.sendKeys("modified title");

		final WebElement descriptionField = driver.findElement(By.name("description"));
		descriptionField.clear();
		descriptionField.sendKeys("modified description");

		final WebElement priorityField = driver.findElement(By.name("priority"));
		priorityField.clear();
		priorityField.sendKeys("10");

		final WebElement doneField = driver.findElement(By.name("done"));
		doneField.clear();
		doneField.sendKeys("true");

		driver.findElement(By.name("btn_submit")).click();

		assertThat(driver.findElement(By.id("tasks_table")).getText()).contains("modified title",
				"modified description", "10", "true");
	}

	private String postTask(String title, String description, int priority, boolean done) throws JSONException {
		JSONObject body = new JSONObject();

		body.put("title", title);
		body.put("description", description);
		body.put("priority", priority);
		body.put("done", done);

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);

		HttpEntity<String> entity = new HttpEntity<String>(body.toString(), headers);

		ResponseEntity<String> answer = new RestTemplate().postForEntity(baseUrl + "/api/tasks/new", entity,
				String.class);

		LOGGER.debug("answer for POST task: {}", answer);

		return new JSONObject(answer.getBody()).get("id").toString();
	}
}
