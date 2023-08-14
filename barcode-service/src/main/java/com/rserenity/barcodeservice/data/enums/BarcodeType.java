package com.rserenity.barcodeservice.data.enums;

import com.rserenity.barcodeservice.exception.BarcodeTypeNotFoundException;

import static com.rserenity.barcodeservice.constants.MessageConstants.BARCODETYPE_DOESNT_EXIST;

public enum BarcodeType {
    ProductBarcode("ProductBarcode",new CategoryUnitType[]{
            new CategoryUnitType("ME","Kilogram"),
            new CategoryUnitType("ME","Adet"),
            new CategoryUnitType("BK","Adet"),
            new CategoryUnitType("BK","Kilogram"),
            new CategoryUnitType("IC","Adet"),
            new CategoryUnitType("IC","Kilogram"),
            new CategoryUnitType("BA","Kilogram")}, 9),
    CashierBarcode("CashierBarcode",new CategoryUnitType[]{new CategoryUnitType("BA", "Adet"),
            new CategoryUnitType("ME", "Kilogram")}, 4),
    // scalebarcode uzunluğu 8 karakter ilk 5 karakter ürün kodundan geldiği için barcode length 3 olarak verdim
    ScaleBarcode("ScaleBarcode",new CategoryUnitType[]{new CategoryUnitType("BA","Kilogram"),
            new CategoryUnitType("ET","Adet"),
            new CategoryUnitType("ET","Kilogram")}, 3);

    private String barcodeTypeText;
    private CategoryUnitType[] applicableCategories;
    private int barcodeRandomPartLength;

    public int getBarcodeRandomPartLength(){
        return barcodeRandomPartLength;
    }

    BarcodeType(String barcodeTypeText, CategoryUnitType[] categories, int barcodeRandomPartLength){
        this.barcodeRandomPartLength = barcodeRandomPartLength;
        this.barcodeTypeText = barcodeTypeText;
        this.applicableCategories = categories;
    }

    public CategoryUnitType[] getApplicableCategories(){
        return applicableCategories;
    }
    public String getBarcodeTypeText(){
        return barcodeTypeText;
    }

    public static BarcodeType findBarcodeTypeByCategoryCode(String categoryCode){
        for (BarcodeType barcodeType: BarcodeType.values()
        ) {
            for (CategoryUnitType categoryUnitType: barcodeType.getApplicableCategories()
            ) {
                if (categoryUnitType.category().equals(categoryCode)){
                    return barcodeType;
                }
            }
        }
        throw new BarcodeTypeNotFoundException(BARCODETYPE_DOESNT_EXIST);
    }

    public static BarcodeType getBarcodeType(String barcodeTypeText){
        for (BarcodeType type : BarcodeType.values()){
            if(type.getBarcodeTypeText().equals(barcodeTypeText)){
                return type;
            }
        }
        throw new BarcodeTypeNotFoundException(BARCODETYPE_DOESNT_EXIST);
    }

}

