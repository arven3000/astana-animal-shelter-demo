package com.aas.astanaanimalshelterdemo.botService;

import com.aas.astanaanimalshelterdemo.botModel.DogUsers;
import com.aas.astanaanimalshelterdemo.botRepositories.DogUsersRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class DogUsersService {
    private static final Logger LOGGER = LoggerFactory.getLogger(DogUsersService.class);

    private final DogUsersRepository dogUsersRepository;

    public DogUsersService(DogUsersRepository dogUsersRepository) {
        this.dogUsersRepository = dogUsersRepository;
    }

    /**
     * Поиск владельца собак по id чата
     * @param chatId - id чата
     * @return Optional<DogUsers>
     */
    public Optional<DogUsers> getUserByChatId(Long chatId) {
        LOGGER.info("Was invoked method for get dogUser by chatId.");
        return dogUsersRepository.findDogUsersByChatId(chatId);
    }

    /**
     * Сохранение владельца собаки
     *
     * @param user - пользователь
     */
    public void save(DogUsers user) {
        LOGGER.info("Was invoked method for save dogUser.");
        dogUsersRepository.save(user);
    }

    /**
     * Удаление владельца собаки
     * @param user - пользователь
     */
    public void delete(DogUsers user) {
        LOGGER.info("Was invoked method for delete dogUser.");
        dogUsersRepository.delete(user);
    }

    /**
     * Поиск владельца собак по id
     * @param id - id пользователя
     * @return Optional<DogUsers>
     */
    public Optional<DogUsers> getUserById(Long id) {
        LOGGER.info("Was invoked method for get dogUser by id.");
        return dogUsersRepository.findById(id);
    }

}
