package com.aas.astanaanimalshelterdemo.botService;

import com.aas.astanaanimalshelterdemo.botModel.Avatar;
import com.aas.astanaanimalshelterdemo.botModel.Pet;
import com.aas.astanaanimalshelterdemo.botRepositories.AvatarRepository;
import com.aas.astanaanimalshelterdemo.botRepositories.PetRepository;
import org.apache.commons.io.output.ByteArrayOutputStream;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import javax.ws.rs.NotFoundException;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Objects;

import static java.nio.file.StandardOpenOption.CREATE_NEW;

@Service
@Transactional
public class AvatarService {

    /**
     * Директорий, где будут хранится файлы с фотографиями.
     */
    @Value("${path.to.avatars.folder}")
    private String avatarDir;

    private final PetRepository petRepository;
    private final AvatarRepository avatarRepository;

    public AvatarService(PetRepository petRepository, AvatarRepository avatarRepository) {
        this.petRepository = petRepository;
        this.avatarRepository = avatarRepository;
    }

    /**
     * Запись фотографий на диск и в таблицу Avatar.
     */
    public void upLoadAvatar(Long petId, MultipartFile avatarFile)
            throws IOException {
        Pet pet = petRepository.findById(petId).orElseThrow();
        Avatar avatar = new Avatar();
        avatar.setPet(pet);
        String pathOfPet = avatarDir + "/" + petId;

        if (avatarFile != null) {
            Path filePath = Path.of(pathOfPet, pet.getName() +
                    getAvatarsByPetId(petId).size() + "." +
                    getExtension(Objects.requireNonNull(avatarFile.getOriginalFilename())));
            uploadingPhoto(avatarFile, filePath);
            avatar.setFilePath(filePath.toString());
            avatar.setMediaType(avatarFile.getContentType());
            avatar.setFileSize(avatarFile.getSize());
            avatar.setPhoto(creatingSmallerCopyOfPhoto(filePath));
        }

        avatarRepository.save(avatar);
    }

    /**
     * Запись файла с фотографией на диск.
     */
    private void uploadingPhoto(MultipartFile avatarFile, Path filePath) throws IOException {
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
    }

    /**
     * Получение расширения файла с фотографией.
     */
    public String getExtension(String fileName) {
        return fileName.substring(fileName.lastIndexOf(".") + 1);
    }

    /**
     * Создание уменьшенной фотографии для записи в таблицу Avatar.
     */
    public byte[] creatingSmallerCopyOfPhoto(Path filePath) throws IOException {
        try (InputStream is = Files.newInputStream(filePath);
             BufferedInputStream bis = new BufferedInputStream(is, 1024);
             ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            BufferedImage image = ImageIO.read(bis);

            int height = image.getHeight() / (image.getWidth() / 100);
            BufferedImage smallCopy = new BufferedImage(100, height, image.getType());
            Graphics2D graphics = smallCopy.createGraphics();
            graphics.drawImage(image, 0, 0, 100, height, null);
            graphics.dispose();

            ImageIO.write(smallCopy, getExtension(filePath.getFileName().toString()), baos);
            return baos.toByteArray();
        }
    }

    /**
     * Получение Автаров по идентификатору питомца.
     */
    public List<Avatar> getAvatarsByPetId(Long petId) {
        Pet pet = petRepository.findById(petId).orElseThrow(NotFoundException::new);
        return avatarRepository.findAvatarByPet(pet);
    }

    public Avatar save(Avatar avatar) {
        return avatarRepository.save(avatar);
    }

    public void delete(Avatar avatar) {
        avatarRepository.delete(avatar);
    }

    public Avatar getAvatarById(Long id) {
        return avatarRepository.findById(id).orElseThrow(NotFoundException::new);
    }
}
