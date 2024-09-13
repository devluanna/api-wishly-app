package com.app.domain.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;

@Entity
@Getter
@Setter
@ToString(onlyExplicitlyIncluded = true)
public class RequestsByOthers {

    @JsonIgnore
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_dashboard")
    @ToString.Exclude
    private ConnectionsDashboard dashboard;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id_requests_pending;

    private Integer id_user_requestor;

    private Integer id_dashboard_user_requestor;

    private String username;

    private Date connection_date;

    @ToString.Include(name = "statusConnections")
    private StatusConnections statusConnections;


}
