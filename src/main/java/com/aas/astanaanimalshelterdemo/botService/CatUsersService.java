package com.aas.astanaanimalshelterdemo.botService;

import com.aas.astanaanimalshelterdemo.botModel.CatUsers;
import com.aas.astanaanimalshelterdemo.botRepositories.CatUsersRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CatUsersService {
    private final CatUsersRepository catUsersRepository;

    public CatUsersService(CatUsersRepository catUsersRepository) {
        this.catUsersRepository = catUsersRepository;
    }

    public Optional<CatUsers> getUserByChatId(Long chatId) {
        return catUsersRepository.findCatUsersByChatId(chatId);
    }

    public CatUsers save(CatUsers user) {
        return catUsersRepository.save(user);
    }

    public void delete(CatUsers user) {
        catUsersRepository.delete(user);
    }

    public Optional<CatUsers> getUserById(Long chatId) {
        return catUsersRepository.findById(chatId);
    }
}
