package com.app.domain.model.ResponseDTO;

import java.util.Date;

public record UserDTO(String first_name, String last_name, String email, String username, Date date_birthday, String gender, String password) {
}
