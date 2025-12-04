package com.tasks.manager;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("mongodb")
class TasksManagerApplicationTests {

	@Test
	void contextLoads() {
		// Verifica che il contesto di spring si avvii correttamente
	}

}
