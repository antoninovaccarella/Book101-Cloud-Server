package com.antonino.book101server.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@Entity
@Table(name = "ORDINE")
public class    Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "CLIENTE")
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    @JsonIgnore
    private User user;

    @NotNull
    @OneToMany(mappedBy = "order")
    @Column(name = "PRODOTTI_ACQUISTATI")
    private Set<OrderItem> orderItems = new HashSet<>();

    @NotBlank(message = "Email is missing")
    @Size(max = 50)
    @Email
    private String email;

    @Size(max = 20)
    @Column(name = "TELEFONO")
    private String phone;

    @NotBlank
    @Column(name = "INDIRIZZO_DI_SPEDIZIONE")
    private String shippingAddress;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "METODO_DI_PAGAMENTO")
    private com.antonino.book101server.models.PaymentMethod paymentMethod;

    @Column(name = "VALORE")
    private double totalAmount;

    @Enumerated(EnumType.STRING)
    @Column(name = "STATO")
    private com.antonino.book101server.models.OrderStatus orderStatus;

    @Column(name = "DATA_DI_ACQUISTO")
    private LocalDate purchaseTime;

    public Order(User user, Set<OrderItem> orderItems, String email, String phone, String shippingAddress, com.antonino.book101server.models.PaymentMethod paymentMethod, double totalAmount) {
        this.user = user;
        this.orderItems = orderItems;
        this.email = email;
        this.phone = phone;
        this.shippingAddress = shippingAddress;
        this.paymentMethod = paymentMethod;
        this.totalAmount = totalAmount;
        this.orderStatus = com.antonino.book101server.models.OrderStatus.PENDING;
        this.purchaseTime = LocalDate.now();
    }
}
