package com.app.domain.repository;

import com.app.domain.model.RequestsByOthers;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface RequestsByOthersRepository extends JpaRepository<RequestsByOthers, Integer> {

    @Modifying
    @Query("DELETE FROM RequestsByOthers r WHERE r.id_requests_pending = :id_requests_pending")
    void deleteByIdRequestsPending(@Param("id_requests_pending") Integer id_requests_pending);

}
