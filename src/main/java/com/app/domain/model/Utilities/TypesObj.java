package com.app.domain.model.Utilities;

public enum TypesObj {

    WISHLIST("WISHLIST"),
    EVENT("EVENT");

    private String object_type;

    private TypesObj(String object_type) {this.object_type = object_type;}

    public String getObject_type() {
        return object_type;
    }

}
