package com.aas.astanaanimalshelterdemo.botModel;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.pengrad.telegrambot.model.User;

import javax.persistence.*;
import java.util.Collection;
import java.util.Objects;

@Entity
@Table(name = "pets")
public class Pet {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @OneToOne
    private Users usersId;

    @Enumerated(EnumType.STRING)
    private AnimalType typeOfAnimal;

    private int age;

    public Pet() {
    }

    public Pet(Long id, String name, AnimalType typeOfAnimal, int age) {
        this.id = id;
        this.name = name;
        this.typeOfAnimal = typeOfAnimal;
        this.age = age;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public AnimalType getTypeOfAnimal() {
        return typeOfAnimal;
    }

    public void setTypeOfAnimal(AnimalType animalTypeOfAnimal) {
        this.typeOfAnimal = animalTypeOfAnimal;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Pet pet = (Pet) o;
        return age == pet.age && Objects.equals(id, pet.id) && Objects.equals(name, pet.name) && typeOfAnimal == pet.typeOfAnimal;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, typeOfAnimal, age);
    }

    @Override
    public String toString() {
        return "Pet{" +
               "id=" + id +
               ", name='" + name + '\'' +
               ", typeOfAnimal=" + typeOfAnimal +
               ", age=" + age +
               '}';
    }
}
