package com.aas.astanaanimalshelterdemo.botRepositories;

import com.aas.astanaanimalshelterdemo.botModel.DogUsers;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface DogUsersRepository extends JpaRepository<DogUsers, Long> {
    Optional<DogUsers> findDogUsersByChatId(Long chatId);

    @Query("select u.id from DogUsers u where u.pet is not null")
    List<Long> findAllIdWithPet();
}
