package com.aas.astanaanimalshelterdemo.botService;

import com.aas.astanaanimalshelterdemo.botModel.Info;
import com.aas.astanaanimalshelterdemo.botRepositories.InfoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static com.aas.astanaanimalshelterdemo.botService.ServiceTestConstants.info;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class InfoServiceTest {

    @Mock
    private InfoRepository infoRepository;

    @InjectMocks
    private InfoService out;

    @BeforeEach
    void init() {
        info.setAddress("Street");
        info.setId(1L);
    }

    @Test
    void addInfo() {
        when(infoRepository.save(Mockito.any())).thenReturn(info);
        out.addInfo(info);
        verify(infoRepository, times(1)).save(any());
    }

    @Test
    void getInfo() {
        when(infoRepository.findById(anyLong())).thenReturn(Optional.of(info));
        Info outInfo = out.getInfo(1L);
        assertEquals(1L, outInfo.getId());
        assertEquals(info, outInfo);
        verify(infoRepository, times(1)).findById(anyLong());
    }

    @Test
    void deleteInfo() {
        doNothing().when(infoRepository).deleteById(anyLong());
        out.deleteInfo(info.getId());
        verify(infoRepository, times(1)).deleteById(any());
    }
}