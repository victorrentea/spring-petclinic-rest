package org.springframework.samples.petclinic.rest.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.samples.petclinic.mapper.UserMapper;
import org.springframework.samples.petclinic.model.User;
import org.springframework.samples.petclinic.rest.dto.UserDto;
import org.springframework.web.bind.annotation.*;
import org.springframework.samples.petclinic.service.UserService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@Tag(name = "user", description = "Endpoints related to users.")
@PreAuthorize("hasRole(@roles.ADMIN)")
public class UserRestController {
    private final UserService userService;
    private final UserMapper userMapper;

    @PostMapping
    public ResponseEntity<UserDto> addUser(@RequestBody @Validated UserDto userDto) {
        User user = userMapper.toUser(userDto);
        userService.saveUser(user);
        return ResponseEntity.created(UriComponentsBuilder.fromPath("/api/users/{username}")
                .buildAndExpand(user.getUsername()).toUri())
            .body(userMapper.toUserDto(user));
    }
}
