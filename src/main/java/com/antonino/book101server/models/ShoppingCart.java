package com.antonino.book101server.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.Accessors;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Data
@Accessors(chain = true)
@NoArgsConstructor
@Entity
@Table(name = "CARRELLO")
public class ShoppingCart {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "CLIENTE")
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    @JsonIgnore
    private User user;

    @OneToMany(mappedBy = "shoppingCart", cascade = CascadeType.ALL, fetch=FetchType.EAGER)
    @Column(name = "PRODOTTO_NEL_CARRELLO")
    private Set<com.antonino.book101server.models.CartItem> cartItems = new HashSet<>();

    @Column(name = "VALORE")
    private double totalAmount;

    public ShoppingCart(Long id, User user, Set<com.antonino.book101server.models.CartItem> cartItems, double totalAmount) {
        this.id = id;
        this.user = user;
        this.cartItems = cartItems;
        this.totalAmount = totalAmount;
    }
}
