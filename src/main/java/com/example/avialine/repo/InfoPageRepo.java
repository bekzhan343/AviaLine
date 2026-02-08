package com.example.avialine.repo;

import com.example.avialine.model.entity.InfoPage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InfoPageRepo extends JpaRepository<InfoPage, Integer> {

    @Query("SELECT i FROM InfoPage i LEFT join FETCH i.subInfos")
    List<InfoPage> findAllWithSubInfos();
}
