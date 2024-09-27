package com.app.domain.model.Wishlist;

public enum Visibility {

    JUSTME("JUSTME"), //apenas para mim
    PRIVATE("PRIVATE"), //apenas pra quem eu compartilhar
    ONLYFRIENDS("ONLYFRIENDS"), //apenas amigos
    PUBLIC("PUBLIC"); //publico



    private String visibility_description;

    private Visibility(String visibility_description) {this.visibility_description = visibility_description;}

    public String getVisibility_description() {
        return visibility_description;
    }


}
