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

    public Optional<DogUsers> getUserByChatId(Long chatId) {
        return dogUsersRepository.findDogUsersByChatId(chatId);
    }

    public DogUsers save(DogUsers user) {
        return dogUsersRepository.save(user);
    }

    public void delete(DogUsers user) {
        dogUsersRepository.delete(user);
    }

    public Optional<DogUsers> getUserById(Long chatId) {
        return dogUsersRepository.findById(chatId);
    }

}
