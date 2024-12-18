package com.app.service.impl;

import com.app.domain.model.DashboardWishlist.DashboardProducts;
import com.app.domain.model.Product.ProductList;
import com.app.domain.model.Wishlist.Wishlist;
import com.app.domain.repository.Wishlist.DashboardProductsRepository;
import com.app.domain.repository.Wishlist.ProductsListRepository;
import com.app.domain.repository.Wishlist.WishlistRepository;
import com.app.service.ProductsService;
import com.app.utils.AuthenticationUtils;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class ProductsServiceImpl implements ProductsService {

    @Autowired
    ProductsListRepository productsListRepository;

    @Autowired
    WishlistRepository wishlistRepository;

    @Autowired
    DashboardProductsRepository dashboardProductsRepository;

    @Override
    @Transactional
    public ProductList createProduct(ProductList productList, Integer id_wishlist, DashboardProducts dashboardProducts) {

        validateUserAndWishlist(id_wishlist);

        // Criar o ProductList
        ProductList newProduct = new ProductList();
        BeanUtils.copyProperties(productList, newProduct);
        newProduct.setDate_product_created(new Date());
        newProduct.setId_wishlist(id_wishlist);

       // newProduct.setDashboardProducts();

        return productsListRepository.save(newProduct);
    }

    private void validateUserAndWishlist(Integer id_wishlist) {

        Wishlist selectedWishlist = findWishlist(id_wishlist);
        Integer idOwner = selectedWishlist.getId_owner();

        validateUserAuthenticated(idOwner);

    }

    private DashboardProducts validateIfExistDashboard(Integer id_wishlist) {

        Wishlist existWishlist = wishlistRepository.findById(id_wishlist)
                .orElseThrow(() -> new EntityNotFoundException("Wishlist not found"));


        DashboardProducts dashboardProductsWishlist = existWishlist.getDashboardAllProducts();
        Integer id = dashboardProductsWishlist.getId_dashboard_products();

        DashboardProducts existDash = dashboardProductsRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Dash not found"));

        return existDash;

    }

    private Wishlist findWishlist(Integer id_wishlist) {

        Wishlist existWishlist = wishlistRepository.findById(id_wishlist)
                .orElseThrow(() -> new EntityNotFoundException("Wishlist not found"));

        return existWishlist;

    }

    private void validateUserAuthenticated(Integer idOwner) {
        AuthenticationUtils.getAuthenticatedUser();
        AuthenticationUtils.validateUser(() -> idOwner);

    }

}
