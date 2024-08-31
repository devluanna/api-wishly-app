package com.app.domain.model;

public enum Status {

    ACTIVATED("ACTIVATED"),
    DISABLED("DISABLED");


    private String status_description;

    private Status(String status_description) {this.status_description = status_description;}

    public String getStatus_description() {
        return status_description;
    }

}
