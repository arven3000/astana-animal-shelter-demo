package com.aas.astanaanimalshelterdemo.botModel.buttonsMenu;

public enum ArrangementCatMenuEnum {
    KITTENS("Для котёнка"),
    CAT_ADULT("Для взрослой кошки "),
    CAT_LIMITED("Для кошки с ограниченными возможностями");

    final String info;

    ArrangementCatMenuEnum(String info) {
        this.info = info;
    }

    public String getInfo() {
        return info;
    }
}
