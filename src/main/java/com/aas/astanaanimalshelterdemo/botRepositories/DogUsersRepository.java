package com.aas.astanaanimalshelterdemo.botRepositories;

import com.aas.astanaanimalshelterdemo.botModel.DogUsers;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface DogUsersRepository extends JpaRepository<DogUsers, Long> {
    Optional<DogUsers> findDogUsersByChatId(Long chatId);
}
