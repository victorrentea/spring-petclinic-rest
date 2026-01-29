package org.springframework.samples.petclinic.service.clinicService;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles({"spring-data-jpa", "hsqldb"})
public class ClinicServiceJpaTests {

}
