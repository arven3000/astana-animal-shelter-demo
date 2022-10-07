package com.aas.astanaanimalshelterdemo.botRepositories;

import com.aas.astanaanimalshelterdemo.botModel.CatUsers;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface CatUsersRepository extends JpaRepository<CatUsers, Long> {

    /**
     * Поиск питомца по id чата
     * @param chatId - id чата
     * @return - Optional<DogUsers>
     */
    Optional<CatUsers> findCatUsersByChatId(Long chatId);

    /**
     * Поиск всех владельцев кошек
     * @return - List<Long>
     */
    @Query("select u.id from CatUsers u where u.pet is not null")
    List<Long> findAllIdWithPet();
}
