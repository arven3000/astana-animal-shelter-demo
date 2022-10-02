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
    private String adviceForHomeForBaby;

    //Рекомендации по обустройству дома для взрослой собаки.
    private String adviceForHomeForAdultPet;

    //Рекомендации по обустройству дома для собаки с ограниченными возможностями.
    private String adviceForHomeForPetWithDisability;

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
                String adviceForTransporting, String adviceForHomeForBaby,
                String adviceForHomeForAdultPet, String adviceForHomeForPetWithDisability,
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
        this.adviceForHomeForBaby = adviceForHomeForBaby;
        this.adviceForHomeForAdultPet = adviceForHomeForAdultPet;
        this.adviceForHomeForPetWithDisability = adviceForHomeForPetWithDisability;
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

    public String getAdviceForHomeForBaby() {
        return adviceForHomeForBaby;
    }

    public void setAdviceForHomeForBaby(String reccomendForHomeForPuppy) {
        this.adviceForHomeForBaby = reccomendForHomeForPuppy;
    }

    public String getAdviceForHomeForAdultPet() {
        return adviceForHomeForAdultPet;
    }

    public void setAdviceForHomeForAdultPet(String reccomendForHomeForAdultDog) {
        this.adviceForHomeForAdultPet = reccomendForHomeForAdultDog;
    }

    public String getAdviceForHomeForPetWithDisability() {
        return adviceForHomeForPetWithDisability;
    }

    public void setAdviceForHomeForPetWithDisability(String reccomendForHomeForDogWithDisability) {
        this.adviceForHomeForPetWithDisability = reccomendForHomeForDogWithDisability;
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
                info.adviceForTransporting) && Objects.equals(adviceForHomeForBaby,
                info.adviceForHomeForBaby) && Objects.equals(adviceForHomeForAdultPet,
                info.adviceForHomeForAdultPet) && Objects.equals(adviceForHomeForPetWithDisability,
                info.adviceForHomeForPetWithDisability) && Arrays.equals(location, info.location);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(id, aboutShelter, workMode, address, contacts, safetyPrecautions,
                datingRules, tipsOfDogHandler, listOfDogHandler, reasonsForRefusal, listOfDocuments,
                adviceForTransporting, adviceForHomeForBaby, adviceForHomeForAdultPet,
                adviceForHomeForPetWithDisability);
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
               ", adviceForHomeForPuppy='" + adviceForHomeForBaby + '\'' +
               ", adviceForHomeForAdultDog='" + adviceForHomeForAdultPet + '\'' +
               ", adviceForHomeForDogWithDisability='" + adviceForHomeForPetWithDisability + '\'' +
               ", location=" + Arrays.toString(location) +
               '}';
    }
}
