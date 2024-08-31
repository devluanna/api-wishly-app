package com.app.domain.model.ResponseDTO;


import com.app.domain.model.Status;
import com.app.domain.model.UserRole;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;


import java.util.Date;


public record UserDTO(
        Integer id_user,
        String first_name,
        String last_name,
        String email,
        String username,
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd") Date date_birthday,
        String gender,
        String password,
        UserRole role,
        Status status
) {


}
