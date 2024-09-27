package com.app.domain.model.Utilities;

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

    @JsonIgnore
    @ToString.Exclude
    @OneToMany(mappedBy = "requests", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<Requests> requests = new ArrayList<>();

    @JsonIgnore
    @ToString.Exclude
    @OneToMany(mappedBy = "pending", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<Pending> pending = new ArrayList<>();

}
