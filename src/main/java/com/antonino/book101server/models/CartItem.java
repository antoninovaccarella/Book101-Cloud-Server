package com.antonino.book101server.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import lombok.experimental.Accessors;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Accessors(chain = true)
@Entity
@Table(name = "PRODOTTI_CARRELLO")
public class CartItem{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne
    @JoinColumn(name = "PRODOTTO")
    private Product product;

    @ManyToOne
    @JoinColumn(name = "CARRELLO")
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    @JsonIgnore
    private ShoppingCart shoppingCart;

    @NotNull
    @Column(name = "QUANTITA")
    private int quantity;

    @NotNull
    @Column(name = "VALORE")
    private double subtotal;

}
