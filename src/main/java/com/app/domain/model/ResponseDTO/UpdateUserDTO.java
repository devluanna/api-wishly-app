package com.app.domain.model.ResponseDTO;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.Date;

public record UpdateUserDTO(Integer id_user,
                            String first_name,
                            String last_name,
                            String email,
                            String username,
                            @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd") Date date_birthday,
                            String gender) {
}
