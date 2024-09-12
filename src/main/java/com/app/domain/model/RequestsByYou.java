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
    private Integer id_requests; //GERADO E PRA INDENTIFICAR DENTRO DA TABLE

    private Integer id_user_to_add; //ID QUE VC QUER ADICIONAR

    private Integer id_dashboard_user_to_add; // ID DE DASHBOARD QUE VC QUER ADICIONAR

    private String username; //USERNAME DO USUARIO QUE QUER ADICIONAR

    private Date connection_date; // DATA QUE VC MANDOU SOLICITACAO

    @ToString.Include(name = "statusConnections")
    private StatusConnections statusConnections; // STATUS INICIALMENTE EM ESPERA > REQUESTS BY OTHERS DO USUARIO QUE QUER ADICIONAR
    // | APROVAR OU NEGAR? = Se APROVAR vai pra CONNECTIONS


}
