package com.app.domain.model;

import com.app.domain.model.Wishlist.Events;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@Data
@Entity
public class DashboardEvents {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id_dashboard_events;


    private Integer id_responsible_user;
    private String responsible_username;
    private Integer count_wishlists = 0;

    @JsonIgnore
    @ToString.Exclude
    @OneToMany(mappedBy = "dashboardEvents", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<Events> events = new ArrayList<>();

}
