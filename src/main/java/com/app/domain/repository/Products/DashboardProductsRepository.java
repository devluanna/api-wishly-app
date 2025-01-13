package com.app.domain.repository.Products;

import com.app.domain.model.DashboardWishlist.DashboardProducts;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DashboardProductsRepository extends JpaRepository<DashboardProducts, Integer> {
}
