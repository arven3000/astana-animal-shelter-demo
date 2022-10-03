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

    /**
     * Поиск питомца по типу
     * @param typeOfAnimal
     * @return List<Pet>
     */
    public List<Pet> getPetsByTypeOfAnimal(AnimalType typeOfAnimal) {
        return petRepository.findPetsByTypeOfAnimal(typeOfAnimal);
    }

    /**
     * Поиск питомца по типу и отсутствию владельца
     * @param typeOfAnimal
     * @return List<Pet>
     */
    public List<Pet> getPetsByTypeOfAnimalAndUsersNull(AnimalType typeOfAnimal) {
        return petRepository.findPetsByTypeAndNullUser(typeOfAnimal);
    }

    /**
     * Поиск питомца по id
     * @param petId
     * @return Optional<Pet>
     */
    public Optional<Pet> getPetByPetId(Long petId) {
        return petRepository.findById(petId);
    }

    /**
     * Поиск питомца по владельцу
     * @param user
     * @return Optional<Pet>
     */
    public Optional<Pet> getPetByUsers(Users user) {
        return petRepository.findPetByUsers(user);
    }

    /**
     * Изменениеб сохранение питомца
     * @param pet
     * @return Pet
     */
    public Pet save(Pet pet) {
        return petRepository.save(pet);
    }

}
