package com.app.service;

import com.app.domain.model.DashboardWishlist.DashboardProducts;
import com.app.domain.model.Product.ProductList;
import com.app.domain.model.Product.RequestsProducts;
import org.springframework.stereotype.Service;

@Service
public interface ProductsService {
    ProductList createProduct(ProductList productList, Integer id_wishlist, DashboardProducts dashboardProducts,  Integer id_dashboard_products);

    RequestsProducts createProductByGuest(RequestsProducts productList, Integer id_wishlist, DashboardProducts dashboardProducts, Integer id_dashboard_products);
}
