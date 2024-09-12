package com.app.domain.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
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
    private Integer id_requests_pending; //GERADO E PRA INDENTIFICAR DENTRO DA TABLE

    private Integer id_user_requestor; //ID QUE TE ADICIONOU

    private Integer id_dashboard_user_requestor; // ID DE DASHBOARD QUE TE ADICIONOU

    private String username; // USERNAME DE QUEM TE ADICIONOU

    private Date connection_date; //DATA QUE FOI FEITA A SOLICITACAO

    @ToString.Include(name = "statusConnections")
    private StatusConnections statusConnections; // STATUS INICIALMENTE EM ESPERA | APROVAR OU NEGAR? = Se APROVAR vai pra CONNECTIONS


}
