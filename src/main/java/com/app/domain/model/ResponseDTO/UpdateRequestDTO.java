package com.app.domain.model.ResponseDTO;

import com.app.domain.model.StatusConnections;


public record UpdateRequestDTO(StatusConnections statusConnections, Integer id_dashboard_requester, Integer id_user_requester, Integer approval_dashboard_id, Integer approval_user_id) {
}
