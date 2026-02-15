package com.example.avialine.repo;

import com.example.avialine.model.entity.ServiceInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ServiceInfoRepo extends JpaRepository<ServiceInfo, Integer> {

    @Query("SELECT si FROM ServiceInfo si JOIN FETCH si.service")
    List<ServiceInfo> getAllServiceInfoWithService();
}
