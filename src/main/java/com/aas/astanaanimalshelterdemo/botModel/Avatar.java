package com.aas.astanaanimalshelterdemo.botModel;


import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.util.Arrays;
import java.util.Objects;

/**
 * Сущность для хранения фотографий питомцев.
 */

@Entity
public class Avatar {

    /**
     * Идентификатор строки.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Уменьшенные фотографии питомцев
     */
    @Lob
    private byte[] photo1;
    @Lob
    private byte[] photo2;
    @Lob
    private byte[] photo3;

    /**
     * Типы файлов фотографий.
     */
    private String mediaType1;
    private String mediaType2;
    private String mediaType3;

    /**
     * Пути к файлам с фотографиями.
     */
    private String filePath1;
    private String filePath2;
    private String filePath3;

    /**
     * Размеры файлов исходных фотографий.
     */
    private Long fileSize1;
    private Long fileSize2;
    private Long fileSize3;


    /**
     * Соответствующий данному аватару питомец.
     */
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

    public void setFilePath1(String filePath1) {
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

    public byte[] getPhoto1() {
        return photo1;
    }

    public void setPhoto1(byte[] photo1) {
        this.photo1 = photo1;
    }

    public byte[] getPhoto2() {
        return photo2;
    }

    public void setPhoto2(byte[] photo2) {
        this.photo2 = photo2;
    }

    public byte[] getPhoto3() {
        return photo3;
    }

    public void setPhoto3(byte[] photo3) {
        this.photo3 = photo3;
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

    public Long getFileSize1() {
        return fileSize1;
    }

    public void setFileSize1(Long fileSize1) {
        this.fileSize1 = fileSize1;
    }

    public Long getFileSize2() {
        return fileSize2;
    }

    public void setFileSize2(Long fileSize2) {
        this.fileSize2 = fileSize2;
    }

    public Long getFileSize3() {
        return fileSize3;
    }

    public void setFileSize3(Long fileSize3) {
        this.fileSize3 = fileSize3;
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
        return Objects.equals(id, avatar.id) && Objects.equals(filePath1, avatar.filePath1)
                && Objects.equals(filePath2, avatar.filePath2) && Objects.equals(filePath3,
                avatar.filePath3) && Arrays.equals(photo1, avatar.photo1)
                && Objects.equals(mediaType1, avatar.mediaType1) && Arrays.equals(photo2,
                avatar.photo2) && Objects.equals(mediaType2, avatar.mediaType2)
                && Arrays.equals(photo3, avatar.photo3) && Objects.equals(mediaType3,
                avatar.mediaType3) && Objects.equals(pet, avatar.pet);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(id, filePath1, filePath2, filePath3, mediaType1, mediaType2, mediaType3, pet);
        result = 31 * result + Arrays.hashCode(photo1);
        result = 31 * result + Arrays.hashCode(photo2);
        result = 31 * result + Arrays.hashCode(photo3);
        return result;
    }

    @Override
    public String toString() {
        return "Avatar{" +
                "id=" + id +
                ", filePath1='" + filePath1 + '\'' +
                ", filePath2='" + filePath2 + '\'' +
                ", filePath3='" + filePath3 + '\'' +
                ", photo1=" + Arrays.toString(photo1) +
                ", mediaType1='" + mediaType1 + '\'' +
                ", photo2=" + Arrays.toString(photo2) +
                ", mediaType2='" + mediaType2 + '\'' +
                ", photo3=" + Arrays.toString(photo3) +
                ", mediaType3='" + mediaType3 + '\'' +
                ", pet=" + pet +
                '}';
    }
}
