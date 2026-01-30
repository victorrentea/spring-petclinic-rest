package org.springframework.samples.petclinic.rest.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.samples.petclinic.mapper.UserMapper;
import org.springframework.samples.petclinic.model.User;
import org.springframework.samples.petclinic.rest.dto.UserDto;
import org.springframework.web.bind.annotation.*;
import org.springframework.samples.petclinic.service.UserService;
import org.springframework.security.access.prepost.PreAuthorize;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@Tag(name = "user", description = "Endpoints related to users.")
public class UserRestController {

    private final UserService userService;
    private final UserMapper userMapper;

    @PreAuthorize( "hasRole(@roles.ADMIN)" )
    @Operation(operationId = "addUser", summary = "Create a user")
    @PostMapping(consumes = "application/json", produces = "application/json")
    public ResponseEntity<UserDto> addUser(@RequestBody UserDto userDto) {
        HttpHeaders headers = new HttpHeaders();
        User user = userMapper.toUser(userDto);
        userService.saveUser(user);
        return new ResponseEntity<>(userMapper.toUserDto(user), headers, HttpStatus.CREATED);
    }
}
