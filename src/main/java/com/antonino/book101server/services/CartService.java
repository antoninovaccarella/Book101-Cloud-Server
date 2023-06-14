package com.antonino.book101server.services;

import com.antonino.book101server.exceptions.ProductNotFoundException;
import com.antonino.book101server.models.*;
import com.antonino.book101server.repositories.CartItemsRepository;
import com.antonino.book101server.repositories.CartRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
public class CartService {

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private CartItemsRepository cartItemsRepository;

    @Autowired
    private ProductService productService;

    @Autowired
    private GoogleCloudStorageService googleCloudStorageService;

    @Transactional(readOnly = false)
    public ShoppingCart createCart(User user) {
        return cartRepository.save(new ShoppingCart(user.getId(), user, new HashSet<>(), 0));
    }

    @Transactional(readOnly = false)
    public ShoppingCart clearCart(ShoppingCart shoppingCart) {
        for (CartItem c : shoppingCart.getCartItems()) {
            cartItemsRepository.delete(c);
        }
        return cartRepository.save(new ShoppingCart(shoppingCart.getId(), shoppingCart.getUser(), new HashSet<>(), 0));
    }

    @Transactional(readOnly = true)
    public Set<CartItem> getCartItemsByUser(User user) {
        return getCartByUser(user).getCartItems();
    }

    // Evito di restituire un optional perché ogni utente ha necessariamente un carrello
    @Transactional(readOnly = true)
    public ShoppingCart getCartByUser(User user) {
        ShoppingCart shoppingCart = cartRepository.findShoppingCartByUser(user);
        shoppingCart.getCartItems().stream().forEach(cartItemDb ->
                {Product productDb = cartItemDb.getProduct();
                    productDb.setPicture(googleCloudStorageService.downloadAnteprima(productDb.getId() + "_jpg.jpg"));
                    productDb.setPdf(googleCloudStorageService.downloadPDF(productDb.getId() + "_pdf.pdf"));
                }
        );
        return cartRepository.findShoppingCartByUser(user);
    }

    @Transactional(readOnly = false)
    public ShoppingCart updateCart(CartItem cartItem, User user) throws ProductNotFoundException {
        Optional<Product> productFromDb = productService.getProduct(cartItem.getProduct().getId());
        ShoppingCart cartFromDb = this.getCartByUser(user);
        // Il prodotto esiste?
        if (productFromDb.isEmpty()) {
            throw new ProductNotFoundException("Prodotto non esiste");
        } else {
            Product p = productFromDb.get();
            cartItem.setShoppingCart(cartFromDb);
            cartItem.setSubtotal(p.getPrice() * cartItem.getQuantity());
            Set<CartItem> cartItems = cartFromDb.getCartItems();
            double newAmount = cartFromDb.getTotalAmount();
            for (CartItem ci : cartItems) {
                if (ci.getProduct().equals(p)) {
                    if (cartItem.getQuantity() == 0) {
                        newAmount -= p.getPrice() * ci.getQuantity();
                        cartItems.remove(ci);
                        cartItemsRepository.delete(ci);
                        cartFromDb.setTotalAmount(newAmount);
                        return cartRepository.save(cartFromDb);
                    } else if (cartItem.getQuantity() < p.getStock()) {
                        newAmount += p.getPrice() * (cartItem.getQuantity() - ci.getQuantity());
                        ci.setSubtotal(p.getPrice() * cartItem.getQuantity());
                        ci.setQuantity(cartItem.getQuantity());
                        cartFromDb.setTotalAmount(newAmount);
                        return cartRepository.save(cartFromDb);
                    } else {
                        throw new ProductNotFoundException("Prodotto non disponibile nella quantità richiesta");
                    }
                }
            }
            newAmount +=  p.getPrice() * cartItem.getQuantity();
            cartItems.add(cartItem);
            cartItemsRepository.save(cartItem);
            cartFromDb.setTotalAmount(newAmount);
            cartFromDb.getCartItems().stream().forEach(cartItemDb ->
                    {Product productDb = cartItemDb.getProduct();
                        productDb.setPicture(googleCloudStorageService.downloadAnteprima(productDb.getId() + "_jpg.jpg"));
                        productDb.setPdf(googleCloudStorageService.downloadPDF(productDb.getId() + "_pdf.pdf"));
                    }
            );
            return cartRepository.save(cartFromDb);
        }
    }

    @Transactional(readOnly = true)
    public List<ShoppingCart> showAllCarts(int pageNumber, int pageSize, String sortBy) {
        Pageable paging = PageRequest.of(pageNumber, pageSize, Sort.by(sortBy));
        Page<ShoppingCart> pagedResult = cartRepository.findAll(paging);
        if (pagedResult.hasContent()) {
            return pagedResult.getContent();
        } else {
            return new ArrayList<>();
        }
    }

    public double getSubtotal(User user) {
        return this.getCartByUser(user).getTotalAmount();
    }
}
