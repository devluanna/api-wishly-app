package com.app.domain.model.Wishlist;


import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;

import java.util.Date;

@Entity
@Data
public class WishlistSubscribers {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id_subscriber;
    private Integer id_user;
    private String username;
    private Date date_you_joined;
    private StatusSubscribers statusSubscribers;
    private boolean isUserWithConnection; //SE O USUARIO TEM CONEXAO COM O OWNER

    @JsonIgnore
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_wishlist")
    @ToString.Exclude
    private Wishlist wishlist;



}
