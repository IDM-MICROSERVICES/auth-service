package com.idm.msvc.auth_service;

import com.idm.msvc.auth_service.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

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

	@MockitoBean
	private UserDetailsService userDetailsService;

	// Mockea el repositorio de usuarios si algún bean lo requiere
	@MockitoBean
	private UserRepository userRepository;

	@Test
	void contextLoads() {
	}
}