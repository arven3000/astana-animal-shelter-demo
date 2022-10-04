package com.aas.astanaanimalshelterdemo.botService;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
public class ReportService {
    private final AvatarService avatarService;

    public ReportService(AvatarService avatarService) {
        this.avatarService = avatarService;
    }

    public void uploadPhotoFromUser(Long petId, MultipartFile file) throws IOException {
        avatarService.upLoadAvatar(petId, file);
    }
}
