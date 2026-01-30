package org.springframework.samples.petclinic.config;


import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Collections;

/**
 * Java config for springdoc-openapi API documentation library
 */
@Configuration
public class SwaggerConfig {

    @Bean
    OpenAPI customOpenAPI() {
        return new OpenAPI()
            .components(new Components())
            .info(new Info()
                .title("REST Petclinic backend API documentation")
                .version("1.0")
                .termsOfService("https://github.com/spring-petclinic/spring-petclinic-rest/blob/master/terms.txt")
                .description(
                    "This is the REST API documentation of the Spring Petclinic backend. " +
                        "If authentication is enabled, use admin/admin when calling the APIs")
                .license(swaggerLicense())
                .contact(swaggerContact()));
    }

    private Contact swaggerContact() {
        Contact petclinicContact = new Contact();
        petclinicContact.setName("Vitaliy Fedoriv");
        petclinicContact.setEmail("vitaliy.fedoriv@gmail.com");
        petclinicContact.setUrl("https://github.com/spring-petclinic/spring-petclinic-rest");
        return petclinicContact;
    }

    private License swaggerLicense() {
        License petClinicLicense = new License();
        petClinicLicense.setName("Apache 2.0");
        petClinicLicense.setUrl("http://www.apache.org/licenses/LICENSE-2.0");
        petClinicLicense.setExtensions(Collections.emptyMap());
        return petClinicLicense;
    }

}
