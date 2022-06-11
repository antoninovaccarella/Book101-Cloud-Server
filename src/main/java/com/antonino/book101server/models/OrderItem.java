package com.antonino.book101server.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name = "PRODOTTI_ORDINE")
public class OrderItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne
    @JoinColumn(name = "PRODOTTO")
    private Product product;

    @ManyToOne
    @JoinColumn(name = "ORDINE")
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    @JsonIgnore
    private com.antonino.book101server.models.Order order;

    @NotNull
    @Column(name = "QUANTITA")
    private int quantity;

    @NotNull
    @Column(name = "VALORE")
    private double subtotal;

    public OrderItem(Product product, com.antonino.book101server.models.Order order, int quantity, double subtotal) {
        this.product = product;
        this.order = order;
        this.quantity = quantity;
        this.subtotal = subtotal;
    }
}
