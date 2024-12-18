package com.app.domain.model.ResponseDTO;

public record ApproveSubscriberDTO(Integer id_dashboard_requester,
                                   Integer id_user_requester,
                                   Integer approval_dashboard_id,
                                   Integer approval_user_id,
                                   Integer id_wishlist_approve) {
}
