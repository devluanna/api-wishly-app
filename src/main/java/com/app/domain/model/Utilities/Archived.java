package com.app.domain.model.Utilities;

import com.app.domain.model.Wishlist.StatusWishlistEvent;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

import java.util.Date;

@Entity
@Data
public class Archived {

    //Arquivados

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id_archived;

    private Integer id_object; //ID DA WISHLIST OU EVENTO

    private String name_object; //NOME DA WISHLIST OU EVENTO

    private String category;//CATEGORIA DA WISHLIST OU EVENTO

    private TypesObj typesObj; //TIPO WISHLIST OU EVENTO

    private Date date_of_archived; //DATA QUE ARQUIVOU

    private StatusWishlistEvent statusWishlistEvent; //STATUS DA WISHLIST OU EVENTO


}
