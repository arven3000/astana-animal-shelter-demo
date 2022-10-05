package com.aas.astanaanimalshelterdemo.botService;

import com.aas.astanaanimalshelterdemo.botModel.DogUsers;
import com.aas.astanaanimalshelterdemo.botRepositories.DogUsersRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static com.aas.astanaanimalshelterdemo.botService.ServiceTestConstants.ANNA;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
class DogUsersServiceTest {

    @Mock
    private DogUsersRepository dogUsersRepository;

    @InjectMocks
    private DogUsersService out;

    @BeforeEach
    void init() {
        ANNA.setChatId(1L);
        ANNA.setId(1L);
        ANNA.setUserName("Anna");
    }

    @Test
    void getUserByChatId() {
        when(dogUsersRepository.findDogUsersByChatId(Mockito.anyLong())).thenReturn(Optional.of(ANNA));
        Optional<DogUsers> userByChatId = out.getUserByChatId(1L);
        assertEquals(ANNA.getId(), userByChatId.get().getId());
        verify(dogUsersRepository, times(1)).findDogUsersByChatId(anyLong());
    }

    @Test
    void save() {
        when(dogUsersRepository.save(Mockito.any())).thenReturn(Mockito.any());
        out.save(ANNA);
        verify(dogUsersRepository, times(1)).save(any());
    }

    @Test
    void delete() {
        doNothing().when(dogUsersRepository).delete(Mockito.any());
        out.delete(ANNA);
        verify(dogUsersRepository, times(1)).delete(any());
    }

    @Test
    void getUserById() {
        when(dogUsersRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(ANNA));
        Optional<DogUsers> userByChatId = out.getUserById(1L);
        assertEquals(ANNA.getId(), userByChatId.get().getId());
        verify(dogUsersRepository, times(1)).findById(anyLong());
    }
}