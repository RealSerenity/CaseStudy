package com.rserenity.categoryservice.data.enums;

import com.rserenity.categoryservice.exception.CategoryNotFoundException;

public enum CategoryType {

    Bakliyat("Bakliyat","BK"),
    Meyve("Meyve","ME"),
    Et("Et","ET"),
    Icecek("Icecek","IC"),
    Balik("Balik","BA");

    private String categoryName;
    private String categoryCode;

    public String getCategoryCode(){
        return categoryCode;
    }
    public String getCategoryName(){
        return categoryName;
    }

    CategoryType(String categoryName, String categoryCode){
        this.categoryName = categoryName;
        this.categoryCode = categoryCode;
    }

    public static CategoryType getCategoryType(String categoryCode){
        for(CategoryType type : CategoryType.values()){
            if(type.getCategoryCode().equals(categoryCode)){
                return type;
            }
        }
        throw new CategoryNotFoundException("CategoryType doesn't exist");
    }
}
