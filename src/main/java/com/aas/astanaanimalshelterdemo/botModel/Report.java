package com.aas.astanaanimalshelterdemo.botModel;

import javax.persistence.*;
import java.util.Arrays;
import java.util.Objects;

@Entity
public class Report {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private Users user;

    @ManyToOne
    @JoinColumn(name = "pet_id")
    private Pet pet;

    private byte[] photo;
    private String diet;
    private String stateOfHealth;
    private String habits;

    public Report() {

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Users getUser() {
        return user;
    }

    public void setUser(Users user) {
        this.user = user;
    }

    public Pet getPet() {
        return pet;
    }

    public void setPet(Pet pet) {
        this.pet = pet;
    }

    public byte[] getPhoto() {
        return photo;
    }

    public void setPhoto(byte[] photo) {
        this.photo = photo;
    }

    public String getDiet() {
        return diet;
    }

    public void setDiet(String diet) {
        this.diet = diet;
    }

    public String getStateOfHealth() {
        return stateOfHealth;
    }

    public void setStateOfHealth(String stateOfHealth) {
        this.stateOfHealth = stateOfHealth;
    }

    public String getHabits() {
        return habits;
    }

    public void setHabits(String habits) {
        this.habits = habits;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Report report = (Report) o;
        return Objects.equals(id, report.id) && Objects.equals(user, report.user)
                && Objects.equals(pet, report.pet) && Arrays.equals(photo, report.photo)
                && Objects.equals(diet, report.diet) && Objects.equals(stateOfHealth,
                report.stateOfHealth) && Objects.equals(habits, report.habits);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(id, user, pet, diet, stateOfHealth, habits);
        result = 31 * result + Arrays.hashCode(photo);
        return result;
    }

    @Override
    public String toString() {
        return "Report{" +
                "id=" + id +
                ", user=" + user +
                ", pet=" + pet +
                ", photo=" + Arrays.toString(photo) +
                ", diet='" + diet + '\'' +
                ", stateOfHealth='" + stateOfHealth + '\'' +
                ", habits='" + habits + '\'' +
                '}';
    }
}
