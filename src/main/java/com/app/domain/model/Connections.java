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
    // COLOCAR SE O USUARIO FOI ADICIONADO OU SE FOI ACEITO

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id_connection;


    private Integer id_user_connection; // ID CONEXAO
    private String name;
    private String username;// USERNAME DA CONEXAO
    private StatusConnections statusConnections; // aprovado por mim ou por outros
    private Boolean profileIsOpenForConnections; // FALSO PARA VER INFORMACOES DO USUARIO Y, ATE QUE O Y APROVE

    private Date connection_date;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_dashboard")
    private ConnectionsDashboard dashboard; //ID DO SEU DASHBOARD





}
