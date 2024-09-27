package com.app.domain.repository;


import com.app.domain.model.ResponseDTO.ListUsersDTO;
import com.app.domain.model.ResponseDTO.UserDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import com.app.domain.model.Users;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface UserRepository extends JpaRepository<Users, Integer> {

    @Query("SELECT u FROM Users u WHERE u.email = :email")
    Users findByEmail(@Param("email") String email);

    @Query("SELECT u FROM Users u WHERE u.username = :username")
    Users findByUsername(@Param("username") String username);

    @Query("SELECT NEW com.app.domain.model.ResponseDTO.UserDTO(u.id_user, u.first_name, u.last_name, u.username, u.email, u.date_birthday, u.gender, u.password, u.role, u.status, u.connectionsDashboard.id_dashboard) FROM Users u WHERE u.id_user = :id_user")
    UserDTO findUserById(@Param("id_user") Integer id_user);


    @Query("SELECT NEW com.app.domain.model.ResponseDTO.ListUsersDTO(u.id_user, u.first_name, u.last_name, u.username, u.status, u.connectionsDashboard.id_dashboard) FROM Users u")
    List<ListUsersDTO> findAllUsers();
}
