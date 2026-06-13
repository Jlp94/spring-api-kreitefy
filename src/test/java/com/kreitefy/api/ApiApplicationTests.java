package com.kreitefy.api;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.modulith.core.ApplicationModules;

@SpringBootTest
class ApiApplicationTests {

	@Test
	void contextLoads() {
	}

	@Test
	void verificarModulos() {
		ApplicationModules.of(ApiApplication.class)
				.verify();
	}

}
