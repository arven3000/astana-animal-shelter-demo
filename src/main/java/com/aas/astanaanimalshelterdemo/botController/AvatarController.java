package com.aas.astanaanimalshelterdemo.botController;

import com.aas.astanaanimalshelterdemo.botModel.Avatar;
import com.aas.astanaanimalshelterdemo.botService.AvatarService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.InvocationTargetException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

/**
 * Контроллер для работы с фотографиями.
 */
@RestController
@RequestMapping("/happyPet/avatar")
public class AvatarController {
    private final AvatarService avatarService;

    public AvatarController(AvatarService avatarService) {
        this.avatarService = avatarService;
    }

    @PostMapping(value = "/uploadAvatars/{petId}",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> uploadAvatar (@PathVariable Long petId,
                                                @RequestParam(required = false)
                                                    MultipartFile file)
                                                     throws IOException {
        avatarService.upLoadAvatar(petId, file);
        return ResponseEntity.ok().body("Фотографии успешно загружены.");
    }

    @GetMapping(value = "/getAvatar/{petId}")
    public ResponseEntity<byte[]> getAvatarByPetId(@PathVariable Long petId,
                                                   @RequestParam Integer numberOfPhoto) {
        List<Avatar> avatars = avatarService.getAvatarsByPetId(petId);
        Avatar avatar = avatars.get(numberOfPhoto - 1);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType(avatar.getMediaType()));
        headers.setContentLength(avatar.getPhoto().length);
        return ResponseEntity.status(HttpStatus.OK).headers(headers).
                body(avatar.getPhoto());
    }

    @GetMapping(value = "/getPhoto/{petId}")
    public void getPhotoByPetId(@PathVariable Long petId,
                                @RequestParam Integer numberOfPhoto,
                                HttpServletResponse response) {
        List<Avatar> avatars = avatarService.getAvatarsByPetId(petId);
        Avatar avatar = avatars.get(numberOfPhoto - 1);
        Path path = Path.of(avatar.getFilePath());
        try (
                InputStream is = Files.newInputStream(path);
                OutputStream os = response.getOutputStream();){
            response.setStatus(200);
            response.setContentType(avatar.getMediaType());
            response.setContentLength(Math.toIntExact(avatar.getFileSize()));
            is.transferTo(os);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
