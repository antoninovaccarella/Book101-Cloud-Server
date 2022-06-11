package com.antonino.book101server.controllers;

import com.antonino.book101server.exceptions.ProductNotFoundException;
import com.antonino.book101server.models.CartItem;
import com.antonino.book101server.models.User;
import com.antonino.book101server.payload.response.MessageResponse;
import com.antonino.book101server.services.CartService;
import com.antonino.book101server.services.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Optional;

@Slf4j
@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping("/api/cart")
public class CartController {

    @Autowired
    private UserService userService;

    @Autowired
    private CartService cartService;

    @PreAuthorize("(hasRole('CUSTOMER') and #username == principal.username) or hasRole('ADMIN')")
    @GetMapping(value = "/{username}", produces = "application/json")
    public ResponseEntity<?> getCart(@PathVariable("username") String username) {
        Optional<User> optionalUser = userService.getUserbyUsername(username);
        return (optionalUser.isPresent())
                ? ResponseEntity.ok(cartService.getCartByUser(optionalUser.get()))
                : ResponseEntity.badRequest().body(new MessageResponse("Utente inesistente"));
    }

    @PreAuthorize("(hasRole('CUSTOMER') and #username == principal.username) or hasRole('ADMIN')")
    @GetMapping(value = "/items/{username}", produces = "application/json")
    public ResponseEntity<?> getCartItems(@PathVariable("username") String username) {
        Optional<User> optionalUser = userService.getUserbyUsername(username);
        return (optionalUser.isPresent())
                ? ResponseEntity.ok(cartService.getCartItemsByUser(optionalUser.get()))
                : ResponseEntity.badRequest().body(new MessageResponse("Utente inesistente"));
    }

    @PreAuthorize("(hasRole('CUSTOMER') and #username == principal.username) or hasRole('ADMIN')")
    @GetMapping(value = "/subtotal/{username}", produces = "application/json")
    public ResponseEntity<?> getSubtotal(@PathVariable("username") String username) {
        Optional<User> optionalUser = userService.getUserbyUsername(username);
        return (optionalUser.isPresent())
                ? ResponseEntity.ok(cartService.getSubtotal(optionalUser.get()))
                : ResponseEntity.badRequest().body(new MessageResponse("Utente inesistente"));
    }

    @PreAuthorize("(hasRole('CUSTOMER') and #username == principal.username) or hasRole('ADMIN')")
    @PostMapping(value = "/items/{username}", produces = "application/json", consumes = "application/json")
    public ResponseEntity<?> updateCart(@RequestBody @Valid CartItem cartItem, @PathVariable("username") String username) {
        Optional<User> optionalUser = userService.getUserbyUsername(username);
        try {
            return (optionalUser.isPresent())
                    ? ResponseEntity.ok(cartService.updateCart(cartItem, optionalUser.get()).getCartItems())
                    : ResponseEntity.badRequest().body(new MessageResponse("Utente inesistente"));
        } catch (ProductNotFoundException e) {
            return ResponseEntity.badRequest().body(new MessageResponse(e.toString()));
        }
    }

    @PreAuthorize("(hasRole('CUSTOMER') and #username == principal.username) or hasRole('ADMIN')")
    @PostMapping(value = "/clear/{username}", produces = "application/json")
    public ResponseEntity<?> clearCart(@PathVariable("username") String username) {
        Optional<User> optionalUser = userService.getUserbyUsername(username);
        return (optionalUser.isPresent())
                ? ResponseEntity.ok(cartService.createCart(optionalUser.get()))
                : ResponseEntity.badRequest().body(new MessageResponse("Utente inesistente"));
    }
}
