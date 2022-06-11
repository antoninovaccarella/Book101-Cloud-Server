package com.antonino.book101server.controllers;

import com.antonino.book101server.exceptions.BadOrderException;
import com.antonino.book101server.exceptions.OutOfStockException;
import com.antonino.book101server.exceptions.ProductNotFoundException;
import com.antonino.book101server.models.Order;
import com.antonino.book101server.payload.response.MessageResponse;
import com.antonino.book101server.services.OrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping("/api/order")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @PreAuthorize("(hasRole('CUSTOMER') and #username == principal.username) or hasRole('ADMIN')")
    @PostMapping(value = "/buy/{username}", produces = "application/json", consumes = "application/json")
    public ResponseEntity<?> buyCart(@RequestBody @Valid Order order, @PathVariable("username") String username) {
        try {
            return ResponseEntity.ok(orderService.purchaseOrder(order, username));
        } catch (UsernameNotFoundException | ProductNotFoundException | BadOrderException | OutOfStockException e) {
            return ResponseEntity.badRequest().body(new MessageResponse(e.toString()));
        }
    }

    @PreAuthorize("(hasRole('CUSTOMER') and #username == principal.username) or hasRole('ADMIN')")
    @GetMapping(value = "/{username}", produces = "application/json")
    public ResponseEntity<List<Order>> getUserOrders(@PathVariable("username") String username) {
        return ResponseEntity.ok(orderService.getUserOrders(username));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping(value = "/remove/{id}", produces = "application/json")
    public ResponseEntity removeOrder(@PathVariable("id") Long id) {
        if(orderService.getOrderById(id)!=null) {
            orderService.deleteOrderById(id);
            return ResponseEntity.ok(new MessageResponse("Ordine " + id + " rimosso con successo."));
        }
        return ResponseEntity.badRequest().body("Ordine " + id + " inesistente.");




    }
}
