package com.aas.astanaanimalshelterdemo.botService;

import com.aas.astanaanimalshelterdemo.botModel.Avatar;
import com.aas.astanaanimalshelterdemo.botModel.Pet;
import com.aas.astanaanimalshelterdemo.botRepositories.AvatarRepository;
import com.aas.astanaanimalshelterdemo.botRepositories.PetRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;

import static java.nio.file.StandardOpenOption.CREATE_NEW;

@Service
public class AvatarService {

    @Value("/avatars")
    private String avatarDir;

    private final PetRepository petRepository;
    private final AvatarRepository avatarRepository;

    public AvatarService(PetRepository petRepository, AvatarRepository avatarRepository) {
        this.petRepository = petRepository;
        this.avatarRepository = avatarRepository;
    }

    public void upLoadAvatar(Long petId, MultipartFile avatarFile) throws IOException {
        Pet pet = petRepository.findById(petId).orElseThrow();
        Path filePath = Path.of(avatarDir, pet + "." +
                getExtension(avatarFile.getOriginalFilename()));
        Files.createDirectories(filePath.getParent());
        Files.deleteIfExists(filePath);
        try (
                InputStream is = avatarFile.getInputStream();
                OutputStream os = Files.newOutputStream(filePath, CREATE_NEW);
                BufferedInputStream bis = new BufferedInputStream(is, 1024);
                BufferedOutputStream bos = new BufferedOutputStream(os, 1024);
        ) {
            bis.transferTo(bos);
        }
        Avatar avatar = avatarRepository.findByPetId(petId).orElse(new Avatar());
        avatar.setPet(pet);
        avatar.setFilePath(filePath.toString());
        avatar.setFoto1(avatarFile.getBytes());
        avatarRepository.save(avatar);
    }

    public String getExtension(String fileName) {
        return fileName.substring(fileName.lastIndexOf(".") + 1);
    }
}
