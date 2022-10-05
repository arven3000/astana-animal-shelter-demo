package com.aas.astanaanimalshelterdemo.botService;


import com.aas.astanaanimalshelterdemo.botModel.Avatar;
import com.aas.astanaanimalshelterdemo.botRepositories.AvatarRepository;
import com.aas.astanaanimalshelterdemo.botRepositories.PetRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static com.aas.astanaanimalshelterdemo.botService.ServiceTestConstants.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AvatarServiceTest {

    @Mock
    private AvatarRepository avatarRepository;
    @Mock
    private PetRepository petRepository;
    @InjectMocks
    private AvatarService out;

    @BeforeEach
    void init() {
        JACK_AVATAR.setPet(JACK);
        JACK_AVATAR.setId(1L);
    }

    @Test
    void getAvatarsByPetId() {
        when(petRepository.findById(Mockito.any())).thenReturn(Optional.of(JACK));
        when(avatarRepository.findAvatarByPet(Mockito.any())).thenReturn(List.of(JACK_AVATAR));
        List<Avatar> avatarsByPetId = out.getAvatarsByPetId(JACK.getId());
        assertEquals(JACK.getId(), avatarsByPetId.get(0).getId());
        verify(petRepository, times(1)).findById(any(Long.class));
        verify(avatarRepository, times(1)).findAvatarByPet(any());
    }

    @Test
    void save() {
        when(avatarRepository.save(Mockito.any())).thenReturn(MURKA_AVATAR);
        out.save(MURKA_AVATAR);
        verify(avatarRepository, times(1)).save(any());
    }

    @Test
    void delete() {
        doNothing().when(avatarRepository).delete(Mockito.any());
        out.delete(MURKA_AVATAR);
        verify(avatarRepository, times(1)).delete(any());
    }

    @Test
    void getAvatarById() {
        when(avatarRepository.findById(Mockito.any())).thenReturn(Optional.of(JACK_AVATAR));
        Avatar avatarById = out.getAvatarById(JACK.getId());
        assertEquals(JACK_AVATAR, avatarById);
        verify(avatarRepository, times(1)).findById(any());
    }
}