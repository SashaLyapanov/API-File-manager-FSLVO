package com.fslvo.FileManagerFSLVO.controllers;

import com.fslvo.FileManagerFSLVO.service.PersonalAccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("personalAccount")
public class PersonalAccountController {

    private final PersonalAccountService personalAccountService;

    @Autowired
    public PersonalAccountController(PersonalAccountService personalAccountService) {
        this.personalAccountService = personalAccountService;
    }

    /**
     * Сохранение картинок в директорию articleImage
     */
    @PostMapping("upload")
    public ResponseEntity<?> uploadArticlePictures(@RequestParam String oldFileName, @RequestParam String sportsmanId, @RequestParam MultipartFile file) throws IOException {
        if (!file.isEmpty()) {
            String uploadImage = personalAccountService.saveFile(oldFileName, sportsmanId, file);
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
            byte[] imageData = personalAccountService.downloadFile(fileName);
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
