package com.aas.astanaanimalshelterdemo.botService;

import com.aas.astanaanimalshelterdemo.botModel.AnimalType;
import com.aas.astanaanimalshelterdemo.botModel.Pet;
import com.aas.astanaanimalshelterdemo.botModel.Users;
import com.aas.astanaanimalshelterdemo.botRepositories.PetRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PetService {
    private static final Logger LOGGER = LoggerFactory.getLogger(PetService.class);

    private final PetRepository petRepository;

    public PetService(PetRepository petRepository) {
        this.petRepository = petRepository;
    }

    /**
     * Поиск питомца по типу
     * @param typeOfAnimal - тип животного
     * @return List<Pet>
     */
    public List<Pet> getPetsByTypeOfAnimal(AnimalType typeOfAnimal) {
        LOGGER.info("Was invoked method for get pet by type of animal.");
        return petRepository.findPetsByTypeOfAnimal(typeOfAnimal);
    }

    /**
     * Поиск питомца по типу и отсутствию владельца
     * @param typeOfAnimal - тип животного
     * @return List<Pet>
     */
    public List<Pet> getPetsByTypeOfAnimalAndUsersNull(AnimalType typeOfAnimal) {
        LOGGER.info("Was invoked method for get pet by type of animal and absence of owner.");
        return petRepository.findPetsByTypeAndNullUser(typeOfAnimal);
    }

    /**
     * Поиск питомца по id
     * @param petId - id животного
     * @return Optional<Pet>
     */
    public Optional<Pet> getPetByPetId(Long petId) {
        LOGGER.info("Was invoked method for get pet by id.");
        return petRepository.findById(petId);
    }

    /**
     * Поиск питомца по владельцу
     * @param user - пользователь
     * @return Optional<Pet>
     */
    public Optional<Pet> getPetByUsers(Users user) {
        LOGGER.info("Was invoked method for get pet by user.");
        return petRepository.findPetByUsers(user);
    }

    /**
     * Изменениеб сохранение питомца
     * @param pet - животное
     * @return Pet
     */
    public Pet save(Pet pet) {
        LOGGER.info("Was invoked method for save pet.");
        return petRepository.save(pet);
    }

}
