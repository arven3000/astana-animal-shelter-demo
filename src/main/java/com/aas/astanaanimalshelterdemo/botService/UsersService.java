package com.aas.astanaanimalshelterdemo.botService;

import com.aas.astanaanimalshelterdemo.botModel.Users;
import com.aas.astanaanimalshelterdemo.botRepositories.UsersRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UsersService {
    private final UsersRepository usersRepository;

    public UsersService(UsersRepository usersRepository) {
        this.usersRepository = usersRepository;
    }

    public List<Users> getUsersWithPet() {
        List<Users> allUsers = usersRepository.findAll();
        List<Users> usersWithPet = allUsers.stream()
                .filter(e->e.getPet() != null).toList();
        return allUsers;
    }
}
