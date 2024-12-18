package com.app.domain.model.Product;

import com.app.domain.model.DashboardWishlist.DashboardProducts;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Data;
import lombok.ToString;

import java.util.Date;
import java.util.List;

@Entity
@Data
@Builder
public class ProductList {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id_product;

    private String product_name;
    private String product_description;
    private String category;
    private String uriImg;
    private Integer price;

    private Integer id_wishlist;

    private Boolean isProductByIndication;
    private Integer indicated_user_id; //id do usuario que INDICOU
    private String indicated_username; //username do usuario que INDICOU

    private StatusProduct statusProduct;

    private Boolean isTheProductReserved;
    private Integer reservation_user_id; //id do usuario que reservou se isTheProductReserved for true
    private String reservation_username; //username do usuario que reservou se isTheProductReserved for true
    private Boolean send_reminder; // reenviar lembrete de notificacao caso a owner demore mt pra responder a solicitacao

    private Date date_product_created;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_dashboard_products")
    @ToString.Exclude
    private DashboardProducts dashboardProducts;

    @ToString.Exclude
    @OneToMany(mappedBy = "productList", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<ReferenceLinks> referenceLinks;


    public ProductList() {

    }
    public ProductList(Integer id_product, String product_name, String product_description, String category, String uriImg, Integer price,
                       Integer id_wishlist, Boolean isProductByIndication, Integer indicated_user_id, String indicated_username,
                       StatusProduct statusProduct, Boolean isTheProductReserved, Integer reservation_user_id, String reservation_username,
                       Boolean send_reminder, Date date_product_created, DashboardProducts dashboardProducts, List<ReferenceLinks> referenceLinks) {
        this.id_product = id_product;
        this.product_name = product_name;
        this.product_description = product_description;
        this.category = category;
        this.uriImg = uriImg;
        this.price = price;
        this.id_wishlist = id_wishlist;
        this.isProductByIndication = isProductByIndication;
        this.indicated_user_id = indicated_user_id;
        this.indicated_username = indicated_username;
        this.statusProduct = statusProduct;
        this.isTheProductReserved = isTheProductReserved;
        this.reservation_user_id = reservation_user_id;
        this.reservation_username = reservation_username;
        this.send_reminder = send_reminder;
        this.date_product_created = date_product_created;
        this.dashboardProducts = dashboardProducts;
        this.referenceLinks = referenceLinks;
    }


}
