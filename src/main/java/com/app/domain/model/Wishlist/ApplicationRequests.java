package com.app.domain.model.Wishlist;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;

import java.util.Date;

@Entity
@Data
public class ApplicationRequests {


    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id_application_requests;
    private Integer id_user;
    private String username;

    private Date date_of_request;

    private StatusSubscribers statusSubscribers;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_wishlist")
    @ToString.Exclude
    private Wishlist wishlist;


}
