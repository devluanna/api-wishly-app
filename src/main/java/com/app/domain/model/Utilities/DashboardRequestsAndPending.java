package com.app.domain.model.Utilities;

import com.app.domain.model.DashboardWishlist.SubscriberRequests;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@Entity
@Data
public class DashboardRequestsAndPending {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id_dashboard_requests_and_pending;

    private Integer id_responsible_user;
    private String responsible_username;
    private Integer count_requests = 0; //solicitacoes que voce enviou
    private Integer count_pending = 0; //solicitacoes para voce aprovar
    private Integer count_subscriptions = 0; //inscricoes nas wishlists


    @JsonIgnore
    @ToString.Exclude
    @OneToMany(mappedBy = "dashboard_requests_pending", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<Requests> requests = new ArrayList<>();

    @JsonIgnore
    @ToString.Exclude
    @OneToMany(mappedBy = "dashboard_requests_pending", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<Pending> pending = new ArrayList<>();

    @JsonIgnore
    @ToString.Exclude
    @OneToMany(mappedBy = "dashboard_requests_pending", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<MySubscriptions> mySubscriptions = new ArrayList<>();

    public void addSubscriber(MySubscriptions newSubscriber) {
        if (this.mySubscriptions == null) {
            this.mySubscriptions = new ArrayList<>();
        }

        this.mySubscriptions.add(newSubscriber);
    }

    public void addRequests(Requests newRequests) {
        if (this.requests == null) {
            this.requests = new ArrayList<>();
        }

        this.requests.add(newRequests);
    }

    public void addInvite(Pending newGuest) {
        if (this.pending == null) {
            this.pending = new ArrayList<>();
        }

        this.pending.add(newGuest);
    }

}
