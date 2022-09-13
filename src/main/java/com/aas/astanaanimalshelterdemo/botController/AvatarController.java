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

//Контроллер для работы с фотографиями.
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
                                                    MultipartFile file1,
                                                @RequestParam(required = false)
                                                    MultipartFile file2,
                                                @RequestParam(required = false)
                                                    MultipartFile file3) throws IOException {
        avatarService.upLoadAvatar(petId, file1, file2, file3);
        return ResponseEntity.ok().body("Фотографии успешно загружены.");
    }

    @GetMapping(value = "/getAvatar/{petId}")
    public ResponseEntity<byte[]> getAvatarByPetId(@PathVariable Long petId,
                                                   @RequestParam Integer numberOfPhoto)
            throws ClassNotFoundException, InvocationTargetException,
            NoSuchMethodException, IllegalAccessException {
        Avatar avatar = avatarService.getAvatarByPetId(petId);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType(avatarService.getMediaTypeByNumber(avatar, numberOfPhoto)));
        headers.setContentLength(avatarService.getPhotoByNumber(avatar, numberOfPhoto).length);
        return ResponseEntity.status(HttpStatus.OK).headers(headers).
                body(avatarService.getPhotoByNumber(avatar, numberOfPhoto));
    }

    @GetMapping(value = "/getPhoto/{petId}")
    public void getPhotoByPetId(@PathVariable Long petId,
                                @RequestParam Integer numberOfPhoto,
                                HttpServletResponse response)
            throws ClassNotFoundException, InvocationTargetException, NoSuchMethodException, IllegalAccessException {
        Avatar avatar = avatarService.getAvatarByPetId(petId);
        Path path = Path.of(avatarService.getFilePathByNumber(avatar, numberOfPhoto));
        try (
                InputStream is = Files.newInputStream(path);
                OutputStream os = response.getOutputStream();){
            response.setStatus(200);
            response.setContentType(avatarService.getMediaTypeByNumber(avatar,
                    numberOfPhoto));
            response.setContentLength(Math.toIntExact(avatarService.getFileSizeByNumber(avatar,
                    numberOfPhoto)));
            is.transferTo(os);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
