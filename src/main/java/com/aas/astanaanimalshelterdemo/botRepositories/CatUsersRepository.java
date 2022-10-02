package com.aas.astanaanimalshelterdemo.botRepositories;

import com.aas.astanaanimalshelterdemo.botModel.CatUsers;
import liquibase.pro.packaged.Q;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface CatUsersRepository extends JpaRepository<CatUsers, Long> {
    Optional<CatUsers> findCatUsersByChatId(Long chatId);

    @Query("select u.id from CatUsers u where u.pet is not null")
    List<Long> findAllIdWithPet();
}
