package com.app.domain.repository;

import com.app.domain.model.RequestsByYou;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface RequestsByYouRepository extends JpaRepository<RequestsByYou, Integer> {

    @Modifying
    @Query("DELETE FROM RequestsByYou r WHERE r.id_requests = :id_requests")
    void deleteByIdRequestsPending(@Param("id_requests") Integer id_requests);

}
