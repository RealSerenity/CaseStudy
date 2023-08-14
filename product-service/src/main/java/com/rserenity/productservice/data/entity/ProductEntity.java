package com.rserenity.productservice.data.entity;

import com.rserenity.productservice.data.enums.UnitType;
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
@Table(name = "product")
public class ProductEntity {


    @Id
    @Column(name = "id",nullable = false,unique = true)
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    Long id;

    @Column(name = "code",nullable = false,unique = true)
    String code;

    @Column(name = "name",nullable = false,unique = true)
    String name;
    @Column(name = "categoryId",nullable = false)
    Long categoryId;
    @Column(name = "price",nullable = false)
    Double price;
    @Column(name = "brand",nullable = false)
    String brand;
    @Column(name = "unitType",nullable = false)
    UnitType unitType;

    public ProductEntity(Long id, String code, String name, Long categoryId, Double price, String brand, UnitType unitType) {
        this.id = id;
        this.code = code;
        this.name = name;
        this.categoryId = categoryId;
        this.price = price;
        this.brand = brand;
        this.unitType = unitType;
    }
    public ProductEntity(String code, String name, Long categoryId, Double price, String brand, UnitType unitType) {
        this.code = code;
        this.name = name;
        this.categoryId = categoryId;
        this.price = price;
        this.brand = brand;
        this.unitType = unitType;
    }
}
