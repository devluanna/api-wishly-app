package com.app.domain.model.ResponseDTO;

import com.app.domain.model.Status;

public record ActiveAccountDTO(Integer user_id, String email, String code_account_activation, String confirm_code_activation, String password, String confirm_password, Status status) {
}
