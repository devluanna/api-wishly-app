package com.app.domain.repository;

import com.app.domain.model.Connections;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ConnectionsRepository extends JpaRepository<Connections, Integer> {




}
