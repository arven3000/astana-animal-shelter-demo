package com.aas.astanaanimalshelterdemo.botModel;


import javax.persistence.*;
import java.util.Arrays;
import java.util.Objects;

@Entity
public class Avatar {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String filePath;

    @Lob
    private byte[] foto1;
//    @Lob
//    private byte[] foto2;
//    @Lob
//    private byte[] foto3;

    @OneToOne
    private Pet pet;

    public Avatar(Long id, String filePath, byte[] foto1, Pet pet) {
        this.id = id;
        this.filePath = filePath;
        this.foto1 = foto1;
        this.pet = pet;
    }

    public Avatar() {

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

    public byte[] getFoto1() {
        return foto1;
    }

    public void setFoto1(byte[] foto1) {
        this.foto1 = foto1;
    }

//    public byte[] getFoto2() {
//        return foto2;
//    }
//
//    public void setFoto2(byte[] foto2) {
//        this.foto2 = foto2;
//    }
//
//    public byte[] getFoto3() {
//        return foto3;
//    }
//
//    public void setFoto3(byte[] foto3) {
//        this.foto3 = foto3;
//    }

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
        return Objects.equals(id, avatar.id) && Objects.equals(filePath, avatar.filePath)
                && Arrays.equals(foto1, avatar.foto1) && Objects.equals(pet, avatar.pet);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(id, filePath, pet);
        result = 31 * result + Arrays.hashCode(foto1);
//        result = 31 * result + Arrays.hashCode(foto2);
//        result = 31 * result + Arrays.hashCode(foto3);
        return result;
    }

    @Override
    public String toString() {
        return "Avatar{" +
                "id=" + id +
                ", filePath='" + filePath + '\'' +
                ", foto1=" + Arrays.toString(foto1) +
//                ", foto2=" + Arrays.toString(foto2) +
//                ", foto3=" + Arrays.toString(foto3) +
                ", pet=" + pet +
                '}';
    }
}
