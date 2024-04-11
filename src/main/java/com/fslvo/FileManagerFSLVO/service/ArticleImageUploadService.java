package com.fslvo.FileManagerFSLVO.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

@Service
public class ArticleImageUploadService {

    @Value("${article.image.dir}")
    private String uploadDirectory;

    private final String articleImageDir = "articleImage/";

    /**
     * Сохранение картинки в дирректорию articleImage
     */
    public String saveFile(String oldFileName, String fileName, MultipartFile file) throws IOException {
        if (oldFileName != null) {
            File oldFile = new File(uploadDirectory + articleImageDir + oldFileName);
            if (oldFile.exists()) {
                oldFile.delete();
            }
        }
        String filePath = uploadDirectory + articleImageDir + fileName;
        file.transferTo(new File(filePath));
        return filePath;
//        String filePath = uploadDirectory + articleImageDir + fileName;
//        file.transferTo(new File(filePath));
//        return filePath;
    }

    /**
     * Загрузка картинки из дирректорию articleImage
     */
    public byte[] downloadFile(String fileName) throws IOException{
        String filePath = uploadDirectory + articleImageDir + fileName;
        return Files.readAllBytes(new File(filePath).toPath());
    }

}
