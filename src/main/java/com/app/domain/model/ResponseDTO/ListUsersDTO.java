package com.app.domain.model.ResponseDTO;

import com.app.domain.model.Status;

public record ListUsersDTO(Integer id_user, String first_name, String last_name, String username, Status status, Integer id_dashboard) {
}
