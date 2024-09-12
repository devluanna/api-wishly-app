package com.app.domain.repository;


import com.app.domain.model.ConnectionsDashboard;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ConnectionsDashboardRepository extends JpaRepository<ConnectionsDashboard, Integer> {
}
