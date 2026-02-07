package com.example.avialine.repo;

import com.example.avialine.model.entity.Banner;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BannerRepo extends JpaRepository<Banner, Integer> {


}
