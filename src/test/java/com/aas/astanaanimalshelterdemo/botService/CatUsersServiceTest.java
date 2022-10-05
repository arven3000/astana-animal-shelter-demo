package com.aas.astanaanimalshelterdemo.botService;

import com.aas.astanaanimalshelterdemo.botModel.CatUsers;
import com.aas.astanaanimalshelterdemo.botRepositories.CatUsersRepository;
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
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CatUsersServiceTest {

    @Mock
    private CatUsersRepository catUsersRepository;

    @InjectMocks
    private CatUsersService out;

    @BeforeEach
    void init() {
        IVAN.setChatId(1L);
        IVAN.setId(1L);
        IVAN.setUserName("Ivan");
    }

    @Test
    void getUserByChatId() {
        when(catUsersRepository.findCatUsersByChatId(Mockito.anyLong())).thenReturn(Optional.of(IVAN));
        Optional<CatUsers> userByChatId = out.getUserByChatId(1L);
        assertEquals(IVAN.getId(), userByChatId.get().getId());
        verify(catUsersRepository, times(1)).findCatUsersByChatId(anyLong());
    }

    @Test
    void save() {
        when(catUsersRepository.save(Mockito.any())).thenReturn(IVAN);
        CatUsers catUsers = out.save(IVAN);
        assertEquals("Ivan", catUsers.getUserName());
        verify(catUsersRepository, times(1)).save(any());
    }

    @Test
    void delete() {
        doNothing().when(catUsersRepository).delete(Mockito.any());
        out.delete(IVAN);
        verify(catUsersRepository, times(1)).delete(any());
    }

    @Test
    void getUserById() {
        when(catUsersRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(IVAN));
        Optional<CatUsers> userByChatId = out.getUserById(1L);
        assertEquals(IVAN.getId(), userByChatId.get().getId());
        verify(catUsersRepository, times(1)).findById(anyLong());
    }
}