package com.app.domain.model.Utilities;

import com.app.domain.model.Wishlist.StatusSubscribers;
import com.app.domain.model.Wishlist.StatusWishlistEvent;
import com.app.domain.model.Wishlist.Visibility;
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
    private Date date_you_joined;

    private String uri_img_wishlist;
    private Integer id_wishlist;
    private String name_wishlist;
    private Integer identity_wishlist;
    private Visibility Visibility;
    private String category;

    private boolean isUserWithConnectionOwner;
    private boolean hasProductsByRecommendationPending; //voce possui produtos por recomendacao pendente nessa wishlist?
    private Integer count_products_by_recommendation_pending = 0; //produtos por recomendacao pendentes
    private Integer count_products_by_recommendation_total = 0; //produtos por recomendacao adicionados


    private Integer count_likes = 0;
    private Integer count_subscribers_wishlist = 0;

    private Date creation_date_wishlist;

    private StatusSubscribers statusSubscribers;
    private SubscriptionType subscriptionType; //FOI CONVIDADO OU SE INSCREVEU?

    @JsonIgnore
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_dashboard_requests_and_pending")
    @ToString.Exclude
    private DashboardRequestsAndPending dashboard_requests_pending;

}
