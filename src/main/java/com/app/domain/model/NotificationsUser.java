package com.app.domain.model;

import com.app.domain.model.Utilities.DashboardRequestsAndPending;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;

import java.util.Date;

@Entity
@Data
public class NotificationsUser {


    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id_notification;

    private Integer id_dashboard_user;

    private String username;

    private String notification_name;

    private String notification_description;

    private boolean notificationWasViewed = false;

    private Date date_of_notification;

    private boolean notification_reminder;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_user")
    @ToString.Exclude
    private Users users;



}
