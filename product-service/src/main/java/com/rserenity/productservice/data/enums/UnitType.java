package com.rserenity.productservice.data.enums;

import com.rserenity.productservice.exception.UnitTypeNotFoundException;

import static com.rserenity.productservice.constants.MessageConstants.UNITTYPE_DOESNT_EXIST;

public enum UnitType {
    Kilogram("Kilogram"),
    Adet("Adet");

    private String unitTypeText;

    private UnitType(String unitTypeText){
        this.unitTypeText = unitTypeText;
    }

    public String getUnitTypeText(){
        return unitTypeText;
    }

    public static UnitType getUnitType(String unitTypeText){
        for (UnitType type : UnitType.values()){
            if(type.getUnitTypeText().equals(unitTypeText)){
                return type;
            }
        }
        throw new UnitTypeNotFoundException(UNITTYPE_DOESNT_EXIST);
    }
}
