package com.idm.msvc.auth_service;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import javax.sql.DataSource;

@SpringBootTest
class AuthServiceApplicationTests {

	@MockitoBean
	private DataSource dataSource;

	@Test
	void contextLoads() {
	}

}
