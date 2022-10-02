package com.aas.astanaanimalshelterdemo.botRepositories;

import com.aas.astanaanimalshelterdemo.botModel.AnimalType;
import com.aas.astanaanimalshelterdemo.botModel.Pet;
import com.aas.astanaanimalshelterdemo.botModel.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PetRepository extends JpaRepository<Pet, Long> {
        /**
         * Поиск питомцев по типу животного.
         */
        List<Pet> findPetsByTypeOfAnimal(AnimalType typeOfAnimal);

        /**
         * Поиск питомца по идентификатору его владельца.
         */
        Optional<Pet> findPetByUsers(Users user);
}
