package com.app.domain.model.Utilities;


import com.app.domain.model.Wishlist.StatusSubscribers;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;

import java.util.Date;

@Entity
@Data
public class Requests {


    //SOLICITACOES PENDENTES QUE EU ME INSCREVI EM ALGUMA WISHLSIT E ESTA PENDENTE O OWNER APROVAR

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id_request;

    private Integer id_user; //seu id
    private String username; //seu username

    private Integer id_owner_user; //id do owner
    private String username_owner; //username do owner
    private Integer id_wishlist;
    private String wishlist_name;

    private Date date_of_request;
    private StatusSubscribers statusSubscribers;
    private boolean isUserWithConnection; //SE O USUARIO TEM CONEXAO COM O OWNER?


    @JsonIgnore
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_dashboard_requests_and_pending")
    @ToString.Exclude
    private DashboardRequestsAndPending dashboard_requests_pending;


}
