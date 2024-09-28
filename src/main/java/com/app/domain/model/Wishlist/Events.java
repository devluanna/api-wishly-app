package com.app.domain.model.Wishlist;
import jakarta.persistence.*;
import lombok.Data;

import java.util.Date;

@Entity
@Data
public class Events {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id_event;
    private String event_name;
    private String event_description;
    private String category;
    private Integer id_owner;
    private String username_owner;
    private Visibility visibility;
    private StatusWishlistEvent status;
    private Date creation_date;
    private Date start_date;
    private Date end_date;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "id_wishlist")
    private Wishlist wishlist;

}
