package com.app.domain.repository.Wishlist;

import com.app.domain.model.Product.ProductList;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductsListRepository extends JpaRepository<ProductList, Integer> {
}
