package com.antonino.book101server.controllers;

import com.antonino.book101server.payload.request.SigninRequest;
import com.antonino.book101server.payload.request.SignupRequest;
import com.antonino.book101server.payload.response.MessageResponse;
import com.antonino.book101server.services.UserService;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Slf4j
@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private UserService userService;

    @PostMapping(value = "/signin", consumes = {"application/json"})
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody SigninRequest signinRequest) {
        try {
            return ResponseEntity.ok(userService.authenticateUser(signinRequest));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new MessageResponse("Credenziali errate"));
        }
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping(value = "/signup/admin", consumes = {"application/json"})
    public ResponseEntity<MessageResponse> registerAdmin(@Valid @RequestBody SignupRequest signUpRequest) {
        if (!signUpRequest.getRole().contains("admin")) {
            return ResponseEntity.badRequest().body(new MessageResponse("Accesso negato"));
        }
        return getMessageResponseResponseEntity(signUpRequest);
    }

    @PostMapping(value = "/signup/customer", consumes = {"application/json"})
    public ResponseEntity<MessageResponse> registerCustomer(@Valid @RequestBody SignupRequest signUpRequest) {
        if (signUpRequest.getRole() != null && signUpRequest.getRole().contains("admin")) {
            return ResponseEntity.badRequest().body(new MessageResponse("Accesso negato"));
        }
        return getMessageResponseResponseEntity(signUpRequest);
    }

    @NotNull
    private ResponseEntity<MessageResponse> getMessageResponseResponseEntity(@RequestBody @Valid SignupRequest signUpRequest) {
        if (userService.checkUsername(signUpRequest.getUsername())) {
            return ResponseEntity.badRequest().body(new MessageResponse("Username non disponibile"));
        }
        if (userService.checkEmail(signUpRequest.getEmail())) {
            return ResponseEntity.badRequest().body(new MessageResponse("Email già in uso"));
        }
        userService.registerUser(signUpRequest);
        return ResponseEntity.ok(new MessageResponse("Utente registrato con successo"));
    }

}
