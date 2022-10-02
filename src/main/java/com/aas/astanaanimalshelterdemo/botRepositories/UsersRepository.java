package com.aas.astanaanimalshelterdemo.botRepositories;

import com.aas.astanaanimalshelterdemo.botModel.DogUsers;
import com.aas.astanaanimalshelterdemo.botModel.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface UsersRepository extends JpaRepository<Users, Long> {
    Optional<Users> findUsersByChatId(Long chatId);
}
