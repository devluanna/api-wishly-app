package com.app.rest.controller;
import com.app.domain.model.ResponseDTO.ProductRequestDTO;
import com.app.service.ProductByGuestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/products/guest")
public class ProductByGuestController {

    @Autowired
    ProductByGuestService productByGuestService;

    @PostMapping("/approve")
    public ResponseEntity<?> approveProduct(@RequestBody ProductRequestDTO approveProductDTO) {

        return productByGuestService.approveProduct(approveProductDTO);

    }


}
