package ru.varnavskii.nexign;

import org.springframework.boot.SpringApplication;

public class TestNexignApplication {

	public static void main(String[] args) {
		SpringApplication.from(NexignApplication::main).with(TestcontainersConfiguration.class).run(args);
	}

}
