package com.aas.astanaanimalshelterdemo.botService;

import com.aas.astanaanimalshelterdemo.botModel.AnimalType;
import com.aas.astanaanimalshelterdemo.botModel.Pet;
import com.aas.astanaanimalshelterdemo.botModel.Users;
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

    public List<Pet> getPetsByTypeOfAnimalAndUsersNull(AnimalType typeOfAnimal) {
        return petRepository.findPetsByTypeAndNullUser(typeOfAnimal);
    }

    public Optional<Pet> getPetByPetId(Long petId) {
        return petRepository.findById(petId);
    }

    public Optional<Pet> getPetByUsers(Users user) {
        return petRepository.findPetByUsers(user);
    }

    public Pet save(Pet pet) {
        return petRepository.save(pet);
    }

}
