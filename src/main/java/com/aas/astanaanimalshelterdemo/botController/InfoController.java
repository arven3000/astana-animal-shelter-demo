package com.aas.astanaanimalshelterdemo.botController;

import com.aas.astanaanimalshelterdemo.botModel.Info;
import com.aas.astanaanimalshelterdemo.botService.InfoService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

//Контроллер для работы со справочной информацией.
@RestController
@RequestMapping("/happyPet/info")
public class InfoController {
    private final InfoService infoService;

    public InfoController(InfoService infoService) {
        this.infoService = infoService;
    }

    //Добавление справочной информации нового приюта.
    @PostMapping
    public ResponseEntity<String> addInfo(@RequestBody Info info) {
        infoService.addInfo(info);
        return ResponseEntity.ok().build();
    }

    //Загрузка схемы проезда по id приюта.
    @PostMapping(value = "/location/{infoId}",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> uploadLocation (@PathVariable Long infoId,
                                                  @RequestParam MultipartFile location)
        throws IOException {

        //Максимальный размер файла в мегабайтах для загрузки схемы проезда.
        int maxSizeOfFileLocation = 1;
        if (location.getSize() >= maxSizeOfFileLocation * 1024 * 1024) {
            return ResponseEntity.badRequest().body("Слишком большой файл. " +
                    "Максимальный размер файла " + maxSizeOfFileLocation + " MB.");
        }
        infoService.uploadLocation(infoId, location);
        return ResponseEntity.ok().build();
    }

    //Получение справочной информации приюта.
    @GetMapping("/{id}")
    public ResponseEntity<Info> getInfo(@PathVariable Long id) {
        Info info = infoService.getInfo(id);
        if (info == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(info);
    }

    //Получение схемы проезда к приюту.
    @GetMapping("/getLocation/{infoId}")
    public ResponseEntity<byte[]> getLocation(@PathVariable Long infoId) {
        Info info = infoService.getInfo(infoId);
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.parseMediaType(info.getMediaType()));
        httpHeaders.setContentLength(info.getLocation().length);
        return ResponseEntity.status(HttpStatus.OK).headers(httpHeaders).
                body(info.getLocation());
    }

    //Удаление справочной информации приюта.
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteInfo(@PathVariable Long id) {
        infoService.deleteInfo(id);
        return ResponseEntity.ok().build();
    }
}
