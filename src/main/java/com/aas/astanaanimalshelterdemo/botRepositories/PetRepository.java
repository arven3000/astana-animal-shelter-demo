package com.aas.astanaanimalshelterdemo.botRepositories;

import com.aas.astanaanimalshelterdemo.botModel.AnimalType;
import com.aas.astanaanimalshelterdemo.botModel.Pet;
import com.aas.astanaanimalshelterdemo.botModel.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
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

        /**
         * Поиск питомца по типу и отсутсвию владельца
         * @param typeOfAnimal - тип питомца
         * @return - List<Pet>
         */
        @Query("select p from Pet p where p.typeOfAnimal=:typeOfAnimal and p.users is null ")
        List<Pet> findPetsByTypeAndNullUser(@Param("typeOfAnimal") AnimalType typeOfAnimal);
}
