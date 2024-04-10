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

    /**
     * Сохранение картинки в дирректорию articleImage
     */
    public String saveFile(MultipartFile file) throws IOException {
        String filePath = uploadDirectory + "/" + file.getOriginalFilename();
        System.out.println(filePath);

        file.transferTo(new File(filePath));

        return filePath;
    }

    /**
     * Загрузка картинки из дирректорию articleImage
     */
    public byte[] downloadFile(String fileName) throws IOException{
        String filePath = uploadDirectory + "/" + fileName;
        byte[] images = Files.readAllBytes(new File(filePath).toPath());
        return images;
    }

}
