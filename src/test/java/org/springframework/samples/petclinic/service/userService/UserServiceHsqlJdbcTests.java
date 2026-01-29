package org.springframework.samples.petclinic.service.userService;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles({"hsqldb", "spring-data-jpa"})
public class UserServiceHsqlJdbcTests {
}
