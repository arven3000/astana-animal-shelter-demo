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
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Objects;

import static java.nio.file.StandardOpenOption.CREATE_NEW;

@Service
@Transactional
public class AvatarService {

    @Value("${path.to.avatars.folder}")
    private String avatarDir;

    private final PetRepository petRepository;
    private final AvatarRepository avatarRepository;

    public AvatarService(PetRepository petRepository, AvatarRepository avatarRepository) {
        this.petRepository = petRepository;
        this.avatarRepository = avatarRepository;
    }

    //Запись фотографий на диск и в таблицу Avatar.
    public void upLoadAvatar(Long petId, MultipartFile avatarFile1,
                             MultipartFile avatarFile2, MultipartFile avatarFile3)
            throws IOException {
        Pet pet = petRepository.findById(petId).orElseThrow();
        Avatar avatar = avatarRepository.findAvatarByPet(pet).orElse(new Avatar());
        avatar.setPet(pet);

        if (avatarFile1 != null) {
            Path filePath1 = Path.of(avatarDir, pet + "1." +
                    getExtension(Objects.requireNonNull(avatarFile1.getOriginalFilename())));
            uploadingPhoto(avatarFile1, filePath1);
            avatar.setFilePath1(filePath1.toString());
            avatar.setMediaType1(avatarFile1.getContentType());
            avatar.setFileSize1(avatarFile1.getSize());
            avatar.setPhoto1(creatingSmallerCopyOfPhoto(filePath1));
        }

        if (avatarFile2 != null) {
            Path filePath2 = Path.of(avatarDir, pet + "2." +
                    getExtension(Objects.requireNonNull(avatarFile2.getOriginalFilename())));
            uploadingPhoto(avatarFile2, filePath2);
            avatar.setFilePath2(filePath2.toString());
            avatar.setMediaType2(avatarFile2.getContentType());
            avatar.setFileSize2(avatarFile2.getSize());
            avatar.setPhoto2(creatingSmallerCopyOfPhoto(filePath2));
        }

        if (avatarFile3 != null) {
            Path filePath3 = Path.of(avatarDir, pet + "3." +
                    getExtension(Objects.requireNonNull(avatarFile3.getOriginalFilename())));
            uploadingPhoto(avatarFile3, filePath3);
            avatar.setFilePath3(filePath3.toString());
            avatar.setMediaType3(avatarFile3.getContentType());
            avatar.setFileSize3(avatarFile3.getSize());
            avatar.setPhoto3(creatingSmallerCopyOfPhoto(filePath3));
        }

        avatarRepository.save(avatar);
    }

    //Запись файла с фотографией на диск.
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

    //Получение расширения файла с фотографией.
    public String getExtension(String fileName) {
        return fileName.substring(fileName.lastIndexOf(".") + 1);
    }

    //Создание уменьшенной фотографии для записи в таблицу Avatar.
    private byte[] creatingSmallerCopyOfPhoto(Path filePath) throws IOException {
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
     * Получение Автара по идентификатору питомца.
     */
    public Avatar getAvatarByPetId(Long petId) {
        Pet pet = petRepository.findById(petId).orElseThrow();
        return avatarRepository.findAvatarByPet(pet).orElseThrow();
    }

    public byte[] getPhotoByNumber(Avatar avatar, Integer numberOfPhoto)
            throws NoSuchMethodException, InvocationTargetException,
            IllegalAccessException, ClassNotFoundException {
        String nameOfMethod = "getPhoto" + numberOfPhoto.toString();
        Class<?> c = Class.forName("com.aas.astanaanimalshelterdemo." +
                "botModel.Avatar");
        Method method = c.getDeclaredMethod(nameOfMethod);
        return (byte[]) method.invoke(avatar);
    }

    public String getMediaTypeByNumber(Avatar avatar, Integer numberOfPhoto)
            throws NoSuchMethodException, InvocationTargetException,
            IllegalAccessException, ClassNotFoundException {
        String nameOfMethod = "getMediaType" + numberOfPhoto.toString();
        Class<?> c = Class.forName("com.aas.astanaanimalshelterdemo." +
                "botModel.Avatar");
        Method method = c.getDeclaredMethod(nameOfMethod);
        return method.invoke(avatar).toString();
    }

    public String getFilePathByNumber(Avatar avatar, Integer numberOfPhoto)
            throws NoSuchMethodException, InvocationTargetException,
            IllegalAccessException, ClassNotFoundException {
        String nameOfMethod = "getFilePath" + numberOfPhoto.toString();
        Class<?> c = Class.forName("com.aas.astanaanimalshelterdemo." +
                "botModel.Avatar");
        Method method = c.getDeclaredMethod(nameOfMethod);
        return method.invoke(avatar).toString();
    }

    public Long getFileSizeByNumber(Avatar avatar, Integer numberOfPhoto)
            throws NoSuchMethodException, InvocationTargetException,
            IllegalAccessException, ClassNotFoundException {
        String nameOfMethod = "getFileSize" + numberOfPhoto.toString();
        Class<?> c = Class.forName("com.aas.astanaanimalshelterdemo." +
                "botModel.Avatar");
        Method method = c.getMethod(nameOfMethod);
        return (Long) method.invoke(avatar);
    }
}
