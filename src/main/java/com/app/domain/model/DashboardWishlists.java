package com.app.domain.model;

import com.app.domain.model.Wishlist.Wishlist;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@ToString(onlyExplicitlyIncluded = true)
public class DashboardWishlists {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id_dashboard_wishlists;

    private Integer id_responsible_user;
    private String responsible_username;
    private Integer count_wishlists = 0;

    @JsonIgnore
    @ToString.Exclude
    @OneToMany(mappedBy = "dashboardWishlists", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<Wishlist> wishlist = new ArrayList<>();



}
