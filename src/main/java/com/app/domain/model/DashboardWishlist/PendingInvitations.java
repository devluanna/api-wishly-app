package com.app.domain.model.DashboardWishlist;


import com.app.domain.model.Wishlist.StatusSubscribers;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;

import java.util.Date;


@Entity
@Data
public class PendingInvitations {

    //CONVITES QUE ENVIEI PRA PESSOAS ENTRAREM

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id_pending_invitation;

    private Integer id_user;
    private String username;
    private Date invitation_date;
    private String url_share;
    private boolean isUserWithConnection; //SE O USUARIO TEM CONEXAO COM O OWNER
    private StatusSubscribers statusSubscribers; //inicia com WAITINGFORAPPROVAL
    private Integer id_wishlist;
    private String name_wishlist;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_dashboard_requests")
    @ToString.Exclude
    private DashboardRequestsSubscribers id_dashboard_requests;


}
