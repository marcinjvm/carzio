package com.carzio.app.controllers;

import com.carzio.app.models.User;
import com.carzio.app.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class UserController {
    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/users")
    List<User> findAll() {
        return userService.findAll();
    }

    @GetMapping("/user")
    User findById(@RequestParam String id) {
        return userService.findById(id);
    }
}
