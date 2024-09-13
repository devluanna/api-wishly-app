package com.app.domain.repository;

import com.app.domain.model.Connections;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ConnectionsRepository extends JpaRepository<Connections, Integer> {

    @Modifying
    @Query("DELETE FROM Connections r WHERE r.id_connection = :id_connection")
    void deleteByIdConnection(@Param("id_connection") Integer id_connection);

}
