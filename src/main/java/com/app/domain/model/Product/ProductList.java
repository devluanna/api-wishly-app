package com.app.domain.model.Product;
import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;

import java.util.Date;
import java.util.List;

@Entity
@Data
public class ProductList extends ProductBase {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id_product;

    private Boolean isProductByIndication;
    private String reservation_username;
    private Boolean isTheProductReserved;
    private Integer reservation_user_id;
    private Date approval_date;

    public ProductList() {}

   // @ToString.Exclude
   // @OneToMany(mappedBy = "productList", cascade = CascadeType.ALL)
   // private List<ReferenceLinks> referenceLinks;
}
