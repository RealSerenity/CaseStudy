package com.rserenity.categoryservice.data.entity.repository;

import com.rserenity.categoryservice.data.entity.CategoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryRepository extends JpaRepository<CategoryEntity, Long> {

    CategoryEntity getByCategoryName(String categoryName);

}
