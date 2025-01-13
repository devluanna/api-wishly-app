package com.app.domain.repository.Products;

import com.app.domain.model.Product.ReferenceLinks;
import com.app.domain.model.Product.RequestsProducts;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ReferenceLinksRepository extends JpaRepository<ReferenceLinks, Integer> {

    @Transactional
    @Modifying
    @Query("DELETE FROM ReferenceLinks r WHERE r.id_request = :id_request")
    void deleteByIdRequests(@Param("id_request") Integer id_request);





}
