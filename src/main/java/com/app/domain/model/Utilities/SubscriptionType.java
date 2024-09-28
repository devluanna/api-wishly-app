package com.app.domain.model.Utilities;

public enum SubscriptionType {

    GUEST("GUEST"),
    SUBSCRIBED("SUBSCRIBED");

    private String subscription_type;

    private SubscriptionType(String subscription_type) {this.subscription_type = subscription_type;}

    public String getSubscription_type() {
        return subscription_type;
    }

}
