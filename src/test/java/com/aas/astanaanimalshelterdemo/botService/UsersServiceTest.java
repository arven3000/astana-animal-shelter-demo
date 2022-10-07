package com.aas.astanaanimalshelterdemo.botService;

import com.aas.astanaanimalshelterdemo.botModel.Users;
import com.aas.astanaanimalshelterdemo.botRepositories.CatUsersRepository;
import com.aas.astanaanimalshelterdemo.botRepositories.DogUsersRepository;
import com.aas.astanaanimalshelterdemo.botRepositories.UsersRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static com.aas.astanaanimalshelterdemo.botService.ServiceTestConstants.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UsersServiceTest {

    @Mock
    private CatUsersRepository catUsersRepository;

    @Mock
    private DogUsersRepository dogUsersRepository;

    @Mock
    private UsersRepository usersRepository;

    @InjectMocks
    private UsersService out;

    @BeforeEach
    void init() {
        MAKSIM.setChatId(1L);
        MAKSIM.setId(1L);
        MAKSIM.setUserName("Maksim");
    }

    @Test
    void save() {
        when(usersRepository.save(Mockito.any())).thenReturn(Mockito.any());
        out.save(MAKSIM);
        verify(usersRepository, times(1)).save(any());
    }

    @Test
    void delete() {
        doNothing().when(usersRepository).delete(Mockito.any());
        out.delete(MAKSIM);
        verify(usersRepository, times(1)).delete(any());
    }

    @Test
    void getUsersByChatId() {
        when(usersRepository.findUsersByChatId(Mockito.anyLong())).thenReturn(Optional.of(MAKSIM));
        Optional<Users> usersByChatId = out.getUsersByChatId(1L);
        assertEquals(MAKSIM.getId(), usersByChatId.get().getId());
        verify(usersRepository, times(1)).findUsersByChatId(anyLong());
    }
}