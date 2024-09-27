package com.app.domain.model.Product;

import com.app.domain.model.Wishlist.Wishlist;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;

@Entity
@Data
public class ReferenceLinks {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id_link;
    private String name_link;
    private String reference_link;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_wishlist")
    @ToString.Exclude
    private Wishlist wishlist;

}
