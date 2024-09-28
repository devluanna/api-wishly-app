package com.app.domain.model.Product;

import com.app.domain.model.Wishlist.Wishlist;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@Entity
@Data
public class ProductList {

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
    @OneToMany(mappedBy = "productList", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<ReferenceLinks> referenceLinks = new ArrayList<>();

    private boolean isTheProductReserved = false;
    private Integer id_user; //id do usuario que reservou se isTheProductReserved for true
    private String username; //username do usuario que reservou se isTheProductReserved for true
    private boolean isProductByIndication = false;
    private StatusProduct statusProduct;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_wishlist")
    @ToString.Exclude
    private Wishlist wishlist;

}
