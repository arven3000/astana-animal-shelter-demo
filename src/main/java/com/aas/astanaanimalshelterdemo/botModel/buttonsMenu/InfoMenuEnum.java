package com.aas.astanaanimalshelterdemo.botModel.buttonsMenu;

public enum InfoMenuEnum {
    WORKING("Расписание работы, схема проезда"),
    SAFETY("Правила по технике безопасности"),
    CONTACT("Оставить контактные данные для связи");
    final String info;

    InfoMenuEnum(String info) {
        this.info = info;
    }

    public String getInfo() {
        return info;
    }
}
