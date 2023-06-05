package com.antonino.book101server.controllers;

import com.antonino.book101server.models.User;
import com.antonino.book101server.payload.response.MessageResponse;
import com.antonino.book101server.services.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Slf4j
@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping("/api/user")
public class UserController {
    @Autowired
    private UserService userService;

    @PreAuthorize("(hasRole('CUSTOMER') and #username == principal.username) or hasRole('ADMIN')")
    @GetMapping(value = "/{username}", produces = "application/json")
    public ResponseEntity<?> getUser(@PathVariable("username") String username) {
        Optional<User> optionalUser = userService.getUserbyUsername(username);
        return (optionalUser.isPresent())
                ? ResponseEntity.ok(optionalUser.get())
                : ResponseEntity.badRequest().body(new MessageResponse("Utente inesistente"));
    }
}
