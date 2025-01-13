package com.app.domain.model.Product;
import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;

import java.util.List;

@Entity
@Data
public class RequestsProducts extends ProductBase {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id_product_request;

   // @ToString.Exclude
   // @OneToMany(mappedBy = "requestsProduct", cascade = CascadeType.ALL)
   // private List<ReferenceLinks> referenceLinks;

}
