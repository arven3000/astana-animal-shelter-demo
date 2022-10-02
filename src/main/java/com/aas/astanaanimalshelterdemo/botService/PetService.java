package com.aas.astanaanimalshelterdemo.botService;

import com.aas.astanaanimalshelterdemo.botModel.AnimalType;
import com.aas.astanaanimalshelterdemo.botModel.Pet;
import com.aas.astanaanimalshelterdemo.botRepositories.PetRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PetService {
    private final PetRepository petRepository;

    public PetService(PetRepository petRepository) {
        this.petRepository = petRepository;
    }

    public List<Pet> getPetsByTypeOfAnimal(AnimalType typeOfAnimal) {
        return petRepository.findPetsByTypeOfAnimal(typeOfAnimal);
    }

    public Optional<Pet> getPetByPetId(Long petId) {
        return petRepository.findById(petId);
    }

    public Optional<Pet> getPetByUserId(Long userId) {
        return petRepository.findByUsersId(userId);
    }

    public Pet save(Pet pet) {
        return petRepository.save(pet);
    }

}
