package com.app.service.impl;

import com.app.domain.model.DashboardWishlist.DashboardProducts;
import com.app.domain.model.Product.*;
import com.app.domain.model.Users;
import com.app.domain.model.Utilities.DashboardRequestsAndPending;
import com.app.domain.model.Utilities.Pending;
import com.app.domain.model.Wishlist.Wishlist;
import com.app.domain.model.Wishlist.WishlistSubscribers;
import com.app.domain.repository.Products.DashboardProductsRepository;
import com.app.domain.repository.Products.ProductsListRepository;
import com.app.domain.repository.Products.ReferenceLinksRepository;
import com.app.domain.repository.Products.RequestsProductsRepository;
import com.app.domain.repository.Wishlist.WishlistRepository;
import com.app.domain.repository.Wishlist.WishlistSubscribersRepository;
import com.app.exception.BusinessRuleException;
import com.app.service.ProductsService;
import com.app.utils.AuthenticationUtils;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

@Service
public class ProductsServiceImpl implements ProductsService {

    @Autowired
    ProductsListRepository productsListRepository;

    @Autowired
    WishlistRepository wishlistRepository;

    @Autowired
    DashboardProductsRepository dashboardProductsRepository;

    @Autowired
    RequestsProductsRepository requestsProductsRepository;

    @Autowired
    WishlistSubscribersRepository wishlistSubscribersRepository;

    @Autowired
    ReferenceLinksRepository referenceLinksRepository;

    @Autowired
    private StepFunctionService stepFunctionService;

    private <T extends ProductBase> T configureProduct(T product, DashboardProducts dashboardProducts, Integer id_dashboard_products,
                                                       Integer id_wishlist) {
        validateDashboardProducts(id_dashboard_products);

        T newProduct;
        try {

            newProduct = (T) product.getClass().getDeclaredConstructor().newInstance();

            BeanUtils.copyProperties(product, newProduct);
            newProduct.setDate_product_created(new Date());
            newProduct.setDashboardProducts(dashboardProducts);
            newProduct.setId_wishlist(id_wishlist);

        } catch (Exception e) {
            throw new RuntimeException("Error creating new instance of " + product.getClass().getName(), e);
        }

        return newProduct;
    }


    @Override
    @Transactional
    public ProductList createProduct(ProductList product, Integer id_wishlist, DashboardProducts dashboardProducts, Integer id_dashboard_products) {
        ProductList newProduct = configureProduct(product, dashboardProducts, id_dashboard_products, id_wishlist);
        newProduct.setStatus_product(StatusProduct.AVAILABLE);
        newProduct.setIsProductByIndication(false);

        Wishlist selectedWishlist = findWishlist(id_wishlist);
        Integer idOwner = selectedWishlist.getId_owner();

        AuthenticationUtils.getAuthenticatedUser();
        AuthenticationUtils.validateUser(() -> idOwner);

        ProductList savedProduct = productsListRepository.save(newProduct);

        validateReferenceLinks(savedProduct);

        return savedProduct;
    }



    @Transactional
    private void validateReferenceLinks(Object object) {
        List<ReferenceLinks> referenceLinks = null;

        if (object instanceof ProductList) {
            referenceLinks = new ArrayList<>(((ProductList) object).getReferenceLinks());
        } else if (object instanceof RequestsProducts) {
            referenceLinks = new ArrayList<>(((RequestsProducts) object).getReferenceLinks());
        }

        if (referenceLinks != null && !referenceLinks.isEmpty()) {
            for (ReferenceLinks link : referenceLinks) {
                if (object instanceof ProductList) {
                    link.setProductList((ProductList) object);
                } else if (object instanceof RequestsProducts) {
                    link.setRequestsProduct((RequestsProducts) object);
                    link.setId_request(((RequestsProducts) object).getId_product_request());
                }
                referenceLinksRepository.save(link);
            }
        }
    }



    private void validateDashboardProducts(Integer id_dashboard_products) {
        dashboardProductsRepository.findById(id_dashboard_products)
                .orElseThrow(() -> new EntityNotFoundException("Dashboard not found"));
    }


    @Transactional
    @Override
    public RequestsProducts createProductByGuest(RequestsProducts productList, Integer id_wishlist, DashboardProducts dashboardProducts, Integer id_dashboard_products) {
        RequestsProducts newProduct = configureProduct(productList, dashboardProducts, id_dashboard_products, id_wishlist);
        newProduct.setStatus_product(StatusProduct.REQUESTSENT);

        validateUserAndWishlist(id_wishlist, newProduct);

        RequestsProducts productSavedByGuest = requestsProductsRepository.save(newProduct);

        validateReferenceLinks(productSavedByGuest);

        return productSavedByGuest;
    }

    private void validateUserAndWishlist(Integer id_wishlist, RequestsProducts newProduct) {

        Wishlist selectedWishlist = findWishlist(id_wishlist);
        Integer idOwner = selectedWishlist.getId_owner();

        Users userAuthenticated = AuthenticationUtils.getAuthenticatedUser();

        if(!userAuthenticated.getId_user().equals(idOwner)) {
            validateUserIsSubscribe(selectedWishlist, userAuthenticated, idOwner, newProduct);
        }

    }

    private void validateUserIsSubscribe(Wishlist selectedWishlist, Users userAuthenticated, Integer idOwner, RequestsProducts newProduct) {

        List<WishlistSubscribers> listSubscribers = selectedWishlist.getWishlistSubscribers();

        boolean isUserSubscribed = false;

        for(WishlistSubscribers list: listSubscribers) {
            if (list.getId_user().equals(userAuthenticated.getId_user())) {
                isUserSubscribed = true;
                addProductByRecommendation(selectedWishlist, idOwner, newProduct, userAuthenticated);
                break;
            }
        }

        if (!isUserSubscribed) {
            throw new BusinessRuleException("Action not allowed. You must be subscribed to the wishlist.", HttpStatus.FORBIDDEN);
        }
    }

    private void addProductByRecommendation(Wishlist selectedWishlist, Integer idOwner, RequestsProducts newProduct, Users userAuthenticated) {

        if (selectedWishlist.getEnableProductsByRecommendation()) {
            newProduct.setIndicated_user_id(userAuthenticated.getId_user());
            newProduct.setIndicated_username(userAuthenticated.getUsername());

            System.out.println("ACIONOU A STEP FUNCTION!");

        } else {
            System.out.println("FALSE");
            AuthenticationUtils.validateUser(() -> idOwner);
            throw new BusinessRuleException("This wishlist does not allow guests to add products based on recommendations.", HttpStatus.BAD_REQUEST);
        }
    }


    private Wishlist findWishlist(Integer id_wishlist) {

        Wishlist existWishlist = wishlistRepository.findById(id_wishlist)
                .orElseThrow(() -> new EntityNotFoundException("Wishlist not found"));

        return existWishlist;

    }



}
