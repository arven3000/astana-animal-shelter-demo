package com.aas.astanaanimalshelterdemo.botController;

import com.aas.astanaanimalshelterdemo.botModel.Info;
import com.aas.astanaanimalshelterdemo.botService.InfoService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static com.aas.astanaanimalshelterdemo.botService.ServiceTestConstants.info;
import static org.mockito.Mockito.*;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class InfoControllerTest {

    @LocalServerPort
    private int port;

    @MockBean
    private InfoService infoService;

    @Autowired
    @InjectMocks
    private InfoController infoController;
    @Autowired
    private TestRestTemplate testRestTemplate;

    @BeforeEach
    void init() {
        info.setAddress("Street");
        info.setId(1L);
    }

    @Test
    void addInfo() {
        doNothing().when(infoService).addInfo(Mockito.any(Info.class));
        Assertions.assertThat(this.testRestTemplate.postForObject(
                "http://localhost:" + port + "/happyPet/info", info, String.class)).isNull();
        verify(infoService, times(1)).addInfo(Mockito.any());
    }

    @Test
    void getInfo() {
        when(infoService.getInfo(1L)).thenReturn(info);
        Assertions.assertThat(this.testRestTemplate.getForObject(
                "http://localhost:" + port + "/happyPet/info/" + info.getId(), String.class)).isNotNull();

        verify(infoService, times(1)).getInfo(Mockito.any());
    }

    @Test
    void deleteInfo() {
        doNothing().when(infoService).deleteInfo(info.getId());
        ResponseEntity<Info> response = testRestTemplate.exchange(("http://localhost:" + port
                                                                   + "/happyPet/info/{id}"), HttpMethod.DELETE, null,
                Info.class, 1L);
        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        verify(infoService, times(1)).deleteInfo(any(Long.class));
    }
}