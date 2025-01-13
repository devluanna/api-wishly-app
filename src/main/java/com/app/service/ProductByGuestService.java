package com.app.service;

import com.app.domain.model.ResponseDTO.ProductRequestDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public interface ProductByGuestService {
    ResponseEntity<?> approveProduct(ProductRequestDTO approveProductDTO);
}
