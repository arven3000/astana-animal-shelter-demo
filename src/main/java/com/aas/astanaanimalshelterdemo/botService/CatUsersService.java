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

    /**
     * Поиск владельца кошки по id чата
     * @param chatId
     * @return Optional<CatUsers>
     */
    public Optional<CatUsers> getUserByChatId(Long chatId) {
        return catUsersRepository.findCatUsersByChatId(chatId);
    }

    /**
     * Сохранение владельца кошки
     * @param user
     */
    public CatUsers save(CatUsers user) {
        return catUsersRepository.save(user);
    }

    /**
     * Удаение владельца собаки
     * @param user
     */
    public void delete(CatUsers user) {
        catUsersRepository.delete(user);
    }

    /**
     * Поиск владельца кошки по id
     * @param id
     * @return Optional<CatUsers>
     */
    public Optional<CatUsers> getUserById(Long id) {
        return catUsersRepository.findById(id);
    }
}
