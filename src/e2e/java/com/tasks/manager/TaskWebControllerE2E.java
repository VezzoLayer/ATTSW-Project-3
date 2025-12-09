package com.tasks.manager;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

import io.github.bonigarcia.wdm.WebDriverManager;

public class TaskWebControllerE2E { // NOSONAR not a standard testcase name

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
}
