package com.aas.astanaanimalshelterdemo.botModel;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Сущность для хранения отчетов опекунов.
 */
@Entity
@Table(name = "reports")
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
    private Pet petId;

    /**
     * Описание рациона питания питомца.
     */
    private String diet;

    /**
     * Описание общего самочувствия питомца.
     */
    private String stateOfHealth;

    /**
     * Изменение в поведении: отказ от старых привычек, приобретение новых.
     */
    private String habits;

    /**
     * Дата отчета
     */
    private LocalDateTime dataTime;

    public Report() {

    }

    public Report(Long id, Users user, Pet petId, String diet, String stateOfHealth,
                  String habits, LocalDateTime dataTime) {
        this.id = id;
        this.user = user;
        this.petId = petId;
        this.diet = diet;
        this.stateOfHealth = stateOfHealth;
        this.habits = habits;
        this.dataTime = dataTime;
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

    public Pet getPetId() {
        return petId;
    }

    public void setPet(Pet pet) {
        this.petId = pet;
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

    public LocalDateTime getDataTime() {
        return dataTime;
    }

    public void setDataTime(LocalDateTime dataTime) {
        this.dataTime = dataTime;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Report report = (Report) o;
        return Objects.equals(id,
                report.id) && Objects.equals(user, report.user)
               && Objects.equals(petId,
                report.petId) && Objects.equals(diet,
                report.diet) && Objects.equals(stateOfHealth,
                report.stateOfHealth) && Objects.equals(habits,
                report.habits) && Objects.equals(dataTime,
                report.dataTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, user, petId, diet, stateOfHealth, habits, dataTime);
    }

    @Override
    public String toString() {
        return "Report{" +
               "id=" + id +
               ", user=" + user +
               ", petId=" + petId +
               ", diet='" + diet + '\'' +
               ", stateOfHealth='" + stateOfHealth + '\'' +
               ", habits='" + habits + '\'' +
               ", dataTime=" + dataTime +
               '}';
    }
}
