package com.app.domain.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;

@Entity
@Data
@Getter
@Setter
@ToString(onlyExplicitlyIncluded = true)
public class RequestsByYou {

    @JsonIgnore
    @ManyToOne(fetch = FetchType.EAGER)
    @ToString.Exclude
    @JoinColumn(name = "id_dashboard")
    private ConnectionsDashboard dashboardRequester;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id_requests;

    private Integer id_user_to_add;

    private Integer id_dashboard_user_to_add;

    private String username;

    private Date connection_date;

    @ToString.Include(name = "statusConnections")
    private StatusConnections statusConnections;


}
