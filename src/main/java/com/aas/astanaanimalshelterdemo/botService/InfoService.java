package com.aas.astanaanimalshelterdemo.botService;

import com.aas.astanaanimalshelterdemo.botModel.Info;
import com.aas.astanaanimalshelterdemo.botRepositories.InfoRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.ws.rs.NotFoundException;
import java.io.IOException;

@Service
public class InfoService {
    private static final Logger LOGGER = LoggerFactory.getLogger(InfoService.class);

    private final InfoRepository infoRepository;

    public InfoService(InfoRepository infoRepository) {
        this.infoRepository = infoRepository;
    }

    /**
     * Добавление справочной информации нового приюта.
     * @param info - информация о приюте
     */
    public void addInfo(Info info) {
        LOGGER.info("Was invoked method for add information about new shelter.");
        infoRepository.save(info);
    }

    /**
     * Получение справочной информации приюта.
     * @param id - id приюта
     * @return Info
     */
    public Info getInfo(Long id) {
        LOGGER.info("Was invoked method for get information about shelter.");
        return infoRepository.findById(id).orElseThrow(NotFoundException::new);
    }

    /**
     * Удаление справочной информации приюта.
     * @param id - id приюта
     */
    public void deleteInfo(Long id) {
        LOGGER.info("Was invoked method for delete information about shelter.");
        infoRepository.deleteById(id);
    }

    /**
     * Загрузка схемы проезда к приюту.
     * @param infoId - id приюта
     * @param locationFile - файл локации
     * @throws IOException - исключение IOException
     */
    public void uploadLocation(Long infoId, MultipartFile locationFile) throws IOException {
        LOGGER.info("Was invoked method for upload location of shelter.");
        Info info = infoRepository.findById(infoId).orElseThrow();
        info.setLocation(locationFile.getBytes());
        info.setMediaType(locationFile.getContentType());
        infoRepository.save(info);
    }
}
