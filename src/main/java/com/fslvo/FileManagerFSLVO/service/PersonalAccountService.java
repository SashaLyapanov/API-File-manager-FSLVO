package com.fslvo.FileManagerFSLVO.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

@Service
public class PersonalAccountService {

    @Value("${article.image.dir}")
    private String uploadDirectory;

    private String personalAccountDir = "personalAccountImage/";

    /**
     * Сохранение картинки в дирректорию personalAccountImage
     */
    public String saveFile(String oldFileName, String sportsmanId, MultipartFile file) throws IOException {
        if (oldFileName != null) {
            File oldFile = new File(uploadDirectory + personalAccountDir + oldFileName);
            System.out.println(oldFile.getPath());
            if (oldFile.exists()) {
                oldFile.delete();
            }
        }
        String filePath = uploadDirectory + personalAccountDir + sportsmanId + "_" + file.getOriginalFilename();
        file.transferTo(new File(filePath));
        return filePath;
    }

    /**
     * Загрузка картинки из дирректории personalAccountImage
     */
    public byte[] downloadFile(String fileName) throws IOException{
        String filePath = uploadDirectory + personalAccountDir + fileName;
        return Files.readAllBytes(new File(filePath).toPath());
    }
}
