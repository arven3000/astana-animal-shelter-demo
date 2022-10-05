package com.aas.astanaanimalshelterdemo.botService;

import com.aas.astanaanimalshelterdemo.botModel.AnimalType;
import com.aas.astanaanimalshelterdemo.botModel.Pet;
import com.aas.astanaanimalshelterdemo.botRepositories.PetRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static com.aas.astanaanimalshelterdemo.botService.ServiceTestConstants.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PetServiceTest {

    @Mock
    private PetRepository petRepository;

    @InjectMocks
    private PetService out;

    @Test
    void getPetsByTypeOfAnimal() {
        when(petRepository.findPetsByTypeOfAnimal(Mockito.any())).thenReturn(List.of(MURKA));
        out.getPetsByTypeOfAnimal(AnimalType.CAT);
        verify(petRepository, times(1)).findPetsByTypeOfAnimal(any());
    }

    @Test
    void getPetsByTypeOfAnimalAndUsersNull() {
        when(petRepository.findPetsByTypeAndNullUser(Mockito.any())).thenReturn(List.of(BOSS, JACK));
        out.getPetsByTypeOfAnimalAndUsersNull(AnimalType.DOG);
        verify(petRepository, times(1)).findPetsByTypeAndNullUser(any());
    }

    @Test
    void getPetByPetId() {
        when(petRepository.findById(anyLong())).thenReturn(Optional.of(BOSS));
        out.getPetByPetId(3L);
        verify(petRepository, times(1)).findById(any());
    }

    @Test
    void getPetByUsers() {
        when(petRepository.findPetByUsers(ANNA)).thenReturn(Optional.of(BOSS));
        out.getPetByUsers(ANNA);
        verify(petRepository, times(1)).findPetByUsers(any());
    }

    @Test
    void save() {
        when(petRepository.save(Mockito.any())).thenReturn(MURKA);
        Pet pet = out.save(MURKA);
        assertEquals(MURKA.getId(), pet.getId());
        verify(petRepository, times(1)).save(any());
    }
}