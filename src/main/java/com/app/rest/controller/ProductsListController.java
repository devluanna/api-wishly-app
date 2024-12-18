package com.app.rest.controller;

import com.app.domain.model.DashboardWishlist.DashboardProducts;
import com.app.domain.model.Product.ProductList;
import com.app.service.ProductsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/v1/products")
public class ProductsListController {

    @Autowired
    ProductsService productsService;

    @PostMapping("/create/{id_wishlist}")
    public ResponseEntity createNewProduct(@PathVariable Integer id_wishlist, @RequestBody ProductList productList, DashboardProducts dashboardProducts) {

        if(id_wishlist == null) {
            return ResponseEntity.badRequest().build();
        }

        ProductList createdProduct = productsService.createProduct(productList, id_wishlist, dashboardProducts);

        return ResponseEntity.ok(createdProduct);

    }

}
