package com.app.domain.model.Wishlist;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;

import java.util.Date;

@Entity
@Data
public class WishlistsInEvents {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id_wishlists_in_events;

    private Integer id_wishlist;
    private String wishlist_name;
    private Integer wishlist_identity;
    private Date creation_date;
    private String visibility;
    private String status;
    private String category;
    private Date start_date;
    private Date end_date;
    private Integer count_likes = 0;
    private Integer count_shares = 0;
    private Integer count_copies = 0;
    private Integer count_total_subscribers = 0;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_event")
    private Events event;


}
