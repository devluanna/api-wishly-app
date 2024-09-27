package com.app.domain.model.DashboardWishlist;


import com.app.domain.model.Wishlist.StatusSubscribers;
import com.app.domain.model.Wishlist.Wishlist;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;

import java.util.Date;

@Entity
@Data
public class SubscriberRequests {

    //INSCRITOS TENTANDO ENTRAR NA WISHLIST

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id_subscriber_request;
    private Integer id_user;
    private String username;
    private Date date_user_requested;
    private Integer id_wishlist;
    private String name_wishlist;
    private boolean isUserWithConnection; //SE O USUARIO TEM CONEXAO COM O OWNER
    private StatusSubscribers statusSubscribers; //inicia com WAITINGFORAPPROVAL

    @JsonIgnore
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_dashboard_requests")
    @ToString.Exclude
    private DashboardRequestsSubscribers id_dashboard_requests;


}
