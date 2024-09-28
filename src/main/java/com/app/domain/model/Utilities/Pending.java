package com.app.domain.model.Utilities;

import com.app.domain.model.Wishlist.StatusSubscribers;
import com.app.domain.model.Wishlist.Wishlist;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;

import java.util.Date;

@Entity
@Data
public class Pending {

    //SOLICITACOES PENDENTES/QUE ALGUM OWNER DA WISHLSIT XXX ME CONVIDOU E EU PRECISO APROVAR!

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id_pending;

    private Integer id_user_guest; //seu id
    private String username_guest; //seu username

    private Integer id_owner_user; //id do owner
    private String username_owner; //username do owner
    private Integer id_wishlist;
    private String wishlist_name;
    private Date date_invited;
    private StatusSubscribers statusSubscribers;
    private boolean isUserWithConnection; //SE O USUARIO TEM CONEXAO COM O OWNER?


    @JsonIgnore
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_dashboard_requests_and_pending")
    @ToString.Exclude
    private DashboardRequestsAndPending dashboard_requests_pending;

}
