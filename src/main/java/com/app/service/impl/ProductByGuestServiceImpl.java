package com.app.service.impl;

import com.app.domain.model.DashboardWishlist.DashboardProducts;
import com.app.domain.model.Product.ProductList;
import com.app.domain.model.Product.ReferenceLinks;
import com.app.domain.model.Product.RequestsProducts;
import com.app.domain.model.Product.StatusProduct;
import com.app.domain.model.ResponseDTO.ProductRequestDTO;
import com.app.domain.model.ResponseDTO.SuccessResponse;
import com.app.domain.model.Users;
import com.app.domain.model.Wishlist.Wishlist;
import com.app.domain.repository.Products.DashboardProductsRepository;
import com.app.domain.repository.Products.ProductsListRepository;
import com.app.domain.repository.Products.ReferenceLinksRepository;
import com.app.domain.repository.Products.RequestsProductsRepository;
import com.app.domain.repository.Wishlist.WishlistRepository;
import com.app.exception.BusinessRuleException;
import com.app.service.ProductByGuestService;
import com.app.utils.AuthenticationUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class ProductByGuestServiceImpl implements ProductByGuestService {

    @Autowired
    ProductsListRepository productsListRepository;

    @Autowired
    WishlistRepository wishlistRepository;

    @Autowired
    DashboardProductsRepository dashboardProductsRepository;

    @Autowired
    ReferenceLinksRepository referenceLinksRepository;

    @Autowired
    RequestsProductsRepository requestsProductsRepository;

    private Wishlist findWishlist(Integer idWishlist) {
        return wishlistRepository.findById(idWishlist)
                .orElseThrow(() -> new EntityNotFoundException("Wishlist not found"));
    }

    @Transactional
    @Override
    public ResponseEntity<?> approveProduct(ProductRequestDTO approveProductDTO) {
        Users authenticatedUser = AuthenticationUtils.getAuthenticatedUser();
        AuthenticationUtils.validateUser(() -> approveProductDTO.id_owner());

        Wishlist wishlistExist = findWishlist(approveProductDTO.id_wishlist());

        DashboardProducts dashboardProductsWishlist = wishlistExist.getDashboardAllProducts();

        List<RequestsProducts> listProductsPending = dashboardProductsWishlist.getRequestsProducts();

        RequestsProducts productSelected = listProductsPending.stream()
                .filter(reqExisting -> approveProductDTO.id_indicated_by().equals(reqExisting.getIndicated_user_id())
                        && approveProductDTO.id_product_request().equals(reqExisting.getId_product_request()))
                .findFirst()
                .orElseThrow(() -> new BusinessRuleException("Requested product not found or already processed.", HttpStatus.BAD_REQUEST));

        processProductApproval(productSelected, approveProductDTO, dashboardProductsWishlist);

        return ResponseEntity.ok(new SuccessResponse("Product was successfully approved!"));
    }

    @Transactional
    private void processProductApproval(RequestsProducts productSelected, ProductRequestDTO approveProductDTO, DashboardProducts dashboardProductsWishlist) {
        if (approveProductDTO.id_product_request().equals(productSelected.getId_product_request())) {
            ProductList newProductApprove = new ProductList();
            BeanUtils.copyProperties(productSelected, newProductApprove);
            newProductApprove.setApproval_date(new Date());
            newProductApprove.setStatus_product(StatusProduct.APPROVED);
            newProductApprove.setIsProductByIndication(true);

            ProductList saved = productsListRepository.save(newProductApprove);

            validateLinks(productSelected, newProductApprove, approveProductDTO, dashboardProductsWishlist, saved);


        } else {
            throw new BusinessRuleException("Unable to approve selected product, please try again or check error.", HttpStatus.BAD_REQUEST);
        }
    }
    @Transactional
    private void validateLinks(RequestsProducts productSelected, ProductList newProductApprove,
                               ProductRequestDTO approveProductDTO,
                               DashboardProducts dashboardProductsWishlist,
                               ProductList saved) {

        List<ReferenceLinks> newReferenceLinks = productSelected.getReferenceLinks().stream()
                .map(link -> {
                    ReferenceLinks newLink = new ReferenceLinks();
                    newLink.setName_link(link.getName_link());
                    newLink.setReference_link(link.getReference_link());
                    newLink.setProductList(saved);
                    newLink.setRequestsProduct(null);
                    return newLink;
                })
                .collect(Collectors.toList());

        referenceLinksRepository.saveAll(newReferenceLinks);

        newProductApprove.setReferenceLinks(newReferenceLinks);

        productsListRepository.save(newProductApprove);

        removeOldLinks(productSelected);

        removeProductRequest(dashboardProductsWishlist, approveProductDTO);
    }

    private void removeOldLinks(RequestsProducts productSelected) {
        List<ReferenceLinks> listAllLinksRequestProduct = productSelected.getReferenceLinks();


        if (listAllLinksRequestProduct == null || listAllLinksRequestProduct.isEmpty()) {
            System.out.println("No old link to remove product  " + productSelected.getId_product_request());
            return;
        }

        listAllLinksRequestProduct.forEach(link -> link.setRequestsProduct(null));

        productSelected.getReferenceLinks().clear();

        requestsProductsRepository.save(productSelected);

        referenceLinksRepository.deleteAll(listAllLinksRequestProduct);
    }


    @Transactional
    private void removeProductRequest(DashboardProducts dashboardProductsWishlist, ProductRequestDTO approveProductDTO) {

        List<RequestsProducts> listProductsPending = dashboardProductsWishlist.getRequestsProducts();

        RequestsProducts productToDelete = null;
        for (RequestsProducts req : listProductsPending) {
            if (approveProductDTO.id_indicated_by().equals(req.getIndicated_user_id()) &&
                    approveProductDTO.id_product_request().equals(req.getId_product_request())) {

                productToDelete = req;
                break;
            }
        }

        if (productToDelete == null) {
            throw new BusinessRuleException("Product not found", HttpStatus.BAD_REQUEST);
        }

        Integer idRequest = productToDelete.getId_product_request();
        requestsProductsRepository.deleteByIdRequests(idRequest);
    }


}
