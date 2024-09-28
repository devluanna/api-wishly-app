package com.app.domain.model.Utilities;

import com.app.domain.model.Wishlist.StatusSubscribers;
import com.app.domain.model.Wishlist.StatusWishlistEvent;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;

import java.util.Date;

@Entity
@Data
public class MySubscriptions {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id_my_subscription;

    private Integer id_user;
    private String username;
    private Integer id_wishlist;
    private String name_wishlist;
    private String description;
    private Integer id_owner;
    private String username_owner;
    private boolean isUserWithConnectionOwner;
    private boolean hasProductsByRecommendationPending; //voce possui produtos por recomendacao pendente nessa wishlist?
    private Integer count_products_by_recommendation = 0;

    private String uri_img_wishlist;
    private String url_public_wishlist;
    private String url_share; //caso tenha sido por convite colocar a url de compartilhamento
    private String category;
    private Integer count_likes = 0;
    private Integer count_shares = 0;
    private Integer count_copies = 0;
    private StatusWishlistEvent status;
    private Date creation_date;
    private Date start_date;
    private Date end_date;
    private Date date_you_joined;

    private boolean haveLinkedEvent; //tem evento vinculado? se sim mostrar o evento/se nao.
    private Integer id_event;
    private String event_name;

    private StatusSubscribers statusSubscribers;
    private SubscriptionType subscriptionType; //FOI CONVIDADO OU SE INSCREVEU?

    @JsonIgnore
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_dashboard_requests_and_pending")
    @ToString.Exclude
    private DashboardRequestsAndPending dashboard_requests_pending;

}
