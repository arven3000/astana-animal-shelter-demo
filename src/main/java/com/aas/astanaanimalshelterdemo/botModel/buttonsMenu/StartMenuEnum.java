package com.aas.astanaanimalshelterdemo.botModel.buttonsMenu;

public enum StartMenuEnum {
    INFORMATION("Узнать информацию о приюте"),
    TAKE("Как взять питомца из приюта"),
    REPORT("Прислать отчет о питомце"),
    CALL("Позвать волонтера"),
    CHOOSING("Перейти к выбору питомца");
    final String info;

    StartMenuEnum(String info) {
        this.info = info;
    }

    public String getInfo() {
        return info;
    }
}
