package com.app.domain.repository;

import com.app.domain.model.ResponseDTO.UserDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import com.app.domain.model.User;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

    @Query("SELECT u FROM User u WHERE u.username = :username")
    User findByUsername(@Param("username") String username);

    @Query("SELECT NEW com.app.domain.model.ResponseDTO.UserDTO(u.id_user, u.first_name, u.last_name, u.username, u.email, u.date_birthday, u.gender, u.password) FROM Users u WHERE u.id_user = :id_user")
    UserDTO findUserById(@Param("id_user") Integer id_user);

}
