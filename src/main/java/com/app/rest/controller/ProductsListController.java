package com.app.rest.controller;

import com.app.domain.model.DashboardWishlist.DashboardProducts;
import com.app.domain.model.Product.ProductList;
import com.app.domain.model.Product.RequestsProducts;
import com.app.service.ProductsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/v1/products")
public class ProductsListController {

    @Autowired
    ProductsService productsService;

    @PostMapping("/create/{id_wishlist}/{id_dashboard_products}")
    public ResponseEntity createNewProduct(@PathVariable Integer id_wishlist, @PathVariable Integer id_dashboard_products, @RequestBody ProductList productList, DashboardProducts dashboardProducts) {

        if(id_wishlist == null) {
            return ResponseEntity.badRequest().build();
        }

        ProductList createdProduct = productsService.createProduct(productList, id_wishlist, dashboardProducts, id_dashboard_products);

        return ResponseEntity.ok(createdProduct);
    }


    @PostMapping("/create-guest/{id_wishlist}/{id_dashboard_products}")
    public ResponseEntity createNewProductByGuest(@PathVariable Integer id_wishlist, @PathVariable Integer id_dashboard_products, @RequestBody RequestsProducts productList, DashboardProducts dashboardProducts) {

        if(id_wishlist == null) {
            return ResponseEntity.badRequest().build();
        }

        RequestsProducts createdProduct = productsService.createProductByGuest(productList, id_wishlist, dashboardProducts, id_dashboard_products);

        return ResponseEntity.ok(createdProduct);
    }

}
