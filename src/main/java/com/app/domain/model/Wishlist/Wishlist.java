package com.app.domain.model.Wishlist;

import com.app.domain.model.DashboardWishlist.DashboardProducts;
import com.app.domain.model.DashboardWishlist.DashboardRequestsSubscribers;
import com.app.domain.model.DashboardWishlists;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
@Entity
@NoArgsConstructor
public class Wishlist {

    @JsonIgnore
    @ToString.Exclude
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_dashboard_wishlists")
    private DashboardWishlists dashboardWishlists;

    //criar uma table pra produtos que estao reservados?

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id_wishlist;

    @Column(unique = true)
    private Integer wishlist_identity;

    private String wishlist_name;
    private String url_img;
    private String description;
    private Integer id_owner;
    private String username_owner;

    private String visibility;
    private Boolean isRequiredRequest;
    //private String url_public_wishlist; //url publica https://wishly.com/wishlist/121323 ou https://wishly.com/wishlist/lua/124e3

    private String url_share; //url gerada enviar para alguem entrar na wishlist
    // (dentro dela endpoint para entrar dentro/no front button pra pessoa convidada aceitar)
    //essa url share precisa ser unica, cada uma vai conter o ID do usuario que ela quer convidar

    private String category;
    private String sub_category;

    @ToString.Exclude
    @OneToMany(mappedBy = "wishlist", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<Tags> tags = new ArrayList<>();

    private String status_wishlist;
    private Date creation_date;

    private Date last_update_date;

    private Boolean haveLinkedEvent; //tem evento vinculado? se sim mostrar o evento/se nao.
    private String name_event_linked;
    private Date start_date; //Possui evento? se sim, a data de inicio e fim vai ser substituida por a do evento
    private Date end_date;
    private Boolean useEventDate; //tem evento vinculado? se sim mostrar o evento/se nao.

    private Boolean enableProductsByRecommendation; // se for true, habilita o lambda/python
    private Boolean enablesProductReservations; // se for true, habilita um botao de RESERVAR no front e aciona o lambda/step functions
    private Boolean isACopiedWishlist; //se for true, linkar com a wishlist que foi copiada e tambem se alguem copiar essa, sempre mostrar a wishlsit mae

    private Integer count_likes = 0;
    private Integer count_shares = 0; //quantidade de pessoas que estao dentro da wishlist/que foi compartilhado/que se inscreveram
    private Integer count_copies = 0;
    private Integer count_total_subscribers = 0;
    private Integer count_recommended_products_pending = 0; //quantidade de produtos por recomendacao pendentes



    public Wishlist(Integer wishlist_identity, String wishlist_name, String url_img, String description, Integer id_owner, String username_owner, String visibility, Boolean isRequiredRequest,
                    String url_share, String category, String sub_category, String status_wishlist,
                    Date creation_date, Boolean enablesProductReservations, Boolean enableProductsByRecommendation,
                    Boolean isACopiedWishlist, List<Tags> tags, DashboardWishlists dashboardWishlists, Boolean haveLinkedEvent,
                    EventsInWishlists eventsInWishlists, String name_event_linked, DashboardRequestsSubscribers dashboardRequestsSubscribers,
                    DashboardProducts dashboardAllProducts) {

        this.wishlist_identity = wishlist_identity;
        this.wishlist_name = wishlist_name;
        this.url_img = url_img;
        this.description = description;
        this.id_owner = id_owner;
        this.username_owner = username_owner;
        this.visibility = visibility;
        this.isRequiredRequest = isRequiredRequest;
        this.url_share = url_share;
        this.category = category;
        this.sub_category = sub_category;
        this.status_wishlist = status_wishlist;
        this.creation_date = creation_date;
        this.enablesProductReservations = enablesProductReservations;
        this.enableProductsByRecommendation = enableProductsByRecommendation;
        this.isACopiedWishlist = isACopiedWishlist;
        this.tags = tags;
        this.haveLinkedEvent = haveLinkedEvent;
        this.name_event_linked = name_event_linked;
        this.dashboardWishlists = dashboardWishlists;
        this.eventsInWishlists = eventsInWishlists;
        this.dashboardRequestsSubscribers = dashboardRequestsSubscribers;
        this.dashboardAllProducts = dashboardAllProducts;
    }

    public void addSubscribers(WishlistSubscribers newSubscribers) {
        if (this.wishlistSubscribers == null) {
            this.wishlistSubscribers = new ArrayList<>();
        }

        this.wishlistSubscribers.add(newSubscribers);
    }


    @Getter
    @JsonIgnore
    @ToString.Exclude
    @OneToOne(mappedBy = "wishlist", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private EventsInWishlists eventsInWishlists;

    @JsonIgnore
    @ToString.Exclude
    @OneToMany(mappedBy = "wishlist", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<WishlistSubscribers> wishlistSubscribers = new ArrayList<>(); //inscritos/convidados/que estao dentro da wishlist

    @JsonIgnore
    @ToString.Exclude
    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_dashboard_requests")
    private DashboardRequestsSubscribers dashboardRequestsSubscribers;

    @JsonIgnore
    @ToString.Exclude
    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "id_dashboard_products")
    private DashboardProducts dashboardAllProducts;


}
