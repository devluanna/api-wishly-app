package com.app.service;

import com.app.domain.model.DashboardWishlist.DashboardProducts;
import com.app.domain.model.Product.ProductList;
import org.springframework.stereotype.Service;

@Service
public interface ProductsService {
    ProductList createProduct(ProductList productList, Integer id_wishlist, DashboardProducts dashboardProducts);
}
