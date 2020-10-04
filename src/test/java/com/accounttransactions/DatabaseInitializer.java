package com.accounttransactions;

import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.test.context.support.TestPropertySourceUtils;
import org.testcontainers.containers.MySQLContainer;

public class DatabaseInitializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {

	private static final MySQLContainer<?> CONTAINER = new MySQLContainer<>("mysql:8.0.21");

	@Override
	public void initialize(ConfigurableApplicationContext configurableApplicationContext) {

		CONTAINER.start();

		TestPropertySourceUtils.addInlinedPropertiesToEnvironment(configurableApplicationContext,
				"spring.datasource.url=" + CONTAINER.getJdbcUrl());

		TestPropertySourceUtils.addInlinedPropertiesToEnvironment(configurableApplicationContext,
				"spring.datasource.username=" + CONTAINER.getUsername());

		TestPropertySourceUtils.addInlinedPropertiesToEnvironment(configurableApplicationContext,
				"spring.datasource.password=" + CONTAINER.getPassword());

	}
}
