package com.app.domain.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

import java.util.Date;

@Entity
@Data
public class NotificationsUser {


    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id_notification;

    private Integer id_user;

    private Integer id_dashboard_user;

    private String username;

    private String notification_name;

    //private String information_data; //DESCOMENTAR PARA UTILIZAR NO FRONT

    private String notification_description; //APAGAR POIS O FRONT SERA RESPONSAVEL PELA MENSAGEM!

    private boolean notificationWasViewed = false;

    private Date date_of_notification; //data da notificacao

    private boolean notification_reminder; //essa notificacao e um lembrete?


}
