package com.aas.astanaanimalshelterdemo.botModel;


import javax.persistence.*;
import java.util.Arrays;
import java.util.Objects;

//Сущность для хранения фотографий питомцев.
@Entity
public class Avatar {

    //Идентификатор строки.
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    //Пути к файлам с фотографиями
    private String filePath1;
    private String filePath2;
    private String filePath3;

    //Уменьшенные фотографии питомцев
    @Lob
    private byte[] foto1;

    //Тип файла foto1.
    private String mediaType1;

    @Lob
    private byte[] foto2;

    //Тип файла foto1.
    private String mediaType2;
    @Lob
    private byte[] foto3;

    //Тип файла foto1.
    private String mediaType3;

    @OneToOne
    private Pet pet;

    public Avatar() {

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFilePath1() {
        return filePath1;
    }

    public void setFilePath1(String filePath) {
        this.filePath1 = filePath1;
    }

    public String getFilePath2() {
        return filePath2;
    }

    public void setFilePath2(String filePath2) {
        this.filePath2 = filePath2;
    }

    public String getFilePath3() {
        return filePath3;
    }

    public void setFilePath3(String filePath3) {
        this.filePath3 = filePath3;
    }

    public byte[] getFoto1() {
        return foto1;
    }

    public void setFoto1(byte[] foto1) {
        this.foto1 = foto1;
    }

    public byte[] getFoto2() {
        return foto2;
    }

    public void setFoto2(byte[] foto2) {
        this.foto2 = foto2;
    }

    public byte[] getFoto3() {
        return foto3;
    }

    public void setFoto3(byte[] foto3) {
        this.foto3 = foto3;
    }

    public String getMediaType1() {
        return mediaType1;
    }

    public void setMediaType1(String mediaType1) {
        this.mediaType1 = mediaType1;
    }

    public String getMediaType2() {
        return mediaType2;
    }

    public void setMediaType2(String mediaType2) {
        this.mediaType2 = mediaType2;
    }

    public String getMediaType3() {
        return mediaType3;
    }

    public void setMediaType3(String mediaType3) {
        this.mediaType3 = mediaType3;
    }

    public Pet getPet() {
        return pet;
    }

    public void setPet(Pet pet) {
        this.pet = pet;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Avatar avatar = (Avatar) o;
        return Objects.equals(id, avatar.id) && Objects.equals(filePath1, avatar.filePath1) && Objects.equals(filePath2, avatar.filePath2) && Objects.equals(filePath3, avatar.filePath3) && Arrays.equals(foto1, avatar.foto1) && Objects.equals(mediaType1, avatar.mediaType1) && Arrays.equals(foto2, avatar.foto2) && Objects.equals(mediaType2, avatar.mediaType2) && Arrays.equals(foto3, avatar.foto3) && Objects.equals(mediaType3, avatar.mediaType3) && Objects.equals(pet, avatar.pet);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(id, filePath1, filePath2, filePath3, mediaType1, mediaType2, mediaType3, pet);
        result = 31 * result + Arrays.hashCode(foto1);
        result = 31 * result + Arrays.hashCode(foto2);
        result = 31 * result + Arrays.hashCode(foto3);
        return result;
    }

    @Override
    public String toString() {
        return "Avatar{" +
                "id=" + id +
                ", filePath1='" + filePath1 + '\'' +
                ", filePath2='" + filePath2 + '\'' +
                ", filePath3='" + filePath3 + '\'' +
                ", foto1=" + Arrays.toString(foto1) +
                ", mediaType1='" + mediaType1 + '\'' +
                ", foto2=" + Arrays.toString(foto2) +
                ", mediaType2='" + mediaType2 + '\'' +
                ", foto3=" + Arrays.toString(foto3) +
                ", mediaType3='" + mediaType3 + '\'' +
                ", pet=" + pet +
                '}';
    }
}
