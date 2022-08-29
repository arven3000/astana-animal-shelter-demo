package com.aas.astanaanimalshelterdemo.botModel;

import javax.persistence.*;
import java.util.Arrays;
import java.util.Objects;

//Таблица со справочной информацией

@Entity
public class Info {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String aboutShelter = "Приют «Счастливый питомец» был создан в 2012 г. усилиями " +
            "нескольких человек и существует исключительно на благотворительные взносы. " + '\'' +
            "Основной деятельностью приюта является спасение, лечение и социализация бездомных " +
            "животных, а также поиск хозяев для них. За это время мы добились немалых успехов: " +
            "нашими усилиями было спасено более 800 животных.";
    private String workMode = "Наш приют можно посетить ежедневно с 09.00 до 21.00.";
    private String address = "Нур-Султан (Астана), ул. Ленина, дом 1.";
    private String contacts = "телефоны: +7 (7172) 34–45–85, +7–707–434–45–85," + '\'' +
            " whatsapp, +7 (7172) 36–54–25," + '\'' +
            " e-mail: happypet@mail.ru.";

    private String safetyPrecautions = "Поездки в приют не стоит бояться: там чисто, " +
            "сотрудники вежливы и открыты к общению, все питомцы привиты и не заразны, " +
            "большинство из них — достаточно социализированы и, безусловно, фотогеничны. " +
            "Однако, чтобы поездка не принесла лишних хлопот, лучше следовать нескольким " +
            "простым советам:" + '\'' +
            "Не стоит открывать вольеры без разрешения администрации и заходить в вольеры " +
            "к животным," +  '\'' +
            "кормить животных пищей «со стола». Если вы хотите угостить животных, то приносите " +
            "специальные лакомства, которые можно купить в зоомагазинах — они не принесут " +
            "никакого вреда здоровью. Лучше не жесткие (например, сушенное легкое говядины), " +
            "так как у некоторых долгожителей приютов плохие зубы, а лакомства они будут " +
            "требовать так же настойчиво, как и молодое поколение.";
    private String datingRules = "Знакомство с животным должно происходить строго в присутствии" +
            "сотрудника приюта. Кормить животное разрешается только специальным кормом." +
            "Постарайтесь быть доброжелательными и в хорошем настроении.";
    private String tipsOfDogHandler = "1. Выбирайте породу по своему характеру." + '\'' +
            "2. Кормить собаку надо после прогулки." + '\'' +
            "3. Перед поездкой познакомьте питомца с контейнером для перевозки." + '\'' +
            "4. Не приучайте питомца брать с земли. Если питомец подбирает с земли, его " +
            "следует отучить." + '\'' +
            "5. Прочитайте книгу Андерса Халгрена «Альфа-синдром: лидерство или " +
            "неоправданная жестокость».";
    private String listOfDogHandler = "1. Иванов Иван, тел. +7(921)555-44-33;" + '\'' +
            "2. Петров Петр, тел. +7(985)444-11-77;" + '\'' +
            "3. Смирнова Ольга, тел. +7(911)888-99-22.";
    private String reasonsForRefusal = "1. Отказ присылать отчеты приюту;" + '\'' +
            "2. Отсутствие собственного жилья и регистрации;" + '\'' +
            "3. Животное забирают в подарок кому-то;" + '\'' +
            "4. Наличие дома большого количества животных;" + '\'' +
            "5. Претендент — пожилой человек, проживающий один.";

    private String listOfDocuments = "1. Паспорт;" + '\'' +
            "2. Фотографии квартры;" + '\'' +
            "3. Фотография подготовленного места для питомца.";
    private String adviceForTransporting = "1. Подготовить контейнер для перевозки;" + '\'' +
            "2. Приучить питомца к контейнеру;" + '\'' +
            "3. Обеспечить контейнер необходимыми постельными принадлежностями" +
            "(подстилками) и кормом.";
    private String adviceForHomeForPuppy = "1. Подготовьте 2 миски: одна для еды," +
            "вторая - для воды;" + '\'' +
            "2. Приобретите ошейник и намордник;" + '\'' +
            "3. Подготовьте подходящую лежанку;" + '\'' +
            "4. Приобретите корм на неделю;" + '\'' +
            "5. Приобретите средства для ухода за шерстью.";
    private String adviceForHomeForAdultDog = "1. Подготовьте 2 миски: одна для еды," +
            "вторая - для воды;" + '\'' +
            "2. Приобретите ошейник и намордник;" + '\'' +
            "3. Подготовьте подходящую лежанку;" + '\'' +
            "4. Приобретите корм на неделю;" + '\'' +
            "5. Приобретите средства для ухода за шерстью.";
    private String adviceForHomeForDogWithDisability = "1. Подготовьте 2 миски: одна для еды," +
            "вторая - для воды;" + '\'' +
            "2. Приобретите памперсы для собак;" + '\'' +
            "3. Подготовьте подходящую лежанку;" + '\'' +
            "4. Приобретите корм на неделю;" + '\'' +
            "5. Приобретите специальную инвалидную тележку для питомца.";

    @Lob
    private byte[] location;

    public Info() {

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAboutShelter() {
        return aboutShelter;
    }

    public void setAboutShelter(String aboutShelter) {
        this.aboutShelter = aboutShelter;
    }

    public String getWorkMode() {
        return workMode;
    }

    public void setWorkMode(String workMode) {
        this.workMode = workMode;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String adress) {
        this.address = adress;
    }

    public String getContacts() {
        return contacts;
    }

    public void setContacts(String contacts) {
        this.contacts = contacts;
    }

    public String getSafetyPrecautions() {
        return safetyPrecautions;
    }

    public void setSafetyPrecautions(String safetyPrecautions) {
        this.safetyPrecautions = safetyPrecautions;
    }

    public String getDatingRules() {
        return datingRules;
    }

    public void setDatingRules(String datingRules) {
        this.datingRules = datingRules;
    }

    public String getTipsOfDogHandler() {
        return tipsOfDogHandler;
    }

    public void setTipsOfDogHandler(String tipsOfDogHandler) {
        this.tipsOfDogHandler = tipsOfDogHandler;
    }

    public String getListOfDogHandler() {
        return listOfDogHandler;
    }

    public void setListOfDogHandler(String listOfDogHandler) {
        this.listOfDogHandler = listOfDogHandler;
    }

    public String getReasonsForRefusal() {
        return reasonsForRefusal;
    }

    public void setReasonsForRefusal(String reasonsForRefusal) {
        this.reasonsForRefusal = reasonsForRefusal;
    }

    public String getListOfDocuments() {
        return listOfDocuments;
    }

    public void setListOfDocuments(String listOfDocuments) {
        this.listOfDocuments = listOfDocuments;
    }

    public String getAdviceForTransporting() {
        return adviceForTransporting;
    }

    public void setAdviceForTransporting(String reccomendForTranspoting) {
        this.adviceForTransporting = reccomendForTranspoting;
    }

    public String getAdviceForHomeForPuppy() {
        return adviceForHomeForPuppy;
    }

    public void setAdviceForHomeForPuppy(String reccomendForHomeForPuppy) {
        this.adviceForHomeForPuppy = reccomendForHomeForPuppy;
    }

    public String getAdviceForHomeForAdultDog() {
        return adviceForHomeForAdultDog;
    }

    public void setAdviceForHomeForAdultDog(String reccomendForHomeForAdultDog) {
        this.adviceForHomeForAdultDog = reccomendForHomeForAdultDog;
    }

    public String getAdviceForHomeForDogWithDisability() {
        return adviceForHomeForDogWithDisability;
    }

    public void setAdviceForHomeForDogWithDisability(String reccomendForHomeForDogWithDisability) {
        this.adviceForHomeForDogWithDisability = reccomendForHomeForDogWithDisability;
    }

    public byte[] getLocation() {
        return location;
    }

    public void setLocation(byte[] location) {
        this.location = location;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Info info = (Info) o;
        return Objects.equals(id, info.id) && Objects.equals(aboutShelter, info.aboutShelter) &&
                Objects.equals(workMode, info.workMode) && Objects.equals(address, info.address) &&
                Objects.equals(contacts, info.contacts) && Objects.equals(safetyPrecautions,
                info.safetyPrecautions) && Objects.equals(datingRules, info.datingRules) &&
                Objects.equals(tipsOfDogHandler, info.tipsOfDogHandler) && Objects.equals(listOfDogHandler,
                info.listOfDogHandler) && Objects.equals(reasonsForRefusal, info.reasonsForRefusal) &&
                Objects.equals(listOfDocuments, info.listOfDocuments) && Objects.equals(adviceForTransporting,
                info.adviceForTransporting) && Objects.equals(adviceForHomeForPuppy,
                info.adviceForHomeForPuppy) && Objects.equals(adviceForHomeForAdultDog,
                info.adviceForHomeForAdultDog) && Objects.equals(adviceForHomeForDogWithDisability,
                info.adviceForHomeForDogWithDisability) && Arrays.equals(location, info.location);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(id, aboutShelter, workMode, address, contacts, safetyPrecautions,
                datingRules, tipsOfDogHandler, listOfDogHandler, reasonsForRefusal, listOfDocuments,
                adviceForTransporting, adviceForHomeForPuppy, adviceForHomeForAdultDog,
                adviceForHomeForDogWithDisability);
        result = 31 * result + Arrays.hashCode(location);
        return result;
    }

    @Override
    public String toString() {
        return "Info{" +
                "id=" + id +
                ", aboutShelter='" + aboutShelter + '\'' +
                ", workMode='" + workMode + '\'' +
                ", address='" + address + '\'' +
                ", contacts='" + contacts + '\'' +
                ", safetyPrecautions='" + safetyPrecautions + '\'' +
                ", datingRules='" + datingRules + '\'' +
                ", tipsOfDogHandler='" + tipsOfDogHandler + '\'' +
                ", listOfDogHandler='" + listOfDogHandler + '\'' +
                ", reasonsForRefusal='" + reasonsForRefusal + '\'' +
                ", listOfDocuments='" + listOfDocuments + '\'' +
                ", adviceForTransporting='" + adviceForTransporting + '\'' +
                ", adviceForHomeForPuppy='" + adviceForHomeForPuppy + '\'' +
                ", adviceForHomeForAdultDog='" + adviceForHomeForAdultDog + '\'' +
                ", adviceForHomeForDogWithDisability='" + adviceForHomeForDogWithDisability + '\'' +
                ", location=" + Arrays.toString(location) +
                '}';
    }
}
