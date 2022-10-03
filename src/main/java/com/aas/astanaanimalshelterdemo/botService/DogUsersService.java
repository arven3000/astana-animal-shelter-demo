package com.aas.astanaanimalshelterdemo.botService;

import com.aas.astanaanimalshelterdemo.botModel.DogUsers;
import com.aas.astanaanimalshelterdemo.botRepositories.DogUsersRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class DogUsersService {

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
        return dogUsersRepository.findDogUsersByChatId(chatId);
    }

    /**
     * Сохранение владельца собаки
     *
     * @param user - пользователь
     */
    public void save(DogUsers user) {
        dogUsersRepository.save(user);
    }

    /**
     * Удаление владельца собаки
     * @param user - пользователь
     */
    public void delete(DogUsers user) {
        dogUsersRepository.delete(user);
    }

    /**
     * Поиск владельца собак по id
     * @param id - id пользователя
     * @return Optional<DogUsers>
     */
    public Optional<DogUsers> getUserById(Long id) {
        return dogUsersRepository.findById(id);
    }

}
