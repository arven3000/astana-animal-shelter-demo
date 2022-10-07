package com.aas.astanaanimalshelterdemo.botModel;


import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "dog_users")
@DiscriminatorValue(value = "dog_user")
public class DogUsers extends Users {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private LocalDateTime dataTimeOfPet;
    @Enumerated(EnumType.STRING)
    private UserType role;
    @OneToOne
    private Pet pet;

    public DogUsers() {

    }

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    public LocalDateTime getDataTimeOfPet() {
        return dataTimeOfPet;
    }

    public void setDataTimeOfPet(LocalDateTime dataTimeOfPet) {
        this.dataTimeOfPet = dataTimeOfPet;
    }

    public UserType getRole() {
        return role;
    }

    public void setRole(UserType role) {
        this.role = role;
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
        if (!super.equals(o)) return false;
        DogUsers dogUsers = (DogUsers) o;
        return Objects.equals(id, dogUsers.id) && Objects.equals(dataTimeOfPet, dogUsers.dataTimeOfPet) && role == dogUsers.role && Objects.equals(pet, dogUsers.pet);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), id, dataTimeOfPet, role, pet);
    }

    @Override
    public String toString() {
        return "DogUsers{" +
               "id=" + id +
               ", dataTimeOfPet=" + dataTimeOfPet +
               ", role=" + role +
               '}';
    }
}
