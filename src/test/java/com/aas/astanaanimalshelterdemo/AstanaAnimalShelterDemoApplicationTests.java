package com.aas.astanaanimalshelterdemo;

import com.aas.astanaanimalshelterdemo.botController.AvatarController;
import com.aas.astanaanimalshelterdemo.botController.InfoController;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class AstanaAnimalShelterDemoApplicationTests {

	@LocalServerPort
	private int port;

	@Autowired
	private AvatarController avatarController;

	@Autowired
	private InfoController infoController;

	@Test
	void contextLoads() {
		Assertions.assertThat(infoController).isNotNull();
		Assertions.assertThat(avatarController).isNotNull();
	}

}

