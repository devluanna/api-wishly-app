package com.app.domain.model.DashboardWishlist;


import com.app.domain.model.Wishlist.Wishlist;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@Entity
@Data
public class DashboardRequestsSubscribers {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id_dashboard_requests;

    @JsonIgnore
    @ToString.Exclude
    @OneToOne(mappedBy = "dashboardRequestsSubscribers", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private Wishlist wishlist;

    private Integer id_responsible_user;
    private Integer id_wishlist;
    private String username_responsible;
    private String name_wishlist;
    private Integer identity_wishlist;

    private Integer count_subscriber_requests = 0;
    private Integer count_pending_invitations = 0;

    @JsonIgnore
    @ToString.Exclude
    @OneToMany(mappedBy = "dashboard_requests_subscribers", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<SubscriberRequests> subscriberRequests = new ArrayList<>(); //PESSOAS INSCRITAS NA WISHLIST PRIVADA


    @JsonIgnore
    @ToString.Exclude
    @OneToMany(mappedBy = "dashboard_pending_subscribers", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<PendingInvitations> pendingInvitations = new ArrayList<>(); //CONVITES QUE O OWNER GEROU E ENVIOU


    public void addSubscriberRequests(SubscriberRequests newSubscriberRequests) {
        if (this.subscriberRequests == null) {
            this.subscriberRequests = new ArrayList<>();
        }

        this.subscriberRequests.add(newSubscriberRequests);
    }
    public void addPendingInvitations(PendingInvitations newInvitation) {
        if (this.pendingInvitations == null) {
            this.pendingInvitations = new ArrayList<>();
        }

        this.pendingInvitations.add(newInvitation);
    }


}

