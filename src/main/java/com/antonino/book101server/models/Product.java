package com.antonino.book101server.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Data
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "PRODOTTO")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Version
    @Column(name = "OPT_LOCK", nullable = false)
    private long version = 0L;

    @NotNull(message = "Product name is required")
    @Column(name = "NOME")
    private String name;

    @NotNull
    private int stock = 0;

    @NotNull(message = "Category is required")
    @Enumerated(EnumType.STRING)
    @Column(name = "CATEGORIA")
    private com.antonino.book101server.models.ProductCategory category;

    @NotNull(message = "Description is required")
    @Column(name = "DETTAGLI")
    private String shortDetails;

    @Column(name = "DESCRIZIONE")
    private String description;

    @Column(name = "ANTEPRIMA")
    private String picture;

    @NotNull(message = "Price is required")
    @Column(name = "PREZZO")
    private double price;

    @Column(name = "PDF")
    private String pdf;

    private byte[] pictureFile;
    private byte[] pdfFile;

}
