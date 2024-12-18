package com.app.domain.model.Wishlist;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;

import java.util.Date;


@Entity
@Data
public class EventsInWishlists {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id_events_in_wishlists;

    private Integer id_event;
    private String event_name;
    private String status;
    private Date creation_date;
    private Date last_update_date;
    private Date start_date;
    private Date end_date;

    @JsonIgnore
    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_wishlist", referencedColumnName = "id_wishlist")
    private Wishlist wishlist;


}
