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
    private byte[] photo;

    /**
     * Типы файлов фотографий.
     */
    private String mediaType;

    /**
     * Пути к файлам с фотографиями.
     */
    private String filePath;

    /**
     * Размеры файлов исходных фотографий.
     */
    private Long fileSize;

    /**
     * Соответствующий данным аватарам питомец.
     */
    @ManyToOne
    @JoinColumn(name = "pet_id")
    private Pet pet;

    @OneToOne
    private Report report;

    public Avatar() {

    }

    public Avatar(Long id, byte[] photo, String mediaType, String filePath, Long fileSize, Pet pet, Report report) {
        this.id = id;
        this.photo = photo;
        this.mediaType = mediaType;
        this.filePath = filePath;
        this.fileSize = fileSize;
        this.pet = pet;
        this.report = report;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public byte[] getPhoto() {
        return photo;
    }

    public void setPhoto(byte[] photo) {
        this.photo = photo;
    }

    public String getMediaType() {
        return mediaType;
    }

    public void setMediaType(String mediaType) {
        this.mediaType = mediaType;
    }

    public Long getFileSize() {
        return fileSize;
    }

    public void setFileSize(Long fileSize) {
        this.fileSize = fileSize;
    }

    public Pet getPet() {
        return pet;
    }

    public void setPet(Pet pet) {
        this.pet = pet;
    }

    public Report getReport() {
        return report;
    }

    public void setReport(Report report) {
        this.report = report;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Avatar avatar = (Avatar) o;
        return Objects.equals(id, avatar.id) && Arrays.equals(photo, avatar.photo)
                && Objects.equals(mediaType, avatar.mediaType)
                && Objects.equals(filePath, avatar.filePath)
                && Objects.equals(fileSize, avatar.fileSize)
                && Objects.equals(pet, avatar.pet)
                && Objects.equals(report, avatar.report);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(id, mediaType, filePath, fileSize, pet, report);
        result = 31 * result + Arrays.hashCode(photo);
        return result;
    }

    @Override
    public String toString() {
        return "Avatar{" +
                "id=" + id +
                ", photo=" + Arrays.toString(photo) +
                ", mediaType='" + mediaType + '\'' +
                ", filePath='" + filePath + '\'' +
                ", fileSize=" + fileSize +
                ", pet=" + pet +
                ", report=" + report +
                '}';
    }
}
