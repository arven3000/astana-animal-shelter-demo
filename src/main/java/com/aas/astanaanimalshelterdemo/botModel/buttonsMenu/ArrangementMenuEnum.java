package com.aas.astanaanimalshelterdemo.botModel.buttonsMenu;

public enum ArrangementMenuEnum {
    PUPPIES("Для щенка"),
    DOG_ADULT("Для взрослой собаки "),
    DOG_LIMITED("Для собаки с ограниченными возможностями"),
    KITTENS("Для котёнка"),
    CAT_ADULT("Для взрослой кошки "),
    CAT_LIMITED("Для кошки с ограниченными возможностями");

    final String info;
    ArrangementMenuEnum(String info) {
        this.info = info;
    }

    public String getInfo() {
        return info;
    }
}
