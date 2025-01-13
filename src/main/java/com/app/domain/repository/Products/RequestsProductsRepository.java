package com.app.domain.repository.Products;

import com.app.domain.model.Product.RequestsProducts;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface RequestsProductsRepository extends JpaRepository <RequestsProducts, Integer> {

    @Modifying
    @Query("DELETE FROM RequestsProducts r WHERE r.id_product_request = :id_product_request")
    void deleteByIdRequests(@Param("id_product_request") Integer id_product_request);


}
