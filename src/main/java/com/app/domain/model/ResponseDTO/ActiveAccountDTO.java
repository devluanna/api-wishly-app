package com.app.domain.model.ResponseDTO;

import com.app.domain.model.Status;

public record ActiveAccountDTO(Integer user_id, String email, String confirm_code_activation, Status status) {
}
