package com.aas.astanaanimalshelterdemo.botModel;

import javax.persistence.*;
import java.util.Objects;

/**
 * Сущность для хранения отчетов опекунов.
 */
@Entity
public class Report {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Идентификатор опекуна.
     */
    @ManyToOne
    @JoinColumn(name = "users_id")
    private Users user;

    /**
     * Идентификатор соответствующего питомца.
     */
    @ManyToOne
    @JoinColumn(name = "pet_id")
    private Pet pet;

    /**
     * Идентификатор фоторафии, направленной опекуном.
     */
    @OneToOne
    private Avatar avatar;

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

    public Avatar getAvatar() {
        return avatar;
    }

    public void setAvatar(Avatar avatar) {
        this.avatar = avatar;
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
        return Objects.equals(id, report.id) && Objects.equals(user, report.user) && Objects.equals(pet, report.pet) && Objects.equals(avatar, report.avatar) && Objects.equals(diet, report.diet) && Objects.equals(stateOfHealth, report.stateOfHealth) && Objects.equals(habits, report.habits);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, user, pet, avatar, diet, stateOfHealth, habits);
    }

    @Override
    public String toString() {
        return "Report{" +
                "id=" + id +
                ", user=" + user +
                ", pet=" + pet +
                ", avatar=" + avatar +
                ", diet='" + diet + '\'' +
                ", stateOfHealth='" + stateOfHealth + '\'' +
                ", habits='" + habits + '\'' +
                '}';
    }
}
