package com.aas.astanaanimalshelterdemo.botRepositories;

import com.aas.astanaanimalshelterdemo.botModel.Users;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UsersRepository extends JpaRepository<Users, Long> {

    /**
     * Поиск пользователей по id чата
     * @param chatId - id чата
     * @return Optional<Users>
     */
    Optional<Users> findUsersByChatId(Long chatId);
}
