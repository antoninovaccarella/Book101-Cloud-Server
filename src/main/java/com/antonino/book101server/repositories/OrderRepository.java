package com.antonino.book101server.repositories;

import com.antonino.book101server.models.Order;
import com.antonino.book101server.models.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<Order,Long> {
    List<Order> findOrderByUser(User user);
    Optional<Order> findOrderById(Long id);
}
