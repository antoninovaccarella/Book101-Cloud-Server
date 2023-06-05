package com.antonino.book101server.models;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
@NoArgsConstructor
@Entity
@Table(name = "UTENTE", uniqueConstraints = @UniqueConstraint(columnNames = "email"))
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Username is missing")
    @Size(max = 20)
    private String username;

    @NotBlank(message = "Email is missing")
    @Size(max = 50)
    @Email
    private String email;

    @NotBlank(message = "Password is missing")
    @Size(max = 120)
    private String password;

    @Column(name = "DATA_REGISTRAZIONE")
    private LocalDate dateRegistered;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(	name = "RUOLI_UTENTI",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<com.antonino.book101server.models.Role> roles = new HashSet<>();

    @OneToMany(mappedBy = "user")
    private Set<com.antonino.book101server.models.Order> orders;

    @OneToOne(mappedBy = "user", cascade = CascadeType.REMOVE)
    private com.antonino.book101server.models.ShoppingCart shoppingCart;

    public User(String username, String email, String password) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.dateRegistered = LocalDate.now();
    }
}
