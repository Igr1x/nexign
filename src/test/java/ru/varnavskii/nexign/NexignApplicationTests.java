package ru.varnavskii.nexign;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

@Import(TestcontainersConfiguration.class)
@SpringBootTest
class NexignApplicationTests {

	@Test
	void contextLoads() {
	}

}
