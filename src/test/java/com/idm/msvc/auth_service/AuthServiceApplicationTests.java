package com.idm.msvc.auth_service;

import com.idm.msvc.auth_service.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;

@SpringBootTest
@ActiveProfiles("test")
@TestPropertySource(properties = {
		"spring.datasource.url=jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1;MODE=PostgreSQL",
		"spring.datasource.driver-class-name=org.h2.Driver",
		"spring.jpa.hibernate.ddl-auto=create-drop",
		"eureka.client.enabled=false",
		"spring.cloud.config.enabled=false"
})
class AuthServiceApplicationTests {

	@TestConfiguration
	static class TestConfig {

		@Bean
		public UserDetailsService userDetailsService() {
			return username -> null;
		}

		@Bean
		public UserRepository userRepository() {
			return null;
		}
	}

	@Test
	void contextLoads() {
	}
}
