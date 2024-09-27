package com.app.domain.model.Wishlist;

import com.app.domain.model.DashboardWishlist.DashboardRequestsSubscribers;
import com.app.domain.model.Product.ProductList;
import com.app.domain.model.Product.RecommendedProducts;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Data
public class Wishlist {

    //criar uma table pra produtos que estao reservados?

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id_wishlist;
    private String wishlist_name;
    private String uriImg;
    private String description;
    private Integer id_owner;
    private String username_owner;
    private Visibility visibility;
    private String url_public_wishlist; //url publica https://wishly.com/wishlist/121323 ou https://wishly.com/wishlist/lua/124e3
    private String url_share; //url gerada enviar para alguem entrar na wishlist (dentro dela endpoint para entrar dentro/no front button pra pessoa convidada aceitar)
    private String category;
    private String sub_category;
    private Integer count_likes = 0;
    private Integer count_shares = 0; //quantidade de pessoas que estao dentro da wishlist/que foi compartilhado/que se inscreveram
    private Integer count_copies = 0;
    private Integer count_recommended_products_pending = 0; //quantidade de produtos por recomendacao pendentes
    private String tags;
    private StatusWishlistEvent status;
    private Date creation_date;
    private Date start_date; //Possui evento? se sim, a data de inicio e fim vai ser substituida por a do evento
    private Date end_date;
    private boolean enableProductsByRecommendation; // se for true, habilita o lambda/python
    private boolean enablesProductReservations; // se for true, habilita um botao de RESERVAR no front e aciona o lambda/step functions

    @JsonIgnore
    @ToString.Exclude
    @OneToOne(mappedBy = "event", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private Events event;

    @JsonIgnore
    @ToString.Exclude
    @OneToMany(mappedBy = "wishlistSubscribers", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<WishlistSubscribers> wishlistSubscribers = new ArrayList<>(); //inscritos/convidados/que estao dentro da wishlist

    @JsonIgnore
    @ToString.Exclude
    @OneToMany(mappedBy = "productsList", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<ProductList> productsList = new ArrayList<>(); //itens dentro da wishlist

    @JsonIgnore
    @ToString.Exclude
    @OneToMany(mappedBy = "recommendedProducts", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<RecommendedProducts> recommendedProducts = new ArrayList<>();

    @JsonIgnore
    @ToString.Exclude
    @OneToOne(mappedBy = "dashboardRequestsSubscribers", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private DashboardRequestsSubscribers dashboardRequestsSubscribers;


}
