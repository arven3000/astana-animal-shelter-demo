package com.aas.astanaanimalshelterdemo.botModel.buttonsMenu;

public enum AdoptiveParentsMenuEnum {
    RULES("Правила знакомства с животным"),
    DOCUMENTS("Список документов для забора животного из приюта"),
    TRANSPORTATION("Рекомендации по транспортировке животного"),
    ARRANGEMENT("Рекомендации по обустройству"),
    REFUSAL("Список причин отказа в заборе");

    final String info;

    AdoptiveParentsMenuEnum(String info) {
        this.info = info;
    }

    public String getInfo() {
        return info;
    }
}
