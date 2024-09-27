package com.app.domain.model.Wishlist;

public enum StatusSubscribers {

    WAITINGFORAPPROVAL("WAITING FOR APPROVAL"),
    SUBSCRIBED("SUBSCRIBED"),
    ARCHIVED("DENIED");



    private String status_wishlist_subscribers;

    private StatusSubscribers(String status_wishlist_subscribers) {this.status_wishlist_subscribers = status_wishlist_subscribers;}

    public String getStatus_wishlist_subscribers() {
        return status_wishlist_subscribers;
    }

}
