package com.app.domain.model.DashboardWishlist;

import com.app.domain.model.Product.ProductList;
import com.app.domain.model.Product.ReferenceLinks;
import com.app.domain.model.Product.RequestsProducts;
import com.app.domain.model.Wishlist.Wishlist;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@Entity
@Data
public class DashboardProducts {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id_dashboard_products;

    private Integer id_wishlist;

    private Integer count_products = 0;
    private Integer count_recommended_products_pending = 0;
    private Integer count_recommended_products_approved = 0;
    private Integer count_reserved_products = 0;


    @JsonIgnore
    @ToString.Exclude
    @OneToMany(mappedBy = "dashboardProducts", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<ProductList> productsList = new ArrayList<>();

    @JsonIgnore
    @ToString.Exclude
    @OneToOne(mappedBy = "dashboardAllProducts", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private Wishlist wishlist;

    @JsonIgnore
    @ToString.Exclude
    @OneToMany(mappedBy = "dashboardProducts", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<RequestsProducts> requestsProducts;

}
