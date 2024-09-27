package com.app.domain.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;


import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@ToString(onlyExplicitlyIncluded = true)
public class ConnectionsDashboard {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id_dashboard;

    private Integer id_responsible_user;
    private String responsible_username;
    private String responsible_user_email;
    private Integer count_friends = 0;
    private Integer count_requests_by_you = 0; //solicitacoes que voce enviou
    private Integer count_requests_by_others = 0; //solicitacoes para voce aprovar

    @JsonIgnore
    @ToString.Exclude
    @OneToMany(mappedBy = "dashboard", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<Connections> connections = new ArrayList<>();

    @JsonIgnore
    @ToString.Exclude
    @OneToMany(mappedBy = "dashboardRequester", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<RequestsByYou> requestsByYou = new ArrayList<>();

    @JsonIgnore
    @ToString.Exclude
    @OneToMany(mappedBy = "dashboard", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<RequestsByOthers> requestsByOthers = new ArrayList<>();

    public void addNewRequest(RequestsByYou newRequests) {
        if (this.requestsByOthers == null) {
            this.requestsByOthers = new ArrayList<>();
        }

        this.requestsByYou.add(newRequests);
    }

    public void addNewRequested(RequestsByOthers requested) {
        if (this.requestsByOthers == null) {
            this.requestsByOthers = new ArrayList<>();
        }
        this.requestsByOthers.add(requested);
    }

    public void addNewConnection(Connections newConnection) {
        if (this.connections == null) {
            this.connections = new ArrayList<>();
        }
        this.connections.add(newConnection);
        this.count_friends = this.connections.size();
    }

    public List<Connections> getConnections() {
        return connections;
    }

    public void setConnections(List<Connections> connections) {
        this.connections = connections;
        this.count_friends = connections.size();
    }





}
