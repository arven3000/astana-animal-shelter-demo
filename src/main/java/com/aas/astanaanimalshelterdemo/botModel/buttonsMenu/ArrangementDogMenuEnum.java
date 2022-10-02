package com.aas.astanaanimalshelterdemo.botModel.buttonsMenu;

public enum ArrangementDogMenuEnum {
    PUPPIES("Для щенка"),
    DOG_ADULT("Для взрослой собаки "),
    DOG_LIMITED("Для собаки с ограниченными возможностями");
    final String info;

    ArrangementDogMenuEnum(String info) {
        this.info = info;
    }

    public String getInfo() {
        return info;
    }
}
