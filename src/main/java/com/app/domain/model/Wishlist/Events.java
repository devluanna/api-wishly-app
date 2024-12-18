package com.app.domain.model.Wishlist;
import com.app.domain.model.DashboardEvents;
import com.app.domain.model.DashboardWishlists;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
public class Events {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id_event;


    private String event_name;

    private String event_description;
    private String category;
    private Integer id_owner;
    private String username_owner;
    private String status;
    private Date creation_date;
    private Date start_date;
    private Date end_date;
    private Date last_update_date;

    @OneToMany(mappedBy = "event", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<WishlistsInEvents> wishlists = new ArrayList<>();

    public Events(String event_name, String event_description, String category, Integer id_owner, String username_owner, String status, Date creation_date, Date start_date, Date end_date, DashboardEvents dashboardEvents) {
        this.event_name = event_name;
        this.event_description = event_description;
        this.category = category;
        this.id_owner = id_owner;
        this.username_owner = username_owner;
        this.status = status;
        this.creation_date = creation_date;
        this.start_date = start_date;
        this.end_date = end_date;
        this.dashboardEvents = dashboardEvents;
    }

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_dashboard_events")
    private DashboardEvents dashboardEvents;
}
