package com.fslvo.FileManagerFSLVO.controllers;

import com.fslvo.FileManagerFSLVO.service.ArticleImageUploadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("articleImages")
public class ArticleImageUploadController {

    private final ArticleImageUploadService articleImageUploadService;

    @Autowired
    public ArticleImageUploadController(ArticleImageUploadService articleImageUploadService) {
        this.articleImageUploadService = articleImageUploadService;
    }

    /**
     * Сохранение картинок в директорию articleImage
     */
    @PostMapping("upload")
    public ResponseEntity<?> uploadArticlePictures(@RequestParam String fileName, @RequestParam MultipartFile file) throws IOException {
        if (!file.isEmpty()) {
            String uploadImage = articleImageUploadService.saveFile(fileName, file);
            return ResponseEntity.status(HttpStatus.OK).body(uploadImage);
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Вы не передали файл для загрузки");
    }

    /**
     * Загрузка файлов из articleImage
     */
    @GetMapping("download")
    public ResponseEntity<?> downloadArticlePictures(@RequestParam String fileName) throws IOException {
        if (fileName != null) {
            byte[] imageData = articleImageUploadService.downloadFile(fileName);
            HttpHeaders headers = new HttpHeaders();
            if (fileName.endsWith("jpg")) {
                headers.setContentType(MediaType.IMAGE_JPEG);
            } else if (fileName.endsWith("png")) {
                headers.setContentType(MediaType.IMAGE_PNG);
            }

            return new ResponseEntity<>(imageData, headers, HttpStatus.OK);
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Вы не передали название файла для загрузки");
        }
    }

}
