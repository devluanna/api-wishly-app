package com.app.domain.repository.Wishlist;

import com.app.domain.model.Wishlist.Tags;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TagsRepository extends JpaRepository<Tags,Integer> {
}
