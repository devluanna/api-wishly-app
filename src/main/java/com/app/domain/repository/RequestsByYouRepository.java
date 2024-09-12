package com.app.domain.repository;

import com.app.domain.model.RequestsByYou;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RequestsByYouRepository extends JpaRepository<RequestsByYou, Integer> {
}
