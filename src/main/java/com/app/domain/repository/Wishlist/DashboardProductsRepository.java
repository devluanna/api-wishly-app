package com.app.domain.repository.Wishlist;

import com.app.domain.model.DashboardWishlist.DashboardProducts;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DashboardProductsRepository extends JpaRepository<DashboardProducts, Integer> {
}
