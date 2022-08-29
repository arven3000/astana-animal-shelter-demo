package com.aas.astanaanimalshelterdemo.botService;

import com.aas.astanaanimalshelterdemo.botModel.Info;
import com.aas.astanaanimalshelterdemo.botRepositories.InfoRepository;
import org.springframework.stereotype.Service;

@Service
public class InfoService {
    private final InfoRepository infoRepository;

    public InfoService(InfoRepository infoRepository) {
        this.infoRepository = infoRepository;
    }

    public void addInfo(Info info) {
        infoRepository.save(info);
    }

    public Info getIngo(Long id) {
        return infoRepository.findById(id).orElseThrow();
    }

    public void deleteInfo(Long id) {
        infoRepository.deleteById(id);
    }
}
