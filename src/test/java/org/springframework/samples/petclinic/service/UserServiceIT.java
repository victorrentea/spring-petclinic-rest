package org.springframework.samples.petclinic.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.samples.petclinic.PetClinicApplication;
import org.springframework.samples.petclinic.model.User;

import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest(classes = PetClinicApplication.class)
class UserServiceIT {

    @Autowired
    UserService userService;

    @Test
    void saveUser_throws_when_roles_null_or_empty() {
        User userWithNullRoles = new User();
        userWithNullRoles.setUsername("no-roles");
        userWithNullRoles.setPassword("pw");
        userWithNullRoles.setEnabled(true);

        assertThrows(IllegalArgumentException.class, () -> userService.saveUser(userWithNullRoles));

        User userWithEmptyRoles = new User();
        userWithEmptyRoles.setUsername("empty-roles");
        userWithEmptyRoles.setPassword("pw");
        userWithEmptyRoles.setEnabled(true);
        userWithEmptyRoles.setRoles(new HashSet<>());

        assertThrows(IllegalArgumentException.class, () -> userService.saveUser(userWithEmptyRoles));
    }
}
