package com.aas.astanaanimalshelterdemo.botRepositories;

import com.aas.astanaanimalshelterdemo.botModel.AnimalType;
import com.aas.astanaanimalshelterdemo.botModel.Pet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PetRepository extends JpaRepository<Pet, Long> {
        List<Pet> findPetsByTypeOfAnimal(AnimalType typeOfAnimal);
}
