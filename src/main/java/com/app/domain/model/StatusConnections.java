package com.app.domain.model;

public enum StatusConnections {

        APPROVEDBYME("APPROVED BY ME"),
        APPROVEDBYOTHERS("APPROVED BY OTHERS"),
        ONSTAND("ON STAND"),
        DENIED("DENIED");


        private String status_connection;

        private StatusConnections(String status_connection) {this.status_connection = status_connection;}

        public String getStatus_connection() {
            return status_connection;
        }



}
