package com.app.domain.model.Utilities;

import com.app.domain.model.Wishlist.StatusWishlistEvent;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Date;

@Entity
@Data
public class RecycleBinBackup {

    //Wishlists ficarao 7 dias aqui

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id_backup;

    private Integer id_object; //ID DA WISHLIST OU EVENTO

    private String name_object; //NOME DA WISHLIST OU EVENTO

    private String category;//CATEGORIA DA WISHLIST OU EVENTO

    private TypesObj typesObj; //TIPO WISHLIST OU EVENTO

    private Date date_of_deletion; //DATA QUE DELETOU

    private Date date_for_recovery; //DATA FINAL CONTANDO 7 DIAS PRA RECUPERAR

    private LocalDateTime expirationDays; //7 DIAS PRA EXPIRAR E EXCLUIR DEFINITAMENTE

    private StatusWishlistEvent statusWishlistEvent; //STATUS DA WISHLIST OU EVENTO



}
