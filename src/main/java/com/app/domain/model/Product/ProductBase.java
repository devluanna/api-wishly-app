package com.app.domain.model.Product;

import com.app.domain.model.DashboardWishlist.DashboardProducts;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;

import java.util.Date;
import java.util.List;

@MappedSuperclass
@Data
public abstract class ProductBase {

    private String product_name;
    private String product_description;
    private String category;
    private String uriImg;
    private Integer price;
    private Integer id_wishlist;
    private Integer indicated_user_id;
    private String indicated_username;
    private StatusProduct status_product;
    private Boolean send_reminder;
    private Date date_product_created;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_dashboard_products")
    @ToString.Exclude
    private DashboardProducts dashboardProducts;

   @ToString.Exclude
   @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
   //@JoinColumn(name = "id_dashboard_products")
    private List<ReferenceLinks> referenceLinks;


}
