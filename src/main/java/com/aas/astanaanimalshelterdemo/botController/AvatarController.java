package com.aas.astanaanimalshelterdemo.botController;

import com.aas.astanaanimalshelterdemo.botService.AvatarService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

//Контроллер для работы с фотографиями.
@RestController
@RequestMapping("/happyPet/avatar")
public class AvatarController {
    private final AvatarService avatarService;

    public AvatarController(AvatarService avatarService) {
        this.avatarService = avatarService;
    }

    @PostMapping(value = "/{petId}",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> uploadAvatar (@PathVariable Long petId,
                                                @RequestParam(required = false)
                                                    MultipartFile file1,
                                                @RequestParam(required = false)
                                                    MultipartFile file2,
                                                @RequestParam(required = false)
                                                    MultipartFile file3) throws IOException {
        avatarService.upLoadAvatar(petId, file1, file2, file3);
        return ResponseEntity.ok().body("Фотографии успешно загружены");
    }
}
