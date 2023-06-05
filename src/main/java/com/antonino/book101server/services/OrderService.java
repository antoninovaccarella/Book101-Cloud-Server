package com.antonino.book101server.services;

import com.antonino.book101server.exceptions.BadOrderException;
import com.antonino.book101server.exceptions.OutOfStockException;
import com.antonino.book101server.exceptions.ProductNotFoundException;
import com.antonino.book101server.models.*;
import com.antonino.book101server.repositories.OrderItemsRepository;
import com.antonino.book101server.repositories.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderItemsRepository orderItemsRepository;

    @Autowired
    private CartService cartService;

    @Autowired
    private ProductService productService;

    @Autowired
    private com.antonino.book101server.services.UserService userService;

    @Transactional(readOnly = false)
    public Order purchaseOrder(Order order, String username) throws ProductNotFoundException, BadOrderException, OutOfStockException {
        Optional<User> userFromDb = userService.getUserbyUsername(username);
        ShoppingCart shoppingCart;
        int totalAmount = 0;
        if (userFromDb.isPresent()) {
            shoppingCart = cartService.getCartByUser(userFromDb.get());
            if(shoppingCart!=null) {
                for(CartItem cartItem : shoppingCart.getCartItems()){
                    if(cartItem.getQuantity()>cartItem.getProduct().getStock())
                        throw new OutOfStockException("Prodotto esaurito");
                    totalAmount += cartItem.getProduct().getPrice() * cartItem.getQuantity();
                }
                if (totalAmount != order.getTotalAmount())
                    throw new BadOrderException("Ordine scorretto");
            }
            else{
                throw new BadOrderException("Carrello insesistente");
            }
        }
        else {
            throw new UsernameNotFoundException("Utente inesistente");
        }
        // controlli superati
        Set<OrderItem> orderItemsFromDb = new HashSet<>();
        Order orderFromDb = new Order(userFromDb.get(), orderItemsFromDb, order.getEmail(), order.getPhone(), order.getShippingAddress(), order.getPaymentMethod(), totalAmount);
        for (CartItem cartItem: shoppingCart.getCartItems()) {
            Product p = cartItem.getProduct();
            productService.updateProduct(p.setStock(p.getStock()-cartItem.getQuantity()));  //lock opt
            OrderItem updatedOrderItem = new OrderItem(cartItem.getProduct(), orderFromDb, cartItem.getQuantity(), cartItem.getSubtotal());
            orderItemsRepository.save(updatedOrderItem);
            orderItemsFromDb.add(updatedOrderItem);
        }
        Order orderFinal = orderRepository.save(orderFromDb);
        ShoppingCart cartFromDb = this.cartService.getCartByUser(userFromDb.get());
        cartService.clearCart(cartFromDb);
        return orderFinal;
    }

    @Transactional(readOnly = true)
    public List<Order> getUserOrders(String username) {
        Optional<User> userFromDb = userService.getUserbyUsername(username);
        if (userFromDb.isEmpty()) {return null;}
            return orderRepository.findOrderByUser(userFromDb.get());
    }


    @Transactional(readOnly = true)
    public Order getOrderById(Long id) {
        Optional<Order> orderFromDb = orderRepository.findOrderById(id);
        if (orderFromDb.isEmpty()) {return null;}
        return orderFromDb.get();
    }


    @Transactional(readOnly = false)
    public void deleteOrderById(Long id) {
        Optional<Order> orderFromDb = orderRepository.findOrderById(id);
        if(orderFromDb.isEmpty()) {return;}
        Order order = orderFromDb.get();
        for (OrderItem orderItem : order.getOrderItems()) {
            Product p = orderItem.getProduct();
            productService.updateProduct(p.setStock(p.getStock() + orderItem.getQuantity()));
       }
        orderItemsRepository.deleteAll(order.getOrderItems());
        orderRepository.delete(order);
    }


    @Transactional(readOnly = true)
    public List<Order> getOrders() {
        return orderRepository.findAll();
    }
}
