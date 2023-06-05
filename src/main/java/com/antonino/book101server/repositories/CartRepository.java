package com.antonino.book101server.repositories;

import com.antonino.book101server.models.ShoppingCart;
import com.antonino.book101server.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CartRepository extends JpaRepository<ShoppingCart,Long> {
    ShoppingCart findShoppingCartByUser(User user);

}
