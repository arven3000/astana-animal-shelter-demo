package com.aas.astanaanimalshelterdemo.botRepositories;

import com.aas.astanaanimalshelterdemo.botModel.CatUsers;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CatUsersRepository extends JpaRepository<CatUsers, Long> {
    Optional<CatUsers> findCatUsersByChatId(Long chatId);
}
