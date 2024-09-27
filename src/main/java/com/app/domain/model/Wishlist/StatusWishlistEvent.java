package com.app.domain.model.Wishlist;

public enum StatusWishlistEvent {

    INSTART("IN START"), //criada mais ainda nao mudou status
    INPROGRESS("IN PROGRESS"), //em andamento
    ARCHIVED("ARCHIVED"), //arquivada -> pode recuperar
    DELETED("DELETED"), //deletada - colocar quantos dias falta para recuperar e mandar notificacao para o usuario quando tiver faltando 3 dias

    FINALIZED("FINALIZED"); //finalizada


    private String status_wishlist_event;

    private StatusWishlistEvent(String status_wishlist_event) {this.status_wishlist_event = status_wishlist_event;}

    public String getStatus_wishlist() {
        return status_wishlist_event;
    }

}
