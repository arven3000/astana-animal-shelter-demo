package com.aas.astanaanimalshelterdemo.botModel;

import javax.persistence.*;
import java.util.Arrays;
import java.util.Objects;

//Таблица со справочной информацией
@Entity
public class Info {

    //Идентификатор приюта.
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    //Информация о приюте.
    private String aboutShelter;

    //Режим работы приюта.
    private String workMode;

    //Адрес приюта.
    private String address;

    //Телефон и адрес электронной почты приюта.
    private String contacts;

    //Правила поведения в приюте.
    private String safetyPrecautions;

    //Правила знакомства с питомцами.
    private String datingRules;

    //Советы кинолога.
    private String tipsOfDogHandler;

    //Список кинологов.
    private String listOfDogHandler;

    //Причины для отказа.
    private String reasonsForRefusal;

    //Список необходимых документов.
    private String listOfDocuments;

    //Рекомендации по транспортировке питомца.
    private String adviceForTransporting;

    //Рекомендации по обустройству дома для щенка.
    private String adviceForHomeForPuppy;

    //Рекомендации по обустройству дома для взрослой собаки.
    private String adviceForHomeForAdultDog;

    //Рекомендации по обустройству дома для собаки с ограниченными возможностями.
    private String adviceForHomeForDogWithDisability;

    //Тип файла со схемой проезда.
    private String mediaType;

    //Схема проезда в битах.
    @Lob
    private byte[] location;

    public Info() {

    }

    public Info(Long id, String aboutShelter, String workMode, String address,
                String contacts, String safetyPrecautions, String datingRules,
                String tipsOfDogHandler, String listOfDogHandler,
                String reasonsForRefusal, String listOfDocuments,
                String adviceForTransporting, String adviceForHomeForPuppy,
                String adviceForHomeForAdultDog, String adviceForHomeForDogWithDisability,
                String mediaType, byte[] location) {
        this.id = id;
        this.aboutShelter = aboutShelter;
        this.workMode = workMode;
        this.address = address;
        this.contacts = contacts;
        this.safetyPrecautions = safetyPrecautions;
        this.datingRules = datingRules;
        this.tipsOfDogHandler = tipsOfDogHandler;
        this.listOfDogHandler = listOfDogHandler;
        this.reasonsForRefusal = reasonsForRefusal;
        this.listOfDocuments = listOfDocuments;
        this.adviceForTransporting = adviceForTransporting;
        this.adviceForHomeForPuppy = adviceForHomeForPuppy;
        this.adviceForHomeForAdultDog = adviceForHomeForAdultDog;
        this.adviceForHomeForDogWithDisability = adviceForHomeForDogWithDisability;
        this.mediaType = mediaType;
        this.location = location;
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

    public String getMediaType() {
        return mediaType;
    }

    public void setMediaType(String mediaType) {
        this.mediaType = mediaType;
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
