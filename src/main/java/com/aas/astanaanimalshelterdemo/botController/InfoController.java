package com.aas.astanaanimalshelterdemo.botController;

import com.aas.astanaanimalshelterdemo.botModel.Info;
import com.aas.astanaanimalshelterdemo.botService.InfoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/**
 * Контроллер для работы со справочной информацией.
 */
@Tag(name = "Info Controller", description = "CRUD операции с сущностью Info")
@RestController
@RequestMapping("/happyPet/info")
public class InfoController {
    private final InfoService infoService;

    public InfoController(InfoService infoService) {
        this.infoService = infoService;
    }

    /**
     * Добавление справочной информации нового приюта.
     *
     * @param info - информация о приюте.
     * @return - ResponseEntity<String>
     */
    @Operation(
            summary = "Добавление информации о приюте.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "метод добавляет информацию о новом приюте"
                    )
            }
    )
    @PostMapping
    public ResponseEntity<String> addInfo(@Parameter(description = "вносим всю необходимую информацию о приюте")
                                          @RequestBody Info info) {
        infoService.addInfo(info);
        return ResponseEntity.ok().build();
    }

    /**
     * Загрузка схемы проезда по id приюта.
     *
     * @param infoId   - id приюта
     * @param location - файл изображения локации
     * @return - ResponseEntity<String>
     * @throws IOException - исключение IOException
     */
    @Operation(
            summary = "Загрузка изображения локации приюта.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "метод загружает изображение с локацией",
                            content = @Content(
                                    mediaType = MediaType.MULTIPART_FORM_DATA_VALUE,
                                    schema = @Schema(implementation = Info.class)
                            )
                    )
            }
    )
    @PostMapping(value = "/location/{infoId}",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> uploadLocation(@Parameter(description = "указываем id приюта")
                                                 @PathVariable Long infoId,
                                                 @Parameter(description = "передаем изображение локации")
                                                 @RequestParam MultipartFile location)
            throws IOException {

        int maxSizeOfFileLocation = 1;
        if (location.getSize() >= maxSizeOfFileLocation * 1024 * 1024) {
            return ResponseEntity.badRequest().body("Слишком большой файл. " +
                                                    "Максимальный размер файла " + maxSizeOfFileLocation + " MB.");
        }
        infoService.uploadLocation(infoId, location);
        return ResponseEntity.ok().build();
    }

    /**
     * Получение справочной информации приюта.
     *
     * @param id - id приюта
     * @return - ResponseEntity<Info>
     */
    @Operation(
            summary = "Поиск информации о приюте.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "метод возвращает информацию о приюте по id"
                    )
            }
    )
    @GetMapping("/{id}")
    public ResponseEntity<Info> getInfo(@Parameter(description = "указываем id приюта")
                                        @PathVariable Long id) {
        Info info = infoService.getInfo(id);
        if (info == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(info);
    }

    /**
     * Получение схемы проезда к приюту.
     *
     * @param infoId - id приюта
     * @return - ResponseEntity<byte[]>
     */
    @Operation(
            summary = "Поиск изображения локации приюта.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "метод возвращает изображение локации приюта по id"
                    )
            }
    )
    @GetMapping("/getLocation/{infoId}")
    public ResponseEntity<byte[]> getLocation(@Parameter(description = "указываем id приюта")
                                              @PathVariable Long infoId) {
        Info info = infoService.getInfo(infoId);
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.parseMediaType(info.getMediaType()));
        httpHeaders.setContentLength(info.getLocation().length);
        return ResponseEntity.status(HttpStatus.OK).headers(httpHeaders).
                body(info.getLocation());
    }

    /**
     * Удаление справочной информации приюта.
     *
     * @param id - id приюта
     * @return - ResponseEntity<String>
     */
    @Operation(
            summary = "Удаляем информацию о приюте.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "метод удаляет информацию о приюте по id"
                    )
            }
    )
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteInfo(@Parameter(description = "указываем id приюта")
                                             @PathVariable Long id) {
        infoService.deleteInfo(id);
        return ResponseEntity.ok().build();
    }
}
