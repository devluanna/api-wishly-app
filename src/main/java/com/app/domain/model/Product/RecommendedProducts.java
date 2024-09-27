package com.app.domain.model.Product;

import com.app.domain.model.Wishlist.Wishlist;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

//LAMBDA
@Entity
@Data
public class RecommendedProducts extends ProductList {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id_product;
    private String product_name;
    private String product_description;
    private String categoria;
    private String uriImg;
    private Integer price;
    @JsonIgnore
    @ToString.Exclude
    @OneToMany(mappedBy = "referenceLinks", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<ReferenceLinks> referenceLinks = new ArrayList<>();
    private boolean isTheProductReserved = false;
    private boolean isProductByIndication = true;
    private Integer id_user; //id do usuario que fez a indicacao se isProductByIndication for true
    private String username; //username do usuario fez a indicacao se isProductByIndication for true
    private StatusProduct statusProduct;

    private boolean send_reminder; // reenviar lembrete de notificacao caso a owner demore mt pra responder a solicitacao

    @JsonIgnore
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_wishlist")
    @ToString.Exclude
    private Wishlist wishlist;
}
