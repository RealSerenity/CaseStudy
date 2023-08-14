package com.rserenity.categoryservice.data.entity;

import com.rserenity.categoryservice.data.enums.CategoryType;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Data
@NoArgsConstructor
@Log4j2
@Builder
@Entity
@Table(name = "category")
public class CategoryEntity {

    @Id
    @Column(name = "id",nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "categoryName",nullable = false)
    private String categoryName;

    @Column(name = "categoryType",nullable = false)
    private CategoryType categoryType;

    public CategoryEntity(Long id, String categoryName, CategoryType categoryType) {
        this.id = id;
        this.categoryName = categoryName;
        this.categoryType = categoryType;
    }
}
