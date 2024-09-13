package com.app.domain.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import jakarta.persistence.*;
import lombok.Data;


import java.util.Date;

@Data
@Entity
@JsonPropertyOrder({ "id", "id_responsible_request", "id_dashboard_request", "username", "id_dashboard_user", "statusConnections", "profileIsOpenForConnections", "connection_date",})
public class Connections {


    //Colocar uma rastreio de quem adicionou e quem aceitou?
    // StatusConnection eh suficiente?

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id_connection;


    private Integer id_user_connection;
    private String name;
    private String username;
    private StatusConnections statusConnections;
    private Boolean profileIsOpenForConnections;

    private Date connection_date;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_dashboard")
    private ConnectionsDashboard dashboard;





}
