package com.spring.blog;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

@SpringBootTest
@TestPropertySource(properties = "jasypt.encryptor.password=blog")
class BlogApplicationTests {

	@Test
	void contextLoads() {
	}

}
