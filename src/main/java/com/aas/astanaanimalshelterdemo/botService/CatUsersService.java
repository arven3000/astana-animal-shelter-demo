package com.aas.astanaanimalshelterdemo.botService;

import com.aas.astanaanimalshelterdemo.botModel.CatUsers;
import com.aas.astanaanimalshelterdemo.botRepositories.CatUsersRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CatUsersService {
    private static final Logger LOGGER = LoggerFactory.getLogger(CatUsersService.class);

    private final CatUsersRepository catUsersRepository;

    public CatUsersService(CatUsersRepository catUsersRepository) {
        this.catUsersRepository = catUsersRepository;
    }

    /**
     * Поиск владельца кошки по id чата
     * @param chatId - id чата
     * @return Optional<CatUsers>
     */
    public Optional<CatUsers> getUserByChatId(Long chatId) {
        LOGGER.info("Was invoked method for get catUser by chatId.");
        return catUsersRepository.findCatUsersByChatId(chatId);
    }

    /**
     * Сохранение владельца кошки
     * @param user - пользователь
     */
    public CatUsers save(CatUsers user) {
        LOGGER.info("Was invoked method for save catUser.");
        return catUsersRepository.save(user);
    }

    /**
     * Удаение владельца собаки
     * @param user - пользователь
     */
    public void delete(CatUsers user) {
        LOGGER.info("Was invoked method for delete catUser.");
        catUsersRepository.delete(user);
    }

    /**
     * Поиск владельца кошки по id
     * @param id  - id пользователя
     * @return Optional<CatUsers>
     */
    public Optional<CatUsers> getUserById(Long id) {
        LOGGER.info("Was invoked method for get catUser by id.");
        return catUsersRepository.findById(id);
    }
}
