package com.rserenity.barcodeservice.data.entity.repository;

import com.rserenity.barcodeservice.data.entity.BarcodeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BarcodeRepository extends JpaRepository<BarcodeEntity,String> {

    List<BarcodeEntity> getBarcodeEntitiesByProductId(Long productId);
}
