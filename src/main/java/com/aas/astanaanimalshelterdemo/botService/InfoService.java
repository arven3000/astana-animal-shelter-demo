package com.aas.astanaanimalshelterdemo.botService;

import com.aas.astanaanimalshelterdemo.botModel.Info;
import com.aas.astanaanimalshelterdemo.botRepositories.InfoRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
public class InfoService {
    private final InfoRepository infoRepository;

    public InfoService(InfoRepository infoRepository) {
        this.infoRepository = infoRepository;
    }

    //Добавление справочной информации нового приюта.
    public void addInfo(Info info) {
        infoRepository.save(info);
    }

    //Получение справочной информации приюта.
    public Info getInfo(Long id) {
        return infoRepository.findById(id).orElseThrow();
    }

    //Удаление справочной информации приюта.
    public void deleteInfo(Long id) {
        infoRepository.deleteById(id);
    }


    //Загрузка схемы проезда к приюту.
    public void uploadLocation(Long infoId, MultipartFile locationFile) throws IOException {
        Info info = infoRepository.findById(infoId).orElseThrow();
        info.setLocation(locationFile.getBytes());
        info.setMediaType(locationFile.getContentType());
        infoRepository.save(info);
    }
}
