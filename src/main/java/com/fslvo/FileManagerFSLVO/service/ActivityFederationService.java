package com.fslvo.FileManagerFSLVO.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

@Service
public class ActivityFederationService {

    @Value("${activityFederation.dir}")
    private String uploadDirectory;

    /**
     * Загрузка файла из дирректорию aboutFederation
     */
    public byte[] downloadFile(String fileName) throws IOException {
        String filePath = uploadDirectory + fileName;
        return Files.readAllBytes(new File(filePath).toPath());
    }

}
